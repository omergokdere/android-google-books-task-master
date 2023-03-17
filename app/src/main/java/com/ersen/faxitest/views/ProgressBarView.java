package com.ersen.faxitest.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.ersen.faxitest.R;

public class ProgressBarView extends RecyclerView.ViewHolder {

    private ProgressBar mProgressBar;
    public ProgressBarView(View itemView) {
        super(itemView);
        mProgressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
}
