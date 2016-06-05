package com.cupslicenew.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.view.ViewText;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentGallery extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_GALLERY = "tag:fragment-gallery";

    private ArrayList<String> album_path, album_name, album_directory,album_count;
    private ListView listview_gallery;
    Tracker tracker;
    ImageButton imagebutton_back;
    LayoutInflater inflater;
    GalleryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_gallery, container, false);
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
        HelperGlobal.sendAnalytic(tracker, "Page Gallery");
    }

    private void init()
    {
        album_path = new ArrayList<String>();
        album_name = new ArrayList<String>();
        album_directory = new ArrayList<String>();
        album_count = new ArrayList<String>();
        adapter = new GalleryAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebutton_back = (ImageButton) activity.findViewById(R.id.gallery_imagebutton_back);
        listview_gallery = (ListView) activity.findViewById(R.id.gallery_listview);

        listview_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> parameter = new HashMap<String, String>();
                parameter.put("album_name", album_name.get(position));
                parameter.put("album_directory", album_directory.get(position));
                parameter.put("album_count", album_count.get(position));
                FragmentGalleryDetail galleryDetail = new FragmentGalleryDetail();
                fragmentInterface.onNavigate(galleryDetail, parameter);
            }
        });
        listview_gallery.setAdapter(adapter);
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
        new AsyncTask<Void, Integer, String>(){

            ArrayList<String> temp_album_name, temp_image_path, temp_album_dir, temp_album_count;

            @Override
            protected void onPreExecute() {
                temp_album_count = new ArrayList<String>();
                temp_image_path = new ArrayList<String>();
                temp_album_dir = new ArrayList<String>();
                temp_album_name = new ArrayList<String>();

                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                ArrayList<String> list_album = new ArrayList<String>();
                String[] PROJECTION_BUCKET = {
                        MediaStore.Images.ImageColumns.BUCKET_ID,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.DATA,
                        "count(" + MediaStore.Images.ImageColumns.DATA + ") as COUNTS"
                };


                String BUCKET_GROUP_BY =
                        "1) GROUP BY 1,(2";
                String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

                // Get the base URI for the People table in the Contacts content provider.
                Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                Cursor cur = activity.getContentResolver().query(
                        images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);


                if (cur.moveToFirst()) {
                    String bucket;
                    String date;
                    String data;
                    String nums;
                    int bucketColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                    int dateColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATE_TAKEN);
                    int dataColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATA);
                    int numColumn = cur.getColumnIndex("COUNTS");

                    int i = 0;
                    do {
                        // Get the field values
                        bucket = cur.getString(bucketColumn);
                        date = cur.getString(dateColumn);
                        data = cur.getString(dataColumn);
                        nums = cur.getString(numColumn);

                        File f = new File(data);
                        String path_image = Uri.fromFile(f).toString();
                        String realpath = data.substring(0, data.lastIndexOf("/"));
                        list_album.add(bucket + "-cup-" + date + "-cup-" + Uri.decode(path_image) + "-cup-" + Uri.decode(realpath) + "-cup-" + nums);
                        // Do something with the values.
                        temp_album_name.add(bucket);
                        temp_image_path.add(Uri.decode(path_image));
                        temp_album_dir.add(Uri.decode(realpath));
                        temp_album_count.add(nums);
                        publishProgress(i);
                        i++;

                    } while (cur.moveToNext());
                }
                return "";
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                try {
                    album_name.add(temp_album_name.get(values[0]));
                    album_path.add(temp_image_path.get(values[0]));
                    album_directory.add(temp_album_dir.get(values[0]));
                    album_count.add(temp_album_count.get(values[0]));
                    adapter.notifyDataSetChanged();
                }catch (Exception ex)
                {
                    Toast.makeText(activity, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

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
        return TAG_FRAGMENT_GALLERY;
    }

    public class GalleryAdapter extends BaseAdapter{

        public  GalleryAdapter()
        {

        }

        private void clear()
        {
            album_path.clear();
            album_name.clear();
            album_directory.clear();
            album_count.clear();
        }

        private void remove(int i)
        {
            album_path.remove(i);
            album_name.remove(i);
            album_directory.remove(i);
            album_count.remove(i);
        }

        @Override
        public int getCount() {
            return album_directory.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[4];
            object[0] = album_path.get(position);
            object[1] = album_name.get(position);
            object[2] = album_directory.get(position);
            object[3] = album_count.get(position);
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
                convertView = inflater.inflate(R.layout.item_fragment_gallery, null);
            }
            holder.text_title = (ViewText) convertView.findViewById(R.id.item_fragment_gallery_title);
            holder.text_title.setRegular();
            holder.text_title.setText(album_name.get(position));
            holder.text_path = (ViewText) convertView.findViewById(R.id.item_fragment_gallery_path);
            holder.text_path.setText(album_directory.get(position));
            holder.image_thumb = (CircularImageView) convertView.findViewById(R.id.item_fragment_gallery_thumb);
            Picasso.with(activity).load(album_path.get(position)).fit().centerCrop().into(holder.image_thumb, new Callback() {
                @Override
                public void onSuccess() {
                    holder.image_thumb.setAdjustViewBounds(true);
                }

                @Override
                public void onError() {

                }
            });
            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ViewText text_title, text_path;
            CircularImageView image_thumb;
        }
    }

}
