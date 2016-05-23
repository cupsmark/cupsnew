package com.cupslicenew.core.model;


public class MEffectCategory {
	
	private int effect_category_id;
	private String effect_category_name, effect_category_file, effect_category_description, effect_category_status;
	
	
	public MEffectCategory()
	{
		effect_category_id = 0;
		effect_category_name = "";
		effect_category_file = "";
		effect_category_description = "";
		effect_category_status = "";
	}
	
	public void setEffectCategoryID(int id)
	{
		this.effect_category_id = id;
	}
	
	public int getEffectCategoryID()
	{
		return this.effect_category_id;
	}
	
	public void setEffectCategoryName(String name)
	{
		this.effect_category_name = name;
	}
	
	public String getEffectCategoryName()
	{
		return this.effect_category_name;
	}
	
	public void setEffectCategoryFile(String file)
	{
		this.effect_category_file = file;
	}
	
	public String getEffectCategoryFile()
	{
		return this.effect_category_file;
	}
	
	public void setEffectCategoryDesc(String desc)
	{
		this.effect_category_description = desc;
	}
	
	public String getEffectCategoryDesc()
	{
		return this.effect_category_description;
	}
	
	public void setEffectCategoryStatus(String status)
	{
		this.effect_category_status = status;
	}
	
	public String getEffectCategoryStatus()
	{
		return this.effect_category_status;
	}
}
