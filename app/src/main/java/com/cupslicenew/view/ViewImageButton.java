package com.cupslicenew.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperImage;

/**
 * Created by ekobudiarto on 12/4/15.
 */
public class ViewImageButton extends ImageButton {

    Context mContext;
    Bitmap source, source_normal, source_hover;
    boolean isAdjust = false;

    public ViewImageButton(Context context) {
        super(context);
        this.mContext = context;
    }

    public ViewImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ViewImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        source = bm;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        source = BitmapFactory.decodeResource(mContext.getResources(), resId);
    }

    public void setAdjust(boolean adjust)
    {
        this.isAdjust = adjust;
        if(adjust)
        {
            source_normal = HelperImage.doColorOverlay(mContext.getResources().getColor(R.color.base_color_normal), source);
            source_hover = HelperImage.doColorOverlay((mContext.getResources().getColor(R.color.base_color_hover)), source);
            setImageBitmap(source_normal);
        }
    }

    public void setOnHover()
    {
        if(this.isAdjust)
        {
            source_hover = HelperImage.doColorOverlay((mContext.getResources().getColor(R.color.base_color_hover)), source);
            setImageBitmap(source_hover);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isAdjust)
                {
                    setImageBitmap(source_hover);
                }
                break;

            case MotionEvent.ACTION_UP:
                setImageBitmap(source_normal);
                break;
        }
        return super.onTouchEvent(event);
    }
}
