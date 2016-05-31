package com.cupslicenew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.cupslicenew.R;

/**
 * Created by ekobudiarto on 12/12/15.
 */
public class ViewDialogFBMessenger extends Dialog {

    Context mContext;
    ViewButton button_cancel;
    ImageView main_imageview;
    View button_send;

    public ViewDialogFBMessenger(Context context) {
        super(context);
        this.mContext = context;
    }

    public ViewDialogFBMessenger(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    protected ViewDialogFBMessenger(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_fbmessenger);

        button_cancel = (ViewButton) findViewById(R.id.dialog_fbmessenger_button_cancel);
        main_imageview = (ImageView) findViewById(R.id.dialog_fbmessenger_imageview);
        button_send = findViewById(R.id.messenger_send_button);
        button_cancel.setSemiBold();
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setImage(Bitmap src)
    {
        main_imageview.setImageBitmap(src);
    }

    public View getButtonSend()
    {
        return this.button_send;
    }

}
