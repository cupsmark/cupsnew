package com.cupslicenew.core.controller;

import android.content.Context;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 6/5/16.
 */
public class ControllerAbout {

    Context context;
    String uri, message, token;
    int li,of;
    boolean isParsed = false;
    boolean isSuccess = false;
    ArrayList<String> htu_id, htu_title, htu_thumb;


    public ControllerAbout(Context context)
    {
        this.context = context;
        this.uri = HelperGlobal.GU(159189);
        htu_id = new ArrayList<String>();
        htu_title = new ArrayList<String>();
        htu_thumb = new ArrayList<String>();
        this.message = "";
        this.token = "";
        this.of = 0;
        this.li = 0;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setL(int l)
    {
        this.li = l;
    }

    public void setO(int o)
    {
        this.of = o;
    }



    public void execute()
    {
        String url_builder = this.uri+"token="+token+"&l="+Integer.toString(this.li)+"&o="+Integer.toString(this.of);
        if(HelperGlobal.checkConnection(context))
        {
            try {
                String response = HelperGlobal.getJSON(url_builder);
                if(response != null)
                {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray objData = mainObj.getJSONArray("data");
                    for(int i = 0;i < objData.length();i++)
                    {
                        JSONObject jsonObject = objData.getJSONObject(i);
                        htu_id.add(jsonObject.getString("i"));
                        htu_title.add(jsonObject.getString("t"));
                        htu_thumb.add(jsonObject.getString("f"));
                    }
                    isSuccess = true;
                    this.of = this.of + this.li;
                }
                else
                {
                    isSuccess = false;
                    message = context.getResources().getString(R.string.base_string_null_json);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                isSuccess = false;
                message = e.getMessage().toString();
            }
        }
        else {
            isSuccess = false;
            message = context.getResources().getString(R.string.base_string_no_internet);
        }

    }

    public String getMessage()
    {
        return this.message;
    }
    public boolean getSuccess()
    {
        return this.isSuccess;
    }
    public int getOffset()
    {
        return this.of;
    }

    public ArrayList<String> getHTUId()
    {
        return this.htu_id;
    }

    public ArrayList<String> getHTUTitle()
    {
        return this.htu_title;
    }

    public ArrayList<String> getHTUThumb()
    {
        return this.htu_thumb;
    }
}
