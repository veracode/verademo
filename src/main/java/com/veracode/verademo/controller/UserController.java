package com.veracode.verademo.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.veracode.verademo.utils.Cleansers;
import com.veracode.verademo.utils.UserSession;


/**
 * @author johnadmin
 */
@Controller
@Scope("request")
public class UserController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:UserController");

	@Autowired
	private UserSession theUser;

	private String dbConnStr = "jdbc:mysql://localhost/blab?user=blab&password=z2^E6J4$;u;d";


	/**
	 * @param target
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(@RequestParam(value = "target", required = false) String target,
							@CookieValue(value="username", required=false) String username,
							Model model) {
		if (username == null) {
			username = "";
		}
		
		logger.info("Entering showLogin with username " + username + " and target " + target);
		model.addAttribute("username", username);
		if (null != target)
			model.addAttribute("target", target);
		else
			model.addAttribute("target", "");
		return "login";
	}

	/**
	 * @param username
	 * @param password
	 * @param target
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String processLogin(@RequestParam(value = "user", required = true) String username,
							   @RequestParam(value = "password", required = true) String password,
							   @RequestParam(value = "target", required = false) String target, 
							   Model model,
							   HttpServletResponse response) {
		String nextView = "login";
		
		/* START BAD CODE */
		response.addCookie(new Cookie("username", username));
		/* END BAD CODE */

		logger.info("Entering processLogin");

		Connection connect = null;
		Statement sqlStatement = null;

		try {
			// Get the Database Connection
			logger.info("Creating the Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(dbConnStr);

			/* START BAD CODE */
			// Execute the query
			logger.info("Creating the Statement");
			String sqlQuery = "select * from users where username='" + username + "' and password='" + password + "';";
			sqlStatement = connect.createStatement();
			logger.info("Execute the Statement");
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			/* END BAD CODE */

			// Did we find exactly 1 user that matched?
			if (result.first()) {
				// OK we have found the user, lets setup their Session object
				logger.info("User Found. Setting up UserSession object");
				logger.info("UserID: " + result.getInt(1));
				theUser.setUserID(result.getInt(1));
				logger.info("Username: " + result.getString(2));
				theUser.setUsername(result.getString(2));
				logger.info("RealName: " + result.getString(6));
				theUser.setRealName(result.getString(6));
				logger.info("BlabName: " + result.getString(7));
				theUser.setBlabName(result.getString(7));
				theUser.setLoggedIn(true);

				logger.info("Login complete. Redirecting (target=" + (null == target ? "null" : Cleansers.cleanLog(target)) + ")");
				if (0 != target.length()) {
					logger.info("redirecting to target");
					nextView = "redirect:" + target;

				} else {
					logger.info("redirecting to feed");
					nextView = "redirect:feed";
				}
			} else {
				logger.info("User Not Found");
				// Login failed...
				model.addAttribute("error", "Login failed. Please try again.");
				model.addAttribute("target", target);
			}

		} catch (SQLException exceptSql) {
			logger.error(exceptSql);
			model.addAttribute("error", exceptSql.getMessage());
			model.addAttribute("target", target);
		} catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);
			model.addAttribute("error", cnfe.getMessage());
			model.addAttribute("target", target);

		} finally {
			try {
				if (sqlStatement != null) {
					sqlStatement.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
				model.addAttribute("error", exceptSql.getMessage());
				model.addAttribute("target", target);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
				model.addAttribute("error", exceptSql.getMessage());
				model.addAttribute("target", target);
			}
		}
		logger.info("returning the nextView: " + Cleansers.cleanLog(nextView));
		return nextView;
	}

	@RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
	public String processLogout(@RequestParam(value = "type", required = false) String type, Model model) {
		logger.info("Entering processLogout");
		logger.info("Clearing UserSession");
		theUser.setBlabName("");
		theUser.setUserID(0);
		theUser.setUsername("");
		theUser.setLoggedIn(false);
		logger.info("Redirecting to Login...");
		return "redirect:login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegister(@RequestParam(value = "type", required = false) String type, Model model) {
		logger.info("Entering showRegister");

		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegister(@RequestParam(value = "user", required = true) String username,
								  @RequestParam(value = "password", required = true) String password,
								  @RequestParam(value = "cpassword", required = true) String cpassword,
								  @RequestParam(value = "realName", required = true) String realName,
								  @RequestParam(value = "blabName", required = true) String blabName, Model model) {
		logger.info("Entering processRegister");

		String nextView = "register";
		// Do the password and cpassword parameters match ?
		if (0 != password.compareTo(cpassword)) {
			logger.info("Password and Confirm Password do not match");
			model.addAttribute("error", "The Password and Confirm Password values do not match. Please try again.");
			return "register";
		}
		Connection connect = null;
		Statement sqlStatement = null;

		try {
			// Get the Database Connection
			logger.info("Creating the Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(dbConnStr);

			/* START BAD CODE */
			// Execute the query
			String mysqlCurrentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
			StringBuilder query = new StringBuilder();
			query.append("insert into users (username, password, date_created, real_name, blab_name) values(");
			query.append("'" + username + "',");
			query.append("'" + password + "',");
			query.append("'" + mysqlCurrentDateTime + "',");
			query.append("'" + realName + "',");
			query.append("'" + blabName + "'");
			query.append(");");
			
			sqlStatement = connect.createStatement();
			sqlStatement.execute(query.toString());
			
			
			/* END BAD CODE */
			
			emailUser(username);
		} catch (SQLException exceptSql) {
			logger.error(exceptSql);
		} catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);

		} finally {
			try {
				if (sqlStatement != null) {
					sqlStatement.close();
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

	private void emailUser(String username) {
	      String to = "admin@example.com";
	      String from = "verademo@veracode.com";
	      String host = "localhost";
	      String port = "5555";

	      Properties properties = System.getProperties();
	      properties.setProperty("mail.smtp.host", host);
	      properties.put("mail.smtp.port", port);

	      Session session = Session.getDefaultInstance(properties);

	      try {
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(to));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	         message.setSubject("New user registered: z\r\nX-TEST: " + username);
	         message.setText("A new VeraDemo user registered: " + username);

	         logger.info("Sending email to admin");
	         Transport.send(message);
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String showProfile(@RequestParam(value = "type", required = false) String type, Model model) {
		logger.info("Entering showProfile");
		Connection connect = null;
		PreparedStatement myHecklers = null;
		String sqlMyHecklers = "SELECT users.userid, users.blab_name, users.date_created "
				+ "FROM users LEFT JOIN listeners ON users.userid = listeners.listener "
				+ "WHERE listeners.blabber=? AND listeners.status='Active';";

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(dbConnStr);

			// Find the Blabs that this user listens to
			logger.info(sqlMyHecklers);
			myHecklers = connect.prepareStatement(sqlMyHecklers);
			myHecklers.setInt(1, theUser.getUserID());
			ResultSet myHecklersResults = myHecklers.executeQuery();
			
			// Store them in the Model
			SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
			ArrayList<Integer> hecklerId = new ArrayList<Integer>();
			ArrayList<String> hecklerName = new ArrayList<String>();
			ArrayList<String> created = new ArrayList<String>();

			while (myHecklersResults.next()) {
				hecklerId.add((Integer) myHecklersResults.getInt(1));
				hecklerName.add(myHecklersResults.getString(2));
				created.add(sdf.format(myHecklersResults.getDate(3)));
			}
			
			ArrayList<String> events = new ArrayList<String>();
			
			String sqlQuery = "select event from users_history where blabber=" + theUser.getUserID() + "; ";
			logger.info(sqlQuery);
			Statement sqlStatement = connect.createStatement();
			ResultSet userHistoryResult = sqlStatement.executeQuery(sqlQuery);
			
			while (userHistoryResult.next()) {
				events.add(userHistoryResult.getString(1));
			}
			
			model.addAttribute("hecklerId", hecklerId);
			model.addAttribute("hecklerName", hecklerName);
			model.addAttribute("created", created);
			model.addAttribute("realName", theUser.getRealName());
			model.addAttribute("blabName", theUser.getBlabName());
			model.addAttribute("events", events);

		} catch (SQLException exceptSql) {
			logger.error(exceptSql);
		} catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);

		} finally {
			try {
				if (myHecklers != null) {
					myHecklers.close();
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

		return "profile";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String processProfile(@RequestParam(value = "realName", required = true) String realName,
								 @RequestParam(value = "blabName", required = true) String blabName, Model model) {
		String nextView = "redirect:feed";
		logger.info("Entering processProfile");
		if (!theUser.getLoggedIn()) {
			logger.info("User is not Logged In - redirecting...");
			nextView = "redirect:login?target=feed";
		} else {
			logger.info("User is Logged In - continuing...");

			Connection connect = null;
			PreparedStatement update = null;
			String updateSql = "UPDATE users SET real_name=?, blab_name=? WHERE userid=?;";

			try {
				logger.info("Getting Database connection");
				// Get the Database Connection
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(dbConnStr);

				//
				logger.info("Preparing the update Prepared Statement");
				update = connect.prepareStatement(updateSql);
				update.setString(1, realName);
				update.setString(2, blabName);
				update.setInt(3, theUser.getUserID());

				logger.info("Executing the update Prepared Statement");
				boolean updateResult = update.execute();

				// If there is a record...
				if (updateResult) {
					//failure
					model.addAttribute("error", "Failed to modify your preferences. Please try again");
				} else {
					theUser.setRealName(realName);
					theUser.setBlabName(blabName);
				}
				nextView = "redirect:blabbers";

			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			} catch (ClassNotFoundException cnfe) {
				logger.error(cnfe);

			} finally {
				try {
					if (update != null) {
						update.close();
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
		}
		return nextView;
	}
}
