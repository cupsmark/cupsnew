package com.cupslicenew.core.model;

public class MEffect {
	
	private int effect_id, effect_category_id;
	private String effect_name, effect_file, effect_description, effect_status;
	
	public MEffect()
	{
		effect_id = 0;
		effect_category_id = 0;
		effect_name = "";
		effect_file = "";
		effect_description = "";
		effect_status = "";
	}

	
	public void setEffectID(int id)
	{
		this.effect_id = id;
	}
	
	public int getEffectID()
	{
		return this.effect_id;
	}
	
	public void setEffectName(String name)
	{
		this.effect_name = name;
	}
	
	public String getEffectName()
	{
		return this.effect_name;
	}
	
	public void setEffectFile(String file)
	{
		this.effect_file = file;
	}
	
	public String getEffectFile()
	{
		return this.effect_file;
	}
	
	public void setEffectDesc(String desc)
	{
		this.effect_description = desc;
	}
	
	public String getEffectDesc()
	{
		return this.effect_description;
	}
	
	public void setEffectCategoryID(int id)
	{
		this.effect_category_id = id;
	}
	
	public int getEffectCategoryID()
	{
		return this.effect_category_id;
	}
	
	public void setEffectStatus(String status)
	{
		this.effect_status = status;
	}
	
	public String getEffectStatus()
	{
		return this.effect_status;
	}
	
}
