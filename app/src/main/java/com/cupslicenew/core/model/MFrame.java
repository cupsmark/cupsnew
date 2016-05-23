package com.cupslicenew.core.model;

public class MFrame {

	private int frameID, frameCategoryID;
	private String frameName, frameURI, frameDesc, frameStatus, frameColor;
	
	public MFrame()
	{
		frameID = 0;
		frameName = "";
		frameURI = "";
		frameDesc = "";
		frameCategoryID = 0;
		frameStatus = "";
		frameColor = "";
	}
	public void setFrameID(int frame_id)
	{
		this.frameID = frame_id;
	}

	public int getFrameID()
	{
		return this.frameID;
	}

	public void setFrameName(String frame_name)
	{
		this.frameName = frame_name;
	}

	public String getFrameName()
	{
		return this.frameName;
	}

	public void setFrameUri(String frame_uri)
	{
		this.frameURI = frame_uri;
	}

	public String getFrameUri()
	{
		return this.frameURI;
	}

	public void setFrameDesc(String frame_desc)
	{
		this.frameDesc = frame_desc;
	}

	public String getFrameDesc()
	{
		return this.frameDesc;
	}

	public void setFrameCategoryID(int cat_id)
	{
		this.frameCategoryID = cat_id;
	}

	public int getFrameCategoryID()
	{
		return this.frameCategoryID;
	}

	public void setFrameStatus(String status)
	{
		this.frameStatus = status;
	}

	public String getFrameStatus()
	{
		return this.frameStatus;
	}
	
	public void setFrameColor(String color)
	{
		this.frameColor = color;
	}
	
	public String getFrameColor()
	{
		return this.frameColor;
	}
}
