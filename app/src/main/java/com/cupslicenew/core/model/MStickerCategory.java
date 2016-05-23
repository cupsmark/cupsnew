package com.cupslicenew.core.model;

public class MStickerCategory {
	
	private int sticker_category_id;
	private String sticker_category_name,sticker_category_desc, sticker_category_uri, sticker_category_status, sticker_category_date;
	
	public MStickerCategory()
	{
		sticker_category_id = 0;
		sticker_category_name = "";
		sticker_category_desc = "";
		sticker_category_uri = "";
		sticker_category_status = "";
		sticker_category_date = "";
	}

	
	public void setStickerCategoryID(int id)
	{
		this.sticker_category_id = id;
	}

	public int getStickerCategoryID()
	{
		return this.sticker_category_id;
	}

	public void setStickerCategoryName(String name)
	{
		this.sticker_category_name = name;
	}
	public String getStickerCategoryName()
	{
		return this.sticker_category_name;
	}

	public void setStickerCategoryDesc(String desc)
	{
		this.sticker_category_desc = desc;
	}

	public String getStickerCategoryDesc()
	{
		return this.sticker_category_desc;
	}

	public void setStickerCategoryUri(String uri)
	{
		this.sticker_category_uri = uri;
	}

	public String getStickerCategoryUri()
	{
		return this.sticker_category_uri;
	}

	public void setStickerCategoryStatus(String status)
	{
		this.sticker_category_status = status;
	}

	public String getStickerCategoryStatus()
	{
		return this.sticker_category_status;
	}

	public void setStickerCategoryDate(String date)
	{
		this.sticker_category_date = date;
	}

	public String getStickerCategoryDate()
	{
		return this.sticker_category_date;
	}
}
