package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.view.ViewGridWithHeader;
import com.cupslicenew.view.ViewText;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 3/31/16.
 */
public class FragmentHome extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_HOME = "tag:fragment-home";
    Tracker tracker;
    ViewGridWithHeader gridWithHeader;
    View view_header;
    LayoutInflater inflater;
    ArrayList<String> menu_title, menu_id;
    ArrayList<Integer> menu_icon;
    MenuAdapter adapter;
    ViewText header_title, header_desc;
    ImageButton imagebutton_invite, imagebutton_facebook, imagebutton_ig, imagebutton_email;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_home, container, false);
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
        HelperGlobal.sendAnalytic(tracker, "Page Home");
    }

    private void init()
    {
        menu_id = new ArrayList<String>();
        menu_title = new ArrayList<String>();
        menu_icon = new ArrayList<Integer>();
        adapter = new MenuAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gridWithHeader = (ViewGridWithHeader) activity.findViewById(R.id.home_gridviewheader);
        view_header = inflater.inflate(R.layout.view_header_home, null, false);
        header_title = (ViewText) view_header.findViewById(R.id.view_header_home_title);
        header_desc = (ViewText) view_header.findViewById(R.id.view_header_home_desc);
        imagebutton_invite = (ImageButton) view_header.findViewById(R.id.view_header_home_invite);
        imagebutton_facebook = (ImageButton) view_header.findViewById(R.id.view_header_home_facebook);
        imagebutton_ig = (ImageButton) view_header.findViewById(R.id.view_header_home_instagram);
        imagebutton_email = (ImageButton) view_header.findViewById(R.id.view_header_home_email);

        header_title.setBold();
        header_title.setRegular();
        gridWithHeader.addHeaderView(view_header);
        gridWithHeader.setAdapter(adapter);
        imagebutton_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInvite();
            }
        });
        imagebutton_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFB();
            }
        });
        imagebutton_ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIG();
            }
        });
        imagebutton_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMail();
            }
        });
        gridWithHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ids = menu_id.get(position - 3);
                if(ids.equals("1"))
                {
                    FragmentCamera camera = new FragmentCamera();
                    Map<String, String> parameter = new HashMap<String, String>();
                    fragmentInterface.onNavigate(camera, parameter);
                }
                else if(ids.equals("2"))
                {
                    FragmentGallery gallery = new FragmentGallery();
                    Map<String, String> parameter = new HashMap<String, String>();
                    fragmentInterface.onNavigate(gallery, parameter);
                }
                else if(ids.equals("3"))
                {
                    FragmentHowToUse htu = new FragmentHowToUse();
                    Map<String, String> parameter = new HashMap<String, String>();
                    fragmentInterface.onNavigate(htu, parameter);
                }
                else if(ids.equals("4"))
                {
                    FragmentStore store = new FragmentStore();
                    Map<String, String> parameter = new HashMap<String, String>();
                    fragmentInterface.onNavigate(store, parameter);
                }
                else if(ids.equals("5"))
                {
                    FragmentRecommended recommended = new FragmentRecommended();
                    Map<String, String> parameter = new HashMap<String, String>();
                    fragmentInterface.onNavigate(recommended, parameter);
                }
                else
                {
                    FragmentSettings settings = new FragmentSettings();
                    Map<String, String> parameter = new HashMap<String, String>();
                    fragmentInterface.onNavigate(settings, parameter);
                }
            }
        });

        getList();
    }

    private void getList()
    {
        menu_id.add("1");
        menu_id.add("2");
        menu_id.add("3");
        menu_id.add("4");
        menu_id.add("5");
        menu_id.add("6");

        menu_title.add("Camera");
        menu_title.add("Album");
        menu_title.add("About");
        menu_title.add("Cupslice Cafe");
        menu_title.add("Recommended Apps");
        menu_title.add("Settings");

        menu_icon.add(R.drawable.icon_camera);
        menu_icon.add(R.drawable.icon_gallery);
        menu_icon.add(R.drawable.icon_about);
        menu_icon.add(R.drawable.icon_cupslicecafe);
        menu_icon.add(R.drawable.icon_download);
        menu_icon.add(R.drawable.icon_setting);

        adapter.notifyDataSetChanged();
    }

    private void startIG()
    {
        String link_ig = "instagram://user?username=cupslice";
        Uri uri = Uri.parse(link_ig);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try{
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/cupslice")));
        }
    }

    private void startFB()
    {
        String facebookUrl = "https://www.facebook.com/Cupslice";
        try {
            int versionCode = activity.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));;
            } else {
                // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/355353641221854")));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    private void startMail()
    {
        try {
            SharedPreferences prefs = activity.getSharedPreferences("mob_gen", Context.MODE_PRIVATE);
            String prefemail = prefs.getString("esupport", "");
            String emails = "";
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("plain/text");
            if (prefemail.equals("")) {
                emails = "support@cupslice.com";
            } else {
                emails = prefemail;
            }
            sendIntent.setData(Uri.parse(emails));
            sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emails});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(sendIntent);
        }catch (ActivityNotFoundException ex)
        {
            Toast.makeText(activity, "Gmail " + getResources().getString(R.string.base_string_activity_notfound), Toast.LENGTH_SHORT).show();
        }
    }

    private void startInvite()
    {
        String shareBody = "Hello, I would like to invite you to use Cupslice Photo Editor. Cupslice is easy-to-use photography app that offers lot of filter, " +
                "frame, font and badge with added basic editing tools combine with up-to-date stickers. " +
                "You can download Cupslice on Google Play or click this link bit.ly/1EKtFze";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation for Cupslice Photo Editor");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "SHARE USING"));
    }


    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_HOME;
    }

    private void back()
    {
        activity.onBackPressed();
    }

    public class MenuAdapter extends BaseAdapter{

        public MenuAdapter()
        {

        }

        public void clear()
        {
            menu_id.clear();
            menu_title.clear();
            menu_icon.clear();
        }

        public void remove(int i)
        {
            menu_id.remove(i);
            menu_title.remove(i);
            menu_icon.remove(i);
        }

        @Override
        public int getCount() {
            return menu_id.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] objects = new Object[3];
            objects[0] = menu_id.get(position);
            objects[1] = menu_title.get(position);
            objects[2] = menu_icon.get(position);
            return objects;
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
                convertView = inflater.inflate(R.layout.item_fragment_home, null);
            }
            holder.imageview_icon = (ImageView) convertView.findViewById(R.id.item_fragment_home_imageview);
            holder.textview_title = (ViewText) convertView.findViewById(R.id.item_fragment_home_textview);

            holder.textview_title.setSemiBold();
            holder.textview_title.setText(menu_title.get(position));
            Picasso.with(activity).load(menu_icon.get(position)).into(holder.imageview_icon, new Callback() {
                @Override
                public void onSuccess() {
                    holder.imageview_icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    holder.imageview_icon.setAdjustViewBounds(true);
                }

                @Override
                public void onError() {

                }
            });
            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ImageView imageview_icon;
            ViewText textview_title;
        }
    }
}

