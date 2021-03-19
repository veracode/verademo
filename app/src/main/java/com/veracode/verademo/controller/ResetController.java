package com.veracode.verademo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Random;

import javax.servlet.ServletContext;

import com.veracode.verademo.utils.Constants;
import com.veracode.verademo.utils.User;
import com.veracode.verademo.utils.Utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Scope("request")
public class ResetController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:ResetController");

	@Autowired
	ServletContext context;

	private static User[] users = new User[] {
			User.create("admin", "admin", "Thats Mr Administrator to you."),
			User.create("john", "John", "John Smith"),
			User.create("paul", "Paul", "Paul Farrington"),
			User.create("chrisc", "Chris", "Chris Campbell"),
			User.create("laurie", "Laurie", "Laurie Mercer"),
			User.create("nabil", "Nabil", "Nabil Bousselham"),
			User.create("julian", "Julian", "Julian Totzek-Hallhuber"),
			User.create("joash", "Joash", "Joash Herbrink"),
			User.create("andrzej", "Andrzej", "Andrzej Szaryk"),
			User.create("april", "April", "April Sauer"),
			User.create("armando", "Armando", "Armando Bioc"),
			User.create("ben", "Ben", "Ben Stoll"),
			User.create("brian", "Brian", "Brian Pitta"),
			User.create("caitlin", "Caitlin", "Caitlin Johanson"),
			User.create("christraut", "Chris Trautwein", "Chris Trautwein"),
			User.create("christyson", "Chris Tyson", "Chris Tyson"),
			User.create("clint", "Clint", "Clint Pollock"),
			User.create("cody", "Cody", "Cody Bertram"),
			User.create("derek", "Derek", "Derek Chowaniec"),
			User.create("glenn", "Glenn", "Glenn Whittemore"),
			User.create("grant", "Grant", "Grant Robinson"),
			User.create("gregory", "Gregory", "Gregory Wolford"),
			User.create("jacob", "Jacob", "Jacob Martel"),
			User.create("jeremy", "Jeremy", "Jeremy Anderson"),
			User.create("johnny", "Johnny", "Johnny Wong"),
			User.create("kevin", "Kevin", "Kevin Rise"),
			User.create("scottrum", "Scott Rumrill", "Scott Rumrill"),
			User.create("scottsim", "Scott Simpson", "Scott Simpson") };

	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String showReset() {
		logger.info("Entering showReset");

		return "reset";
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String processReset(
			@RequestParam(value = "confirm", required = true) String confirm,
			@RequestParam(value = "primary", required = false) String primary,
			Model model) {
		logger.info("Entering processReset");

		Connection connect = null;
		PreparedStatement usersStatement = null;
		PreparedStatement listenersStatement = null;
		PreparedStatement blabsStatement = null;
		PreparedStatement commentsStatement = null;
		java.util.Date now = new java.util.Date();

		Random rand = new Random();

		// Drop existing tables and recreate from schema file
		recreateDatabaseSchema();

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());
			logger.info(connect);
			connect.setAutoCommit(false);

			// Add the users
			logger.info("Preparing the Stetement for adding users");
			usersStatement = connect.prepareStatement(
					"INSERT INTO users (username, password, password_hint, created_at, last_login, real_name, blab_name) values (?, ?, ?, ?, ?, ?, ?);");
			for (int i = 0; i < users.length; i++) {
				logger.info("Adding user " + users[i].getUserName());
				usersStatement.setString(1, users[i].getUserName());
				usersStatement.setString(2, users[i].getPassword());
				usersStatement.setString(3, users[i].getPasswordHint());
				usersStatement.setTimestamp(4, users[i].getDateCreated());
				usersStatement.setTimestamp(5, users[i].getLastLogin());
				usersStatement.setString(6, users[i].getRealName());
				usersStatement.setString(7, users[i].getBlabName());

				usersStatement.executeUpdate();
			}
			connect.commit();

			// Add the listeners
			logger.info("Preparing the Stetement for adding listeners");
			listenersStatement = connect
					.prepareStatement("INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');");
			for (int i = 1; i < users.length; i++) {
				for (int j = 1; j < users.length; j++) {
					if (rand.nextBoolean() && i != j) {
						String blabber = users[i].getUserName();
						String listener = users[j].getUserName();

						logger.info("Adding " + listener + " as a listener of " + blabber);

						listenersStatement.setString(1, blabber);
						listenersStatement.setString(2, listener);

						listenersStatement.executeUpdate();
					}
				}
			}
			connect.commit();

			// Fetch pre-loaded Blabs
			logger.info("Reading blabs from file");
			String[] blabsContent = loadFile("blabs.txt");

			// Add the blabs
			logger.info("Preparing the Statement for adding blabs");
			blabsStatement = connect
					.prepareStatement("INSERT INTO blabs (blabber, content, timestamp) values (?, ?, ?);");
			for (String blabContent : blabsContent) {
				// Get the array offset for a random user, except admin who's offset 0.
				int randomUserOffset = rand.nextInt(users.length - 2) + 1;

				// get the number or seconds until some time in the last 30 days.
				long vary = rand.nextInt(30 * 24 * 3600);

				String username = users[randomUserOffset].getUserName();
				logger.info("Adding a blab for " + username);

				blabsStatement.setString(1, username);
				blabsStatement.setString(2, blabContent);
				blabsStatement.setTimestamp(3, new Timestamp(now.getTime() - (vary * 1000)));

				blabsStatement.executeUpdate();
			}
			connect.commit();

			// Fetch pre-loaded Comments
			logger.info("Reading comments from file");
			String[] commentsContent = loadFile("comments.txt");

			// Add the comments
			logger.info("Preparing the Statement for adding comments");
			commentsStatement = connect.prepareStatement(
					"INSERT INTO comments (blabid, blabber, content, timestamp) values (?, ?, ?, ?);");
			for (int i = 1; i <= blabsContent.length; i++) {
				// Add a random number of comment
				int count = rand.nextInt(6); // (between 0 and 6)

				for (int j = 0; j < count; j++) {
					// Get the array offset for a random user, except admin who's offset 0.
					int randomUserOffset = rand.nextInt(users.length - 2) + 1;
					String username = users[randomUserOffset].getUserName();

					// Pick a random comment to add
					int commentNum = rand.nextInt(commentsContent.length);
					String comment = commentsContent[commentNum];

					// get the number or seconds until some time in the last 30 days.
					long vary = rand.nextInt(30 * 24 * 3600);

					logger.info("Adding a comment from " + username + " on blab ID " + String.valueOf(i));
					commentsStatement.setInt(1, i);
					commentsStatement.setString(2, username);
					commentsStatement.setString(3, comment);
					commentsStatement.setTimestamp(4, new Timestamp(now.getTime() - (vary * 1000)));

					commentsStatement.executeUpdate();
				}
			}
			connect.commit();
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (usersStatement != null) {
					usersStatement.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (listenersStatement != null) {
					listenersStatement.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (blabsStatement != null) {
					blabsStatement.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (commentsStatement != null) {
					commentsStatement.close();
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

		return Utils.redirect("reset");
	}

	/**
	 * Drop and recreate the entire database schema
	 */
	private void recreateDatabaseSchema() {
		// Fetch database schema
		logger.info("Reading database schema from file");
		String[] schemaSql = loadFile("blab_schema.sql", new String[] { "--", "/*" }, ";");

		Connection connect = null;
		Statement stmt = null;
		try {
			// Get the Database Connection
			logger.info("Getting Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			stmt = connect.createStatement();

			for (String sql : schemaSql) {
				sql = sql.trim(); // Remove any remaining whitespace
				if (!sql.isEmpty()) {
					logger.info("Executing: " + sql);
					System.out.println("Executing: " + sql);
					stmt.executeUpdate(sql);
				}
			}
		} catch (ClassNotFoundException | SQLException ex) {
			logger.error(ex);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				logger.error(ex);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException ex) {
				logger.error(ex);
			}
		}
	}

	/**
	 * Read a file from the non-web accessible resources directory. This overload
	 * discards no lines and separates content by newlines.
	 * 
	 * @param filename Name and extension of the file to read
	 * @return A String array containing the contents of the file broken by newlines
	 */
	private String[] loadFile(String filename) {
		return loadFile(filename, new String[0], System.lineSeparator());
	}

	/**
	 * Read a file from the non-web accessible resources directory
	 * 
	 * @param filename       Name and extension of the file to read
	 * @param skipCharacters A String array of sequences to skip, should the lines
	 *                       start with them
	 * @param delimiter      A String to break the contents of the file by
	 * @return A String array containing the contents of the file broken by the
	 *         delimiter
	 */
	private String[] loadFile(String filename, String[] skipCharacters, String delimiter) {
		String path = "/app/src/main/resources" + File.separator + filename;

		String regex = "";
		if (skipCharacters.length > 0) {
			String skipString = String.join("|", skipCharacters);
			skipString = skipString.replaceAll("(?=[]\\[+&!(){}^\"~*?:\\\\])", "\\\\");
			regex = "^(" + skipString + ").*?";
		}

		String[] lines = null;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));

			String line = br.readLine();
			while (line != null) {
				if (line.matches(regex)) {
					line = br.readLine();
					continue;
				}

				sb.append(line);
				sb.append(System.lineSeparator());

				line = br.readLine();
			}

			// Break content by delimiter
			lines = sb.toString().split(delimiter);
		} catch (IOException ex) {
			logger.error(ex);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
		}

		return lines;
	}
}
