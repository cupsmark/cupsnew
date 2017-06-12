package com.cupslicenew.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cupslicenew.core.helper.HelperImage;

/**
 * Created by ekobudiarto on 7/14/16.
 */
public class ViewImageCategoryItem extends ImageView {

    Context mContext;
    Bitmap source, overlay;

    public ViewImageCategoryItem(Context context) {
        super(context);
        mContext = context;
    }

    public ViewImageCategoryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ViewImageCategoryItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setBitmapTarget(Bitmap source)
    {
        this.source = source;
    }

    public void setBitmapOverlay(Bitmap overlay)
    {
        this.overlay = overlay;
    }

    public void applyOverlay()
    {
        Bitmap result = HelperImage.doFilter(overlay, source);
        setImageBitmap(result);
    }
}
