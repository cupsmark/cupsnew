package com.cupslicenew.core.model;

public class MSticker {

	private int sticker_id, sticker_category_id;
	private String sticker_name, sticker_uri, sticker_status, sticker_color;
	
	public MSticker()
	{
		sticker_id = 0;
		sticker_name = "";
		sticker_uri = "";
		sticker_category_id = 0;
		sticker_status = "";
		sticker_color = "";
	}
	
	public void setStickerID(int id)
	{
		this.sticker_id = id;
	}
	public int getStickerID()
	{
		return this.sticker_id;
	}
	public void setStickerName(String name)
	{
		this.sticker_name = name;
	}
	public String getStickerName()
	{
		return this.sticker_name;
	}
	public void setStickerUri(String uri)
	{
		this.sticker_uri = uri;
	}
	public String getStickerUri()
	{
		return this.sticker_uri;
	}
	public void setStickerCategoryID(int cat_id)
	{
		this.sticker_category_id = cat_id;
	}
	public int getStickerCategoryID()
	{
		return this.sticker_category_id;
	}
	
	public void setStickerStatus(String status)
	{
		this.sticker_status = status;
	}
	public String getStickerStatus()
	{
		return this.sticker_status;
	}
	
	public void setStickerColor(String color)
	{
		this.sticker_color = color;
	}
	
	public String getStickerColor()
	{
		return this.sticker_color;
	}
}
