package com.cupslicenew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.cupslicenew.R;


/**
 * Created by ekobudiarto on 7/11/15.
 */
public class ViewLoadingDialog extends Dialog {

    Context context;
    ViewLoading loading;

    public ViewLoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ViewLoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected ViewLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        loading = (ViewLoading)findViewById(R.id.view_loading);
        loading.setImageResource(R.drawable.icon_loading_white);
        loading.show();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void dismiss() {
        //loading.dismiss();
        super.dismiss();
    }
}
