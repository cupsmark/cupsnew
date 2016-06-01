package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.view.ViewCamera;
import com.cupslicenew.view.ViewText;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentCamera extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_CAMERA = "tag:fragment-camera";

    Tracker tracker;
    RelativeLayout previewContainer;
    ImageButton button_flash, button_timer, button_switch, button_take;
    FrameLayout button_gallery;
    ViewCamera preview;
    int cameraID;
    boolean isFrontCamera;
    boolean isFlashlightOn;
    ViewText txtTimer;
    int currentTime = 0;
    int periode = 0;
    boolean isTimerRun = false;
    int timerCounter = 0;
    Handler timerHandler;
    int timerPeriodeSet = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_camera, container, false);
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
        HelperGlobal.sendAnalytic(tracker, "Page Camera");
    }

    private void back()
    {
        activity.onBackPressed();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_CAMERA;
    }
}
