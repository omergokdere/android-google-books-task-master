package com.ersen.faxitest.utilities;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.ersen.faxitest.views.PlaceholderView;


public class VisibilityManager {
    private View[] mMainContent; //butun gösterilen main contenti tutar
    private PlaceholderView mPlaceholderView;

    public VisibilityManager(@NonNull PlaceholderView placeholderView, View... mainContent) {
        this.mPlaceholderView = placeholderView;
        this.mMainContent = mainContent;
    }

    public void showMainContent() {
        setMainContentVisibility(View.VISIBLE);
    }

    public void showLoading(@StringRes String message){
        setMainContentVisibility(View.GONE);
        mPlaceholderView.showLoading(message);
    }

    public void showFailure(@StringRes String message){
        setMainContentVisibility(View.GONE);
        mPlaceholderView.showFailure(message);
    }

    private void setMainContentVisibility(int visibility) {
        if (mMainContent != null) {
            for (View view : mMainContent) {
                if (view != null) {
                    view.setVisibility(visibility);
                }
            }
        }
        if (visibility == View.VISIBLE) { // Eğer main content görünecekse placeholderı gizleyip goruntusunu siler
            mPlaceholderView.setVisibility(View.GONE);
            mPlaceholderView.removeAllViews();
        } else {
            mPlaceholderView.setVisibility(View.VISIBLE); // Eğer main content görünmeyecekse placeholderı gosterir
        }
    }
}
