package com.cupslicenew.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.view.ViewText;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentGalleryDetail extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_GALLERY_DETAIL = "tag:fragment-gallery-detail";

    String title_page,dirPath, directory;
    int album_count;
    ArrayList<String> mIds, mThumb;
    Tracker tracker;
    ViewText pagetitle;
    Map<String, String> parameter;
    ImageButton imagebutton_back;
    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
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
        HelperGlobal.sendAnalytic(tracker, "Page Gallery Detail");
    }

    private void back()
    {
        activity.onBackPressed();
    }



    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_GALLERY_DETAIL;
    }

}
