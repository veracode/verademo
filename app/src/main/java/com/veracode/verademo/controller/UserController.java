package com.veracode.verademo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.veracode.verademo.model.Blabber;
import com.veracode.verademo.utils.Constants;
import com.veracode.verademo.utils.User;
import com.veracode.verademo.utils.UserFactory;
import com.veracode.verademo.utils.Utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author johnadmin
 */
@Controller
@Scope("request")
public class UserController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:UserController");

	@Autowired
	ServletContext context;

	@Autowired
	private Environment env;

	/**
	 * @param target
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(
			@RequestParam(value = "target", required = false) String target,
			@RequestParam(value = "username", required = false) String username,
			Model model,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		// Check if user is already logged in
		if (httpRequest.getSession().getAttribute("username") != null) {
			logger.info("User is already logged in - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return Utils.redirect("feed");
			}
		}

		User user = UserFactory.createFromRequest(httpRequest);
		if (user != null) {
			Utils.setSessionUserName(httpRequest, httpResponse, user.getUserName());
			logger.info("User is remembered - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return Utils.redirect("feed");
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
	public String processLogin(
			@RequestParam(value = "user", required = true) String username,
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
			nextView = Utils.redirect("feed");
		}

		Connection connect = null;
		Statement sqlStatement = null;

		try {
			// Get the Database Connection
			logger.info("Creating the Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			/* START EXAMPLE VULNERABILITY */
			// Execute the query
			logger.info("Creating the Statement");
			String sqlQuery = "select username, password, password_hint, created_at, last_login, real_name, blab_name from users where username='"
					+ username + "' and password='" + md5(password) + "';";
			sqlStatement = connect.createStatement();
			logger.info("Execute the Statement");
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			/* END EXAMPLE VULNERABILITY */

			// Did we find exactly 1 user that matched?
			if (result.first()) {
				logger.info("User Found.");
				// Remember the username as a courtesy.
				Utils.setUsernameCookie(response, result.getString("username"));

				// If the user wants us to auto-login, store the user details as a cookie.
				if (remember != null) {
					User currentUser = new User(result.getString("username"), result.getString("password_hint"),
							result.getTimestamp("created_at"), result.getTimestamp("last_login"),
							result.getString("real_name"), result.getString("blab_name"));

					UserFactory.updateInResponse(currentUser, response);
				}

				Utils.setSessionUserName(req, response, result.getString("username"));

				// Update last login timestamp
				PreparedStatement update = connect.prepareStatement("UPDATE users SET last_login=NOW() WHERE username=?;");
				update.setString(1, result.getString("username"));
				update.execute();
			} else {
				// Login failed...
				logger.info("User Not Found");
				model.addAttribute("error", "Login failed. Please try again.");
				model.addAttribute("target", target);
				nextView = "login";
			}
		} catch (SQLException exceptSql) {
			logger.error(exceptSql);
			model.addAttribute("error", exceptSql.getMessage() + "<br/>" + displayErrorForWeb(exceptSql));
			model.addAttribute("target", target);
			nextView = "login";
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

		// Redirect to the appropriate place based on login actions above
		logger.info("Redirecting to view: " + nextView);
		return nextView;
	}

	@RequestMapping(value = "/password-hint", method = RequestMethod.GET)
	@ResponseBody
	public String showPasswordHint(String username) {
		logger.info("Entering password-hint with username: " + username);

		if (username == null || username.isEmpty()) {
			return "No username provided, please type in your username first";
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			String sql = "SELECT password_hint FROM users WHERE username = '" + username + "'";
			logger.info(sql);
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.first()) {
				String password = result.getString("password_hint");
				String formatString = "Username '" + username + "' has password: %.2s%s";
				logger.info(formatString);
				return String.format(
						formatString,
						password,
						String.format("%0" + (password.length() - 2) + "d", 0).replace("0", "*"));
			} else {
				return "No password found for " + username;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "ERROR!";
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String processLogout(
			@RequestParam(value = "type", required = false) String type,
			Model model,
			HttpServletRequest req,
			HttpServletResponse response) {
		logger.info("Entering processLogout");

		Utils.setSessionUserName(req, response, null);

		User currentUser = null;
		UserFactory.updateInResponse(currentUser, response);
		logger.info("Redirecting to Login...");
		return Utils.redirect("login");
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
			HttpServletResponse httpResponse,
			Model model) {
		logger.info("Entering processRegister");
		Utils.setSessionUserName(httpRequest, httpResponse, username);

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
			} else {
				return "register-finish";
			}
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
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
			Model model) {
		logger.info("Entering processRegisterFinish");

		String username = (String) httpRequest.getSession().getAttribute("username");

		// Do the password and cpassword parameters match ?
		if (password.compareTo(cpassword) != 0) {
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

			/* START EXAMPLE VULNERABILITY */
			// Execute the query
			String mysqlCurrentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
					.format(Calendar.getInstance().getTime());
			StringBuilder query = new StringBuilder();
			query.append("insert into users (username, password, created_at, real_name, blab_name) values(");
			query.append("'" + username + "',");
			query.append("'" + md5(password) + "',");
			query.append("'" + mysqlCurrentDateTime + "',");
			query.append("'" + realName + "',");
			query.append("'" + blabName + "'");
			query.append(");");

			sqlStatement = connect.createStatement();
			sqlStatement.execute(query.toString());
			logger.info(query.toString());
			/* END EXAMPLE VULNERABILITY */

			emailUser(username);
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
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

		return Utils.redirect("login?username=" + username);
	}

	private void emailUser(String username) {
		String to = env.getProperty("mail.to");
		String from = env.getProperty("mail.from");
		String host = env.getProperty("mail.smtp.host");
		String port = env.getProperty("mail.smtp.port");

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);

		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			/* START EXAMPLE VULNERABILITY */
			message.setSubject(env.getProperty("mail.subject.new_user") + " " + username);
			/* END EXAMPLE VULNERABILITY */

			message.setText("A new VeraDemo user registered: " + username);

			logger.info("Sending email to admin");
			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String showProfile(
			@RequestParam(value = "type", required = false) String type,
			Model model,
			HttpServletRequest httpRequest) {
		logger.info("Entering showProfile");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=profile");
		}

		Connection connect = null;
		PreparedStatement myHecklers = null, myInfo = null;
		String sqlMyHecklers = "SELECT users.username, users.blab_name, users.created_at "
				+ "FROM users LEFT JOIN listeners ON users.username = listeners.listener "
				+ "WHERE listeners.blabber=? AND listeners.status='Active';";

		try {
			logger.info("Getting Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabbers that this user listens to
			logger.info(sqlMyHecklers);
			myHecklers = connect.prepareStatement(sqlMyHecklers);
			myHecklers.setString(1, username);
			ResultSet myHecklersResults = myHecklers.executeQuery();

			List<Blabber> hecklers = new ArrayList<Blabber>();
			while (myHecklersResults.next()) {
				Blabber heckler = new Blabber();
				heckler.setUsername(myHecklersResults.getString(1));
				heckler.setBlabName(myHecklersResults.getString(2));
				heckler.setCreatedDate(myHecklersResults.getDate(3));
				hecklers.add(heckler);
			}

			// Get the audit trail for this user
			ArrayList<String> events = new ArrayList<String>();

			/* START EXAMPLE VULNERABILITY */
			String sqlMyEvents = "select event from users_history where blabber=\"" + username
					+ "\" ORDER BY eventid DESC; ";
			logger.info(sqlMyEvents);
			Statement sqlStatement = connect.createStatement();
			ResultSet userHistoryResult = sqlStatement.executeQuery(sqlMyEvents);
			/* END EXAMPLE VULNERABILITY */

			while (userHistoryResult.next()) {
				events.add(userHistoryResult.getString(1));
			}

			// Get the users information
			String sql = "SELECT username, real_name, blab_name FROM users WHERE username = '" + username + "'";
			logger.info(sql);
			myInfo = connect.prepareStatement(sql);
			ResultSet myInfoResults = myInfo.executeQuery();
			myInfoResults.next();

			// Send these values to our View
			model.addAttribute("hecklers", hecklers);
			model.addAttribute("events", events);
			model.addAttribute("username", myInfoResults.getString("username"));
			model.addAttribute("image", getProfileImageNameFromUsername(myInfoResults.getString("username")));
			model.addAttribute("realName", myInfoResults.getString("real_name"));
			model.addAttribute("blabName", myInfoResults.getString("blab_name"));
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
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
	public String processProfile(
			@RequestParam(value = "realName", required = true) String realName,
			@RequestParam(value = "blabName", required = true) String blabName,
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "file", required = false) MultipartFile file,
			MultipartHttpServletRequest request,
			HttpServletResponse response) {
		logger.info("Entering processProfile");

		String sessionUsername = (String) request.getSession().getAttribute("username");
		// Ensure user is logged in
		if (sessionUsername == null) {
			logger.info("User is not Logged In - redirecting...");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return "{\"message\": \"<script>alert('Error - please login');</script>\"}";
		}

		logger.info("User is Logged In - continuing... UA=" + request.getHeader("User-Agent") + " U=" + sessionUsername);

		String oldUsername = sessionUsername;

		// Update user information
		Connection connect = null;
		PreparedStatement update = null;
		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			logger.info("Preparing the update Prepared Statement");
			update = connect.prepareStatement("UPDATE users SET real_name=?, blab_name=? WHERE username=?;");
			update.setString(1, realName);
			update.setString(2, blabName);
			update.setString(3, sessionUsername);

			logger.info("Executing the update Prepared Statement");
			boolean updateResult = update.execute();

			// If there is a record...
			if (updateResult) {
				// failure
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return "{\"message\": \"<script>alert('An error occurred, please try again.');</script>\"}";
			}
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
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

		// Rename profile image if username changes
		if (!username.equals(oldUsername)) {
			if (usernameExists(username)) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return "{\"message\": \"<script>alert('That username already exists. Please try another.');</script>\"}";
			}

			if (!updateUsername(oldUsername, username)) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return "{\"message\": \"<script>alert('An error occurred, please try again.');</script>\"}";
			}

			// Update all session and cookie logic
			Utils.setSessionUserName(request, response, username);
			Utils.setUsernameCookie(response, username);

			// Update remember me functionality
			User currentUser = UserFactory.createFromRequest(request);
			if (currentUser != null) {
				currentUser.setUserName(username);
				UserFactory.updateInResponse(currentUser, response);
			}
		}

		// Update user profile image
		if (file != null && !file.isEmpty()) {
			String imageDir = context.getRealPath("/resources/images") + File.separator;

			// Get old image name, if any, to delete
			String oldImage = getProfileImageNameFromUsername(username);
			if (oldImage != null) {
				new File(imageDir + oldImage).delete();
			}

			// TODO: check if file is png first
			try {
				String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				String path = imageDir + username + extension;

				logger.info("Saving new profile image: " + path);

				file.transferTo(new File(path)); // will delete any existing file first
			} catch (IllegalStateException | IOException ex) {
				logger.error(ex);
			}
		}

		response.setStatus(HttpServletResponse.SC_OK);
		String msg = "Successfully changed values!\\\\nusername: %1$s\\\\nReal Name: %2$s\\\\nBlab Name: %3$s";
		String respTemplate = "{\"values\": {\"username\": \"%1$s\", \"realName\": \"%2$s\", \"blabName\": \"%3$s\"}, \"message\": \"<script>alert('"
				+ msg + "');</script>\"}";
		return String.format(respTemplate, username.toLowerCase(), realName, blabName);
	}

	@RequestMapping(value = "/downloadprofileimage", method = RequestMethod.GET)
	public String downloadImage(
			@RequestParam(value = "image", required = true) String imageName,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("Entering downloadImage");

		// Ensure user is logged in
		String sessionUsername = (String) request.getSession().getAttribute("username");
		if (sessionUsername == null) {
			logger.info("User is not Logged In - redirecting...");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Utils.redirect("login?target=profile");
		}

		logger.info("User is Logged In - continuing... UA=" + request.getHeader("User-Agent") + " U=" + sessionUsername);

		String path = context.getRealPath("/resources/images") + File.separator + imageName;

		logger.info("Fetching profile image: " + path);

		InputStream inputStream = null;
		OutputStream outStream = null;
		try {
			File downloadFile = new File(path);
			inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = context.getMimeType(path);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			logger.info("MIME type: " + mimeType);

			// Set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=" + imageName);

			// get output stream of the response
			outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			outStream.flush();
		} catch (IllegalStateException | IOException ex) {
			logger.error(ex);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
		}

		return "profile";
	}

	/**
	 * Check if the username already exists
	 *
	 * @param username The username to check
	 * @return true if the username exists, false otherwise
	 */
	private boolean usernameExists(String username) {
		username = username.toLowerCase();

		// Check is the username already exists
		Connection connect = null;
		PreparedStatement sqlStatement = null;
		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			logger.info("Preparing the duplicate username check Prepared Statement");
			sqlStatement = connect.prepareStatement("SELECT username FROM users WHERE username=?");
			sqlStatement.setString(1, username);
			ResultSet result = sqlStatement.executeQuery();

			if (!result.first()) {
				// username does not exist
				return false;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (sqlStatement != null) {
					sqlStatement.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}

		logger.info("Username: " + username + " already exists. Try again.");
		return true;
	}

	/**
	 * Change the user's username. Since the username is the DB key, we have a lot
	 * to do
	 *
	 * @param oldUsername Prior username
	 * @param newUsername Desired new username
	 * @return
	 */
	private boolean updateUsername(String oldUsername, String newUsername) {
		// Enforce all lowercase usernames
		oldUsername = oldUsername.toLowerCase();
		newUsername = newUsername.toLowerCase();

		// Check is the username already exists
		Connection connect = null;
		List<PreparedStatement> sqlUpdateQueries = new ArrayList<PreparedStatement>();
		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());
			connect.setAutoCommit(false);

			// Update all references to this user
			String[] sqlStrQueries = new String[] {
					"UPDATE users SET username=? WHERE username=?",
					"UPDATE blabs SET blabber=? WHERE blabber=?",
					"UPDATE comments SET blabber=? WHERE blabber=?",
					"UPDATE listeners SET blabber=? WHERE blabber=?",
					"UPDATE listeners SET listener=? WHERE listener=?",
					"UPDATE users_history SET blabber=? WHERE blabber=?" };
			for (String sql : sqlStrQueries) {
				logger.info("Preparing the Prepared Statement: " + sql);
				sqlUpdateQueries.add(connect.prepareStatement(sql));
			}

			// Execute updates as part of a batch transaction
			// This will roll back all changes if one query fails
			for (PreparedStatement stmt : sqlUpdateQueries) {
				stmt.setString(1, newUsername);
				stmt.setString(2, oldUsername);
				stmt.executeUpdate();
			}
			connect.commit();

			// Rename the user profile image to match new username
			String oldImage = getProfileImageNameFromUsername(oldUsername);
			if (oldImage != null) {
				String extension = oldImage.substring(oldImage.lastIndexOf("."));

				logger.info("Renaming profile image from " + oldImage + " to " + newUsername + extension);
				String path = context.getRealPath("/resources/images") + File.separator;

				File oldName = new File(path + oldImage);
				File newName = new File(path + newUsername + extension);
				oldName.renameTo(newName);
			}

			return true;
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (sqlUpdateQueries != null) {
					for (PreparedStatement stmt : sqlUpdateQueries) {
						stmt.close();
					}
				}
			} catch (SQLException e) {
				logger.error(e);
			}
			try {
				if (connect != null) {
					logger.error("Transaction is being rolled back");
					connect.rollback();
					connect.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}

		// Error occurred
		return false;
	}

	private String getProfileImageNameFromUsername(final String username) {
		File f = new File(context.getRealPath("/resources/images"));
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(username + ".");
			}
		});

		if (matchingFiles.length < 1) {
			return null;
		}
		return matchingFiles[0].getName();
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

			/* START EXAMPLE VULNERABILITY */
			message.setSubject("Error detected: " + t.getMessage());
			/* END EXAMPLE VULNERABILITY */

			message.setText(t.getMessage() + "<br>" + properties.getProperty("test") + displayErrorForWeb(t));

			logger.info("Sending email to admin");
			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	private static String md5(String val) {
		MessageDigest md;
		String ret = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(val.getBytes());
			byte[] digest = md.digest();
			ret = DatatypeConverter.printHexBinary(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return ret;
	}
}
