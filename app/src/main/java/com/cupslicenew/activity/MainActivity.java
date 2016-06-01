package com.cupslicenew.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.fragment.FragmentCafeSlider;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "fuck that", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addFragmentCafeSlider();
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
