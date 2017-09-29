package com.veracode.verademo.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	private Timestamp dateCreated;
	private Timestamp lastLogin;
	private String blabName;
	private String realName;
	
	public static User create(String userName, String blabName, String realName) {
		String password = userName;
		Timestamp dateCreated = new Timestamp(System.currentTimeMillis());
		Timestamp lastLogin = null;
		
		return new User(userName, password, dateCreated, lastLogin, blabName, realName);
	}
	
	public User(String userName, String password, Timestamp dateCreated, Timestamp lastLogin, String blabName, String realName) {
		this.userName = userName;
		this.password = password;
		this.dateCreated = dateCreated;
		this.lastLogin = lastLogin;
		this.blabName = blabName;
		this.realName = realName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
	public String setPassword(String password) {
		this.password = password;
		return password;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public String getBlabName() {
		return blabName;
	}

	public String getRealName() {
		return realName;
	}
}
