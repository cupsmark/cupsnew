package com.cupslicenew.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 7/5/15.
 */
public class ViewSlideShow extends RelativeLayout {

    Context context;
    LayoutInflater inflater;
    View main_view;
    private String ULink;
    private ArrayList<String> ban_i, ban_t, ban_f, ban_l;

    boolean done_loaded = false;

    public ViewSlideShow(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public ViewSlideShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public ViewSlideShow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout()
    {
        main_view = inflate(context, R.layout.fragment_cafe_slider,this);
        ULink = HelperGlobal.GU(159191) + "token=" + HelperGlobal.getDeviceID(context)+"&l=10&o=0";
    }

    public void setDefaultImage()
    {
        AutoScrollViewPager viewpager = (AutoScrollViewPager) main_view.findViewById(R.id.cafe_slider_autoviewpager);
        viewpager.setVisibility(View.GONE);
    }

    public void extractContent()
    {
        new AsyncTask<Void,Integer,String>(){


            @Override
            protected void onPreExecute() {
                ban_i = new ArrayList<String>();
                ban_t = new ArrayList<String>();
                ban_f = new ArrayList<String>();
                ban_l = new ArrayList<String>();
                done_loaded = false;


                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = "";
                String js = "";

                js = HelperGlobal.getJSON(ULink);
                if (js != null) {
                    try {
                        JSONObject obj = new JSONObject(js);
                        if (obj.getBoolean("status")) {
                            String datas = obj.getString("data");
                            JSONArray arr_data = new JSONArray(datas);
                            int countdata = arr_data.length();
                            if(countdata > 0)
                            {
                                for (int i = 0; i < countdata; i++) {
                                    JSONObject newObj = arr_data.getJSONObject(i);
                                    ban_i.add(newObj.getString("i"));
                                    ban_t.add(newObj.getString("t"));
                                    ban_f.add(newObj.getString("f"));
                                    ban_l.add(newObj.getString("l"));
                                }
                                done_loaded = true;
                            }
                            else {
                                done_loaded = false;
                            }

                        } else {
                            done_loaded = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        done_loaded = false;
                    }
                } else {
                    done_loaded = false;

                }

                return js;
            }

            @Override
            protected void onPostExecute(String s) {
                if(done_loaded)
                {
                    ImageView imageview = (ImageView) main_view.findViewById(R.id.cafe_slider_imageview);
                    AutoScrollViewPager pager = (AutoScrollViewPager)main_view.findViewById(R.id.cafe_slider_autoviewpager);
                    imageview.setVisibility(View.GONE);
                    pager.setVisibility(View.VISIBLE);
                    ScreenAdapter adapter = new ScreenAdapter();
                    pager.setAdapter(adapter);
                    pager.setInterval(5000);
                    pager.startAutoScroll();
                }

                super.onPostExecute(s);
            }
        }.execute();
    }


    private class ScreenAdapter extends PagerAdapter {

        LayoutInflater inflate;


        public ScreenAdapter()
        {
            inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ban_i.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub
            View item = inflate.inflate(R.layout.item_fragment_cafe_slider, null);
            final ImageButton btn_item = (ImageButton) item.findViewById(R.id.fragment_item_cafe_slider_imagebutton);
            final FrameLayout.LayoutParams layoutParam = (FrameLayout.LayoutParams) btn_item.getLayoutParams();
            layoutParam.width = HelperGlobal.getScreenSize(context, "w");
            btn_item.setLayoutParams(layoutParam);
            btn_item.setImageResource(R.drawable.cafe_slider_default);

            btn_item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "hallo", Toast.LENGTH_LONG).show();
                    Uri f = Uri.parse(ban_l.get(position));
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, f));
                    } catch (ActivityNotFoundException anfe) {
                        //context.startActivity(new Intent(Intent.ACTION_VIEW, f));
                        Toast.makeText(context,"This application not installed yet",Toast.LENGTH_LONG).show();
                    }
                }
            });
            container.addView(item);
            //loader.displayImage("http://" + GlobalParam.UPL + "/upload/" + ban_f.get(position), btn_item, opt);
            Picasso.with(context).load(HelperGlobal.GU(159180) + ban_f.get(position)).into(btn_item, new Callback() {
                @Override
                public void onSuccess() {
                    btn_item.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    btn_item.setAdjustViewBounds(true);
                }

                @Override
                public void onError() {

                }
            });

            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((ImageView) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return (arg0 == arg1);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            // TODO Auto-generated method stub

        }
        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            // TODO Auto-generated method stub
        }
        @Override
        public Parcelable saveState() {
            return null;
            // TODO Auto-generated method stub
        }
        @Override
        public void startUpdate(ViewGroup container) {
            // TODO Auto-generated method stub
        }
    }
}
