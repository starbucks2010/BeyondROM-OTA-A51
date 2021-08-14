package com.mesalabs.ten.update.activity.aboutpage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.mesalabs.cerberus.base.BaseToolbarActivity;
import com.mesalabs.cerberus.ui.callback.OnSingleClickListener;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;

/*
 * 십 Update
 *
 * Coded by BlackMesa123 @2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class OpenSourceLicenseActivity extends BaseToolbarActivity {
    private final String TAG = getClass().getSimpleName();
    public WebView mWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        toolBar.setTitleText(getString(R.string.mesa_open_source_licence));
        toolBar.setHomeAsUpButton(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            String htmlContent = readAssetFileAsString("text/NOTICE.html");
            mWebView.setBackgroundColor(0xff252525);
            StringBuilder data = new StringBuilder();
            data.append("<html><head><style type=\"text/css\">body{color: #ffffff; background-color: #252525;}</style></head><body>");
            data.append(htmlContent);
            data.append("</body></html>");
            mWebView.loadDataWithBaseURL(null, data.toString(), "text/html", "utf-8", null);
        } else {
            mWebView.loadUrl("file:///android_asset/text/NOTICE.html");
        }

        WebSettings wvSettings = mWebView.getSettings();
        wvSettings.setDefaultTextEncodingName("UTF-8");
        wvSettings.setSupportZoom(true);
        wvSettings.setBuiltInZoomControls(true);
        wvSettings.setDisplayZoomControls(false);
        wvSettings.setLoadWithOverviewMode(true);
        wvSettings.setUseWideViewPort(true);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView = null;
        }

        super.onDestroy();
    }

    public final String readAssetFileAsString(String file) {
        try {
            InputStream is = getAssets().open(file);
            byte[] content = new byte[is.available()];
            is.read(content);
            is.close();
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LogUtils.e(TAG, e.toString());
            return "";
        }
    }
}
