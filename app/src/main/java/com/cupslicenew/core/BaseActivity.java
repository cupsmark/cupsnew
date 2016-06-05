package com.cupslicenew.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        fragmentSrc.setParameter(parameter);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        ft.add(R.id.main_container_fragment, fragmentSrc);
        ft.commit();
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
                    List<Fragment> subFragment = fragment.getChildFragmentManager().getFragments();
                    if(subFragment != null)
                    {
                        for(int x = subFragment.size() - 1;x >= 0;x--)
                        {
                            BaseFragment fragmentChild = (BaseFragment) subFragment.get(x);
                            if(fragmentChild != null)
                            {
                                fragment.getChildFragmentManager().beginTransaction().remove(fragmentChild).commit();
                                return false;
                            }
                        }
                    }
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
