package com.veracode.verademo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.veracode.verademo.utils.*;
import com.veracode.annotation.CRLFCleanser;

@Controller
@Scope("request")
public class BlabController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:BlabController");

	private String dbConnStr = "jdbc:mysql://localhost/blab?user=blab&password=z2^E6J4$;u;d";

	@CRLFCleanser
	private String safeLog(String msg) {
		return msg.replaceAll("\n", "[newline]");
	}

	@Autowired
	private UserSession theUser;

	@RequestMapping(value="/feed", method=RequestMethod.GET)
	public String showFeed(@RequestParam(value="type", required=false) String type, Model model) {
		String nextView = "feed";
		logger.info("Entering showFeed");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=feed";
		} else {
			logger.info("User is Logged In - continuing...");
			
			Connection connect = null;
			PreparedStatement blabsByMe = null;
			PreparedStatement blabsForMe = null;
			String sqlBlabsByMe = "SELECT blabs.content, blabs.timestamp, COUNT(comments.blabber), blabs.blabid "
								+ "FROM blabs LEFT JOIN comments ON blabs.blabid = comments.blabid "
								+ "WHERE blabs.blabber = ? GROUP BY blabs.blabid ORDER BY blabs.timestamp DESC;";
			
			String sqlBlabsForMe = "SELECT users.userid, users.blab_name, blabs.content, blabs.timestamp, COUNT(comments.blabber), blabs.blabid "
								 + "FROM blabs INNER JOIN users ON blabs.blabber = users.userid INNER JOIN listeners ON blabs.blabber = listeners.blabber LEFT JOIN comments ON blabs.blabid = comments.blabid "
								 + "WHERE listeners.listener = ? GROUP BY blabs.blabid ORDER BY blabs.timestamp DESC;";
			
			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);
				
				// Find the Blabs that this user listens to
				logger.info("Preparing the BlabsForMe Prepared Statement");
				blabsForMe = connect.prepareStatement(sqlBlabsForMe);
				blabsForMe.setInt(1,  theUser.getUserID());
				logger.info("Executing the BlabsForMe Prepared Statement");
				ResultSet blabsForMeResults = blabsForMe.executeQuery();
				// Store them in the Model
				ArrayList<Integer> userID = new ArrayList<Integer>();
				ArrayList<String> blabName = new ArrayList<String>();
				ArrayList<String> contentForMe = new ArrayList<String>();
				ArrayList<String> timestampForMe = new ArrayList<String>();
				ArrayList<Integer> countForMe = new ArrayList<Integer>();
				ArrayList<Integer> blabIdForMe = new ArrayList<Integer>();
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
				while (blabsForMeResults.next()) {
					userID.add((Integer)blabsForMeResults.getInt(1));
					blabName.add(blabsForMeResults.getString(2));
					contentForMe.add(blabsForMeResults.getString(3));
					timestampForMe.add(sdf.format(blabsForMeResults.getDate(4)));
					countForMe.add(blabsForMeResults.getInt(5));
					blabIdForMe.add(blabsForMeResults.getInt(6));
				}
				model.addAttribute("userID", userID);
				model.addAttribute("blabName", blabName);
				model.addAttribute("contentForMe", contentForMe);
				model.addAttribute("timestampForMe", timestampForMe);
				model.addAttribute("countForMe", countForMe);
				model.addAttribute("blabIdForMe", blabIdForMe);
				
				model.addAttribute("currentUser", theUser.getBlabName());
				
				// Find the Blabs by this user
				logger.info("Preparing the BlabsByMe Prepared Statement");
				blabsByMe = connect.prepareStatement(sqlBlabsByMe);
				blabsByMe.setInt(1,  theUser.getUserID());
				logger.info("Executing the BlabsByMe Prepared Statement");
				ResultSet blabsByMeResults = blabsByMe.executeQuery();
				// Store them in the model
				ArrayList<String> contentByMe = new ArrayList<String>();
				ArrayList<String> timestampByMe = new ArrayList<String>();
				ArrayList<Integer> countByMe = new ArrayList<Integer>();
				ArrayList<Integer> blabIdByMe = new ArrayList<Integer>();
				while (blabsByMeResults.next()) {
					contentByMe.add(blabsByMeResults.getString(1));
					timestampByMe.add(sdf.format(blabsByMeResults.getDate(2)));
					countByMe.add(blabsByMeResults.getInt(3));
					blabIdByMe.add(blabsByMeResults.getInt(4));
				}
				model.addAttribute("contentByMe", contentByMe);
				model.addAttribute("timestampByMe", timestampByMe);
				model.addAttribute("countByMe", countByMe);
				model.addAttribute("blabIdByMe", blabIdByMe);
				
			}catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);
				
			} finally {
				try {
					if (blabsByMe != null) {
						blabsByMe.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (blabsForMe != null) {
						blabsForMe.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (connect != null){
						connect.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
			}
			
		}
		return nextView;
	}

	@RequestMapping(value="/feed", method=RequestMethod.POST)
	public String processFeed(@RequestParam(value="blab", required=true) String blab, Model model) {
		String nextView = "redirect:feed";
		logger.info("Entering processBlab");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=feed";
		} else {
			logger.info("User is Logged In - continuing...");
			Connection connect = null;
			PreparedStatement addBlab = null;
			String addBlabSql = "INSERT INTO blabs (blabber, content, timestamp) values (?, ?, ?);";

			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);
				
				java.util.Date now = new java.util.Date();
				// 
				logger.info("Preparing the addBlab Prepared Statement");
				addBlab = connect.prepareStatement(addBlabSql);
				addBlab.setInt(1,  theUser.getUserID());
				addBlab.setString(2,  blab);
				addBlab.setTimestamp(3,  new Timestamp(now.getTime()));
				
				logger.info("Executing the addComment Prepared Statement");
				boolean addBlabResult = addBlab.execute();
				
				// If there is a record...
				if (addBlabResult) {
					//failure
					model.addAttribute("error", "Failed to add comment");
				}
				nextView = "redirect:feed";
				
			}catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);
				
			} finally {
				try {
					if (addBlab != null) {
						addBlab.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (connect != null){
						connect.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
			}
		}
		return nextView;
	}
	
	@RequestMapping(value="/blab", method=RequestMethod.GET)
	public String showBlab(@RequestParam(value="blabid", required=true) Integer blabid, Model model) {
		String nextView = "redirect:feed";
		logger.info("Entering showBlab");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=blab";
		} else {
			logger.info("User is Logged In - continuing...");
			Connection connect = null;
			PreparedStatement blabDetails = null;
			PreparedStatement blabComments = null;
			String blabDetailsSql = "SELECT blabs.content, users.blab_name "
					  + "FROM blabs INNER JOIN users ON blabs.blabber = users.userid "
					  + "WHERE blabs.blabid = ?;";

			String blabCommentsSql = "SELECT comments.blabber, users.blab_name, comments.content, comments.timestamp "
					  + "FROM comments INNER JOIN users ON comments.blabber = users.userid "
					  + "WHERE comments.blabid = ? ORDER BY comments.timestamp DESC;";

			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);
				
				// Find the Blabs that this user listens to
				logger.info("Preparing the blabDetails Prepared Statement");
				blabDetails = connect.prepareStatement(blabDetailsSql);
				blabDetails.setInt(1,  blabid);
				logger.info("Executing the blabDetails Prepared Statement");
				ResultSet blabDetailsResults = blabDetails.executeQuery();
				
				// If there is a record...
				if (blabDetailsResults.next()) {
					model.addAttribute("content", blabDetailsResults.getString(1));
					model.addAttribute("blab_name", blabDetailsResults.getString(2));
					// Now lets get the comments...
					logger.info("Preparing the blabComments Prepared Statement");
					blabComments = connect.prepareStatement(blabCommentsSql);
					blabComments.setInt(1,  blabid);
					logger.info("Executing the blabComments Prepared Statement");
					ResultSet blabCommentsResults = blabComments.executeQuery();
					// Store them in the model
					ArrayList<Integer> commenterId = new ArrayList<Integer>();
					ArrayList<String> commenterName = new ArrayList<String>();
					ArrayList<String> comment = new ArrayList<String>();
					ArrayList<String> timestamp = new ArrayList<String>();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
					while (blabCommentsResults.next()) {
						commenterId.add(blabCommentsResults.getInt(1));
						commenterName.add(blabCommentsResults.getString(2));
						comment.add(blabCommentsResults.getString(3));
						timestamp.add(sdf.format(blabCommentsResults.getDate(4)));
					}
					model.addAttribute("commenterId", commenterId);
					model.addAttribute("commenterName", commenterName);
					model.addAttribute("comment", comment);
					model.addAttribute("timestamp", timestamp);
					model.addAttribute("blabid", blabid);

					nextView = "blab";
				}
				
			}catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);
				
			} finally {
				try {
					if (blabDetails != null) {
						blabDetails.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (connect != null){
						connect.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
			}
		}
		return nextView;
	}

	@RequestMapping(value="/blab", method=RequestMethod.POST)
	public String processBlab(@RequestParam(value="comment", required=true) String comment, 
							  @RequestParam(value="blabid", required=true) Integer blabid, Model model) {
		String nextView = "redirect:feed";
		logger.info("Entering processBlab");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=feed";
		} else {
			logger.info("User is Logged In - continuing...");
	 		Connection connect = null;
			PreparedStatement addComment = null;
			String addCommentSql = "INSERT INTO comments (blabid, blabber, content, timestamp) values (?, ?, ?, ?);";

			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);
				
				java.util.Date now = new java.util.Date();
				// 
				logger.info("Preparing the addComment Prepared Statement");
				addComment = connect.prepareStatement(addCommentSql);
				addComment.setInt(1,  blabid);
				addComment.setInt(2,  theUser.getUserID());
				addComment.setString(3,  comment);
				addComment.setTimestamp(4,  new Timestamp(now.getTime()));
				
				logger.info("Executing the addComment Prepared Statement");
				boolean addCommentResult = addComment.execute();
				
				// If there is a record...
				if (addCommentResult) {
					//failre
					model.addAttribute("error", "Failed to add comment");
				}
				nextView = "redirect:blab?blabid=" + blabid;
				
			}catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);
				
			} finally {
				try {
					if (addComment != null) {
						addComment.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (connect != null){
						connect.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
			}
		}
		return nextView;
	}
	
	@RequestMapping(value="/blabber", method=RequestMethod.GET)
	public String showBlabber(@RequestParam(value="userid", required=true) String userid, Model model) {
		logger.info("Entering showBlabber");
		return "notimplemented";
	}

	@RequestMapping(value="/blabber", method=RequestMethod.POST)
	public String processBlabber(@RequestParam(value="userid", required=true) String userid, Model model) {
		logger.info("Entering processBlabber");
		return "notimplemented";
	}

	@RequestMapping(value="/blabbers", method=RequestMethod.GET)
	public String showBlabbers(
			@RequestParam(value="sort", required=false) String sort,
			Model model
		) {
		if (sort == null || sort.isEmpty()) {
			sort = "blab_name ASC";
		}
		String nextView = "redirect:feed";
		logger.info("Entering showBlabbers");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=blabbers";
		} else {
			logger.info("User is Logged In - continuing...");
			Connection connect = null;
			PreparedStatement blabbers = null;
			
			/* START BAD CODE */
			String blabbersSql = "SELECT users.userid,"
									  + " users.blab_name,"
									  + " users.date_created,"
									  + " SUM(if(listeners.listener=?, 1, 0)) as listeners,"
									  + " SUM(if(listeners.status='Active',1,0)) as listening"
							   + " FROM users LEFT JOIN listeners ON users.userid = listeners.blabber"
							   + " WHERE users.userid NOT IN (1,?)"
							   + " GROUP BY users.userid"
							   + " ORDER BY " + sort + ";";

			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);
				
				// Find the Blabbers
				logger.info(blabbersSql);
				blabbers = connect.prepareStatement(blabbersSql);
				blabbers.setInt(1,  theUser.getUserID());
				blabbers.setInt(2,  theUser.getUserID());
				ResultSet blabbersResults = blabbers.executeQuery();
				/* END BAD CODE */
				
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
				ArrayList<Integer> blabberId = new ArrayList<Integer>();
				ArrayList<String> blabberName = new ArrayList<String>();
				ArrayList<String> created = new ArrayList<String>();
				ArrayList<Integer> listening = new ArrayList<Integer>();
				ArrayList<Integer> listeners = new ArrayList<Integer>();

				while (blabbersResults.next()) {
					blabberId.add(blabbersResults.getInt(1));
					blabberName.add(blabbersResults.getString(2));
					created.add(sdf.format(blabbersResults.getDate(3)));
					listening.add(blabbersResults.getInt(4));
					listeners.add(blabbersResults.getInt(5));
				}
				model.addAttribute("blabberId", blabberId);
				model.addAttribute("blabberName", blabberName);
				model.addAttribute("created", created);
				model.addAttribute("listening", listening);
				model.addAttribute("listeners", listeners);

				nextView = "blabbers";
				
			}catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);
				
			} finally {
				try {
					if (blabbers != null) {
						blabbers.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (connect != null){
						connect.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
			}
		}
		return nextView;
	}

	@RequestMapping(value="/blabbers", method=RequestMethod.POST)
	public String processBlabbers(@RequestParam(value="blabberId", required=true) Integer blabberId, 
								  @RequestParam(value="command", required=true) String command, Model model) {
		String nextView = "redirect:feed";
		logger.info("Entering processBlabbers");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=blabbers";
		} else {
			logger.info("User is Logged In - continuing...");
			logger.info("blabberId = " + blabberId);
			logger.info("command = " + Cleansers.cleanLog(command));
			
	 		Connection connect = null;
			PreparedStatement action = null;
			String ignoreSql = "DELETE FROM listeners WHERE blabber=? AND listener=?;";
			String listenSql = "INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');";

			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);
				
				java.util.Date now = new java.util.Date();
				
				String sqlQuery = (0 == command.compareTo("ignore") ? ignoreSql : listenSql);
				logger.info(sqlQuery);
				action = connect.prepareStatement(sqlQuery);
				action.setInt(1, blabberId);
				action.setInt(2, theUser.getUserID());
				action.execute();
							
				sqlQuery = "SELECT blab_name FROM users WHERE userid = " + blabberId;
				Statement sqlStatement = connect.createStatement();
				logger.info(sqlQuery);
				ResultSet result = sqlStatement.executeQuery(sqlQuery);
				result.next();
				
				/* START BAD CODE */
				String listenEvent = theUser.getBlabName() + " started listening to " + result.getString(1);
				String ignoreEvent = theUser.getBlabName() + " stopped listening to " + result.getString(1);
				String event = (0 == command.compareTo("ignore") ? ignoreEvent : listenEvent);
				sqlQuery = "INSERT INTO users_history (blabber, event) VALUES ('" + theUser.getUserID() + "', '" + event + "')";
				logger.info(sqlQuery);
				sqlStatement.execute(sqlQuery);
				/* END BAD CODE */
				
				nextView = "redirect:blabbers";
				
			}catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);
				
			} finally {
				try {
					if (action != null) {
						action.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
				try {
					if (connect != null){
						connect.close();
					}
				} catch (SQLException exceptSql) {
					logger.error(exceptSql);
				}
			}
		}
		return nextView;
	}
}
