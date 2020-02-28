package com.mesalabs.on.update.activity.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
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
import com.mesalabs.cerberus.ui.utils.ActionBarUtils;
import com.mesalabs.cerberus.ui.widget.ToolbarImageButton;
import com.mesalabs.cerberus.update.utils.AppUpdateUtils;
import com.mesalabs.cerberus.utils.CerberusException;
import com.mesalabs.cerberus.utils.StateUtils;
import com.mesalabs.cerberus.utils.ViewUtils;
import com.mesalabs.on.update.OnUpdateApp;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.activity.settings.SettingsActivity;
import com.mesalabs.on.update.fragment.home.DownloadProgressFragment;
import com.mesalabs.on.update.fragment.home.MainCardsFragment;
import com.mesalabs.on.update.ota.ROMUpdate;
import com.mesalabs.on.update.ota.utils.GeneralUtils;
import com.mesalabs.on.update.ota.utils.SystemUtils;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.samsung.android.ui.app.SeslAlertDialog;

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

public class MainActivity extends BaseAppBarActivity {
    private boolean mAppUpdateAvailable = false;
    private boolean mIsFromLauncher = true;
    private boolean mIsRootAvailable = false;
    private final Class[] mFragmentClasses = {MainCardsFragment.class, DownloadProgressFragment.class};
    private final String[] mFragmentTags = {"MainCards", "DownloadProgress"};
    private int newFragmentIndex;

    private AppUpdateUtils mAppUpdate;
    private AppUpdateUtils.StubListener mAppStubListener = status -> {
        mAppUpdateAvailable = status == AppUpdateUtils.STATE_NEW_VERSION_AVAILABLE;
        PreferencesUtils.setIsAppUpdateAvailable(mAppUpdateAvailable);
        initMoreMenuButton();
    };

    private AlphaAnimation mFadeInAnim;
    private AlphaAnimation mFadeOutAnim;
    private AlphaAnimation mFadeOutAnim_F;

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

        mIsRootAvailable = SystemUtils.isDeviceRooted();
        PreferencesUtils.setIsRootAvailable(mIsRootAvailable);
        if (!mIsRootAvailable) {
            showNoRootDialog();
        }

        //  init AppUpdateUtils
        mAppUpdate = new AppUpdateUtils(this, OnUpdateApp.getAppPackageName(), mAppStubListener);
        mAppUpdate.checkUpdates();

        // init UX
        setBaseContentView(R.layout.mesa_ota_activity_mainactivity_layout);

        appBar = new ActionBarUtils(this);
        appBar.initAppBar(false);
        appBar.setTitleText(getString(R.string.mesa_onupdate));
        appBar.setHomeAsUpButton(v -> onBackPressed());
        if (!mIsFromLauncher)
            appBar.setHomeAsUpButtonVisible(false);
        appBar.addOverflowButton(false, R.drawable.mesa_ota_ic_ab_refresh, R.string.mesa_check_for_updates, v -> {
            if (!StateUtils.isNetworkConnected(mContext)) {
                Toast.makeText(mContext, R.string.mesa_no_network_connection, Toast.LENGTH_LONG).show();
            }
            animateRefreshButton(false);
            ((MainCardsFragment) mFragment).getChangelogView().setVisibility(View.GONE);
            ((MainCardsFragment) mFragment).getUpdateStatusView().setUpdateStatus(ROMUpdate.STATE_CHECKING);
            ((MainCardsFragment) mFragment).checkForROMUpdates();
        });
        mRefreshBtn = appBar.getOverflowIcon(0);
        initMoreMenuButton();

        initUX();
    }

    @Override
    public void onBackPressed() {
        if (newFragmentIndex != 0) {
            switchToFragment(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewUtils.updateListBothSideMargin(this, mFragmentContainer);
    }

    private void showNoRootDialog() {
        SeslAlertDialog.Builder dialog = new SeslAlertDialog.Builder(mContext);
        dialog.setCancelable(true);
        dialog.setTitle(getString(R.string.mesa_no_root_access_dialog_title));
        dialog.setMessage(R.string.mesa_no_root_access_dialog_message);
        dialog.setPositiveButton(getString(android.R.string.ok), null);
        dialog.show();
    }

    private void initUX() {
        initAnimationFields();

        mFragmentManager = getSupportFragmentManager();
        mFragmentContainer = findViewById(R.id.mesa_fragmentcontainer_ota_mainactivity);

        mBottomLayout = findViewById(R.id.mesa_bottomlayout_ota_mainactivity);
        mBottomButton = findViewById(R.id.mesa_bottombtn_ota_mainactivity);

        switchToFragment(0);
        animateRefreshButton(false);
    }

    private void initAnimationFields() {
        mFadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim.setDuration(250);
        mFadeOutAnim = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim.setDuration(250);

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
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentTags[index]);
        if (mFragment == null) {
            mFragment = mFragmentManager.findFragmentById(R.id.mesa_fragmentcontainer_ota_mainactivity);
        }
        if (mFragment != null) {
            transaction.hide(mFragment);
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

    public void animateRefreshButton(boolean enabled) {
        mRefreshBtn.setAlpha(enabled ? 1.0f : 0.4f);
        mRefreshBtn.setEnabled(enabled);
    }

    public AppCompatButton getBottomBtnView() {
        return mBottomButton;
    }

    public void switchToFragment(int index) {
        newFragmentIndex = index;
        appBar.setHomeAsUpButtonVisible(index != 0 || !mIsFromLauncher);
        animateRefreshButton(!PreferencesUtils.Download.getIsDownloadOnGoing() && newFragmentIndex == 0);
        mFragmentContainer.startAnimation(mFadeOutAnim_F);
    }

}
