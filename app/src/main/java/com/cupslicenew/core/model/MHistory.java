package com.cupslicenew.core.model;

public class MHistory {

	private int history_id;
	private String history_file;
	
	public MHistory()
	{
		history_id = 0;
		history_file = "";
	}
	
	public void set_history_id(int ids)
	{
		this.history_id = ids;
	}
	
	public int get_history_id()
	{
		return this.history_id;
	}
	
	public void set_history_file(String files)
	{
		this.history_file = files;
	}
	
	public String get_history_files()
	{
		return this.history_file;
	}
}
