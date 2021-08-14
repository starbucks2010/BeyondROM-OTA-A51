package com.mesalabs.cerberus.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.mesalabs.ten.update.TenUpdateApp;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.activity.aboutpage.CreditsActivity;
import com.mesalabs.ten.update.activity.aboutpage.OpenSourceLicenseActivity;
import com.mesalabs.cerberus.ui.callback.OnSingleClickListener;
import com.mesalabs.cerberus.update.utils.AppUpdateUtils;
import com.mesalabs.cerberus.utils.StateUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * ULTRA-MEGA-PRIVATE SOURCE CODE. SHARING TO DEVKINGS TEAM
 * EXTERNALS IS PROHIBITED AND WILL BE PUNISHED WITH ANAL ABUSE.
 */

public class BaseAboutActivity extends BaseAppBarActivity {
    protected final String TAG = getClass().getSimpleName();
    private static final String KEY_UPDATE_STATE = "update_state";

    protected LinearLayout mBaseLayout;
    protected LinearLayout mAppInfoView;
    protected TextView mAvailableText;
    protected int mCheckingStatus = AppUpdateUtils.STATE_CHECKING;
    protected View mEmptyBottom;
    protected View mEmptyMiddle;
    protected View mEmptyTop;
    protected LinearLayout mLowerLayout;
    protected AppCompatButton mCredits;
    protected AppCompatButton mOpenSource;
    protected AppCompatButton mCreditsInLower;
    protected AppCompatButton mOpenSourceInLower;
    protected ProgressBar mProgressBar;
    protected AppCompatButton mUpdateButton;
    protected LinearLayout mUpperLayout;
    protected TextView mAppNameText;
    protected TextView mVersionText;
    protected LinearLayout mWebLinkView;

    protected AppUpdateUtils mAppUpdate;
    private AppUpdateUtils.StubListener mStubListener = new AppUpdateUtils.StubListener() {
        public void onUpdateCheckCompleted(int status) {
            checkForUpdatesCompleted(status);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mesa_baselayout_baseaboutactivity);

        appBar.setHomeAsUpButton(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                onBackPressed();
            }
        });
        appBar.addOverflowButton(true, R.drawable.sesl_ic_ab_app_info,
                R.string.mesa_app_info,
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", TenUpdateApp.getAppPackageName(), null));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

        mAppUpdate = new AppUpdateUtils(this, TenUpdateApp.getAppPackageName(), mStubListener);

        mBaseLayout = findViewById(R.id.mesa_aboutlayout_baseaboutactivity);
        mAppInfoView = findViewById(R.id.mesa_appinfolayout_baseaboutactivity);
        mUpperLayout = findViewById(R.id.mesa_upperlayout_appinfolayout_baseaboutactivity);
        mLowerLayout = findViewById(R.id.mesa_lowerlayout_baseaboutactivity);
        mWebLinkView = findViewById(R.id.mesa_weblinklayout_baseaboutactivity);
        mEmptyTop = findViewById(R.id.mesa_emptyviewtop_appinfolayout_baseaboutactivity);
        mEmptyMiddle = findViewById(R.id.mesa_emptyviewmiddle_appinfolayout_baseaboutactivity);
        mEmptyBottom = findViewById(R.id.mesa_emptyviewbottom_lowerlayout_baseaboutactivity);
        mProgressBar = findViewById(R.id.mesa_progress_appinfolayout_baseaboutactivity);
        mAvailableText = findViewById(R.id.mesa_versionnotice_appinfolayout_baseaboutactivity);
        mUpdateButton = findViewById(R.id.mesa_updatebutton_appinfolayout_baseaboutactivity);
        mUpdateButton.setCompoundDrawables(null, null, null, null);
        mCredits = findViewById(R.id.mesa_creditsbtn_baseaboutactivity);
        mOpenSource = findViewById(R.id.mesa_opensourcebtn_baseaboutactivity);
        mCreditsInLower = findViewById(R.id.mesa_creditsbtn_lowerlayout_baseaboutactivity);
        mOpenSourceInLower = findViewById(R.id.mesa_opensourcebtn_lowerlayout_baseaboutactivity);

        setOpenSourceButtonWidth(mCredits);
        setOpenSourceButtonWidth(mOpenSource);
        setOpenSourceButtonWidth(mCreditsInLower);
        setOpenSourceButtonWidth(mOpenSourceInLower);

        setClickListener();

        mAppNameText = findViewById(R.id.mesa_appname_appinfolayout_baseaboutactivity);
        mAppNameText.setText(getAppName());

        mVersionText = findViewById(R.id.mesa_versiontextview_appinfolayout_baseaboutactivity);
        mVersionText.setText(getString(R.string.mesa_version) + " " + TenUpdateApp.getAppVersionString());

        if (getIsAppUpdateable()) {
            if (savedInstanceState != null) {
                mCheckingStatus = savedInstanceState.getInt(KEY_UPDATE_STATE, AppUpdateUtils.STATE_CHECKING);
            }
            checkForUpdatesCompleted(mCheckingStatus);
            if (mCheckingStatus == AppUpdateUtils.STATE_CHECKING) {
                checkForUpdatesNotCompleted();
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            mAvailableText.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);
        }

        setOrientationLayout();
        setTextSize();
    }

    @Override
    protected boolean getIsAppBarExpanded() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setOpenSourceButtonWidth(mCredits);
        setOpenSourceButtonWidth(mOpenSource);
        setOpenSourceButtonWidth(mCreditsInLower);
        setOpenSourceButtonWidth(mOpenSourceInLower);
        setOrientationLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(KEY_UPDATE_STATE, mCheckingStatus);
    }

    private void checkForUpdatesCompleted(int status) {
        mCheckingStatus = status;
        setCheckingStatus(true);

        if (status == AppUpdateUtils.STATE_NEW_VERSION_AVAILABLE) {
            mAvailableText.setText(getString(R.string.mesa_new_version_available));
            mUpdateButton.setText(getString(R.string.mesa_update));
            mUpdateButton.setVisibility(View.VISIBLE);
        } else if (status == AppUpdateUtils.STATE_ERROR) {
            mAvailableText.setText(getString(Utils.isTabletDevice(mContext) ? R.string.mesa_updates_error_tablet : R.string.mesa_updates_error_phone));
            mUpdateButton.setText(getString(R.string.mesa_retry));
            mUpdateButton.setVisibility(View.VISIBLE);
        } else if (status != AppUpdateUtils.STATE_CHECKING) {
            mAvailableText.setText(getString(R.string.mesa_latest_version));
            mUpdateButton.setVisibility(View.GONE);
        } else {
            mAvailableText.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);
        }

        setOrientationLayout();
    }

    protected void checkForUpdatesNotCompleted() {
        setCheckingStatus(false);
        mAppUpdate.checkUpdates();
    }

    protected String getAppName() {
        return TAG;
    }

    protected boolean getIsAppUpdateable() {
        return false;
    }

    protected void setCheckingStatus(boolean status) {
        mProgressBar.setVisibility(status ? View.GONE : View.VISIBLE);
        mAvailableText.setVisibility(status ? View.VISIBLE : View.GONE);
        mUpdateButton.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    protected void setClickListener() {
        mUpdateButton.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                if (mCheckingStatus != AppUpdateUtils.STATE_ERROR) {
                    mAppUpdate.installUpdate();
                } else if (StateUtils.isNetworkConnected(mContext)) {
                    checkForUpdatesNotCompleted();
                } else {
                    Toast.makeText(mContext, getString(R.string.mesa_no_network_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
        mCredits.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                startActivity(new Intent(mContext, CreditsActivity.class));
            }
        });
        mOpenSource.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                startActivity(new Intent(mContext, OpenSourceLicenseActivity.class));
            }
        });
        mCreditsInLower.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                startActivity(new Intent(mContext, CreditsActivity.class));
            }
        });
        mOpenSourceInLower.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                startActivity(new Intent(mContext, OpenSourceLicenseActivity.class));
            }
        });
    }

    protected final void setOrientationLayout() {
        LinearLayout.LayoutParams appInfoViewLp = (LinearLayout.LayoutParams) mAppInfoView.getLayoutParams();
        LinearLayout.LayoutParams webLinkViewLp = (LinearLayout.LayoutParams) mWebLinkView.getLayoutParams();
        boolean isPortrait = ViewUtils.isPortrait(mContext);
        double heightPixels = (double) getResources().getDisplayMetrics().heightPixels;

        double d = isPortrait ? 0.05d : 0.036d;
        double d2 = isPortrait ? 0.086d : 0.036d;
        int i = (int) (d2 * heightPixels);

        mEmptyTop.getLayoutParams().height = i;
        mEmptyMiddle.getLayoutParams().height = i;
        mEmptyBottom.getLayoutParams().height = (int) (heightPixels * d);

        if (isPortrait) {
            mBaseLayout.setOrientation(LinearLayout.VERTICAL);
            mAppInfoView.setGravity(49);
            mLowerLayout.setVisibility(View.VISIBLE);
            mWebLinkView.setVisibility(View.GONE);
        } else {
            mBaseLayout.setOrientation(LinearLayout.HORIZONTAL);
            appInfoViewLp.weight = 5.0f;
            webLinkViewLp.weight = 5.0f;
            mAppInfoView.setGravity(17);
            mUpperLayout.setGravity(17);
            mLowerLayout.setVisibility(View.GONE);
            mWebLinkView.setVisibility(View.VISIBLE);
            mWebLinkView.setGravity(17);
        }
    }

    protected final void setTextSize() {
        ViewUtils.setLargeTextSize(mContext, mAppNameText, (float) getResources().getDimensionPixelSize(R.dimen.mesa_appname_text_baseaboutactivity));
        ViewUtils.setLargeTextSize(mContext, mUpdateButton, (float) getResources().getDimensionPixelSize(R.dimen.mesa_updatebtn_text_baseaboutactivity));

        float dimensionPixelSize = (float) getResources().getDimensionPixelSize(R.dimen.mesa_version_text_baseaboutactivity);
        ViewUtils.setLargeTextSize(mContext, mVersionText, dimensionPixelSize);
        ViewUtils.setLargeTextSize(mContext, mAvailableText, dimensionPixelSize);

        dimensionPixelSize = (float) getResources().getDimensionPixelSize(R.dimen.mesa_bottombtn_text_baseaboutactivity);
        ViewUtils.setLargeTextSize(mContext, mCredits, dimensionPixelSize);
        ViewUtils.setLargeTextSize(mContext, mOpenSource, dimensionPixelSize);
        ViewUtils.setLargeTextSize(mContext, mCreditsInLower, dimensionPixelSize);
        ViewUtils.setLargeTextSize(mContext, mOpenSourceInLower, dimensionPixelSize);
    }

    protected final void setOpenSourceButtonWidth(Button button) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthPixels = displayMetrics.widthPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            widthPixels /= 2;
        }
        button.setMaxWidth((int) (0.75d * widthPixels));
        button.setMinWidth((int) (widthPixels * 0.61d));
    }
}
