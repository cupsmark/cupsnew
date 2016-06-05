package com.cupslicenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.view.ViewText;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 6/5/16.
 */
public class FragmentAbout extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_ABOUT = "tag:fragment-about";

    ViewText textAppName, textAppVersion;
    ImageButton imagebutton_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_about, container, false);
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
            init();
        }
    }

    private void init()
    {
        textAppName = (ViewText) activity.findViewById(R.id.about_textview_name);
        textAppVersion = (ViewText) activity.findViewById(R.id.about_textview_desc);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.about_imagebutton_back);

        textAppName.setSemiBold();
        textAppVersion.setText(HelperGlobal.getAppVersionName(activity));
        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back()
    {
        activity.onBackPressed();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_ABOUT;
    }

}
