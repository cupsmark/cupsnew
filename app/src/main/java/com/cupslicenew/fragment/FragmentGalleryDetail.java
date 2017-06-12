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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cupslicenew.activity.ActivityEditor;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.util.RecyclerViewItemDecoration;
import com.cupslicenew.core.view.ViewSquareImage;
import com.cupslicenew.view.ViewLoadingDialog;
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
    RecyclerView gallery_detail_recyclerview;
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
        gallery_detail_recyclerview = (RecyclerView) activity.findViewById(R.id.gallery_detail_recyclerview);

        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(2);
        final GridLayoutManager recycleLayoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
        gallery_detail_recyclerview.addItemDecoration(decoration);
        gallery_detail_recyclerview.setHasFixedSize(true);
        gallery_detail_recyclerview.setLayoutManager(recycleLayoutManager);
        gallery_detail_recyclerview.setItemAnimator(new DefaultItemAnimator());
        gallery_detail_recyclerview.setAdapter(adapter);
        gallery_detail_recyclerview.addOnItemTouchListener(new HelperGlobal.RecyclerTouchListener(activity.getApplicationContext(), gallery_detail_recyclerview, new HelperGlobal.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                chooseImage(mThumb.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

    private void chooseImage(final String filename)
    {
        new AsyncTask<Void, Integer, String>()
        {
            ViewLoadingDialog dialog;
            boolean success = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ViewLoadingDialog(activity);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HelperDB db = new HelperDB(activity);
                db.resetHistory();
                Uri uri = Uri.parse(filename);
                File file = new File(uri.getPath().toString());
                String newfilename = file.getAbsolutePath();
                if(newfilename != null && !newfilename.equals(""))
                {
                    HelperGlobal.SetFirstHistory(activity,newfilename);
                    success = true;
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(success)
                {
                    /*Map<String, String> param = new HashMap<String, String>();
                    FragmentEditor editor = new FragmentEditor();
                    fragmentInterface.onNavigate(editor, param);*/
                    Intent i = new Intent(activity, ActivityEditor.class);
                    activity.startActivity(i);
                }
                if(!success)
                {
                    Toast.makeText(activity, "Something Wrong with your Device", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_GALLERY_DETAIL;
    }

    public class GalleryDetailAdapter extends RecyclerView.Adapter<GalleryDetailAdapter.GalleryDetailViewHolder> {

        public GalleryDetailAdapter()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public GalleryDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_fragment_gallery_detail, parent, false);

            return new GalleryDetailViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final GalleryDetailViewHolder holder, int position) {

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

        }


        @Override
        public int getItemCount() {
            return mIds.size();
        }

        public class GalleryDetailViewHolder extends RecyclerView.ViewHolder{
            ViewSquareImage imagethumb;

            public GalleryDetailViewHolder(View itemView) {
                super(itemView);
                imagethumb = (ViewSquareImage) itemView.findViewById(R.id.item_fragment_gallery_detail_thumbnail);
            }
        }

        public void clear() {
            mIds.clear();
            mThumb.clear();
        }
        public void remove(int index){
            mIds.remove(index);
            mThumb.remove(index);
        }
    }
}
