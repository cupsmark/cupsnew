package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.view.ViewText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 6/5/16.
 */
public class FragmentSocialList extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_SOCIAL_LIST = "tag:fragment-social-list";

    ImageButton imagebutton_back;
    ArrayList<String> follow_icon, follow_title, follow_link;
    LayoutInflater inflater;
    SocialListAdapter adapter;
    ListView social_list_listview;
    int l = 20, o = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_social_list, container, false);
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
        follow_title = new ArrayList<String>();
        follow_icon = new ArrayList<String>();
        follow_link = new ArrayList<String>();
        adapter = new SocialListAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.social_list_imagebutton_back);
        social_list_listview = (ListView) activity.findViewById(R.id.social_list_listview);

        social_list_listview.setAdapter(adapter);
        social_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HelperGlobal.openAppURL(activity, follow_link.get(position));
            }
        });
        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        getList();
    }

    private void getList()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            ArrayList<String> tempid, temptitle, tempicon, templink;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tempid = new ArrayList<String>();
                temptitle = new ArrayList<String>();
                tempicon = new ArrayList<String>();
                templink = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... params) {
                ControllerSettings settings = new ControllerSettings(activity);
                settings.setToken(HelperGlobal.getDeviceID(activity));
                settings.setO(o);
                settings.setL(l);
                settings.executeFollow();
                if(settings.getSuccess())
                {
                    temptitle.addAll(settings.getFollowTitle());
                    templink.addAll(settings.getFollowLink());
                    tempicon.addAll(settings.getFollowIcon());
                    success = true;
                }
                else
                {
                    success = false;
                    msg = settings.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    if(temptitle.size() > 0)
                    {
                        follow_title.addAll(temptitle);
                        follow_icon.addAll(tempicon);
                        follow_link.addAll(templink);
                        adapter.notifyDataSetChanged();
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
        return TAG_FRAGMENT_SOCIAL_LIST;
    }

    public class SocialListAdapter extends BaseAdapter {

        public  SocialListAdapter()
        {

        }

        private void clear()
        {
            follow_title.clear();
            follow_icon.clear();
            follow_link.clear();
        }

        private void remove(int i)
        {
            follow_title.remove(i);
            follow_icon.remove(i);
            follow_link.remove(i);
        }

        @Override
        public int getCount() {
            return follow_title.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[3];
            object[0] = follow_title.get(position);
            object[1] = follow_icon.get(position);
            object[2] = follow_link.get(position);
            return object;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder = new ViewHolder();
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_fragment_social_list, null);
            }
            holder.text_title = (ViewText) convertView.findViewById(R.id.item_fragment_social_list_title);
            holder.text_title.setRegular();
            holder.text_title.setText(follow_title.get(position));
            holder.image_icon = (ImageView) convertView.findViewById(R.id.item_fragment_social_list_icon);
            Picasso.with(activity).load(follow_icon.get(position)).into(holder.image_icon, new Callback() {
                @Override
                public void onSuccess() {
                    holder.image_icon.setAdjustViewBounds(true);
                    holder.image_icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }

                @Override
                public void onError() {

                }
            });
            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ViewText text_title;
            ImageView image_icon;
        }
    }
}
