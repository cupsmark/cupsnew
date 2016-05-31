package com.cupslicenew.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;

import java.util.List;
import java.util.Map;

public class BaseActivity extends FragmentActivity implements HelperGlobal.FragmentInterface {

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onNavigate(BaseFragment fragmentSrc, Map<String, String> parameter) {

    }

    private boolean removeFragment()
    {
        List<Fragment> lists = getSupportFragmentManager().getFragments();
        if(lists != null)
        {
            for(int i = lists.size() - 1;i > 0;i--)
            {
                BaseFragment fragment = (BaseFragment) lists.get(i);
                if(fragment != null)
                {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).remove(fragment).commit();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        boolean isNoFragment = removeFragment();
        if(isNoFragment)
        {
            HelperGlobal.ExitApplication(BaseActivity.this);
        }
    }
}
