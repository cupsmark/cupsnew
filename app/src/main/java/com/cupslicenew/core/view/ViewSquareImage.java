package com.cupslicenew.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ekobudiarto on 7/12/16.
 */
public class ViewSquareImage extends ImageView {

    public ViewSquareImage(Context context) {
        super(context);
    }

    public ViewSquareImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewSquareImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);

    }
}