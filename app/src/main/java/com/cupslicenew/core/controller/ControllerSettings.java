package com.cupslicenew.core.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 1/6/16.
 */
public class ControllerSettings {

    Context mContext;
    String uri, message, token, compareVersionFlag;
    int l, o;
    boolean isSuccess = false;
    ArrayList<String> follow_icon, follow_title, follow_id, follow_link;

    public ControllerSettings(Context context) {
        this.mContext = context;
        this.l = 0;
        this.o = 0;
        this.message = "";
        this.token = "";
        this.uri = "";
        this.compareVersionFlag = "";

        follow_icon = new ArrayList<String>();
        follow_link = new ArrayList<String>();
        follow_title = new ArrayList<String>();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setO(int o)
    {
        this.o = o;
    }

    public void executeFollow()
    {
        if(HelperGlobal.checkConnection(mContext))
        {
            String js = HelperGlobal.getJSON(HelperGlobal.GU(159188) + "token=" + token + "&l=" + Integer.toString(l) + "&o=" + Integer.toString(o));
            if(js != null)
            {
                try {
                    JSONObject object = new JSONObject(js);
                    if(object.getBoolean("status"))
                    {
                        JSONArray arr = object.getJSONArray("data");

                        for(int i = 0;i < arr.length();i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);
                            follow_title.add(obj.getString("t"));
                            follow_icon.add(HelperGlobal.GU(159180) + obj.getString("f"));
                            follow_link.add(obj.getString("l"));
                        }
                        isSuccess = true;
                        o = l + o;
                    }
                    else {
                        isSuccess = false;
                        message = object.getString("msg");
                    }
                } catch (JSONException e) {
                    isSuccess = false;
                    message = e.getMessage().toString();
                }
            }
            else {
                isSuccess = false;
                message = mContext.getResources().getString(R.string.base_string_null_json);
            }
        }
        else
        {
            isSuccess = false;
            message = mContext.getResources().getString(R.string.base_string_no_internet);
        }
    }

    public void executeCheckUpdate()
    {
        if(HelperGlobal.checkConnection(mContext))
        {
            String js = HelperGlobal.getJSON(HelperGlobal.GU(159190) + "token="+token);
            if(js != null)
            {
                try {
                    JSONObject object = new JSONObject(js);
                    if(object.getBoolean("status"))
                    {
                        SharedPreferences pref = mContext.getSharedPreferences("mob_gen",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        JSONArray arr = object.getJSONArray("data");
                        JSONObject object2 = arr.getJSONObject(0);
                        int vcode = HelperGlobal.getAppVersionCode(mContext);
                        int serverVCode = Integer.parseInt(object2.getString("vc"));
                        editor.putString("esupport",object2.getString("s"));
                        editor.commit();
                        if(vcode != serverVCode)
                        {
                            isSuccess = true;
                            compareVersionFlag = object2.getString("vn");
                        }
                        else
                        {
                            isSuccess = true;
                            compareVersionFlag = "0";
                        }
                    }
                    else {
                        isSuccess = false;
                        message = object.getString("msg");
                    }
                } catch (JSONException e) {
                    isSuccess = false;
                    message = e.getMessage().toString();
                }
            }
            else {
                isSuccess = false;
                message = mContext.getResources().getString(R.string.base_string_null_json);
            }
        }
        else
        {
            isSuccess = false;
            message = mContext.getResources().getString(R.string.base_string_no_internet);
        }
    }

    public boolean getSuccess()
    {
        return this.isSuccess;
    }

    public String getMessage()
    {
        return this.message;
    }

    public int getOffset()
    {
        return this.o;
    }

    public ArrayList<String> getFollowIcon()
    {
        return this.follow_icon;
    }

    public ArrayList<String> getFollowTitle()
    {
        return this.follow_title;
    }

    public ArrayList<String> getFollowLink()
    {
        return this.follow_link;
    }

    public String getCompareVersionFlag()
    {
        return this.compareVersionFlag;
    }

}
