package com.veracode.verademo.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.veracode.verademo.commands.BlabberCommand;
import com.veracode.verademo.model.Blab;
import com.veracode.verademo.model.Blabber;
import com.veracode.verademo.model.Comment;
import com.veracode.verademo.utils.Constants;
import com.veracode.verademo.utils.Utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("request")
public class BlabController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:BlabController");

	private final String sqlBlabsByMe = "SELECT blabs.content, blabs.timestamp, COUNT(comments.blabber), blabs.blabid "
			+ "FROM blabs LEFT JOIN comments ON blabs.blabid = comments.blabid "
			+ "WHERE blabs.blabber = ? GROUP BY blabs.blabid ORDER BY blabs.timestamp DESC;";

	private final String sqlBlabsForMe = "SELECT users.username, users.blab_name, blabs.content, blabs.timestamp, COUNT(comments.blabber), blabs.blabid "
			+ "FROM blabs INNER JOIN users ON blabs.blabber = users.username INNER JOIN listeners ON blabs.blabber = listeners.blabber "
			+ "LEFT JOIN comments ON blabs.blabid = comments.blabid WHERE listeners.listener = ? "
			+ "GROUP BY blabs.blabid ORDER BY blabs.timestamp DESC LIMIT %d OFFSET %d;";

	@RequestMapping(value = "/feed", method = RequestMethod.GET)
	public String showFeed(
			@RequestParam(value = "type", required = false) String type,
			Model model,
			HttpServletRequest httpRequest) {
		logger.info("Entering showFeed");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=profile");
		}

		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);

		Connection connect = null;
		PreparedStatement blabsByMe = null;
		PreparedStatement blabsForMe = null;

		try {
			logger.info("Getting Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabs that this user listens to
			logger.info("Preparing the BlabsForMe Prepared Statement");
			blabsForMe = connect.prepareStatement(String.format(sqlBlabsForMe, 10, 0));
			blabsForMe.setString(1, username);
			logger.info("Executing the BlabsForMe Prepared Statement");
			ResultSet blabsForMeResults = blabsForMe.executeQuery();

			// Store them in the Model
			List<Blab> feedBlabs = new ArrayList<Blab>();
			while (blabsForMeResults.next()) {
				Blabber author = new Blabber();
				author.setUsername(blabsForMeResults.getString(1));
				author.setBlabName(blabsForMeResults.getString(2));

				Blab post = new Blab();
				post.setId(blabsForMeResults.getInt(6));
				post.setContent(blabsForMeResults.getString(3));
				post.setPostDate(blabsForMeResults.getDate(4));
				post.setCommentCount(blabsForMeResults.getInt(5));
				post.setAuthor(author);

				feedBlabs.add(post);
			}
			model.addAttribute("blabsByOthers", feedBlabs);
			model.addAttribute("currentUser", username);

			// Find the Blabs by this user
			logger.info("Preparing the BlabsByMe Prepared Statement");
			blabsByMe = connect.prepareStatement(sqlBlabsByMe);
			blabsByMe.setString(1, username);
			logger.info("Executing the BlabsByMe Prepared Statement");
			ResultSet blabsByMeResults = blabsByMe.executeQuery();

			// Store them in the model
			List<Blab> myBlabs = new ArrayList<Blab>();
			while (blabsByMeResults.next()) {
				Blab post = new Blab();
				post.setId(blabsByMeResults.getInt(4));
				post.setContent(blabsByMeResults.getString(1));
				post.setPostDate(blabsByMeResults.getDate(2));
				post.setCommentCount(blabsByMeResults.getInt(3));

				myBlabs.add(post);
			}
			model.addAttribute("blabsByMe", myBlabs);
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
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
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return "feed";
	}

	@RequestMapping(value = "/morefeed", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMoreFeed(
			@RequestParam(value = "count", required = true) String count,
			@RequestParam(value = "len", required = true) String length,
			Model model,
			HttpServletRequest httpRequest) {
		String template = "<li><div>" + "\t<div class=\"commenterImage\">" + "\t\t<img src=\"resources/images/%s.png\">"
				+ "\t</div>" + "\t<div class=\"commentText\">" + "\t\t<p>%s</p>"
				+ "\t\t<span class=\"date sub-text\">by %s on %s</span><br>"
				+ "\t\t<span class=\"date sub-text\"><a href=\"blab?blabid=%d\">%d Comments</a></span>" + "\t</div>"
				+ "</div></li>";

		int cnt, len;
		try {
			// Convert input to integers
			cnt = Integer.parseInt(count);
			len = Integer.parseInt(length);
		} catch (NumberFormatException e) {
			return "";
		}

		String username = (String) httpRequest.getSession().getAttribute("username");

		// Get the Database Connection
		Connection connect;
		PreparedStatement feedSql;
		StringBuilder ret = new StringBuilder();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());
			feedSql = connect.prepareStatement(String.format(sqlBlabsForMe, len, cnt));
			feedSql.setString(1, username);

			ResultSet results = feedSql.executeQuery();
			while (results.next()) {
				Blab blab = new Blab();
				blab.setPostDate(results.getDate(4));

				ret.append(String.format(template, results.getString(1), // username
						results.getString(3), // blab content
						results.getString(2), // blab name
						blab.getPostDateString(), // timestamp
						results.getInt(6), // blabID
						results.getInt(5) // comment count
				));
			}
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		}

		return ret.toString();
	}

	@RequestMapping(value = "/feed", method = RequestMethod.POST)
	public String processFeed(
			@RequestParam(value = "blab", required = true) String blab,
			Model model,
			HttpServletRequest httpRequest) {
		String nextView = Utils.redirect("feed");
		logger.info("Entering processBlab");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=profile");
		}
		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);

		Connection connect = null;
		PreparedStatement addBlab = null;
		String addBlabSql = "INSERT INTO blabs (blabber, content, timestamp) values (?, ?, ?);";

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			java.util.Date now = new java.util.Date();
			logger.info("Preparing the addBlab Prepared Statement");
			addBlab = connect.prepareStatement(addBlabSql);
			addBlab.setString(1, username);
			addBlab.setString(2, blab);
			addBlab.setTimestamp(3, new Timestamp(now.getTime()));

			logger.info("Executing the addComment Prepared Statement");
			boolean addBlabResult = addBlab.execute();

			// If there is a record...
			if (addBlabResult) {
				// failure
				model.addAttribute("error", "Failed to add comment");
			}
			nextView = Utils.redirect("feed");
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (addBlab != null) {
					addBlab.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return nextView;
	}

	@RequestMapping(value = "/blab", method = RequestMethod.GET)
	public String showBlab(
			@RequestParam(value = "blabid", required = true) Integer blabid,
			Model model,
			HttpServletRequest httpRequest) {
		String nextView = Utils.redirect("feed");
		logger.info("Entering showBlab");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=profile");
		}

		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);

		Connection connect = null;
		PreparedStatement blabDetails = null;
		PreparedStatement blabComments = null;
		String blabDetailsSql = "SELECT blabs.content, users.blab_name "
				+ "FROM blabs INNER JOIN users ON blabs.blabber = users.username " + "WHERE blabs.blabid = ?;";

		String blabCommentsSql = "SELECT users.username, users.blab_name, comments.content, comments.timestamp "
				+ "FROM comments INNER JOIN users ON comments.blabber = users.username "
				+ "WHERE comments.blabid = ? ORDER BY comments.timestamp DESC;";

		try {
			logger.info("Getting Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabs that this user listens to
			logger.info("Preparing the blabDetails Prepared Statement");
			blabDetails = connect.prepareStatement(blabDetailsSql);
			blabDetails.setInt(1, blabid);
			logger.info("Executing the blabDetails Prepared Statement");
			ResultSet blabDetailsResults = blabDetails.executeQuery();

			// If there is a record...
			if (blabDetailsResults.next()) {
				// Get the blab contents
				model.addAttribute("content", blabDetailsResults.getString(1));
				model.addAttribute("blab_name", blabDetailsResults.getString(2));
				model.addAttribute("blabid", blabid);

				// Now lets get the comments...
				logger.info("Preparing the blabComments Prepared Statement");
				blabComments = connect.prepareStatement(blabCommentsSql);
				blabComments.setInt(1, blabid);
				logger.info("Executing the blabComments Prepared Statement");
				ResultSet blabCommentsResults = blabComments.executeQuery();

				// Store them in the model
				List<Comment> comments = new ArrayList<Comment>();
				while (blabCommentsResults.next()) {
					Blabber author = new Blabber();
					author.setUsername(blabCommentsResults.getString(1));
					author.setBlabName(blabCommentsResults.getString(2));

					Comment comment = new Comment();
					comment.setContent(blabCommentsResults.getString(3));
					comment.setTimestamp(blabCommentsResults.getDate(4));
					comment.setAuthor(author);

					comments.add(comment);
				}
				model.addAttribute("comments", comments);

				nextView = "blab";
			}

		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (blabDetails != null) {
					blabDetails.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return nextView;
	}

	@RequestMapping(value = "/blab", method = RequestMethod.POST)
	public String processBlab(
			@RequestParam(value = "comment", required = true) String comment,
			@RequestParam(value = "blabid", required = true) Integer blabid,
			Model model,
			HttpServletRequest httpRequest) {
		String nextView = Utils.redirect("feed");
		logger.info("Entering processBlab");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=feed");
		}

		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);
		Connection connect = null;
		PreparedStatement addComment = null;
		String addCommentSql = "INSERT INTO comments (blabid, blabber, content, timestamp) values (?, ?, ?, ?);";

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			java.util.Date now = new java.util.Date();
			//
			logger.info("Preparing the addComment Prepared Statement");
			addComment = connect.prepareStatement(addCommentSql);
			addComment.setInt(1, blabid);
			addComment.setString(2, username);
			addComment.setString(3, comment);
			addComment.setTimestamp(4, new Timestamp(now.getTime()));

			logger.info("Executing the addComment Prepared Statement");
			boolean addCommentResult = addComment.execute();

			// If there is a record...
			if (addCommentResult) {
				// failure
				model.addAttribute("error", "Failed to add comment");
			}

			nextView = Utils.redirect("blab?blabid=" + blabid);
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (addComment != null) {
					addComment.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return nextView;
	}

	@RequestMapping(value = "/blabbers", method = RequestMethod.GET)
	public String showBlabbers(
			@RequestParam(value = "sort", required = false) String sort,
			Model model,
			HttpServletRequest httpRequest) {
		if (sort == null || sort.isEmpty()) {
			sort = "blab_name ASC";
		}

		String nextView = Utils.redirect("feed");
		logger.info("Entering showBlabbers");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=blabbers");
		}

		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);

		Connection connect = null;
		PreparedStatement blabberQuery = null;

		/* START EXAMPLE VULNERABILITY */
		String blabbersSql = "SELECT users.username," + " users.blab_name," + " users.created_at,"
				+ " SUM(if(listeners.listener=?, 1, 0)) as listeners,"
				+ " SUM(if(listeners.status='Active',1,0)) as listening"
				+ " FROM users LEFT JOIN listeners ON users.username = listeners.blabber"
				+ " WHERE users.username NOT IN (\"admin\",?)" + " GROUP BY users.username" + " ORDER BY " + sort + ";";

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabbers
			logger.info(blabbersSql);
			blabberQuery = connect.prepareStatement(blabbersSql);
			blabberQuery.setString(1, username);
			blabberQuery.setString(2, username);
			ResultSet blabbersResults = blabberQuery.executeQuery();
			/* END EXAMPLE VULNERABILITY */

			List<Blabber> blabbers = new ArrayList<Blabber>();
			while (blabbersResults.next()) {
				Blabber blabber = new Blabber();
				blabber.setBlabName(blabbersResults.getString(2));
				blabber.setUsername(blabbersResults.getString(1));
				blabber.setCreatedDate(blabbersResults.getDate(3));
				blabber.setNumberListeners(blabbersResults.getInt(4));
				blabber.setNumberListening(blabbersResults.getInt(5));

				blabbers.add(blabber);
			}
			model.addAttribute("blabbers", blabbers);

			nextView = "blabbers";

		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (blabberQuery != null) {
					blabberQuery.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return nextView;
	}

	@RequestMapping(value = "/blabbers", method = RequestMethod.POST)
	public String processBlabbers(
			@RequestParam(value = "blabberUsername", required = true) String blabberUsername,
			@RequestParam(value = "command", required = true) String command,
			Model model,
			HttpServletRequest httpRequest) {
		String nextView = Utils.redirect("feed");
		logger.info("Entering processBlabbers");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=blabbers");
		}

		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);

		if (command == null || command.isEmpty()) {
			logger.info("Empty command provided...");
			return nextView = Utils.redirect("login?target=blabbers");
		}

		logger.info("blabberUsername = " + blabberUsername);
		logger.info("command = " + command);

		Connection connect = null;
		PreparedStatement action = null;

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			/* START EXAMPLE VULNERABILITY */
			Class<?> cmdClass = Class.forName("com.veracode.verademo.commands." + ucfirst(command) + "Command");
			BlabberCommand cmdObj = (BlabberCommand) cmdClass.getDeclaredConstructor(Connection.class, String.class)
					.newInstance(connect, username);
			cmdObj.execute(blabberUsername);
			/* END EXAMPLE VULNERABILITY */

			nextView = Utils.redirect("blabbers");

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			logger.error(ex);
		} finally {
			try {
				if (action != null) {
					action.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}
		return nextView;
	}

	final private static String ucfirst(String subject) {
		return Character.toUpperCase(subject.charAt(0)) + subject.substring(1);
	}
}
