package com.cupslicenew.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.fragment.FragmentCafeSlider;
import com.cupslicenew.fragment.FragmentHome;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addFragmentHome();
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
}
