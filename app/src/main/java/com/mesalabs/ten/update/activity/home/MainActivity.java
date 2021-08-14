package com.mesalabs.ten.update.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.LinkedHashMap;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mesalabs.cerberus.base.BaseAppBarActivity;
import com.mesalabs.cerberus.ui.callback.OnSingleClickListener;
import com.mesalabs.cerberus.ui.utils.ActionBarUtils;
import com.mesalabs.cerberus.ui.widget.ToolbarImageButton;
import com.mesalabs.cerberus.update.utils.AppUpdateUtils;
import com.mesalabs.cerberus.utils.CerberusException;
import com.mesalabs.cerberus.utils.StateUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;
import com.mesalabs.ten.update.TenUpdateApp;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.activity.settings.SettingsActivity;
import com.mesalabs.ten.update.fragment.home.DownloadProgressFragment;
import com.mesalabs.ten.update.fragment.home.MainCardsFragment;
import com.mesalabs.ten.update.ota.ROMUpdate;
import com.mesalabs.ten.update.ota.tasks.GenerateRecoveryScript;
import com.mesalabs.ten.update.ota.utils.GeneralUtils;
import com.mesalabs.ten.update.ota.utils.PreferencesUtils;
import com.mesalabs.ten.update.utils.LogUtils;
import com.samsung.android.ui.app.SeslAlertDialog;

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

public class MainActivity extends BaseAppBarActivity {
    public static int MAIN_PAGE_FRAGMENT = 0;
    public static int DOWNLOAD_PROGRESS_FRAGMENT = 1;

    private boolean mAppUpdateAvailable = false;
    private boolean mIsDownloadeing = false;
    private boolean mIsFromLauncher = true;
    private final Class[] mFragmentClasses = {MainCardsFragment.class, DownloadProgressFragment.class};
    private final String[] mFragmentTags = {"MainCards", "DownloadProgress"};
    private int newFragmentIndex;

    private AppUpdateUtils mAppUpdate;
    private AppUpdateUtils.StubListener mAppStubListener = status -> {
        mAppUpdateAvailable = status == AppUpdateUtils.STATE_NEW_VERSION_AVAILABLE;
        PreferencesUtils.setIsAppUpdateAvailable(mAppUpdateAvailable);
        initMoreMenuButton();
    };
    private ROMUpdate.Download mROMUpdateDownload;

    private AlphaAnimation mFadeInAnim;
    private AlphaAnimation mFadeOutAnim;
    private AlphaAnimation mFadeInAnim_Dwn;
    private AlphaAnimation mFadeOutAnim_Dwn;
    private AlphaAnimation mFadeOutAnim_F;

    private Dialog mProgressCircle;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private FrameLayout mFragmentContainer;

    private ToolbarImageButton mRefreshBtn;
    private LinearLayout mBottomLayout;
    private AppCompatButton mBottomButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init
        if (!GeneralUtils.isNotificationAlarmSet(mContext)) {
            GeneralUtils.setBackgroundCheck(mContext, PreferencesUtils.getBgServiceEnabled());
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mIsFromLauncher = getIntent().getExtras().getBoolean("mesa_ota_isfromlauncher", true);
        }

        mIsDownloadeing = PreferencesUtils.Download.getDownloadFinished();

        if (!mIsDownloadeing) {
            PreferencesUtils.ROM.clean();
            PreferencesUtils.Download.clean();
        }

        //  init pt.2
        mAppUpdate = new AppUpdateUtils(this, TenUpdateApp.getAppPackageName(), mAppStubListener);
        mAppUpdate.checkUpdates();

        mROMUpdateDownload = new ROMUpdate.Download(this);

        // init UX
        setBaseContentView(R.layout.mesa_ota_activity_mainactivity_layout);

        appBar = new ActionBarUtils(this);
        appBar.initAppBar(false);
        appBar.setTitleText(getString(R.string.mesa_tenupdate));
        appBar.setHomeAsUpButton(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                onBackPressed();
            }
        });
        if (mIsFromLauncher)
            appBar.setHomeAsUpButtonVisible(false);
        appBar.addOverflowButton(false,
                R.drawable.mesa_ota_ic_ab_refresh,
                R.string.mesa_check_for_updates,
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        if (!StateUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, R.string.mesa_no_network_connection, Toast.LENGTH_LONG).show();
                        }
                        animateRefreshButton(false);
                        ((MainCardsFragment) mFragment).checkForROMUpdates();
                    }
                });
        mRefreshBtn = appBar.getOverflowIcon(0);
        initMoreMenuButton();

        initUX();
    }

    @Override
    public void onBackPressed() {
        if (PreferencesUtils.Download.getIsDownloadOnGoing()) {
            new SeslAlertDialog.Builder(mContext)
                    .setCancelable(true)
                    .setTitle(getString(R.string.mesa_onbackpressed_dwn_dialog_title))
                    .setMessage(getString(R.string.mesa_onbackpressed_dwn_dialog_msg))
                    .setPositiveButton(getString(R.string.mesa_ok), (dialog, which) -> {
                        mROMUpdateDownload.cancelDownload();
                        switchToFragment(MAIN_PAGE_FRAGMENT);
                    })
                    .setNegativeButton(getString(R.string.mesa_cancel), null)
                    .show();
        } else if (newFragmentIndex == DOWNLOAD_PROGRESS_FRAGMENT) {
            switchToFragment(MAIN_PAGE_FRAGMENT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewUtils.updateListBothSideMargin(this, mFragmentContainer);
    }

    private void initUX() {
        initAnimationFields();

        mProgressCircle = new Dialog(mContext, Utils.isNightMode(mContext) ? R.style.mesa_ProgressCircleDialogStyle : R.style.mesa_ProgressCircleDialogStyle_Light);
        mProgressCircle.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressCircle.getWindow().setGravity(Gravity.CENTER);
        mProgressCircle.setCancelable(false);
        mProgressCircle.setCanceledOnTouchOutside(false);
        mProgressCircle.setContentView(LayoutInflater.from(mContext).inflate(R.layout.mesa_view_progress_circle_dialog_layout, null));

        mFragmentManager = getSupportFragmentManager();
        mFragmentContainer = findViewById(R.id.mesa_fragmentcontainer_ota_mainactivity);
        ViewUtils.updateListBothSideMargin(this, mFragmentContainer);

        mBottomLayout = findViewById(R.id.mesa_bottomlayout_ota_mainactivity);
        mBottomButton = findViewById(R.id.mesa_bottombtn_ota_mainactivity);

        switchToFragment(MAIN_PAGE_FRAGMENT);
        animateRefreshButton(false);
    }

    private void initAnimationFields() {
        mFadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim.setDuration(250);
        mFadeOutAnim = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim.setDuration(250);

        mFadeInAnim_Dwn = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim_Dwn.setDuration(250);
        mFadeInAnim_Dwn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFragmentContainer.setAlpha(1.0f);
                mBottomLayout.setAlpha(1.0f);
                mBottomButton.startAnimation(mFadeInAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) { }
        });
        mFadeOutAnim_Dwn = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim_Dwn.setDuration(250);
        mFadeOutAnim_Dwn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFragmentContainer.setAlpha(0.0f);
                mBottomLayout.setAlpha(0.0f);
                mBottomLayout.setVisibility(View.VISIBLE);
                inflateFragment(newFragmentIndex);
                mROMUpdateDownload.startDownload();
            }
        });

        mFadeOutAnim_F = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim_F.setDuration(250);
        mFadeOutAnim_F.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBottomButton.startAnimation(mFadeOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomLayout.setVisibility(newFragmentIndex != 0 ? View.VISIBLE : View.GONE);
                inflateFragment(newFragmentIndex);
                mFragmentContainer.startAnimation(mFadeInAnim);
                mBottomButton.startAnimation(mFadeInAnim);
            }
        });
    }

    private void inflateFragment(int index) {
        appBar.setHomeAsUpButtonVisible(index != 0 || !mIsFromLauncher);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentTags[index]);
        if (mFragment == null) {
            mFragment = mFragmentManager.findFragmentById(R.id.mesa_fragmentcontainer_ota_mainactivity);
        }
        if (mFragment != null) {
            transaction.hide(mFragment);
            if (mFragment instanceof DownloadProgressFragment) {
                transaction.remove(mFragment);
            }
        }

        if (fragment != null) {
            mFragment = fragment;
            transaction.show(fragment);
        } else {
            try {
                mFragment = (Fragment) mFragmentClasses[index].newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new CerberusException(e.toString());
            }
            transaction.add(R.id.mesa_fragmentcontainer_ota_mainactivity, mFragment, mFragmentTags[index]);
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    private LinkedHashMap<String, Integer> initMoreMenuButtonList() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(getString(R.string.mesa_settings), mAppUpdateAvailable ? ActionBarUtils.NEW_UPDATE_AVAILABLE : 0);
        return linkedHashMap;
    }

    private void initMoreMenuButton() {
        appBar.setMoreMenuButton(initMoreMenuButtonList(),
                (adapterView, view, i, j) -> {
                    appBar.dismissMoreMenuPopupWindow();

                    String obj = adapterView.getAdapter().getItem(i).toString();
                    if (obj.equals(getString(R.string.mesa_settings))) {
                        Intent intent = new Intent(mContext, SettingsActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private long getAvailableInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
    }



    public void animateBottomDownloadButton(boolean enabled, boolean paused) {
        mBottomButton.setEnabled(enabled);
        mBottomButton.setAlpha(enabled ? 1.0f : 0.4f);
        if (paused) {
            mBottomButton.setText(getString(R.string.mesa_resume));
            mBottomButton.setOnClickListener(v -> mROMUpdateDownload.resumeDownload());
        } else {
            mBottomButton.setText(getString(R.string.mesa_pause));
            mBottomButton.setOnClickListener(v -> mROMUpdateDownload.pauseDownload());
        }
    }

    public void animateBottomInstallButton(boolean enabled) {
        mBottomButton.setEnabled(enabled);
        mBottomButton.setAlpha(enabled ? 1.0f : 0.4f);
        mBottomButton.setText(getString(R.string.mesa_install_now));
        mBottomButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                new GenerateRecoveryScript(mContext).execute();
            }
        });
        mBottomButton.postDelayed(() -> mBottomButton.setPressed(true), 250);
        mBottomButton.postDelayed(() -> mBottomButton.setPressed(false), 250);
    }

    public void animateRefreshButton(boolean enabled) {
        mRefreshBtn.setAlpha(enabled ? 1.0f : 0.4f);
        mRefreshBtn.setEnabled(enabled);
    }

    public DownloadProgressFragment getDownloadFragment() {
        if (mFragment instanceof DownloadProgressFragment) {
            return (DownloadProgressFragment) mFragment;
        } else {
            LogUtils.e("MainActivity", "DownloadProgressFragment not inflated!!!");
            return (DownloadProgressFragment) mFragmentManager.findFragmentById(R.id.mesa_fragmentcontainer_ota_mainactivity);
        }
    }

    public void onPreROMUpdateDownload() {
        mProgressCircle.show();
        if (getAvailableInternalMemorySize() < PreferencesUtils.ROM.getFileSize()) {
            Toast.makeText(mContext, getString(R.string.mesa_no_space_left), Toast.LENGTH_LONG).show();
            mProgressCircle.dismiss();
            return;
        }
        newFragmentIndex = DOWNLOAD_PROGRESS_FRAGMENT;
        animateRefreshButton(false);
        mFragmentContainer.startAnimation(mFadeOutAnim_Dwn);
    }

    public void onPostROMUpdateDownload() {
        mProgressCircle.dismiss();
        mFragmentContainer.startAnimation(mFadeInAnim_Dwn);
    }

    public void onErrorROMUpdateDownload() {
        Toast.makeText(mContext, getString(R.string.mesa_download_failed), Toast.LENGTH_LONG).show();
        switchToFragment(MAIN_PAGE_FRAGMENT);
        mROMUpdateDownload.cancelDownload();
    }

    public void switchToFragment(int index) {
        newFragmentIndex = index;
        mIsDownloadeing = PreferencesUtils.Download.getDownloadFinished() || PreferencesUtils.Download.getIsDownloadOnGoing();
        animateRefreshButton(!mIsDownloadeing);
        mFragmentContainer.startAnimation(mFadeOutAnim_F);
    }

}
