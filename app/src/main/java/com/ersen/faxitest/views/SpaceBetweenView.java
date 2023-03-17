package com.ersen.faxitest.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ersen.faxitest.utilities.DisplayMetricsUtils;

public class SpaceBetweenView extends RecyclerView.ItemDecoration {

    private int mSpacingInDp;

    public SpaceBetweenView(int spacingInDp){
        this.mSpacingInDp = DisplayMetricsUtils.convertDpToPixels(spacingInDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int currentPosition = parent.getChildLayoutPosition(view);
        int lastPosition = state.getItemCount()-1;
        if(currentPosition == lastPosition){
            outRect.bottom = 0;
        }else{
            outRect.bottom = mSpacingInDp;
        }
    }
}
