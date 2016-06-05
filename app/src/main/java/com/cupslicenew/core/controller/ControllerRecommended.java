package com.cupslicenew.core.controller;

import android.content.Context;


import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 9/18/15.
 */

public class ControllerRecommended {

    Context context;
    String uri, message, token;
    int li,of;
    boolean isParsed = false;
    boolean isSuccess = false;
    ArrayList<String> rec_id;
    ArrayList<String> rec_title;
    ArrayList<String> rec_thumb;
    ArrayList<String> rec_desc;
    ArrayList<String> rec_coin;
    ArrayList<String> rec_link;


    public ControllerRecommended(Context context)
    {
        this.context = context;
        this.uri = HelperGlobal.GU(159181);
        rec_id = new ArrayList<String>();
        rec_title = new ArrayList<String>();
        rec_thumb = new ArrayList<String>();
        rec_desc = new ArrayList<String>();
        rec_coin = new ArrayList<String>();
        rec_link = new ArrayList<String>();
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
                        rec_id.add(jsonObject.getString("i"));
                        rec_title.add(jsonObject.getString("t"));
                        rec_thumb.add(jsonObject.getString("f"));
                        rec_desc.add(jsonObject.getString("d"));
                        rec_coin.add(jsonObject.getString("c"));
                        rec_link.add(jsonObject.getString("l"));
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

    public ArrayList<String> getRecId()
    {
        return this.rec_id;
    }

    public ArrayList<String> getRecTitle()
    {
        return this.rec_title;
    }

    public ArrayList<String> getRecThumb()
    {
        return this.rec_thumb;
    }

    public ArrayList<String> getRecDesc()
    {
        return this.rec_desc;
    }

    public ArrayList<String> getRecCoin()
    {
        return this.rec_coin;
    }

    public ArrayList<String> getRecLink()
    {
        return this.rec_link;
    }

}
