package com.cupslicenew.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.fragment.FragmentCafeSlider;
import com.cupslicenew.fragment.FragmentHome;

import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragmentHome();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addFragmentHome()
    {
        FragmentHome home = new FragmentHome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right);
        if(!home.isAdded())
        {
            ft.add(R.id.main_container_fragment, home);
        }
        ft.commit();
    }

    private void addFragmentCafeSlider()
    {
        FragmentCafeSlider slider = new FragmentCafeSlider();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        if(!slider.isAdded())
        {
            ft.add(R.id.main_container_fragment, slider);
        }
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
        super.onBackPressed();
        boolean isNoFragment = removeFragment();
        if(isNoFragment)
        {
            HelperGlobal.ExitApplication(MainActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
