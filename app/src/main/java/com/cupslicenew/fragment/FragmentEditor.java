package com.cupslicenew.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.core.view.ViewCoreSticker;
import com.cupslicenew.view.ViewLoadingDialog;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import jp.co.cyberagent.android.gpuimage.GPUImage;


/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentEditor extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_EDITOR = "tag:fragment-editor";

    Tracker tracker;
    ImageView imageview_editor;
    GPUImage mGPUImage;
    Bitmap bitmapSource, bitmapFilter, bitmapFrame, bitmapSticker;
    ViewCoreSticker viewSticker;
    RelativeLayout centerContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_editor, container, false);
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
            initGoogleAnalytics();
            init();
            getImage();
        }
    }

    private void init()
    {
        imageview_editor = (ImageView) activity.findViewById(R.id.fragment_editor_imageview_main);
        centerContainer = (RelativeLayout) activity.findViewById(R.id.fragment_editor_content_image);
    }

    private void getImage()
    {
        new AsyncTask<Void, Integer, String>()
        {

            ViewLoadingDialog dialog;
            boolean success = false;
            Bitmap bmpConverted;

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
                File directory = activity.getFilesDir();
                String lastFile = HelperGlobal.getLastHistoryFile(activity);
                if(db.getHistoryCount() != 0)
                {
                    String fileResult = "";
                    if(db.getHistoryCount() > 1)
                    {
                        File file = new File(directory,lastFile);
                        fileResult = file.getPath().toString();
                    }
                    else {
                        fileResult = lastFile;
                    }
                    bmpConverted = HelperGlobal.getExifBitmap(fileResult);
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
                if(success && bmpConverted != null)
                {
                    imageview_editor.setImageBitmap(bmpConverted);
                    bitmapSource = bmpConverted;
                    addFragmentMainMenu();
                }
            }
        }.execute();
    }

    private void initGoogleAnalytics()
    {
        tracker = ((HelperGoogle) activity.getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Editor");
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_EDITOR;
    }

    private void addFragmentMainMenu()
    {
        FragmentEditorMainMenu mainMenu = new FragmentEditorMainMenu();
        //mainMenu.setFragmentEditor(FragmentEditor.this);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_editor_bottombar, mainMenu);
        ft.commit();
    }

    public void setFragmentSideMenu(BaseFragment fragment)
    {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        if(!fragment.isAdded())
        {
            ft.add(R.id.fragment_editor_sidemenu, fragment);
        }
        else {
            fragment.updateFragment();
        }
        ft.commit();
    }

    public void setFragmentItemSupportMenu(Map<String, String> parameter)
    {
        FragmentEditorItemSupportMenu supportMenu = new FragmentEditorItemSupportMenu();
        supportMenu.setParameter(parameter);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        if(!supportMenu.isAdded())
        {
            ft.add(R.id.fragment_editor_bottombar, supportMenu);
        }
        else {
            supportMenu.updateFragment();
        }
        ft.commit();
    }

    public void setBitmap(Bitmap bmp)
    {
        imageview_editor.setImageBitmap(bmp);
    }

    public Bitmap getBitmap()
    {
        return bitmapSource;
    }

    public void setBitmapFilter(Bitmap filter)
    {
        this.bitmapFilter = filter;
    }

    public void setBitmapFrame(Bitmap frame)
    {
        this.bitmapFrame = frame;
    }

    public void setBitmapSticker(Bitmap sticker)
    {
        this.bitmapSticker = sticker;
    }

    public Bitmap getBitmapFilter()
    {
        return this.bitmapFilter;
    }

    public Bitmap getBitmapFrame()
    {
        return this.bitmapFrame;
    }

    public Bitmap getBitmapSticker()
    {
        return this.bitmapSticker;
    }

    public void updateFilter()
    {
        Bitmap filtered = HelperImage.doFilter(bitmapSource, bitmapFilter);
        setBitmap(filtered);

    }

    public void updateFrame()
    {
        Bitmap filtered = HelperImage.doFrame(bitmapSource, bitmapFrame);
        setBitmap(filtered);
    }

    public void updateSticker()
    {
        if(viewSticker == null)
        {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            param.addRule(RelativeLayout.ALIGN_LEFT, R.id.fragment_editor_imageview_main);
            param.addRule(RelativeLayout.ALIGN_RIGHT, R.id.fragment_editor_imageview_main);
            param.addRule(RelativeLayout.ALIGN_TOP, R.id.fragment_editor_imageview_main);
            param.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.fragment_editor_imageview_main);
            viewSticker = new ViewCoreSticker(activity);
            viewSticker.setLayoutParams(param);
            viewSticker.setId(R.id.view_touch);

            centerContainer.addView(viewSticker);
        }
        viewSticker.setWaterMark(bitmapSticker);
    }

}
