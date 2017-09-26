package com.veracode.verademo.utils;

public class Constants {
	public final String JDBC_DRIVER;
	public final String JDBC_HOSTNAME;
	public final String JDBC_DATABASE;
	public final String JDBC_USER;
	public final String JDBC_PASSWORD;
	
	public Constants() {
		JDBC_DRIVER = "mysql";
		JDBC_HOSTNAME = "localhost";
		JDBC_DATABASE = "blab";
		JDBC_USER = "blab";
		/* START BAD CODE */
		JDBC_PASSWORD = "z2^E6J4$;u;d";
		/* END BAD CODE */
	}

	public static final Constants create() {
		return new Constants();
	}
	
	public final String getJdbcConnectionString() {
		return String.format(
				"jdbc:%s://%s/%s?user=%s&password=%s", 
				JDBC_DRIVER, 
				JDBC_HOSTNAME, 
				JDBC_DATABASE, 
				JDBC_USER, 
				JDBC_PASSWORD
		);
	}
}
