package com.mesalabs.on.update.activity.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.InputStream;
import java.net.URL;

import androidx.core.graphics.ColorUtils;

import com.mesalabs.cerberus.base.BaseAppBarActivity;
import com.mesalabs.cerberus.ui.utils.ActionBarUtils;
import com.mesalabs.cerberus.utils.StateUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.mesalabs.on.update.utils.LogUtils;
import com.samsung.android.ui.appbar.SeslAppBarLayout;

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

public class ChangelogActivity extends BaseAppBarActivity {
    private boolean isBgColorDark;
    private ImageView mImageView;
    private WebView mWebView;
    private WebSettings mWVSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init
        if (!StateUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.mesa_no_network_connection, Toast.LENGTH_LONG).show();
            finish();
        }

        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.sesl_abl_height_proportion, outValue, true);

        if (outValue.getFloat() == 0.0f)
            setFullscreen();

        // init UX
        setBaseContentView(R.layout.mesa_ota_activity_changelogactivity_layout);

        appBar = new ActionBarUtils(this);
        appBar.initAppBar(true);
        appBar.getAppBarLayout().addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onOffsetChanged(SeslAppBarLayout layout, int verticalOffset) {
                super.onOffsetChanged(layout, verticalOffset);
                float range = (float) -layout.getTotalScrollRange();
                mImageView.setImageAlpha((int) (255 * (1.0f - (float) verticalOffset / range)));
            }

            @Override
            public void onStateChanged(SeslAppBarLayout appBarLayout, State state) {
                setLightStatusBar(state == State.COLLAPSED ? !Utils.isNightMode(mContext) : !isBgColorDark);
            }
        });
        appBar.setHomeAsUpButton(v -> onBackPressed());
        appBar.setTitleText(getString(R.string.mesa_whats_new));

        new LoadChangelogTask().execute(PreferencesUtils.ROM.getChangelogHeaderImgUrl());
    }

    private void setLightStatusBar(boolean enable) {
        View decorView = getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();

        if (enable) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        decorView.setSystemUiVisibility(flags);
    }

    private void setFullscreen() {
        if (!Utils.isInMultiWindowMode(this)) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            Utils.genericInvokeMethod(params, "semAddExtensionFlags", 1 /* WindowManager.LayoutParams.SEM_EXTENSION_FLAG_RESIZE_FULLSCREEN_WINDOW_ON_SOFT_INPUT */);
            getWindow().setAttributes(params);
        }
    }


    private abstract static class AppBarStateChangeListener implements SeslAppBarLayout.OnOffsetChangedListener {
        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public void onOffsetChanged(SeslAppBarLayout layout, int verticalOffset) {
            if (verticalOffset == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(layout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(verticalOffset) >= layout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(layout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(layout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(SeslAppBarLayout layout, State state);
    }

    private class LoadChangelogTask extends AsyncTask<String, Void, Bitmap> {
        private Dialog mProgressCircle;

        public LoadChangelogTask() {
            mImageView = findViewById(R.id.mesa_appbarbg_changelogactivity);
            mWebView = findViewById(R.id.mesa_webview_changelogactivity);
            mWVSettings = mWebView.getSettings();
            mWebView.loadUrl(PreferencesUtils.ROM.getChangelogUrl());
            mWVSettings.setSupportZoom(false);
            mWVSettings.setBuiltInZoomControls(false);
            mWVSettings.setDisplayZoomControls(false);
            mWVSettings.setLoadWithOverviewMode(true);
            mWVSettings.setUseWideViewPort(true);
        }

        @Override
        protected void onPreExecute() {
            mProgressCircle = new Dialog(mContext, Utils.isNightMode(mContext) ? R.style.mesa_ProgressCircleDialogStyle : R.style.mesa_ProgressCircleDialogStyle_Light);
            mProgressCircle.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressCircle.getWindow().setGravity(Gravity.CENTER);
            mProgressCircle.setCancelable(false);
            mProgressCircle.setCanceledOnTouchOutside(false);
            mProgressCircle.setContentView(LayoutInflater.from(mContext).inflate(R.layout.mesa_progress_circle_dialog_layout, null));
            mProgressCircle.show();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String headerUrl = urls[0];
            Bitmap mIcon = null;

            try {
                InputStream in = new URL(headerUrl).openStream();
                mIcon = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                LogUtils.e("Exception", e.getMessage());
            }

            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                appBar.setTitleText("");
                mImageView.setImageBitmap(result);
                isBgColorDark = isColorDark(getDominantColor(result));
                setLightStatusBar(!isBgColorDark);
            } else {
                LogUtils.e("DownloadHeaderImageTask", "result is null!!!");
            }

            mProgressCircle.cancel();
        }

        private int getDominantColor(Bitmap bitmap) {
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
            final int color = newBitmap.getPixel(0, 0);
            newBitmap.recycle();
            return color;
        }

        private boolean isColorDark(int color) {
            return ColorUtils.calculateLuminance(color) < 0.5;
        }
    }
}
