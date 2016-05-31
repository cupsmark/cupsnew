package com.cupslicenew.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperImage;


/**
 * Created by ekobudiarto on 12/6/15.
 */
public class ViewImageView extends ImageView{

    Context mContext;
    Bitmap source, source_normal, source_hover;
    boolean isAdjust = false;
    boolean isScretchRatio = false;
    int w = 0;
    int h = 0;
    int ow = 0;
    int oh = 0;

    public ViewImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ViewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ViewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setOnHover()
    {
        if(this.isAdjust)
        {
            source_hover = HelperImage.doColorOverlay(mContext.getResources().getColor(R.color.base_color_hover), source);
            setImageBitmap(source_hover);
        }
    }

    public void setAdjust(boolean adjust)
    {
        this.isAdjust = adjust;
        if(adjust)
        {
            source_normal = HelperImage.doColorOverlay(mContext.getResources().getColor(R.color.base_color_normal), source);
            source_hover = HelperImage.doColorOverlay(mContext.getResources().getColor(R.color.base_color_hover), source);
            setImageBitmap(source_normal);
        }
    }

    public void setScretchRatio(boolean flag)
    {
        this.isScretchRatio = flag;
    }

    public void setBitmap(Bitmap src)
    {
        this.source = src;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        this.ow = oldw;
        this.oh = oldh;


        //FOR EDITOR ACTIVITY
        if(isScretchRatio)
        {
            Bitmap r = null;
            if(source.getWidth() < source.getHeight())
            {
                r = HelperImage.doResizeFromBitmap(source, h, h, true);
            }
            else{
                r = HelperImage.doResizeFromBitmap(source, w, w, true);
            }
            /*ActivityEditor.default_image = r;*/
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*setImageBitmap(ActivityEditor.default_image);*/
                }
            }, 200);
            isScretchRatio = false;
        }

    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        getAnimation().setDuration(2000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final Drawable d = this.getDrawable();

        if (d != null) {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final int height = (int) Math.ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
            Bitmap src = HelperImage.doResizeFromBitmap(source,width, height, true);
            setImageBitmap(src);
            //this.setMeasuredDimension(width, height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
