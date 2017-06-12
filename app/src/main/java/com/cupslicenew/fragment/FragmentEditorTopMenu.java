package com.cupslicenew.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ekobudiarto on 7/24/16.
 */
public class FragmentEditorTopMenu extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    HelperGlobal.EditorBehavior behaviorEditor;
    public static final String TAG_FRAGMENT_EDITOR_TOP_MENU = "tag:fragment-editor-top-menu";
    Bitmap source;
    ImageButton imageButtonSave, imageButtonHome, imageButtonUndo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_editor_top_menu, container, false);
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
        imageButtonUndo = (ImageButton) activity.findViewById(R.id.fragment_editor_top_menu_undo);
        imageButtonHome = (ImageButton) activity.findViewById(R.id.fragment_editor_top_menu_home);
        imageButtonSave = (ImageButton) activity.findViewById(R.id.fragment_editor_top_menu_save);

        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behaviorEditor.onSave();
            }
        });
    }


    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_EDITOR_TOP_MENU;
    }
}
