package com.cupslicenew.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.cupslicenew.core.controller.ControllerRecommended;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.util.EndlessScrollListener;
import com.cupslicenew.view.ViewLoadingDialog;
import com.cupslicenew.view.ViewText;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentRecommended extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_RECOMMENDED = "tag:fragment-recommended";

    int l = 3, o = 0;
    ArrayList<String> rec_id, rec_title, rec_thumb, rec_desc, rec_coin, rec_link;
    Tracker tracker;
    ViewLoadingDialog dialog;
    ImageButton imagebutton_back;
    ListView recommended_listview;
    LayoutInflater inflater;
    RecommendedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_recommended, container, false);
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
        rec_id = new ArrayList<String>();
        rec_title = new ArrayList<String>();
        rec_thumb = new ArrayList<String>();
        rec_desc = new ArrayList<String>();
        rec_coin = new ArrayList<String>();
        rec_link = new ArrayList<String>();
        adapter = new RecommendedAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.recommended_imagebutton_back);
        recommended_listview = (ListView) activity.findViewById(R.id.recommended_listview);

        recommended_listview.setAdapter(adapter);
        recommended_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HelperGlobal.openAppURL(activity, rec_link.get(position));
            }
        });
        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void getList()
    {
        new AsyncTask<Void, Integer, String>(){


            ArrayList<String> temp_rec_id, temp_rec_title, temp_rec_thumb, temp_rec_desc, temp_rec_coin, temp_rec_link;
            boolean isSuccess = false;
            String message;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                temp_rec_id = new ArrayList<String>();
                temp_rec_title = new ArrayList<String>();
                temp_rec_thumb = new ArrayList<String>();
                temp_rec_desc = new ArrayList<String>();
                temp_rec_coin = new ArrayList<String>();
                temp_rec_link = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... voids) {
                ControllerRecommended recommended = new ControllerRecommended(activity);
                recommended.setToken(HelperGlobal.getDeviceID(activity));
                recommended.setL(l);
                recommended.setO(o);
                recommended.execute();
                if(recommended.getSuccess())
                {
                    temp_rec_id.addAll(recommended.getRecId());
                    temp_rec_title.addAll(recommended.getRecTitle());
                    temp_rec_thumb.addAll(recommended.getRecThumb());
                    temp_rec_desc.addAll(recommended.getRecDesc());
                    temp_rec_coin.addAll(recommended.getRecCoin());
                    temp_rec_link.addAll(recommended.getRecLink());
                    o = recommended.getOffset();
                    isSuccess = true;
                }
                else
                {
                    isSuccess = false;
                    message = recommended.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(isSuccess)
                {
                    if(temp_rec_id.size() > 0)
                    {
                        for(int i = 0;i < temp_rec_id.size();i++)
                        {
                            rec_id.add(temp_rec_id.get(i));
                            rec_title.add(temp_rec_title.get(i));
                            rec_thumb.add(temp_rec_thumb.get(i));
                            rec_desc.add(temp_rec_desc.get(i));
                            rec_coin.add(temp_rec_coin.get(i));
                            rec_link.add(temp_rec_link.get(i));
                        }

                        adapter.notifyDataSetChanged();
                        recommended_listview.setOnScrollListener(new EndlessScrollListener() {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                getList();
                            }
                        });
                    }
                }
                if(!isSuccess)
                {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    private void initAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Recommended");
    }

    private void back()
    {
        activity.onBackPressed();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_RECOMMENDED;
    }

    public class RecommendedAdapter extends BaseAdapter{

        public RecommendedAdapter()
        {

        }

        public void clear()
        {
            rec_id.clear();
            rec_title.clear();
            rec_thumb.clear();
            rec_desc.clear();
            rec_coin.clear();
            rec_link.clear();
        }

        public void remove(int position)
        {
            rec_id.remove(position);
            rec_title.remove(position);
            rec_thumb.remove(position);
            rec_desc.remove(position);
            rec_coin.remove(position);
            rec_link.remove(position);
        }
        @Override
        public int getCount() {
            return rec_id.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[6];
            object[0] = rec_id.get(position);
            object[1] = rec_title.get(position);
            object[2] = rec_thumb.get(position);
            object[3] = rec_desc.get(position);
            object[4] = rec_coin.get(position);
            object[5] = rec_link.get(position);
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
                convertView = inflater.inflate(R.layout.item_fragment_recommended, null);
            }
            holder.text_title = (ViewText) convertView.findViewById(R.id.recommended_item_title);
            holder.text_desc = (ViewText) convertView.findViewById(R.id.recommended_item_desc);
            holder.image_thumb = (ImageView) convertView.findViewById(R.id.recommended_item_icon);

            holder.text_title.setSemiBold();
            holder.text_title.setText(rec_title.get(position));
            holder.text_desc.setRegular();
            holder.text_desc.setText(rec_desc.get(position));
            Picasso.with(activity).load(HelperGlobal.GU(159180) + rec_thumb.get(position)).into(holder.image_thumb, new Callback() {
                @Override
                public void onSuccess() {
                    holder.image_thumb.setAdjustViewBounds(true);
                    holder.image_thumb.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }

                @Override
                public void onError() {

                }
            });
            convertView.setTag(holder);

            return convertView;
        }

        class ViewHolder{
            ViewText text_title, text_desc, text_thumb;
            ImageView image_thumb;
        }
    }


}
