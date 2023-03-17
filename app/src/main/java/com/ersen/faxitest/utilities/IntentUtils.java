package com.ersen.faxitest.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ersen.faxitest.R;
import com.ersen.faxitest.application.Moblib;

/** This utility class is used to start other applications outside of Moozeo such as internet browser, email client and google maps etc. */
public class IntentUtils {

    public static void startInternetBrowser(@NonNull Context context,@NonNull String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(browserIntent, Moblib.getInstance().getResources().getString(R.string.whichBrowser)));
    }

}
