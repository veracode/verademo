package com.veracode.verademo.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Blab {

	private int id;
	private String content;
	private Date postDate;
	private int commentCount;
	private Blabber author;

	private final SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");

	public Blab() {
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

	public Date getPostDate()
	{
		return postDate;
	}

	public String getPostDateString()
	{
		return sdf.format(postDate);
	}

	public void setPostDate(Date postDate)
	{
		this.postDate = postDate;
	}

	public Blabber getAuthor()
	{
		return author;
	}

	public void setAuthor(Blabber author)
	{
		this.author = author;
	}

	public int getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}
}
