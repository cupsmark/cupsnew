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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

    String pathDir, titleDir;
    int album_count;
    ArrayList<String> mIds, mThumb;
    Tracker tracker;
    ViewText pagetitle;
    Map<String, String> parameter;
    ImageButton imagebutton_back;
    GridView gallery_detail_gridview;
    LayoutInflater inflater;
    GalleryDetailAdapter adapter;

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
            init();
        }
    }

    private void init(){
        parameter = getParameter();
        titleDir = parameter.get("album_name");
        pathDir = parameter.get("album_directory");
        album_count = Integer.parseInt(parameter.get("album_count"));
        mIds = new ArrayList<String>();
        mThumb = new ArrayList<String>();
        adapter = new GalleryDetailAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.gallery_detail_imagebutton_back);
        pagetitle = (ViewText) activity.findViewById(R.id.gallery_detail_pagetitle);
        gallery_detail_gridview = (GridView) activity.findViewById(R.id.gallery_detail_gridview);

        gallery_detail_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gallery_detail_gridview.setAdapter(adapter);
        pagetitle.setText(titleDir);
        imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        getList();
    }

    private void initAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Gallery Detail");
    }

    private void getList()
    {
        new AsyncTask<Void, Integer, String>(){

            ArrayList<String> temp_id, temp_thumb;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                temp_id = new ArrayList<String>();
                temp_thumb = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... voids) {
                File dir = new File(pathDir);
                if(dir != null)
                {
                    File[] fList = dir.listFiles();
                    Arrays.sort(fList, new Comparator<File>() {
                        @Override
                        public int compare(File file, File t1) {
                            return t1.lastModified() < file.lastModified() ? -1 : file.lastModified() == t1.lastModified() ? 0 : 1;
                        }
                    });
                    int iterator = 0;
                    for (File file : fList) {
                        if (file.isFile()) {
                            if (HelperGlobal.checkIfFileIsImage(file.getAbsolutePath().toString())) {
                                temp_id.add(Integer.toString(iterator));
                                temp_thumb.add(Uri.decode(Uri.fromFile(file).toString()));
                                publishProgress(iterator);
                                iterator++;
                            }
                        }
                    }
                }
                return "";
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                mIds.add(temp_id.get(values[0]));
                mThumb.add(temp_thumb.get(values[0]));
                adapter.notifyDataSetChanged();

                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

        }.execute();
    }

    private void back()
    {
        activity.onBackPressed();
    }



    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_GALLERY_DETAIL;
    }

    public class GalleryDetailAdapter extends BaseAdapter {

        public GalleryDetailAdapter()
        {

        }

        public void add(String path) {
            mIds.add(path);
            mThumb.add(path);
        }
        public void clear() {
            mIds.clear();
            mThumb.clear();
        }
        public void remove(int index){
            mIds.remove(index);
            mThumb.remove(index);
        }

        public int getCount() {
            return mIds.size();
        }

        public Object getItem(int position) {
            Object[] objects = new Object[2];
            objects[0] = mIds.get(position);
            objects[1] = mThumb.get(position);
            return objects;
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        public View getView(final int position, View view, ViewGroup parent) {

            final ViewHolder holder;
            if(view == null)
            {
                view = inflater.inflate(R.layout.item_fragment_gallery_detail, null);
            }
            holder = new ViewHolder();
            holder.imagethumb = (ImageView) view.findViewById(R.id.item_fragment_gallery_detail_thumb);
            Picasso.with(activity).load(mThumb.get(position)).fit().centerCrop().into(holder.imagethumb, new Callback() {
                @Override
                public void onSuccess() {
                    holder.imagethumb.setAdjustViewBounds(true);
                    holder.imagethumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onError() {

                }
            });
            view.setTag(holder);
            return view;
        }

        class ViewHolder{
            ImageView imagethumb;
        }

    }
}
