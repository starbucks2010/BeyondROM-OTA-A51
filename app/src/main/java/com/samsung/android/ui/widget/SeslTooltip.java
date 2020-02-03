package com.samsung.android.ui.widget;

import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Build;
import android.text.TextUtils;
import android.provider.Settings.System;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;

import com.mesalabs.cerberus.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;

/*
 * Cerberus Core App
 *
 * Coded by Samsung. All rights reserved to their respective owners.
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

public class SeslTooltip implements View.OnLongClickListener, View.OnHoverListener, View.OnAttachStateChangeListener {
    private static final String TAG = "SeslTooltip";
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 3000;
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000;
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000;
    private static SeslTooltip sActiveHandler;
    private static SeslTooltip sPendingHandler;
    private static boolean sIsCustomTooltipPosition;
    private static boolean sIsTooltipNull = false;
    private static int sLayoutDirection;
    private static int sPosX;
    private static int sPosY;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mFromTouch;
    private boolean mIsSPenPointChanged = false;
    private boolean mIsShowRunnablePostDelayed = false;
    private SeslTooltipPopup mPopup;
    private final CharSequence mTooltipText;
    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            show(false /* not from touch*/);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public static void setTooltipText(View view, CharSequence tooltipText) {
        if (sPendingHandler != null && sPendingHandler.mAnchor == view) {
            setPendingHandler(null);
        }
        if (TextUtils.isEmpty(tooltipText)) {
            if (sActiveHandler != null && sActiveHandler.mAnchor == view) {
                sActiveHandler.hide();
            }
            view.setOnLongClickListener(null);
            view.setLongClickable(false);
            view.setOnHoverListener(null);
        } else if (sActiveHandler == null || sActiveHandler.mAnchor != view) {
            new SeslTooltip(view, tooltipText);
        } else {
            sActiveHandler.hide();
        }
        view.setHapticFeedbackEnabled(false);
    }

    private SeslTooltip(View anchor, CharSequence tooltipText) {
        mAnchor = anchor;
        mTooltipText = tooltipText;
        mAnchor.setOnLongClickListener(this);
        mAnchor.setOnHoverListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        mAnchorX = v.getWidth() / 2;
        mAnchorY = v.getHeight() / 2;
        show(true /* from touch */);
        return true;
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        if ((mPopup != null && mFromTouch) && (event.isFromSource(InputDeviceCompat.SOURCE_STYLUS) || !isSPenHoveringSettingsEnabled())) {
            return false;
        }
        AccessibilityManager manager = (AccessibilityManager) mAnchor.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (manager.isEnabled() && manager.isTouchExplorationEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_MOVE:
                if (mAnchor.isEnabled() && mPopup == null) {
                    mAnchorX = (int) event.getX();
                    mAnchorY = (int) event.getY();
                    showPenPointEffect(event, true);
                    if (!mIsShowRunnablePostDelayed) {
                        setPendingHandler(this);
                        mIsShowRunnablePostDelayed = true;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                LogUtils.d(TAG, "MotionEvent.ACTION_HOVER_EXIT : hide SeslTooltipPopup");
                showPenPointEffect(event, false);
                hide();
                break;
        }

        return false;
    }

    @Override
    public void onViewAttachedToWindow(View v) { }

    @Override
    public void onViewDetachedFromWindow(View v) {
        hide();
    }

    private void show(boolean fromTouch) {
        if (!ViewCompat.isAttachedToWindow(mAnchor)) {
            return;
        }
        setPendingHandler(null);
        if (sActiveHandler != null) {
            sActiveHandler.hide();
        }
        sActiveHandler = this;

        mFromTouch = fromTouch;
        mPopup = new SeslTooltipPopup(mAnchor.getContext());
        if (!sIsCustomTooltipPosition) {
            mPopup.show(mAnchor, mAnchorX, mAnchorY, mFromTouch, mTooltipText);
        } else if (!sIsTooltipNull || fromTouch) {
            mPopup.showActionItemTooltip(sPosX, sPosY, sLayoutDirection, mTooltipText);
            sIsCustomTooltipPosition = false;
        } else {
            return;
        }
        mAnchor.addOnAttachStateChangeListener(this);

        final long timeout;
        if (mFromTouch) {
            timeout = LONG_CLICK_HIDE_TIMEOUT_MS;
        } else if ((ViewCompat.getWindowSystemUiVisibility(mAnchor)
                & SYSTEM_UI_FLAG_LOW_PROFILE) == SYSTEM_UI_FLAG_LOW_PROFILE) {
            timeout = HOVER_HIDE_TIMEOUT_SHORT_MS - ViewConfiguration.getLongPressTimeout();
        } else {
            timeout = HOVER_HIDE_TIMEOUT_MS - ViewConfiguration.getLongPressTimeout();
        }
        mAnchor.removeCallbacks(mHideRunnable);
        mAnchor.postDelayed(mHideRunnable, timeout);
    }

    private void hide() {
        if (sActiveHandler == this) {
            sActiveHandler = null;
            if (mPopup != null) {
                mPopup.hide();
                mPopup = null;
                mAnchor.removeOnAttachStateChangeListener(this);
            } else {
                LogUtils.e(TAG, "sActiveHandler.mPopup == null");
            }
        }
        mIsShowRunnablePostDelayed = false;
        if (sPendingHandler == this) {
            setPendingHandler(null);
        }
        mAnchor.removeCallbacks(mHideRunnable);
    }

    private static void setPendingHandler(SeslTooltip handler) {
        if (sPendingHandler != null) {
            sPendingHandler.cancelPendingShow();
        }
        sPendingHandler = handler;
        if (sPendingHandler != null) {
            sPendingHandler.scheduleShow();
        }
    }

    private void scheduleShow() {
        mAnchor.postDelayed(mShowRunnable, ViewConfiguration.getLongPressTimeout());
    }

    private void cancelPendingShow() {
        mAnchor.removeCallbacks(mShowRunnable);
    }

    private void update(CharSequence tooltipText) {
        mPopup.updateContent(tooltipText);
    }

    public static void setTooltipPosition(int x, int y, int layoutDirection) {
        sLayoutDirection = layoutDirection;
        sPosX = x;
        sPosY = y;
        sIsCustomTooltipPosition = true;
    }

    public static void setTooltipNull(boolean tooltipNull) {
        sIsTooltipNull = tooltipNull;
    }

    private void showPenPointEffect(MotionEvent event, boolean show) {
        if (event.getToolType(MotionEvent.TOOL_TYPE_UNKNOWN) == MotionEvent.TOOL_TYPE_STYLUS) {
            if (show) {
                Utils.genericInvokeMethod(InputManager.class, Build.VERSION.SDK_INT >= 29 ? "hidden_setPointerIconType" : "setPointerIconType", (int) 0x4e2a);
                mIsSPenPointChanged = true;
            } else if (mIsSPenPointChanged) {
                Utils.genericInvokeMethod(InputManager.class, Build.VERSION.SDK_INT >= 29 ? "hidden_setPointerIconType" : "setPointerIconType", (int) 0x4e21);
                mIsSPenPointChanged = false;
            }
        }
    }

    boolean isSPenHoveringSettingsEnabled() {
        return System.getInt(mAnchor.getContext().getContentResolver(), "pen_hovering", 0) == 1;
    }
}
