package com.cupslicenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.view.ViewItemMainMenu;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 7/12/16.
 */
public class FragmentEditorMainMenu extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    HelperGlobal.EditorBehavior behaviorEditor;
    public static final String TAG_FRAGMENT_MAIN_MENU = "tag:fragment-main-menu";

    LinearLayout linearItem;
    FragmentEditorSideMenu editorSideMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_editor_main_menu, container, false);
        return main_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null)
        {
            this.activity = (BaseActivity) activity;
            fragmentInterface = (HelperGlobal.FragmentInterface) this.activity;
            behaviorEditor = (HelperGlobal.EditorBehavior) this.activity;
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
        editorSideMenu = new FragmentEditorSideMenu();
        linearItem = (LinearLayout) activity.findViewById(R.id.fragment_main_menu_linear_item);
        setMenu();
    }

    private void setMenu()
    {
        final ViewItemMainMenu itemSticker = new ViewItemMainMenu(activity);
        itemSticker.setColoringHover(true);
        itemSticker.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemSticker.setItemThumb(R.drawable.icon_menu_sticker);
        itemSticker.setItemTitle("Sticker");

        final ViewItemMainMenu itemFilter = new ViewItemMainMenu(activity);
        itemFilter.setColoringHover(true);
        itemFilter.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemFilter.setItemThumb(R.drawable.icon_menu_effect);
        itemFilter.setItemTitle("Filter");

        final ViewItemMainMenu itemFrame = new ViewItemMainMenu(activity);
        itemFrame.setColoringHover(true);
        itemFrame.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemFrame.setItemThumb(R.drawable.icon_menu_frame);
        itemFrame.setItemTitle("Frame");

        final ViewItemMainMenu itemAdjust = new ViewItemMainMenu(activity);
        itemAdjust.setColoringHover(true);
        itemAdjust.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemAdjust.setItemThumb(R.drawable.icon_menu_adjust);
        itemAdjust.setItemTitle("Adjust");

        final ViewItemMainMenu itemCrop = new ViewItemMainMenu(activity);
        itemCrop.setColoringHover(true);
        itemCrop.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemCrop.setItemThumb(R.drawable.icon_menu_crop);
        itemCrop.setItemTitle("Crop");

        final ViewItemMainMenu itemRotate = new ViewItemMainMenu(activity);
        itemRotate.setColoringHover(true);
        itemRotate.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemRotate.setItemThumb(R.drawable.icon_menu_rotate);
        itemRotate.setItemTitle("Rotate");


        linearItem.addView(itemSticker);
        linearItem.addView(itemFilter);
        linearItem.addView(itemFrame);
        linearItem.addView(itemAdjust);
        linearItem.addView(itemCrop);
        linearItem.addView(itemRotate);

        itemSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSticker.selected();
                itemFilter.unselected();
                itemFrame.unselected();
                itemAdjust.unselected();
                itemCrop.unselected();
                itemRotate.unselected();
                Map<String, String> param = new HashMap<String, String>();
                param.put("feature", "sticker");
                behaviorEditor.setActiveFeature("sticker");
                /*editorSideMenu.setParameter(param);
                editorSideMenu.setFragmentEditor(editor);
                editor.setFragmentSideMenu(editorSideMenu);*/
            }
        });
        itemFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSticker.unselected();
                itemFilter.selected();
                itemFrame.unselected();
                itemAdjust.unselected();
                itemCrop.unselected();
                itemRotate.unselected();
                behaviorEditor.setActiveFeature("filter");
                /*editorSideMenu.setParameter(param);
                editorSideMenu.setFragmentEditor(editor);
                editor.setFragmentSideMenu(editorSideMenu);*/
            }
        });
        itemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSticker.unselected();
                itemFilter.unselected();
                itemFrame.selected();
                itemAdjust.unselected();
                itemCrop.unselected();
                itemRotate.unselected();
                behaviorEditor.setActiveFeature("frame");
                /*editorSideMenu.setParameter(param);
                editorSideMenu.setFragmentEditor(editor);
                editor.setFragmentSideMenu(editorSideMenu);*/
            }
        });
        itemAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSticker.unselected();
                itemFilter.unselected();
                itemFrame.unselected();
                itemAdjust.selected();
                itemCrop.unselected();
                itemRotate.unselected();
                behaviorEditor.setActiveFeature("adjust");
            }
        });
        itemCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSticker.unselected();
                itemFilter.unselected();
                itemFrame.unselected();
                itemAdjust.unselected();
                itemCrop.selected();
                itemRotate.unselected();
                behaviorEditor.setActiveFeature("crop");
            }
        });
        itemRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSticker.unselected();
                itemFilter.unselected();
                itemFrame.unselected();
                itemAdjust.unselected();
                itemCrop.unselected();
                itemRotate.selected();
                behaviorEditor.setActiveFeature("rotate");
            }
        });

    }


    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_MAIN_MENU;
    }

    public void show()
    {
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slideup_menu, R.anim.slidedown_menu).
                show(FragmentEditorMainMenu.this).commit();
    }

    public void hide()
    {
        getChildFragmentManager().beginTransaction().
                hide(FragmentEditorMainMenu.this).commit();
    }

}
