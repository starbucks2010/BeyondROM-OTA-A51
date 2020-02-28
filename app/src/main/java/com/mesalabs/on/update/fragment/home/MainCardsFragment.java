package com.mesalabs.on.update.fragment.home;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.mesalabs.on.update.R;
import com.mesalabs.on.update.activity.home.ChangelogActivity;
import com.mesalabs.on.update.activity.home.FirmwareInfoActivity;
import com.mesalabs.on.update.activity.home.MainActivity;
import com.mesalabs.on.update.ota.ROMUpdate;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.mesalabs.on.update.ui.widget.CardView;
import com.mesalabs.on.update.ui.widget.ChangelogView;
import com.mesalabs.on.update.ui.widget.UpdateStatusView;

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

public class MainCardsFragment extends Fragment {
    private ROMUpdate mROMUpdate;
    private ROMUpdate.StubListener mROMStubListener = new ROMUpdate.StubListener() {
        public void onUpdateCheckCompleted(int status) {
            updateStatusView.setUpdateStatus(status);
            ((MainActivity) mActivity).animateRefreshButton(true);
            unc.setEnabled(true);
            changelogView.start(status == ROMUpdate.STATE_NEW_VERSION_AVAILABLE);
        }
    };

    private FragmentActivity mActivity;
    private Context mContext;

    private View mRootView;
    private LinearLayout mContainer;
    private UpdateStatusView updateStatusView;
    private ChangelogView changelogView;

    private CardView unc;
    private CardView fic;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
        mContext = mActivity.getApplicationContext();

        //  init ROMUpdate
        mROMUpdate = new ROMUpdate(context, mROMStubListener);
        if (!PreferencesUtils.Download.getIsDownloadOnGoing())
            mROMUpdate.checkUpdates(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.mesa_ota_fragment_maincards_mainactivity, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mRootView == null) {
            mRootView = getView();
        }
        if (mContext == null) {
            mContext = mActivity.getApplicationContext();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LayoutTransition itemLayoutTransition = new LayoutTransition();
        Animator scaleDown = ObjectAnimator.ofPropertyValuesHolder(mContainer, PropertyValuesHolder.ofFloat("alpha", 1, 0));
        Animator scaleUp = ObjectAnimator.ofPropertyValuesHolder(mContainer, PropertyValuesHolder.ofFloat("alpha", 0, 1));
        itemLayoutTransition.setAnimator(LayoutTransition.APPEARING, scaleUp);
        itemLayoutTransition.setDuration(LayoutTransition.APPEARING, 650);
        itemLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        itemLayoutTransition.setDuration(LayoutTransition.CHANGE_APPEARING, 150);
        itemLayoutTransition.setInterpolator(LayoutTransition.CHANGE_APPEARING, new FastOutSlowInInterpolator());
        itemLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, scaleDown);
        itemLayoutTransition.setDuration(LayoutTransition.DISAPPEARING, 80);

        mContainer = mRootView.findViewById(R.id.mesa_container_maincards_ota_mainactivity);
        mContainer.setLayoutTransition(itemLayoutTransition);

        updateStatusView = mRootView.findViewById(R.id.mesa_usv_maincards_ota_mainactivity);
        changelogView = mRootView.findViewById(R.id.mesa_cv_maincards_ota_mainactivity);
        changelogView.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ChangelogActivity.class));
        });
        changelogView.setClickable(false);

        unc = mRootView.findViewById(R.id.mesa_m1_ota_mainactivity);
        unc.setOnClickListener(v -> {
            ((MainActivity) mActivity).switchToFragment(1);
            updateStatusView.setUpdateStatus(ROMUpdate.STATE_DOWNLOADING);
        });
        unc.setEnabled(false);

        fic = mRootView.findViewById(R.id.mesa_m2_ota_mainactivity);
        fic.setOnClickListener(v -> {
            startActivity(new Intent(mContext, FirmwareInfoActivity.class));
        });
    }

    public ChangelogView getChangelogView() {
        return changelogView;
    }

    public UpdateStatusView getUpdateStatusView() {
        return updateStatusView;
    }

    public void checkForROMUpdates() {
        mROMUpdate.checkUpdates(true);
    }

}