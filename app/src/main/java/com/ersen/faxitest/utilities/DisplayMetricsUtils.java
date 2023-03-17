package com.ersen.faxitest.utilities;

import android.util.DisplayMetrics;

import com.ersen.faxitest.application.Moblib;

public class DisplayMetricsUtils {

    public static int convertDpToPixels(int dp){
        DisplayMetrics displayMetrics = Moblib.getInstance().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / 160.0F));
    }

    public static int convertPixelsToDp(float px){
        DisplayMetrics displayMetrics = Moblib.getInstance().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.densityDpi / 160.F));
    }
}
