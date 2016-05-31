package com.cupslicenew.core.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import java.io.File;

import bolts.AppLinks;

/**
 * Created by ekobudiarto on 12/12/15.
 */
public class HelperFBMessenger {

    Context mContext;
    Activity activity;
    View button_send;
    String path;

    private static final String EXTRA_PROTOCOL_VERSION = "com.facebook.orca.extra.PROTOCOL_VERSION";
    private static final String EXTRA_APP_ID = "com.facebook.orca.extra.APPLICATION_ID";
    private static final int PROTOCOL_VERSION = 20150314;
    private static final String YOUR_APP_ID = "348846111886362";
    private static final String EXTRA_THREAD_TOKEN = "com.facebook.orca.extra.THREAD_TOKEN";
    private static final String EXTRA_METADATA = "com.facebook.orca.extra.METADATA";
    private static final String EXTRA_EXTERNAL_URI = "com.facebook.orca.extra.EXTERNAL_URI";
    public static final String ORCA_THREAD_CATEGORY_20150314 =
            "com.facebook.orca.category.PLATFORM_THREAD_20150314";
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    boolean messenger = false;


    public HelperFBMessenger(Context context)
    {
        this.mContext = context;
        FacebookSdk.sdkInitialize(mContext);
    }

    public void setMessenger(boolean isMessenger)
    {
        this.messenger = isMessenger;
    }

    public void setPath(String oriBitmap)
    {
        this.path = oriBitmap;
    }

    public void setActivity(Activity act)
    {
        this.activity = act;
    }

    public void onSend()
    {
        boolean isMessengerInstalled = HelperGlobal.isAppInstalled(mContext, "com.facebook.orca");
        if (isMessengerInstalled)
        {
            onReplyMessenger();
        }
        else
        {
            HelperGlobal.openApplicationInPlayStore(mContext,"com.facebook.orca");
        }
    }


    private void onReplyMessenger()
    {
        Uri urifile = Uri.fromFile(new File(path));
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(urifile, "image/*")
                        .setMetaData("{ \"image\" : \"trees\" }")
                        .build();
        if (messenger)
        {
            HelperDB helper = new HelperDB(activity);
            helper.resetHistory();
            helper.close();
            MessengerUtils.finishShareToMessenger(activity, shareToMessengerParams);
        }
        else
        {
            MessengerUtils.shareToMessenger(
                    activity,
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }
    }
}
