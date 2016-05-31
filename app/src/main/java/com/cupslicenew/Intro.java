package com.cupslicenew;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cupslicenew.activity.MainActivity;
import com.cupslicenew.core.helper.HelperDB;
import com.cupslicenew.core.helper.HelperData;
import com.cupslicenew.core.helper.HelperGlobal;

import io.fabric.sdk.android.Fabric;

public class Intro extends FragmentActivity {

    ImageView main_imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Fabric.with(Intro.this, new Crashlytics());

        init();
    }

    private void init()
    {
        main_imageview = (ImageView) findViewById(R.id.intro_imageview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        runTask();
    }

    private void runTask()
    {
        new AsyncTask<Void, Integer, String>()
        {
            HelperDB helperDB;
            HelperData helperData;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                helperDB = new HelperDB(Intro.this);
                helperData = new HelperData(Intro.this);

                if(helperDB.getSetCount() == 0)
                {
                    helperData.runSet(helperDB);
                    main_imageview.setImageResource(R.drawable.logo_app_setup);
                }
                else
                {
                    main_imageview.setImageResource(R.drawable.logo_app);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                helperData.run();
                SystemClock.sleep(3000);
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                helperDB.resetHistory();
                helperDB.close();
                goHome();
                super.onPostExecute(s);
            }
        }.execute();
    }

    private void goHome()
    {
        Intent i = new Intent(Intro.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
