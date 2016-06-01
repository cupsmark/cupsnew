package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 3/31/16.
 */
public class FragmentHome extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_HOME = "tag:fragment-home";
    ImageButton imagebutton_camera, imagebutton_gallery, imagebutton_htu, imagebutton_settings, imagebutton_store, imagebutton_recommended;
    ImageButton imagebutton_ig, imagebutton_fb, imagebutton_mail, imagebutton_invite;
    Tracker tracker;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_home, container, false);
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
        HelperGlobal.sendAnalytic(tracker, "Page Home");
    }


    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_HOME;
    }

    private void back()
    {
        activity.onBackPressed();
    }
}

