package com.veracode.verademo.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.veracode.verademo.utils.User;

public class ListenCommand implements BlabberCommand {
	private static final Logger logger = LogManager.getLogger("VeraDemo:ListenCommand");
	
	private Connection connect;
	
	private User theUser;
	
	public ListenCommand(Connection connect, User theUser) {
		super();
		this.connect = connect;
		this.theUser = theUser;
	}

	@Override
	public void execute(int blabberId) {
		String sqlQuery = "INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');";
		logger.info(sqlQuery);
		PreparedStatement action;
		try {
			action = connect.prepareStatement(sqlQuery);
			
			action.setInt(1, blabberId);
			action.setInt(2, theUser.getUserID());
			action.execute();
						
			sqlQuery = "SELECT username FROM users WHERE userid = " + blabberId;
			Statement sqlStatement = connect.createStatement();
			logger.info(sqlQuery);
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			result.next();
			
			/* START BAD CODE */
			String event = theUser.getBlabName() + " started listening to " + result.getString(1);
			sqlQuery = "INSERT INTO users_history (blabber, event) VALUES (\"" + theUser.getUserID() + "\", \"" + event + "\")";
			logger.info(sqlQuery);
			sqlStatement.execute(sqlQuery);
			/* END BAD CODE */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
