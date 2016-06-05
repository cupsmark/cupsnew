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
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.controller.ControllerAbout;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.view.ViewLoadingDialog;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
    LinearLayout linear_container_item;

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
            init();
            getList();
        }
    }

    private void init()
    {
        data_id = new ArrayList<String>();
        data_title = new ArrayList<String>();
        data_image = new ArrayList<String>();
        imagebutton_back = (ImageButton) activity.findViewById(R.id.htu_imagebutton_back);
        linear_container_item = (LinearLayout) activity.findViewById(R.id.htu_linear_data);

        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void getList()
    {
        new AsyncTask<Void, Integer, String>()
        {
            String msg;
            boolean success = false;
            ArrayList<String> tempid, temptitle, tempthumb;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tempid = new ArrayList<String>();
                temptitle = new ArrayList<String>();
                tempthumb = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... params) {
                ControllerAbout about = new ControllerAbout(activity);
                about.setToken(HelperGlobal.getDeviceID(activity));
                about.setL(l);
                about.setO(o);
                about.execute();
                if(about.getSuccess())
                {
                    tempid.addAll(about.getHTUId());
                    temptitle.addAll(about.getHTUTitle());
                    tempthumb.addAll(about.getHTUThumb());
                    o = about.getOffset();
                    success = true;
                }
                else
                {
                    success = false;
                    msg = about.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    for(int i = 0;i < tempid.size();i++)
                    {
                        final ImageView item = new ImageView(activity);
                        item.setId(i);
                        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        param.gravity = Gravity.CENTER;
                        item.setLayoutParams(param);
                        item.setAdjustViewBounds(true);
                        item.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        linear_container_item.addView(item);
                        Picasso.with(activity).load(HelperGlobal.GU(159180) + tempthumb.get(i)).into(item, new Callback() {
                            @Override
                            public void onSuccess() {
                                item.setAdjustViewBounds(true);
                                item.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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
