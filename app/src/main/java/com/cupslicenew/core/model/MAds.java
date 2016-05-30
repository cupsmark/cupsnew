package com.cupslicenew.core.model;

/**
 * Created by ekobudiarto on 5/30/16.
 */
public class MAds {

    int data_count;
    String ads_id, ads_category, ads_next_time, ads_state, ads_overlay;


    public MAds()
    {
        ads_id = "0";
        data_count = 0;
        ads_category = "";
        ads_next_time = "";
        ads_state = "";
        ads_overlay = "";
    }

    public void setAdsID(String id)
    {
        this.ads_id = id;
    }

    public String getAdsID()
    {
        return this.ads_id;
    }

    public void setAdsCategory(String category)
    {
        this.ads_category = category;
    }

    public String getAdsCategory()
    {
        return this.ads_category;
    }

    public void setAdsNextTime(String time)
    {
        this.ads_next_time = time;
    }

    public String getAdsNextTime()
    {
        return this.ads_next_time;
    }

    public void setAdsState(String state)
    {
        this.ads_state = state;
    }

    public String getAdsState()
    {
        return this.ads_state;
    }

    public void setAdsOverlay(String overlay)
    {
        this.ads_overlay = overlay;
    }

    public String getAdsOverlay()
    {
        return this.ads_overlay;
    }
}
