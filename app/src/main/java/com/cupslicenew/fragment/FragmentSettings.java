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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.cupslicenew.core.controller.ControllerSettings;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.view.ViewDialogConfirm;
import com.cupslicenew.view.ViewText;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<String> settings_id, settings_title;
    ListView listview_settings;
    LayoutInflater inflater;
    ImageButton imagebutton_back;
    SettingsAdapter adapter;

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
            init();
            getList();
        }
    }

    private void init()
    {
        settings_id = new ArrayList<String>();
        settings_title = new ArrayList<String>();
        adapter = new SettingsAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.settings_imagebutton_back);
        listview_settings = (ListView) activity.findViewById(R.id.settings_listview);

        listview_settings.setAdapter(adapter);
        listview_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    check_update();
                }
                else if(position == 1)
                {
                    sendFeedback();
                }
                else if(position == 2)
                {
                    HelperGlobal.openApplicationInPlayStore(activity, "com.cupslice");
                }
                else if(position == 3)
                {
                    addFragmentSocialList();
                }
                else
                {
                    addFragmentAbout();
                }
            }
        });
        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void initAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Settings");
    }

    private void getList()
    {
        settings_id.add("1");
        settings_id.add("2");
        settings_id.add("3");
        settings_id.add("4");
        settings_id.add("5");

        settings_title.add("Check Updates");
        settings_title.add("Feedback");
        settings_title.add("Rate Us");
        settings_title.add("Follow Us");
        settings_title.add("About");

        adapter.notifyDataSetChanged();
    }

    private void check_update()
    {
        new AsyncTask<Void, Integer, String>(){

            boolean success = false;
            String msg;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = "0";
                ControllerSettings settings = new ControllerSettings(activity);
                settings.setToken(HelperGlobal.getDeviceID(activity));
                settings.executeCheckUpdate();
                if(settings.getSuccess())
                {
                    success = true;
                    result = settings.getCompareVersionFlag();
                }
                else
                {
                    msg = settings.getMessage();
                    success = false;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    if(!s.equals("0"))
                    {
                        showDialogUpdates(false, s);
                    }
                    else {
                        showDialogUpdates(true, s);
                    }
                }
                else {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void showDialogUpdates(boolean isUpdate, String versionName)
    {
        final ViewDialogConfirm dialog = new ViewDialogConfirm(activity);
        dialog.setCancelable(true);
        dialog.show();
        dialog.getButtonOK().setRegular();
        if(isUpdate)
        {
            dialog.setTextMessage("You already have the latest version");
            dialog.getButtonCancel().setVisibility(View.INVISIBLE);
            dialog.getButtonOK().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        else {
            dialog.setTextMessage("Updates available. The latest version is " + versionName);
            dialog.getButtonOK().setText("Update");
            dialog.getButtonOK().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    HelperGlobal.openApplicationInPlayStore(activity,"com.cupslice");
                }
            });
            dialog.getButtonCancel().setRegular();
            dialog.getButtonCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void sendFeedback()
    {
        SharedPreferences prefs = activity.getSharedPreferences("mob_gen",Context.MODE_PRIVATE);
        String prefemail = prefs.getString("esupport", "");
        String emails = "";
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        if(prefemail.equals(""))
        {
            emails = "support@cupslice.com";
        }
        else {
            emails = prefemail;
        }
        sendIntent.setData(Uri.parse(emails));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emails });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(sendIntent);
    }

    private void addFragmentSocialList()
    {
        FragmentSocialList socialList = new FragmentSocialList();
        Map<String, String> param = new HashMap<String, String>();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(!socialList.isAdded())
        {
            ft.add(R.id.settings_container_fragment, socialList);
        }
        ft.commit();
    }

    private void addFragmentAbout()
    {
        FragmentAbout about = new FragmentAbout();
        Map<String, String> param = new HashMap<String, String>();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(!about.isAdded())
        {
            ft.add(R.id.settings_container_fragment, about);
        }
        ft.commit();
    }

    private void back()
    {
        activity.onBackPressed();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_SETTINGS;
    }

    public class SettingsAdapter extends BaseAdapter{

        public  SettingsAdapter()
        {

        }

        private void clear()
        {
            settings_id.clear();
            settings_title.clear();
        }

        private void remove(int i)
        {
            settings_id.remove(i);
            settings_title.remove(i);
        }

        @Override
        public int getCount() {
            return settings_id.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[2];
            object[0] = settings_id.get(position);
            object[1] = settings_title.get(position);
            return object;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_fragment_settings, null);
            }
            holder.text_title = (ViewText) convertView.findViewById(R.id.item_fragment_settings_title);
            holder.text_title.setRegular();
            holder.text_title.setText(settings_title.get(position));
            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ViewText text_title;
        }
    }

}
