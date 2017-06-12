package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.controller.ControllerStore;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.util.EndlessRecyclerViewScrollListener;
import com.cupslicenew.core.util.RecyclerViewItemDecoration;
import com.cupslicenew.core.view.ViewGridWithHeader;
import com.cupslicenew.view.ViewButton;
import com.cupslicenew.view.ViewSlideShow;
import com.cupslicenew.view.ViewText;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

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
    int tabIndex = 1, l = 6, o = 0;
    ImageButton imagebutton_back, imagebutton_home;
    RecyclerView recyclerView;
    View header;
    LayoutInflater inflater;
    ArrayList<String> product_id, product_title, product_example, product_price, product_client;
    StoreAdapter adapter;
    boolean success = false, isThreadRun = false;
    String message, tabState = "new", keyword = "", storeState = "stickers";
    SearchTaskStore search;

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
        recyclerView = (RecyclerView) activity.findViewById(R.id.store_recyclerview);


        RecyclerViewItemDecoration paddingDecoration = new RecyclerViewItemDecoration(12);
        paddingDecoration.setHaveHeader(true);
        final GridLayoutManager recycleLayoutManager = new GridLayoutManager(activity.getApplicationContext(), 2);
        recyclerView.addItemDecoration(paddingDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(recycleLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recycleLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? recycleLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.addOnItemTouchListener(new HelperGlobal.RecyclerTouchListener(activity.getApplicationContext(), recyclerView, new HelperGlobal.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(activity, product_title.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(recycleLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getMore();
            }
        });
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
        getList();
    }

    private void getList()
    {
        o = 0;
        search = new SearchTaskStore(keyword);
        search.setO(o);
        search.setL(l);
        search.execute();
    }

    private void getMore()
    {
        search = new SearchTaskStore(keyword);
        search.setO(o);
        search.setL(l);
        search.execute();
    }

    public class SearchTaskStore extends AsyncTask<Void, Integer, String>{
        private boolean canceled = false, success = false;
        String message, keyword = "";
        ArrayList<String> tempid, tempexamp, tempprc, temptitle, tempclient;
        int l = 6, o = 0;

        public SearchTaskStore(String keyword)
        {
            this.keyword = keyword;
        }
        public void setL(int l)
        {
            this.l = l;
        }

        public void setO(int o)
        {
            this.o = o;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tempid = new ArrayList<String>();
            tempexamp = new ArrayList<String>();
            tempprc = new ArrayList<String>();
            temptitle = new ArrayList<String>();
            tempclient = new ArrayList<String>();
        }

        @Override
        protected String doInBackground(Void... params) {
            if(!canceled)
            {
                ControllerStore store = new ControllerStore(activity);
                store.setL(this.l);
                store.setO(this.o);
                store.setArea("a");
                store.setURL(HelperGlobal.GU(159182));
                store.setState(storeState);
                store.setSorting(tabState);
                store.isCategory(false);
                store.setCategory(0);
                store.executeListProduct();
                if(store.getSuccess())
                {
                    tempid.addAll(store.getID());
                    tempexamp.addAll(store.getFile());
                    tempprc.addAll(store.getPrice());
                    temptitle.addAll(store.getTitle());
                    tempclient.addAll(store.getClient());
                    FragmentStore.this.o = store.getOffset();
                    success = true;
                }
                else {
                    success = false;
                    message = store.getMessage();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!canceled)
            {
                if(success)
                {
                    if(tempid.size() > 0)
                    {
                        for(int i = 0;i < tempid.size();i++)
                        {
                            product_id.add(tempid.get(i));
                            product_example.add(tempexamp.get(i));
                            product_title.add(temptitle.get(i));
                            product_price.add(tempprc.get(i));
                            product_client.add(tempclient.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void cancel()
        {
            canceled = true;
        }
    }

    private void back()
    {
        activity.onBackPressed();
    }

    private void setHeader(View view)
    {

    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_STORE;
    }

    public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        @Override
        public int getItemViewType(int position) {
            return isHeader(position) ?
                    TYPE_HEADER : TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_HEADER) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_header_store, parent, false);

                return new StoreHeader(itemView);
            }
            else
            {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_fragment_store, parent, false);

                return new StoreViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof StoreHeader)
            {
                StoreHeader mainHolder = (StoreHeader) holder;
                ViewSlideShow slideShow = new ViewSlideShow(activity);
                slideShow.setDefaultImage();
                mainHolder.containerSlider.addView(slideShow);
                slideShow.extractContent();

                mainHolder.text_categories.setRegular();
                mainHolder.text_popular.setRegular();
                mainHolder.text_new.setRegular();
            }
            else
            {
                final StoreViewHolder mainHolder = (StoreViewHolder) holder;
                String price = (product_price.get(position).equals("0")) ? "Free" : product_price.get(position);
                mainHolder.text_name.setSemiBold();
                mainHolder.text_name.setText(product_title.get(position));
                mainHolder.text_price.setText(price.toUpperCase(Locale.US));
                Picasso.with(activity).load(product_example.get(position)).fit().centerCrop().into(mainHolder.imagethumb, new Callback() {
                    @Override
                    public void onSuccess() {
                        mainHolder.imagethumb.setAdjustViewBounds(true);
                        mainHolder.imagethumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return product_id.size();
        }

        public class StoreViewHolder extends RecyclerView.ViewHolder{
            ImageView imagethumb;
            ViewText text_name, text_price;

            public StoreViewHolder(View itemView) {
                super(itemView);
                imagethumb = (ImageView) itemView.findViewById(R.id.item_fragment_store_image);
                text_name = (ViewText) itemView.findViewById(R.id.item_fragment_store_name);
                text_price = (ViewText) itemView.findViewById(R.id.item_fragment_store_price);
            }
        }

        class StoreHeader extends RecyclerView.ViewHolder{
            RelativeLayout containerSlider;
            LinearLayout btn_categories, btn_new, btn_popular;
            ImageButton icon_categories, icon_new, icon_popular;
            ViewText text_categories, text_new, text_popular;

            public StoreHeader(View itemView) {
                super(itemView);
                containerSlider = (RelativeLayout) itemView.findViewById(R.id.view_header_store_slider);
                btn_categories = (LinearLayout) itemView.findViewById(R.id.view_header_store_button_categories);
                btn_new = (LinearLayout) itemView.findViewById(R.id.view_header_store_button_new);
                btn_popular = (LinearLayout) itemView.findViewById(R.id.view_header_store_button_popular);
                icon_categories = (ImageButton) itemView.findViewById(R.id.view_header_store_btn_categories_icon);
                icon_new = (ImageButton) itemView.findViewById(R.id.view_header_store_btn_new_icon);
                icon_popular = (ImageButton) itemView.findViewById(R.id.view_header_store_btn_popular_icon);
                text_categories = (ViewText) itemView.findViewById(R.id.view_header_store_btn_categories_text);
                text_new = (ViewText) itemView.findViewById(R.id.view_header_store_btn_new_text);
                text_popular = (ViewText) itemView.findViewById(R.id.view_header_store_btn_popular_text);
            }
        }

        public void clear()
        {
            product_id.clear();
            product_title.clear();
            product_client.clear();
            product_price.clear();
            product_example.clear();
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

    }

}
