package com.veracode.verademo.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a Blab User
 */
public class Blabber {

	private int id;
	private String username;
	private String realName;
	private String blabName;
	private Date createdDate;
	private int numberListeners;
	private int numberListening;

	private final SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");

	public Blabber() {
		// Empty constructor
	}

	public Blabber(String username, String blabName, Date created) {
		this.username = username;
		this.blabName = blabName;
		this.createdDate = created;
	}

	public Blabber(int id, String username, String realName, String blabName, Date created) {
		this.id = id;
		this.username = username;
		this.realName = realName;
		this.blabName = blabName;
		this.createdDate = created;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getBlabName()
	{
		return blabName;
	}

	public void setBlabName(String blabName)
	{
		this.blabName = blabName;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public String getCreatedDateString()
	{
		return sdf.format(createdDate);
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public int getNumberListeners()
	{
		return numberListeners;
	}

	public void setNumberListeners(int numberListeners)
	{
		this.numberListeners = numberListeners;
	}

	public int getNumberListening()
	{
		return numberListening;
	}

	public void setNumberListening(int numberListening)
	{
		this.numberListening = numberListening;
	}

}
