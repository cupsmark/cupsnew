package com.cupslicenew.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "fuck that", Toast.LENGTH_LONG).show();
    }



}
