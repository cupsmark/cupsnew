package com.cupslicenew.core.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ekobudiarto on 6/16/16.
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public RecyclerViewItemDecoration(int space) {
        this.mSpace = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) != 0)
        {
            outRect.top = mSpace;
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
        }
    }
}
