package com.cupslicenew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.cupslicenew.R;


/**
 * Created by ekobudiarto on 12/11/15.
 */
public class ViewDialogConfirm extends Dialog {

    Context mContext;
    ViewText text_message;
    ViewButton button_ok, button_cancel;

    public ViewDialogConfirm(Context context) {
        super(context);
        this.mContext = context;
    }

    public ViewDialogConfirm(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    protected ViewDialogConfirm(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        text_message = (ViewText) findViewById(R.id.dialog_exit_message);
        button_ok = (ViewButton) findViewById(R.id.dialog_exit_button_yes);
        button_cancel = (ViewButton) findViewById(R.id.dialog_exit_button_cancel);
    }

    public void setTextMessage(String message)
    {
        text_message.setText(message);
    }

    public ViewButton getButtonOK()
    {
        return this.button_ok;
    }

    public ViewButton getButtonCancel()
    {
        return this.button_cancel;
    }
}
