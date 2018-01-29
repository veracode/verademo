package com.veracode.verademo.utils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import javax.xml.bind.DatatypeConverter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	private String hint;
	private Timestamp dateCreated;
	private Timestamp lastLogin;
	private String blabName;
	private String realName;

	public static User create(String userName, String blabName, String realName)
	{
		String password = userName;
		Timestamp dateCreated = new Timestamp(System.currentTimeMillis());
		Timestamp lastLogin = null;

		return new User(userName, password, dateCreated, lastLogin, blabName, realName);
	}

	public User(String userName, String password, Timestamp dateCreated, Timestamp lastLogin, String blabName,
			String realName) {
		this.userName = userName;
		this.password = md5(password);
		this.hint = password;
		this.dateCreated = dateCreated;
		this.lastLogin = lastLogin;
		this.blabName = blabName;
		this.realName = realName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public String setPassword(String password)
	{
		this.password = md5(password);
		return password;
	}
	
	public String getPasswordHint()
	{
		return hint;
	}

	public String setPasswordHint(String hint)
	{
		this.hint = hint;
		return hint;
	}

	public Timestamp getDateCreated()
	{
		return dateCreated;
	}

	public Timestamp getLastLogin()
	{
		return lastLogin;
	}

	public String getBlabName()
	{
		return blabName;
	}

	public String getRealName()
	{
		return realName;
	}
	
	private static String md5(String val)
	{
		MessageDigest md;
		String ret = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(val.getBytes());
		    byte[] digest = md.digest();
		    ret = DatatypeConverter.printHexBinary(digest);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
