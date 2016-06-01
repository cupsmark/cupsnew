package com.cupslicenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;


/**
 * Created by ekobudiarto on 4/8/16.
 */
public class FragmentStoreDetail extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    public static final String TAG_FRAGMENT_STORE_DETAIL = "tag:fragment-store-detail";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_store_detail, container, false);
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
            init();
        }
    }

    private void init()
    {

    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_STORE_DETAIL;
    }

}
