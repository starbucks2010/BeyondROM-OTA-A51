package com.mesalabs.on.update.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mesalabs.on.update.R;

/*
 * On Update
 *
 * Coded by BlackMesa @2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class DownloadProgressView extends FrameLayout {
    private Context mContext;

    private TextView mTitleTextView;
    private ProgressBar mProgressBar;
    private TextView mDescTextView;

    private int mProgress = 0;

    public DownloadProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        removeAllViews();

        LayoutInflater.from(mContext).inflate(R.layout.mesa_ota_downloadprogressview_layout, this);

        mTitleTextView = findViewById(R.id.mesa_progresstitle_ota_downloadprogressview);
        mProgressBar = findViewById(R.id.mesa_progressbar_ota_downloadprogressview);
        mDescTextView = findViewById(R.id.mesa_progressbody_ota_downloadprogressview);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        if (mProgress < 100) {
            mTitleTextView.setText(R.string.mesa_downloading);
        }
        mProgressBar.setProgress(mProgress, true);
    }

    public void setTimeLeftText(String string) {
        mDescTextView.setText(getResources().getString(R.string.mesa_time_left_desc, string));
    }
}
