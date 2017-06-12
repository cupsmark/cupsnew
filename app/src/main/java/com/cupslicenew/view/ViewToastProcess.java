package com.cupslicenew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;

/**
 * Created by ekobudiarto on 7/21/16.
 */
public class ViewToastProcess extends RelativeLayout {

    Context mContext;
    String text = "0";
    ViewText message;
    View main_view;

    public ViewToastProcess(Context context) {
        super(context);
        mContext = context;
        initLayout();
    }

    public ViewToastProcess(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayout();
    }

    public ViewToastProcess(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    private void initLayout()
    {
        main_view = inflate(mContext, R.layout.view_toast_process,this);
    }

    public void newInstance()
    {
        message = (ViewText) main_view.findViewById(R.id.view_toast_process_message);
        message.setBold();
    }

    public void setMessage(String text)
    {
        this.text = text;
        message.setText(this.text);
    }


}
