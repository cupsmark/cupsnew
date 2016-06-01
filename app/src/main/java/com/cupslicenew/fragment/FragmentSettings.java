package com.cupslicenew.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentSettings extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_SETTINGS = "tag:fragment-settings";

    Tracker tracker;
    private ArrayList<String> settings_data, follow_title, follow_icon, follow_link;
    ListView listview_settings, listview_follow;
    View settings_main, settings_follow, settings_about;
    int active_settings;//0 settings, 1 follow
    LayoutInflater inflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_settings, container, false);
        return main_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null)
        {
            this.activity = (BaseActivity) activity;
            fragmentInterface = (HelperGlobal.FragmentInterface) this.activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(activity != null)
        {
            Fabric.with(activity, new Crashlytics());
            initAnalytics();
        }
    }


    private void initAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Settings");
    }



    private void back()
    {
        if(active_settings == 0)
        {
            activity.onBackPressed();
        }
        else {
           // init();
        }
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_SETTINGS;
    }

}
