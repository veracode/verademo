package com.veracode.verademo.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veracode.verademo.utils.Constants;
import com.veracode.verademo.utils.User;
import com.veracode.verademo.utils.UserFactory;

/**
 * @author johnadmin
 */
@Controller
@Scope("request")
public class UserController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:UserController");

	@Autowired
	ServletContext context;
	
	/**
	 * @param target
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(@RequestParam(value = "target", required = false) String target,
							@RequestParam(value = "username", required=false) String username,
							Model model,
							HttpServletRequest httpRequest,
							HttpServletResponse httpResponse)
	{
		// Check if user is already logged in
		if (httpRequest.getSession().getAttribute("username") != null) {
			logger.info("User is already logged in - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return "redirect:feed";
			}
		}
		
		User user = UserFactory.createFromRequest(httpRequest);
		if (user != null) {
			httpRequest.getSession().setAttribute("username", user.getUserName());
			logger.info("User is remembered - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return "redirect:feed";
			}
		} else {
			logger.info("User is not remembered");
		}
		
		if (username == null) {
			username = "";
		}
		
		if (target == null) {
			target = "";
		}
		
		logger.info("Entering showLogin with username " + username + " and target " + target);
		
		model.addAttribute("username", username);
		model.addAttribute("target", target);
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
							   @RequestParam(value = "remember", required = false) String remember,
							   @RequestParam(value = "target", required = false) String target,
							   Model model,
							   HttpServletRequest req,
							   HttpServletResponse response) {
		logger.info("Entering processLogin");

		// Determine eventual redirect. Do this here in case we're already logged in
		String nextView;
		if (target != null && !target.isEmpty() && !target.equals("null")) {
			nextView = "redirect:" + target;
		} else {
			// default to user's feed
			nextView = "redirect:feed";
		}
		
		Connection connect = null;
		Statement sqlStatement = null;

		try {
			// Get the Database Connection
			logger.info("Creating the Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			/* START BAD CODE */
			// Execute the query
			logger.info("Creating the Statement");
			String sqlQuery = "select username, password, created_at, last_login, real_name, blab_name from users where username='" + username + "' and password='" + password + "';";
			sqlStatement = connect.createStatement();
			logger.info("Execute the Statement");
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			/* END BAD CODE */

			// Did we find exactly 1 user that matched?
			if (result.first()) {
				logger.info("User Found.");
				// Remember the username as a courtesy.
				response.addCookie(new Cookie("username", username));
				
				// If the user wants us to auto-login, store the user details as a cookie.
				if (remember != null) {
					User currentUser = new User(
							result.getString("username"),
							result.getString("password"),
							result.getTimestamp("created_at"),
							result.getTimestamp("last_login"),
							result.getString("real_name"),
							result.getString("blab_name")
					);
					
					UserFactory.updateInResponse(currentUser, response);
				}
				
				req.getSession().setAttribute("username", username);
			}
			else {
				// Login failed...
				logger.info("User Not Found");
				model.addAttribute("error", "Login failed. Please try again.");
				model.addAttribute("target", target);
				nextView = "login";
			}
		}
		catch (SQLException exceptSql) {
			logger.error(exceptSql);
			model.addAttribute("error", exceptSql.getMessage() + "<br/>" + displayErrorForWeb(exceptSql));
			model.addAttribute("target", target);
		}
		catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);
			model.addAttribute("error", cnfe.getMessage());
			model.addAttribute("target", target);

		}
		finally {
			try {
				if (sqlStatement != null) {
					sqlStatement.close();
				}
			}
			catch (SQLException exceptSql) {
				logger.error(exceptSql);
				model.addAttribute("error", exceptSql.getMessage());
				model.addAttribute("target", target);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			}
			catch (SQLException exceptSql) {
				logger.error(exceptSql);
				model.addAttribute("error", exceptSql.getMessage());
				model.addAttribute("target", target);
			}
		}
		
		// Redirect to the appropriate place based on login actions above
		logger.info("Redirecting to view: " + nextView);
		return nextView;
	}

	@RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
	public String processLogout(
			@RequestParam(value = "type", required = false) String type, 
			Model model,
			HttpServletRequest req,
			HttpServletResponse response
		) {
		logger.info("Entering processLogout");
		
		req.getSession().setAttribute("username", null);
		
		User currentUser = null;
		UserFactory.updateInResponse(currentUser, response);
		logger.info("Redirecting to Login...");
		return "redirect:login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegister() {
		logger.info("Entering showRegister");

		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegister(
			@RequestParam(value = "user") String username, 
			HttpServletRequest httpRequest,
			Model model
		) {
		logger.info("Entering processRegister");
		
		// Get the Database Connection
		logger.info("Creating the Database connection");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());
			
			String sql = "SELECT username FROM users WHERE username = '" + username + "'";
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.first()) {
				model.addAttribute("error", "Username '" + username + "' already exists!");
				return "register";
			}
			else {
				httpRequest.getSession().setAttribute("username", username);
				return "register-finish";
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "register";
	}
	
	
	@RequestMapping(value = "/register-finish", method = RequestMethod.GET)
	public String showRegisterFinish() {
		logger.info("Entering showRegisterFinish");
		
		return "register-finish";
	}

	@RequestMapping(value = "/register-finish", method = RequestMethod.POST)
	public String processRegisterFinish(
								  @RequestParam(value = "password", required = true) String password,
								  @RequestParam(value = "cpassword", required = true) String cpassword,
								  @RequestParam(value = "realName", required = true) String realName,
								  @RequestParam(value = "blabName", required = true) String blabName,
								  HttpServletRequest httpRequest,
								  HttpServletResponse response,
								  Model model
		) {
		logger.info("Entering processRegisterFinish");
		
		String username = (String) httpRequest.getSession().getAttribute("username");

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
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			/* START BAD CODE */
			// Execute the query
			String mysqlCurrentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
			StringBuilder query = new StringBuilder();
			query.append("insert into users (username, password, created_at, real_name, blab_name) values(");
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
		return "redirect:login?username=" + username;
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
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			/* START BAD CODE */
			message.setSubject("New user registered: " + username);
			/* END BAD CODE */
			
			message.setText("A new VeraDemo user registered: " + username);

			logger.info("Sending email to admin");
			Transport.send(message);
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String showProfile(
			@RequestParam(value = "type", required = false) String type, 
			Model model,
			HttpServletRequest httpRequest)
	{
		logger.info("Entering showProfile");
		
		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return "redirect:login?target=profile";
		}
		
		Connection connect = null;
		PreparedStatement myHecklers = null, myInfo = null;
		String sqlMyHecklers = "SELECT users.username, users.blab_name, users.created_at "
				+ "FROM users LEFT JOIN listeners ON users.username = listeners.listener "
				+ "WHERE listeners.blabber=? AND listeners.status='Active';";
		
		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabs that this user listens to
			logger.info(sqlMyHecklers);
			myHecklers = connect.prepareStatement(sqlMyHecklers);
			myHecklers.setString(1, username);
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
			
			String sqlQuery = "select event from users_history where blabber=\"" + username + "\" ORDER BY eventid DESC; ";
			logger.info(sqlQuery);
			Statement sqlStatement = connect.createStatement();
			ResultSet userHistoryResult = sqlStatement.executeQuery(sqlQuery);
			
			while (userHistoryResult.next()) {
				events.add(userHistoryResult.getString(1));
			}
			
			// Get the users information
			String sql = "SELECT username, real_name, blab_name FROM users WHERE username = '" + username + "'";
			logger.info(sql);
			myInfo = connect.prepareStatement(sql);
			ResultSet myInfoResults = myInfo.executeQuery();
			myInfoResults.next();
			
			model.addAttribute("hecklerId", hecklerId);
			model.addAttribute("hecklerName", hecklerName);
			model.addAttribute("created", created);
			model.addAttribute("userID", currentUser.getUserID());
			model.addAttribute("realName", myInfoResults.getString("real_name"));
			model.addAttribute("blabName", myInfoResults.getString("blab_name"));
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

	@RequestMapping(value = "/profile", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String processProfile(@RequestParam(value = "realName", required = true) String realName,
								 @RequestParam(value = "blabName", required = true) String blabName, 
								 HttpServletRequest httpRequest, 
								 HttpServletResponse response
	) {
								 @RequestParam(value = "blabName", required = true) String blabName,
								 @RequestParam(value = "file", required = true) MultipartFile file,
								 MultipartHttpServletRequest request,
								 HttpServletResponse response)
	{
		logger.info("Entering processProfile");
		
		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return "redirect:login?target=profile";
		}

		logger.info("User is Logged In - continuing...");

		Connection connect = null;
		PreparedStatement update = null;
		String updateSql = "UPDATE users SET real_name=?, blab_name=? WHERE username=?;";

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			//
			logger.info("Preparing the update Prepared Statement");
			update = connect.prepareStatement(updateSql);
			update.setString(1, realName);
			update.setString(2, blabName);
			update.setString(3, username);

			logger.info("Executing the update Prepared Statement");
			boolean updateResult = update.execute();

			// If there is a record...
			if (updateResult) {
				//failure
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return "{\"message\": \"<script>alert('An error occurred, please try again.');</script>\"}";
			}
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
			catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}
		
		System.out.println("processing upload!");
		
		// Update user profile image
		if (file != null && !file.isEmpty()) {
            try {
            	String path = context.getRealPath("/resources/images")
            			+ File.separator
            			+ file.getOriginalFilename();
            	
            	System.out.println(path);
            	
                File destinationFile = new File(path);
				file.transferTo(destinationFile);
			}
            catch (IllegalStateException | IOException ex) {
				logger.error(ex);
				System.out.println(ex);
			}
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		String respTemplate = "{\"values\": {\"realName\": \"%s\", \"blabName\": \"%s\"}, \"message\": \"<script>alert('Blab Name changed to %s');</script>\"}";
		return String.format(respTemplate, realName, blabName, blabName);
	}
	
	public String displayErrorForWeb(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String stackTrace = sw.toString();
		pw.flush();
		pw.close();
		return stackTrace.replace(System.getProperty("line.separator"), "<br/>\n");
	}
	
	public void emailExceptionsToAdmin(Throwable t) {
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
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			/* START BAD CODE */
			message.setSubject("Error detected: " + t.getMessage());
			/* END BAD CODE */
			
			message.setText(t.getMessage() + "<br>" + properties.getProperty("test") +  displayErrorForWeb(t));

			logger.info("Sending email to admin");
			Transport.send(message);
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
