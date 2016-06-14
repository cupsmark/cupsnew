package com.cupslicenew.core.controller;

import android.content.Context;


import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ekobudiarto on 12/4/15.
 */
public class ControllerStore {

    Context mContext;
    private String urlx,or, message,state;
    private int li;
    private int of;
    private ArrayList<String> ids, title, file, preview, desc, background, example, price, client;
    ArrayList<String> cat_id,cat_name,cat_file,cat_count;
    ArrayList<String> det_id,det_title, det_file, det_cat,det_color,det_f_small;
    private String catalogid,catalogtitle,catalogdesc,catalogbg,catalogprev,catalogfiles,catalogprice,catalogfeature,countryid;
    private String area;
    boolean isSuccess = false;
    boolean isCategory = false;
    int categoryID;
    String provider;

    public ControllerStore(Context context)
    {
        this.mContext = context;
        this.provider = HelperGlobal.getNetworkProvider(mContext);
        this.urlx = "";
        this.li = 0;
        this.of = 0;
        this.or = "";
        ids = new ArrayList<String>();
        title = new ArrayList<String>();
        file = new ArrayList<String>();
        preview = new ArrayList<String>();
        desc = new ArrayList<String>();
        background = new ArrayList<String>();
        example = new ArrayList<String>();
        price = new ArrayList<String>();
        client = new ArrayList<String>();

        cat_id = new ArrayList<String>();
        cat_name = new ArrayList<String>();
        cat_count = new ArrayList<String>();
        cat_file = new ArrayList<String>();

        det_id = new ArrayList<String>();
        det_title = new ArrayList<String>();
        det_file = new ArrayList<String>();
        det_cat = new ArrayList<String>();
        det_color = new ArrayList<String>();
        det_f_small = new ArrayList<String>();
    }

    public void setL(int l)
    {
        this.li = l;
    }

    public void setO(int o)
    {
        this.of = o;
    }

    public void isCategory(boolean isCategory)
    {
        this.isCategory = isCategory;
    }

    public void setCategory(int category)
    {
        this.categoryID = category;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setSorting(String order)
    {
        this.or = order;
    }

    public void setURL(String url)
    {
        this.urlx = url;
    }

    public void executeListProduct()
    {
        String url_builder = this.urlx + "token="+HelperGlobal.getDeviceID(mContext)+"&l="+Integer.toString(li)+"&o="+Integer.toString(of)+"&s="+state+"&countrid="+area+"&or="+or+"&prov="+provider;
        if(isCategory)
        {
            url_builder += "&c="+Integer.toString(categoryID);
        }
        this.urlx = url_builder.replace(" ","%20");
        if(HelperGlobal.checkConnection(mContext))
        {
            String response = HelperGlobal.getJSON(urlx);
            if(response != null)
            {
                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status"))
                    {
                        JSONArray arr = new JSONArray(obj.getString("data"));
                        boolean show = false;
                        for(int i = 0;i < arr.length();i++)
                        {
                            JSONObject obj2 = arr.getJSONObject(i);
                            String ci = obj2.getString("i");
                            String cn = obj2.getString("t");
                            String cbg = obj2.getString("bg");
                            String cd = obj2.getString("d");
                            String cfeat = obj2.getString("feat");
                            String cf = obj2.getString("f");
                            String cprev = obj2.getString("p");
                            String cp = obj2.getString("pr");
                            String cl = obj2.getString("cl").toLowerCase(Locale.US);

                            if(cl.toLowerCase(Locale.US).equals("cupslice"))
                            {
                                show = true;
                            }
                            if(!cl.toLowerCase(Locale.US).equals("cupslice"))
                            {
                                if(provider.toLowerCase(Locale.US).equals(cl.toLowerCase(Locale.US)))
                                {
                                    show = true;
                                }
                                else {
                                    show = false;
                                }
                            }
                            if(show == true)
                            {
                                ids.add(ci);
                                title.add(cn);
                                file.add(HelperGlobal.GU(159180) + cf);
                                preview.add(HelperGlobal.GU(159180) + cprev);
                                desc.add(cd);
                                background.add(HelperGlobal.GU(159180) + cbg);
                                price.add(cp);
                                client.add(cl);
                            }
                        }
                        isSuccess = true;
                        this.of = this.of + this.li;
                    }
                    else {
                        isSuccess = false;
                        message = obj.getString("msg");
                    }
                }
                catch (JSONException ex)
                {
                    message = ex.getMessage().toString();
                    isSuccess = false;
                }
            }
            else
            {
                isSuccess = false;
                message = mContext.getResources().getString(R.string.base_string_null_json);
            }
        }
        else {
            isSuccess = false;
            message = mContext.getResources().getString(R.string.base_string_no_internet);
        }
    }

    public void executeListCategory()
    {
        String url_builder = this.urlx+"token="+HelperGlobal.getDeviceID(mContext)+"&prov="+provider.replaceAll(" ","%20")+"&l=50&o=0";
        this.urlx = url_builder.replace(" ","%20");
        if(HelperGlobal.checkConnection(mContext))
        {
            String response = HelperGlobal.getJSON(urlx);
            if(response != null)
            {
                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status"))
                    {
                        JSONArray arr = new JSONArray(obj.getString("data"));
                        for(int i = 0;i < arr.length();i++)
                        {
                            JSONObject obj2 = arr.getJSONObject(i);
                            cat_id.add(obj2.getString("i"));
                            cat_name.add(obj2.getString("t"));
                            cat_count.add(obj2.getString("c"));
                            cat_file.add(obj2.getString("f"));

                        }
                        isSuccess = true;
                        this.of = this.of + this.li;
                    }
                    else {
                        isSuccess = false;
                        message = obj.getString("msg");
                    }
                }
                catch (JSONException ex)
                {
                    message = ex.getMessage().toString();
                    isSuccess = false;
                }
            }
            else
            {
                isSuccess = false;
                message = mContext.getResources().getString(R.string.base_string_null_json);
            }
        }
        else {
            isSuccess = false;
            message = mContext.getResources().getString(R.string.base_string_no_internet);
        }
    }

    public void executeDetail()
    {
        String url_builder = this.urlx.replace(" ","%20");
        if(HelperGlobal.checkConnection(mContext))
        {
            String response = HelperGlobal.getJSON(url_builder);
            if(response != null)
            {
                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status"))
                    {
                        JSONArray arr = new JSONArray(obj.getString("data"));
                        for(int i = 0;i < arr.length();i++)
                        {
                            JSONObject obj2 = arr.getJSONObject(i);
                            det_id.add(obj2.getString("i"));
                            det_title.add(obj2.getString("n"));
                            det_file.add(obj2.getString("f"));
                            det_cat.add(obj2.getString("c"));
                            det_color.add(obj2.getString("col"));
                            det_f_small.add(obj2.getString("fs"));

                        }

                        JSONArray arrcatalog = new JSONArray(obj.getString("cat_data"));
                        JSONObject catalogjs = arrcatalog.getJSONObject(0);
                        catalogid = catalogjs.getString("i");
                        catalogtitle = catalogjs.getString("t");
                        catalogdesc = catalogjs.getString("d");
                        catalogbg = catalogjs.getString("bg");
                        catalogprev = catalogjs.getString("prev");
                        catalogfiles = catalogjs.getString("f");
                        catalogprice = catalogjs.getString("pr");
                        catalogfeature = catalogjs.getString("feat");
                        countryid = catalogjs.getString("couid");

                        isSuccess = true;
                        this.of = this.of + this.li;
                    }
                    else {
                        isSuccess = false;
                        message = obj.getString("msg");
                    }
                }
                catch (JSONException ex)
                {
                    message = ex.getMessage().toString();
                    isSuccess = false;
                }
            }
            else
            {
                isSuccess = false;
                message = mContext.getResources().getString(R.string.base_string_null_json);
            }
        }
        else {
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
        return this.of;
    }

    public ArrayList<String> getID()
    {
        return this.ids;
    }

    public ArrayList<String> getTitle()
    {
        return this.title;
    }

    public ArrayList<String> getFile()
    {
        return this.file;
    }

    public ArrayList<String> getPreview()
    {
        return this.preview;
    }

    public ArrayList<String> getDesc()
    {
        return this.desc;
    }

    public ArrayList<String> getBackground()
    {
        return this.background;
    }

    public ArrayList<String> getExample()
    {
        return this.example;
    }

    public ArrayList<String> getPrice()
    {
        return this.price;
    }

    public ArrayList<String> getClient()
    {
        return this.client;
    }

    public ArrayList<String> getCatID()
    {
        return this.cat_id;
    }

    public ArrayList<String> getCatName()
    {
        return this.cat_name;
    }

    public ArrayList<String> getCatCount()
    {
        return this.cat_count;
    }

    public ArrayList<String> getCatFile()
    {
        return this.cat_file;
    }


    public ArrayList<String> getDetID()
    {
        return this.det_id;
    }

    public ArrayList<String> getDetTitle()
    {
        return this.det_title;
    }

    public ArrayList<String> getDetFile()
    {
        return this.det_file;
    }

    public ArrayList<String> getDetCat()
    {
        return this.det_cat;
    }

    public ArrayList<String> getDetColor()
    {
        return this.det_color;
    }

    public ArrayList<String> getDetFSmall()
    {
        return this.det_f_small;
    }

    public String getcatalogID()
    {
        return this.catalogid;
    }

    public String getcatalogTitle()
    {
        return this.catalogtitle;
    }

    public String getcatalogDesc()
    {
        return this.catalogdesc;
    }

    public String getcatalogBG()
    {
        return this.catalogbg;
    }

    public String getcatalogPreview()
    {
        return this.catalogprev;
    }

    public String getcatalogFiles()
    {
        return this.catalogfiles;
    }

    public String getcatalogPrice()
    {
        return this.catalogprice;
    }

    public String getcatalogFeature()
    {
        return this.catalogfeature;
    }

    public String getcatalogCountry()
    {
        return this.countryid;
    }
}


