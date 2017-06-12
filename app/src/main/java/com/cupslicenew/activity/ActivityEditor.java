package com.cupslicenew.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperGoogle;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.core.view.ViewCoreCrop;
import com.cupslicenew.core.view.ViewCoreSticker;
import com.cupslicenew.fragment.FragmentEditorItemSupportMenu;
import com.cupslicenew.fragment.FragmentEditorMainMenu;
import com.cupslicenew.fragment.FragmentEditorSideMenu;
import com.cupslicenew.fragment.FragmentEditorTopMenu;
import com.cupslicenew.view.ViewLoadingDialog;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import jp.co.cyberagent.android.gpuimage.GPUImage;

public class ActivityEditor extends BaseActivity implements HelperGlobal.EditorBehavior{

    Tracker tracker;
    ImageView imageview_editor;
    GPUImage mGPUImage;
    Bitmap bitmapSource, bitmapFeature, bitmapTempSource;
    ViewCoreSticker viewSticker;
    ViewCoreCrop viewCrop;
    RelativeLayout centerContainer;
    FragmentEditorMainMenu fragmentMainMenu;
    FragmentEditorSideMenu fragmentSideMenu;
    FragmentEditorItemSupportMenu fragmentSupportMenu;
    FragmentEditorTopMenu fragmentTopMenu;
    String activeFeature = "", adjustFeature = "";
    int selectedColor = 0x00FFFFFF, percentageOpacity = 100, degreeRotate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Fabric.with(this, new Crashlytics());
        initGoogleAnalytics();
        getImage();
    }

    private void init()
    {
        imageview_editor = (ImageView) findViewById(R.id.fragment_editor_imageview_main);
        centerContainer = (RelativeLayout) findViewById(R.id.fragment_editor_content_image);
        fragmentMainMenu = (FragmentEditorMainMenu) getSupportFragmentManager().findFragmentById(R.id.fragment_editor_main_menu);
        fragmentSideMenu = (FragmentEditorSideMenu) getSupportFragmentManager().findFragmentById(R.id.fragment_editor_side_menu);
        fragmentSupportMenu = (FragmentEditorItemSupportMenu) getSupportFragmentManager().findFragmentById(R.id.fragment_editor_item_support_menu);
        fragmentTopMenu = (FragmentEditorTopMenu) getSupportFragmentManager().findFragmentById(R.id.fragment_editor_top_menu);

        if(fragmentSideMenu != null)
        {
            getSupportFragmentManager().beginTransaction().hide(fragmentSideMenu).commit();
        }
        if(fragmentSupportMenu != null)
        {
            getSupportFragmentManager().beginTransaction().hide(fragmentSupportMenu).commit();
        }
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
                dialog = new ViewLoadingDialog(ActivityEditor.this);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HelperDB db = new HelperDB(ActivityEditor.this);
                File directory = getFilesDir();
                String lastFile = HelperGlobal.getLastHistoryFile(ActivityEditor.this);
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
                    Bitmap exif = HelperGlobal.getExifBitmap(fileResult);
                    bmpConverted = HelperImage.doResizeFromBitmap(exif, 1200, 1200, false);
                    //bmpConverted = HelperGlobal.getExifBitmap(fileResult);
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
                    setBitmap(bmpConverted);
                    bitmapSource = bmpConverted;
                    bitmapTempSource = bitmapSource;
                }
            }
        }.execute();
    }

    private void initGoogleAnalytics()
    {
        tracker = ((HelperGoogle) getApplication()).getTracker(HelperGoogle.TrackerName.APP_TRACKER);
        HelperGlobal.sendAnalytic(tracker, "Page Editor");
    }



    public void setBitmap(Bitmap bmp)
    {
        imageview_editor.setImageBitmap(bmp);
    }

    public Bitmap getBitmap()
    {
        return bitmapSource;
    }

    private Bitmap getImageViewBitmap()
    {
        Bitmap result = ((BitmapDrawable) imageview_editor.getDrawable()).getBitmap();
        return result;
    }

    public void updateFilter()
    {
        Bitmap opacityFeature = HelperImage.doOpacityFilter(ActivityEditor.this, bitmapFeature, percentageOpacity);
        Bitmap filtered = HelperImage.doFilter(bitmapTempSource, opacityFeature);
        setBitmap(filtered);
    }

    public void updateFrame()
    {
        Bitmap opacityFrame = HelperImage.doOpacity(bitmapFeature, percentageOpacity);
        Bitmap colorFrame = HelperImage.doColorOverlay(selectedColor, opacityFrame);
        Bitmap filtered = HelperImage.doFrame(bitmapTempSource, colorFrame);
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
            viewSticker = new ViewCoreSticker(ActivityEditor.this);
            viewSticker.setLayoutParams(param);
            viewSticker.setId(R.id.view_touch);

            centerContainer.addView(viewSticker);
        }
        viewSticker.setWaterMark(bitmapFeature);
        viewSticker.setColor(selectedColor);
        viewSticker.setOpacity(percentageOpacity);
    }

    private void updateAdjust()
    {
        if(!adjustFeature.equals(""))
        {
            updateAdjustment();
        }
    }

    private void updateAdjustment()
    {
        new AsyncTask<Void, Integer, String>()
        {

            Bitmap adjusted;
            ViewLoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ViewLoadingDialog(ActivityEditor.this);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                if(adjustFeature.equals("brightness"))
                {
                    adjusted = HelperImage.doBrightness(ActivityEditor.this, bitmapTempSource, percentageOpacity);
                }
                if(adjustFeature.equals("contrast"))
                {
                    adjusted = HelperImage.doContrast(ActivityEditor.this, bitmapTempSource, percentageOpacity);
                }
                if(adjustFeature.equals("saturation"))
                {
                    adjusted = HelperImage.doSaturation(ActivityEditor.this, bitmapTempSource, percentageOpacity);
                }
                if(adjustFeature.equals("balance"))
                {
                    adjusted = HelperImage.doColorBalance(bitmapTempSource, percentageOpacity);
                }
                if(adjustFeature.equals("bw"))
                {
                    adjusted = HelperImage.doBW(bitmapTempSource, percentageOpacity);
                }
                if(adjustFeature.equals("vignette"))
                {
                    adjusted = HelperImage.doVignette(ActivityEditor.this, bitmapTempSource, percentageOpacity);
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
                if(adjusted != null)
                {
                    setBitmap(adjusted);
                }
            }
        }.execute();

    }

    private void updateCrop()
    {
        if(viewCrop == null)
        {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            param.addRule(RelativeLayout.ALIGN_LEFT, R.id.fragment_editor_imageview_main);
            param.addRule(RelativeLayout.ALIGN_RIGHT, R.id.fragment_editor_imageview_main);
            param.addRule(RelativeLayout.ALIGN_TOP, R.id.fragment_editor_imageview_main);
            param.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.fragment_editor_imageview_main);
            viewCrop = new ViewCoreCrop(ActivityEditor.this);
            viewCrop.setLayoutParams(param);
            viewCrop.setId(R.id.view_crop);
        }
    }


    @Override
    public void setActiveFeature(String feature) {
        activeFeature = feature;
        Map<String, String> param = new HashMap<String, String>();
        param.put("feature", activeFeature);
        if(activeFeature.equals("adjust"))
        {
            fragmentSupportMenu.setParameter(param);
            fragmentSupportMenu.setBitmapFeature(bitmapFeature);
            fragmentSupportMenu.setBitmapSource(bitmapTempSource);
            fragmentSupportMenu.updateFragment();
        }
        else if(activeFeature.equals("rotate"))
        {

        }
        else {
            fragmentSideMenu.setParameter(param);
            fragmentSideMenu.updateFragment();
        }
    }

    @Override
    public void processFeature() {
        fragmentMainMenu.hide();
        Map<String, String> param = new HashMap<String, String>();
        param.put("feature", activeFeature);
        fragmentSupportMenu.setParameter(param);
        fragmentSupportMenu.setBitmapFeature(bitmapFeature);
        fragmentSupportMenu.setBitmapSource(bitmapTempSource);
        fragmentSupportMenu.updateFragment();
    }

    @Override
    public void setOptionBlend(Map<String, String> parameter) {
        selectedColor = Color.parseColor(parameter.get("selectedColor"));
        percentageOpacity = Integer.parseInt(parameter.get("percentageOpacity"));
        adjustFeature = parameter.get("adjustFeature");
    }

    @Override
    public void setBitmapFeature(Bitmap asset) {
        this.bitmapFeature = asset;
    }

    @Override
    public void pushImage(Bitmap result) {

    }

    @Override
    public void updateFeature() {
        if(activeFeature.equals("sticker"))
        {
            updateSticker();
        }
        else if(activeFeature.equals("filter"))
        {
            updateFilter();
        }
        else if(activeFeature.equals("frame"))
        {
            updateFrame();
        }
        else if(activeFeature.equals("adjust"))
        {
            updateAdjust();
        }
        else if(activeFeature.equals("crop"))
        {
            updateCrop();
        }
        else
        {

        }
    }

    @Override
    public void onEditApply() {
        fragmentSupportMenu.hide();
        fragmentMainMenu.show();
        applyFeature();
    }

    @Override
    public void onEditCancel() {
        fragmentSupportMenu.hide();
        fragmentMainMenu.show();
        cancelFeature();
    }

    @Override
    public void onUndo() {

    }

    @Override
    public void onSave() {
        savePicture();
    }

    private void applyFeature()
    {
        if(activeFeature.equals("sticker"))
        {
            applySticker();
        }
        else if(activeFeature.equals("filter"))
        {
            applyFilter();
        }
        else if(activeFeature.equals("frame"))
        {
            applyFrame();
        }
        else if(activeFeature.equals("adjust"))
        {
            applyAdjustment();
        }
        else {

        }
        resetStatVariable();
    }

    private void cancelFeature()
    {
        if(activeFeature.equals("sticker"))
        {
            cancelSticker();
        }
        else if(activeFeature.equals("filter"))
        {
            cancelFilter();
        }
        else if(activeFeature.equals("frame"))
        {
            cancelFrame();
        }
        else if(activeFeature.equals("adjust"))
        {
            cancelAdjustment();
        }
        else {

        }
        resetStatVariable();
    }

    private void applySticker()
    {
        if(viewSticker != null)
        {
            Bitmap sticker = viewSticker.getBitmap();
            Bitmap applied = HelperImage.doSticker(bitmapTempSource, sticker);
            bitmapTempSource = applied;
            setBitmap(applied);
            cancelSticker();
        }
    }

    private void cancelSticker()
    {
        if(viewSticker != null)
        {
            centerContainer.removeView(viewSticker);
            viewSticker = null;
        }
    }

    private void applyFrame()
    {
        Bitmap result = getImageViewBitmap();
        bitmapTempSource = result;
        setBitmap(bitmapTempSource);
    }

    private void cancelFrame()
    {
        setBitmap(bitmapTempSource);
    }

    private void applyFilter()
    {
        Bitmap result = getImageViewBitmap();
        bitmapTempSource = result;
        setBitmap(bitmapTempSource);
    }

    private void cancelFilter()
    {
        setBitmap(bitmapTempSource);
    }

    private void applyAdjustment()
    {
        Bitmap result = getImageViewBitmap();
        bitmapTempSource = result;
        setBitmap(bitmapTempSource);
    }

    private void cancelAdjustment()
    {
        setBitmap(bitmapTempSource);
    }

    private void resetStatVariable()
    {
        selectedColor = 0x00FFFFFF;
        percentageOpacity = 100;
    }

    private void savePicture()
    {
        new AsyncTask<Void, Integer, String>()
        {

            ViewLoadingDialog dialog;
            String path = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ViewLoadingDialog(ActivityEditor.this);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                Bitmap res = getImageViewBitmap();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
                Calendar cal = Calendar.getInstance();
                Date now = cal.getTime();
                String dateFileName = sdf.format(now);
                path = HelperGlobal.doSave(ActivityEditor.this,res,"Cupslice",dateFileName);
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(!path.equals(""))
                {
                    HelperImage.doPushToGallery(ActivityEditor.this,new File(path));
                    Toast.makeText(ActivityEditor.this, "Saved to Gallery", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
