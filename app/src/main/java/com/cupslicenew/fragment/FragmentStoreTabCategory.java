package com.cupslicenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;

/**
 * Created by ekobudiarto on 4/9/16.
 */
public class FragmentStoreTabCategory extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_STORE_TAB_CATEGORY = "tag:fragment-store-tab-category";

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_store_tab_category, container, false);
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
            Toast.makeText(activity, "CATEGORY", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_STORE_TAB_CATEGORY;
    }
}
