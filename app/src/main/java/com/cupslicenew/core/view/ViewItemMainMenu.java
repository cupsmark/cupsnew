package com.cupslicenew.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cupslicenew.R;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.view.ViewText;

/**
 * Created by ekobudiarto on 7/12/16.
 */
public class ViewItemMainMenu extends RelativeLayout {

    Context mContext;
    View main_view;
    int itemThumb, colorHover = 0;
    String itemTitle;
    boolean coloringHover = false;
    ViewText viewTitle;
    ViewSquareImage viewThumb;
    Bitmap itemThumbSelected, itemThumbUnselected;

    public ViewItemMainMenu(Context context) {
        super(context);
        mContext = context;
        initLayout();
    }

    public ViewItemMainMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayout();
    }

    public ViewItemMainMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    private void initLayout()
    {
        main_view = inflate(mContext, R.layout.view_item_main_menu, this);
        viewThumb = (ViewSquareImage) main_view.findViewById(R.id.view_item_main_menu_thumb);
        viewTitle = (ViewText) main_view.findViewById(R.id.view_item_main_menu_title);
        viewTitle.setRegular();
    }

    public void setColoringHover(boolean coloringHover)
    {
        this.coloringHover = coloringHover;
    }

    public void setColor(int color)
    {
        this.colorHover = color;
    }

    public void setItemThumb(int resID)
    {
        this.itemThumb = resID;
        viewThumb.setImageResource(itemThumb);
        Bitmap hovered = BitmapFactory.decodeResource(mContext.getResources(), itemThumb);
        itemThumbUnselected = hovered;
        itemThumbSelected = HelperImage.doColorOverlay(colorHover, hovered);
    }

    public void setItemTitle(String title)
    {
        this.itemTitle = title;
        viewTitle.setText(itemTitle);
    }

    public void selected()
    {
        viewThumb.setImageBitmap(itemThumbSelected);
        viewTitle.setTextColor(colorHover);
    }

    public void unselected()
    {
        viewThumb.setImageResource(itemThumb);
        viewTitle.setTextColor(HelperGlobal.getColor(mContext, R.color.base_gray));
    }


}
