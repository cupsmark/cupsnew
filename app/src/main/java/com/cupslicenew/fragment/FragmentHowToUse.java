package com.cupslicenew.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.view.ViewLoadingDialog;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentHowToUse extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_HTU = "tag:fragment-htu";

    private ArrayList<String> data_id, data_title, data_image;
    Tracker tracker;
    int l = 10, o = 0;
    ViewLoadingDialog dialog;
    ImageButton imagebutton_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_how_to_use, container, false);
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


    private void back()
    {
        activity.onBackPressed();
    }



    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_HTU;
    }

    private void initAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page How To Use");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
}
