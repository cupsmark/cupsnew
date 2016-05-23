package com.cupslicenew.core.model;

public class MFrameCategory {
	private int frame_category_id;
	private String frame_category_name, frame_category_uri, frame_category_desc, frame_category_status;
	
	public MFrameCategory()
	{
		frame_category_id = 0;
		frame_category_name = "";
		frame_category_uri = "";
		frame_category_desc = "";
		frame_category_status = "";
	}
	
	public void setFrameCategoryID(int frame_category_id)
	{
		this.frame_category_id = frame_category_id;
	}

	public int getFrameCategoryID()
	{
		return this.frame_category_id;
	}

	public void setFrameCategoryName(String name)
	{
		this.frame_category_name = name;
	}

	public String getFrameCategoryName()
	{
		return this.frame_category_name;
	}

	public void setFrameCategoryUri(String uri)
	{
		this.frame_category_uri = uri;
	}

	public String getFrameCategoryUri()
	{
		return this.frame_category_uri;
	}

	public void setFrameCategoryDesc(String desc)
	{
		this.frame_category_desc = desc;
	}

	public String getFrameCategoryDesc()
	{
		return this.frame_category_desc;
	}

	public void setFrameCategoryStatus(String status)
	{
		this.frame_category_status = status;
	}

	public String getFrameCategoryStatus()
	{
		return this.frame_category_status;
	}
	
}
