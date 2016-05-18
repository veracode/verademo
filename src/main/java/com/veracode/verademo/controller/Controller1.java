package com.veracode.verademo.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class Controller1 {

	private ApplicationContext applicationContext = null;
	private boolean debug = true;
	private String dbConnStr = "jdbc:mysql://localhost/discoveryminer?user=discovery&password=veN9E2Grij6eIt";
	
	//////////////////////////////////////////////////////////////
	// GET Request handler for the / path
	//
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(@RequestParam(value="type", required=false) String type, Model model) {

		
		Statement sqlStatement = null;
		String sqlQuery = "insert into users (status) values ('updated') where name='"+type+"'";
		
//		PreparedStatement sqlStatement = null;
//		String sqlQuery = "insert into users (status) values ('updated') where name=?";

        Connection connect = null;

		try {
			// Get the Database Connection
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbConnStr);
			
			sqlStatement = connect.createStatement();
			Boolean result = sqlStatement.execute(sqlQuery);

//			sqlStatement = connect.prepareStatement(sqlQuery);
//			sqlStatement.setString(1, type);
//			Boolean result = sqlStatement.execute();

		}catch (SQLException exceptSql) {
                //IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
        } catch (ClassNotFoundException cnfe) {
        	
        } finally {
        	try {
                if (sqlStatement != null) {
                    sqlStatement.close();
                }
        	} catch (SQLException exceptSql) {
                //IO.logger.log(Level.WARNING, "Error closing Statement", exceptSql);
            }
        	try {
                if (connect != null){
                    connect.close();
                }
            } catch (SQLException exceptSql) {
                //IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
            }
        }
		return "start";
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;
	}
	
}
