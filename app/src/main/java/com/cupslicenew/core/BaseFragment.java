package com.cupslicenew.core;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 5/31/16.
 */
public class BaseFragment extends Fragment {

    String TAG;

    Map<String, String> parameter;
    boolean isSliding = false;

    public BaseFragment()
    {
        TAG = "fragment:default";
        parameter = new HashMap<String, String>();
    }

    public void setFragmentTAG(String tag)
    {
        this.TAG = tag;
    }

    public String getFragmentTAG()
    {
        return this.TAG;
    }

    public void setParameter(Map<String, String> param)
    {
        this.parameter = param;
    }

    public Map<String, String> getParameter()
    {
        return this.parameter;
    }

    public void setSlidingMenu(boolean slidingMenu)
    {
        this.isSliding = slidingMenu;
    }

    public boolean getSlidingMenu()
    {
        return this.isSliding;
    }
}
