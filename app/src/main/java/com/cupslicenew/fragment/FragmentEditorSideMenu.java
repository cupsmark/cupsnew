package com.cupslicenew.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.core.model.MEffect;
import com.cupslicenew.core.model.MEffectCategory;
import com.cupslicenew.core.model.MFrame;
import com.cupslicenew.core.model.MFrameCategory;
import com.cupslicenew.core.model.MSticker;
import com.cupslicenew.core.model.MStickerCategory;
import com.cupslicenew.view.ViewImageCategoryItem;
import com.cupslicenew.view.ViewText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 7/13/16.
 */
public class FragmentEditorSideMenu extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    HelperGlobal.EditorBehavior editorBehavior;
    public static final String TAG_FRAGMENT_SIDE_MENU = "tag:fragment-side-menu";

    Map<String, String> parameter;
    ImageButton imagebuttonClose;
    RelativeLayout buttonChoose;
    ViewText textChoose;
    ImageView imageviewChoose;
    boolean chooserOpened = true;
    Bitmap categoryBG;
    String categorySelectedID, categorySelectedTitle, feature;
    ArrayList<String> categoryID, categoryTitle, categoryFile,itemID, itemFile;
    LayoutInflater inflater;
    SideMenuCategoryAdapter categoryAdapter;
    SideMenuItemAdapter itemAdapter;
    ListView listviewCategory, listviewItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_editor_side_menu, container, false);
        return main_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null)
        {
            this.activity = (BaseActivity) activity;
            fragmentInterface = (HelperGlobal.FragmentInterface) this.activity;
            editorBehavior = (HelperGlobal.EditorBehavior) this.activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(activity != null)
        {
            Fabric.with(activity, new Crashlytics());
            init();
        }
    }

    private void init()
    {
        chooserOpened = true;
        categoryID = new ArrayList<String>();
        categoryTitle = new ArrayList<String>();
        categoryFile = new ArrayList<String>();
        itemID = new ArrayList<String>();
        itemFile = new ArrayList<String>();
        categoryAdapter = new SideMenuCategoryAdapter();
        itemAdapter = new SideMenuItemAdapter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagebuttonClose = (ImageButton) activity.findViewById(R.id.fragment_editor_side_menu_close);
        buttonChoose = (RelativeLayout) activity.findViewById(R.id.fragment_editor_side_menu_choose);
        textChoose = (ViewText) activity.findViewById(R.id.fragment_editor_side_menu_choose_title);
        imageviewChoose = (ImageView) activity.findViewById(R.id.fragment_editor_side_menu_choose_icon);
        listviewCategory = (ListView) activity.findViewById(R.id.fragment_editor_side_menu_listcategory);
        listviewItem = (ListView) activity.findViewById(R.id.fragment_editor_side_menu_listitem);

        listviewCategory.setAdapter(categoryAdapter);
        listviewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listviewCategorySelected(position);
            }
        });
        listviewItem.setAdapter(itemAdapter);
        listviewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);
            }
        });
        textChoose.setRegular();
        imagebuttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_left).hide(FragmentEditorSideMenu.this).commit();
            }
        });

    }

    @Override
    public void updateFragment() {
        super.updateFragment();
        categoryAdapter.clear();
        chooserOpened = true;
        Map<String, String> parameter = getParameter();
        feature = parameter.get("feature");
        switchFeature();
        switchFeatureItem();
        segmentChoose();
        if(!FragmentEditorSideMenu.this.isVisible())
        {
            show();
        }
    }


    private void segmentChoose()
    {
        if (chooserOpened) {
            chooserOpened = false;
            imageviewChoose.setImageResource(R.drawable.icon_arrow_up_gray);
            listviewCategory.setVisibility(View.GONE);
        } else {
            chooserOpened = true;
            imageviewChoose.setImageResource(R.drawable.icon_arrow_down_gray);
            listviewCategory.setVisibility(View.VISIBLE);
        }
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segmentChoose();
            }
        });
    }

    private void updateCategorySelected() {
        textChoose.setText(categorySelectedTitle);
    }

    private void switchFeature()
    {
        if(feature.equals("sticker"))
        {
            getStickerCategory(false);
            getStickerCategory(true);
        }
        else if(feature.equals("filter"))
        {
            getFilterCategory(false);
            getFilterCategory(true);
        }
        else
        {
            getFrameCategory(false);
            getFrameCategory(true);
        }
    }

    private void switchFeatureItem()
    {
        itemAdapter.clear();
        itemAdapter.notifyDataSetChanged();
        if(feature.equals("sticker"))
        {
            getItemSticker(categorySelectedID);
        }
        else if(feature.equals("filter"))
        {
            getItemFilter(categorySelectedID);
        }
        else
        {
            getItemFrame(categorySelectedID);
        }
    }

    private void itemSelected(int position)
    {
        if(feature.equals("sticker"))
        {
            Bitmap bmpSticker = HelperGlobal.getAssetBitmap(itemFile.get(position), activity);
            editorBehavior.setBitmapFeature(bmpSticker);
            editorBehavior.processFeature();
            editorBehavior.updateFeature();
            hide();
        }
        else if(feature.equals("filter"))
        {
            Bitmap bmpFilter = HelperGlobal.getAssetBitmap(itemFile.get(position), activity);
            editorBehavior.setBitmapFeature(bmpFilter);
            editorBehavior.processFeature();
            editorBehavior.updateFeature();
            hide();
        }
        else
        {
            Bitmap bmpFrame = HelperGlobal.getAssetBitmap(itemFile.get(position), activity);
            editorBehavior.setBitmapFeature(bmpFrame);
            editorBehavior.processFeature();
            editorBehavior.updateFeature();
            hide();
        }
    }

    public void hide()
    {
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_left).hide(FragmentEditorSideMenu.this).commit();
    }

    public void show()
    {
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(FragmentEditorSideMenu.this).commit();
    }


    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_SIDE_MENU;
    }

    private void listviewCategorySelected(int position)
    {
        categorySelectedID = categoryID.get(position);
        categorySelectedTitle = categoryTitle.get(position);
        if(feature.equals("filter"))
        {
            Bitmap convBG = HelperGlobal.getAssetBitmap(categoryFile.get(position), activity);
            categoryBG = convBG;
        }

        updateCategorySelected();
        switchFeatureItem();
        segmentChoose();
    }

    private void getStickerCategory(boolean fetchAll)
    {
        HelperDB db = new HelperDB(activity);
        List<MStickerCategory> listStickerCategory = db.getAllStickerCategory();
        if(fetchAll)
        {
            for(MStickerCategory category : listStickerCategory)
            {
                categoryID.add(Integer.toString(category.getStickerCategoryID()));
                categoryTitle.add(category.getStickerCategoryName());
            }
            categoryAdapter.notifyDataSetChanged();
        }
        else
        {
            MStickerCategory stickerCategory = listStickerCategory.get(0);
            categorySelectedID = Integer.toString(stickerCategory.getStickerCategoryID());
            categorySelectedTitle = stickerCategory.getStickerCategoryName();
            updateCategorySelected();
        }
    }

    private void getFilterCategory(boolean fetchAll)
    {
        HelperDB db = new HelperDB(activity);
        List<MEffectCategory> listEffectCategory = db.getAllEffectCategory();
        if(fetchAll)
        {
            for(MEffectCategory category : listEffectCategory)
            {
                categoryID.add(Integer.toString(category.getEffectCategoryID()));
                categoryTitle.add(category.getEffectCategoryName());
                categoryFile.add(category.getEffectCategoryFile());
            }
            categoryAdapter.notifyDataSetChanged();
        }
        else
        {
            MEffectCategory effectCategory = listEffectCategory.get(0);
            categorySelectedID = Integer.toString(effectCategory.getEffectCategoryID());
            categorySelectedTitle = effectCategory.getEffectCategoryName();
            Bitmap convBG = HelperGlobal.getAssetBitmap(effectCategory.getEffectCategoryFile(), activity);
            categoryBG = convBG;
            updateCategorySelected();
        }
    }

    private void getFrameCategory(boolean fetchAll)
    {
        HelperDB db = new HelperDB(activity);
        List<MFrameCategory> listFrameCategory = db.getAllFrameCategory();
        if(fetchAll)
        {
            for(MFrameCategory category : listFrameCategory)
            {
                categoryID.add(Integer.toString(category.getFrameCategoryID()));
                categoryTitle.add(category.getFrameCategoryName());
            }
            categoryAdapter.notifyDataSetChanged();
        }
        else
        {
            MFrameCategory frameCategory = listFrameCategory.get(0);
            categorySelectedID = Integer.toString(frameCategory.getFrameCategoryID());
            categorySelectedTitle = frameCategory.getFrameCategoryName();
            updateCategorySelected();
        }
    }

    private void getItemSticker(String category)
    {
        HelperDB db = new HelperDB(activity);
        List<MSticker> listSticker = db.getStickerByCategory(Integer.parseInt(category));
        for(MSticker sticker : listSticker)
        {
            itemID.add(Integer.toString(sticker.getStickerID()));
            itemFile.add(sticker.getStickerUri());
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void getItemFilter(String category)
    {
        HelperDB db = new HelperDB(activity);
        List<MEffect> listFilter = db.getEffectByCategory(Integer.parseInt(category));
        for(MEffect effect : listFilter)
        {
            itemID.add(Integer.toString(effect.getEffectID()));
            itemFile.add(effect.getEffectFile());
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void getItemFrame(String category)
    {
        HelperDB db = new HelperDB(activity);
        List<MFrame> listFrame = db.getFrameByCategory(Integer.parseInt(category));
        for(MFrame frame : listFrame)
        {
            itemID.add(Integer.toString(frame.getFrameID()));
            itemFile.add(frame.getFrameUri());
        }
        itemAdapter.notifyDataSetChanged();
    }


    public class SideMenuCategoryAdapter extends BaseAdapter {

        public  SideMenuCategoryAdapter()
        {

        }

        private void clear()
        {
            categoryID.clear();
            categoryTitle.clear();
        }

        private void remove(int i)
        {
            categoryID.remove(i);
            categoryTitle.remove(i);
        }

        @Override
        public int getCount() {
            return categoryID.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[2];
            object[0] = categoryID.get(position);
            object[1] = categoryTitle.get(position);
            return object;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_fragment_editor_side_menu_category, null);
            }
            holder.text_title = (ViewText) convertView.findViewById(R.id.item_fragment_editor_side_menu_category_title);
            holder.text_title.setRegular();
            holder.text_title.setText(categoryTitle.get(position));
            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ViewText text_title;
        }
    }

    public class SideMenuItemAdapter extends BaseAdapter {

        public  SideMenuItemAdapter()
        {

        }

        private void clear()
        {
            itemID.clear();
            itemFile.clear();
        }

        private void remove(int i)
        {
            itemID.remove(i);
            itemFile.remove(i);
        }

        @Override
        public int getCount() {
            return itemID.size();
        }

        @Override
        public Object getItem(int position) {
            Object[] object = new Object[2];
            object[0] = itemID.get(position);
            object[1] = itemFile.get(position);
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
                convertView = inflater.inflate(R.layout.item_fragment_editor_side_menu_product, null);
            }
            holder.itemThumbnail = (ViewImageCategoryItem) convertView.findViewById(R.id.item_fragment_editor_side_menu_product_thumbnail);
            if(feature.equals("filter"))
            {
                Picasso.with(activity).load(HelperGlobal.getAssetsPath("files/"+itemFile.get(position))).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.itemThumbnail.setAdjustViewBounds(true);
                        holder.itemThumbnail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        holder.itemThumbnail.setBitmapTarget(bitmap);
                        holder.itemThumbnail.setBitmapOverlay(categoryBG);
                        holder.itemThumbnail.applyOverlay();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
            else {
                Picasso.with(activity).load(HelperGlobal.getAssetsPath("files/"+itemFile.get(position))).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap resized = HelperImage.doResizeFromBitmap(bitmap, 200,200, false);
                        holder.itemThumbnail.setAdjustViewBounds(true);
                        holder.itemThumbnail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        holder.itemThumbnail.setImageBitmap(resized);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            convertView.setTag(holder);
            return convertView;
        }

        class ViewHolder{
            ViewImageCategoryItem itemThumbnail;
        }
    }
}
