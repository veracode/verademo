package com.veracode.verademo.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {

	private int id;
	private String content;
	private Date timestamp;
	private Blabber author;

	private final SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");

	public Comment() {
		// Empty constructor
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Date getTimestamp()
	{
		return timestamp;
	}

	public String getTimestampString()
	{
		return sdf.format(timestamp);
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	public Blabber getAuthor()
	{
		return author;
	}

	public void setAuthor(Blabber author)
	{
		this.author = author;
	}

}
