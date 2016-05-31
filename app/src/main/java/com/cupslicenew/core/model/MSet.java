package com.cupslicenew.core.model;

/**
 * Created by developer on 5/31/16.
 */
public class MSet {

    int set_id;
    String set_name, set_value;

    public MSet()
    {
        set_id = 0;
        set_name = "";
        set_value = "";
    }

    public void setID(int id)
    {
        this.set_id = id;
    }

    public int getID()
    {
        return this.set_id;
    }

    public void setName(String name)
    {
        this.set_name = name;
    }

    public String getName()
    {
        return this.set_name;
    }

    public void setValue(String value)
    {
        this.set_value = value;
    }

    public String getValue()
    {
        return this.set_value;
    }
}
