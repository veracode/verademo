package com.veracode.verademo.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.veracode.verademo.utils.User;

public class RemoveAccountCommand implements BlabberCommand {
	private static final Logger logger = LogManager.getLogger("VeraDemo:RemoveAccountCommand");
	
	private Connection connect;
	
	public RemoveAccountCommand(Connection connect, User theUser) {
		super();
		this.connect = connect;
	}

	/* (non-Javadoc)
	 * @see com.veracode.verademo.commands.Command#execute()
	 */
	@Override
	public void execute(int blabberId) {
		String sqlQuery = "DELETE FROM listeners WHERE blabber=? OR listener=?;";
		logger.info(sqlQuery);
		PreparedStatement action;
		try {
			action = connect.prepareStatement(sqlQuery);
			
			action.setInt(1, blabberId);
			action.setInt(2, blabberId);
			action.execute();

			sqlQuery = "SELECT blab_name FROM users WHERE userid = " + blabberId;
			Statement sqlStatement = connect.createStatement();
			logger.info(sqlQuery);
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			result.next();
			
			/* START BAD CODE */
			String event = "Removed account for blabber " + result.getString(1);
			sqlQuery = "INSERT INTO users_history (blabber, event) VALUES ('" + blabberId + "', '" + event + "')";
			logger.info(sqlQuery);
			sqlStatement.execute(sqlQuery);
			
			sqlQuery = "DELETE FROM users WHERE userid = " + blabberId;
			logger.info(sqlQuery);
			sqlStatement.execute(sqlQuery);
			/* END BAD CODE */
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
