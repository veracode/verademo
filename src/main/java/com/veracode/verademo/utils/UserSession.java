package com.veracode.verademo.utils;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class UserSession implements Serializable{

	private int userID = 0;
	private String userName = null;
	private String blabName = null;
	private String realName = null;
	private boolean loggedIn = false;
	
	public void setUserID(int value) {
		userID = value;
	}
	public int getUserID() {
		return userID;
	}
	public void setUsername(String value) {
		userName = value;
	}
	public String getUsername() {
		return userName;
	}
	public void setRealName(String value) {
		realName = value;
	}
	public String getRealName() {
		return realName;
	}
	public void setBlabName(String value) {
		blabName = value;
	}
	public String getBlabName() {
		return blabName;
	}
	public void setLoggedIn(boolean value) {
		loggedIn = value;
	}
	public boolean getLoggedIn() {
		return loggedIn;
	}
}
