package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.view.ViewGridWithHeader;
import com.cupslicenew.view.ViewButton;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentStore extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_STORE = "tag:fragment-store";

    Tracker tracker;
    int tabIndex = 1;
    ImageButton imagebutton_back, imagebutton_home;
    ViewGridWithHeader gridWithHeader;
    View header;
    LayoutInflater inflater;
    ArrayList<String> product_id, product_title, product_example, product_price, product_client;
    StoreAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_store, container, false);
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
        }
    }

    private void initAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Cupslice Cafe");
    }

    private void init()
    {
        product_id = new ArrayList<String>();
        product_title = new ArrayList<String>();
        product_example = new ArrayList<String>();
        product_price = new ArrayList<String>();
        product_client = new ArrayList<String>();
        adapter = new StoreAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.store_imagebutton_back);
        imagebutton_home = (ImageButton) activity.findViewById(R.id.store_imagebutton_home);
        gridWithHeader = (ViewGridWithHeader) activity.findViewById(R.id.store_gridviewheader);

        header = inflater.inflate(R.layout.view_header_store, null);
        gridWithHeader.addHeaderView(header);
        addFragmentSlider();
        gridWithHeader.setAdapter(adapter);

        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        imagebutton_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void addFragmentSlider()
    {
        FragmentCafeSlider slider = new FragmentCafeSlider();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        if(!slider.isAdded())
        {
            ft.add(R.id.view_header_store_container_fragment, slider);
        }
        ft.commit();
    }
    private void back()
    {
        activity.onBackPressed();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_STORE;
    }

    public class StoreAdapter extends BaseAdapter{

        public StoreAdapter()
        {

        }

        public void clear()
        {
            product_id.clear();
            product_title.clear();
            product_client.clear();
            product_price.clear();
            product_example.clear();
        }
        @Override
        public int getCount() {
            return product_id.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[5];
            object[0] = product_id.get(position);
            object[1] = product_title.get(position);
            object[2] = product_example.get(position);
            object[3] = product_price.get(position);
            object[4] = product_client.get(position);
            return object;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_fragment_gallery_detail, null);
            }
            holder = new ViewHolder();
            holder.imagethumb = (ImageView) convertView.findViewById(R.id.item_fragment_gallery_detail_thumb);
            Picasso.with(activity).load(product_example.get(position)).fit().centerCrop().into(holder.imagethumb, new Callback() {
                @Override
                public void onSuccess() {
                    holder.imagethumb.setAdjustViewBounds(true);
                    holder.imagethumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onError() {

                }
            });
            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ImageView imagethumb;
        }
    }

}
