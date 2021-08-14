package com.samsung.android.ui.widget;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Build;
import android.text.TextUtils;
import android.provider.Settings.System;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;

import com.mesalabs.ten.update.utils.LogUtils;
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
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000L;
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000L;
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500L;
    private static final String TAG = "SeslTooltip";
    private static boolean mIsForceActionBarX;
    private static boolean mIsForceBelow;
    private static SeslTooltip sActiveHandler;
    private static boolean sIsCustomTooltipPosition;
    private static boolean sIsTooltipNull;
    private static int sLayoutDirection;
    private static SeslTooltip sPendingHandler;
    private static int sPosX;
    private static int sPosY;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mFromTouch;
    private final Runnable mHideRunnable = new Runnable() {
        public void run() {
            hide();
        }
    };
    private final int mHoverSlop;
    private boolean mIsSPenPointChanged = false;
    private boolean mIsShowRunnablePostDelayed = false;
    private SeslTooltipPopup mPopup;
    private final Runnable mShowRunnable = new Runnable() {
        public void run() {
            show(false);
        }
    };
    private final CharSequence mTooltipText;

    public SeslTooltip(View var1, CharSequence var2) {
        this.mAnchor = var1;
        this.mTooltipText = var2;
        this.mHoverSlop = ViewConfigurationCompat.getScaledHoverSlop(ViewConfiguration.get(this.mAnchor.getContext()));
        this.clearAnchorPos();
        this.mAnchor.setOnLongClickListener(this);
        this.mAnchor.setOnHoverListener(this);
    }

    private void cancelPendingShow() {
        this.mAnchor.removeCallbacks(this.mShowRunnable);
    }

    private void clearAnchorPos() {
        this.mAnchorX = 2147483647;
        this.mAnchorY = 2147483647;
    }

    private void scheduleShow() {
        this.mAnchor.postDelayed(this.mShowRunnable, (long)ViewConfiguration.getLongPressTimeout());
    }

    public static void seslSetTooltipForceActionBarPosX(boolean var0) {
        mIsForceActionBarX = var0;
    }

    public static void seslSetTooltipForceBelow(boolean var0) {
        mIsForceBelow = var0;
    }

    public static void seslSetTooltipNull(boolean var0) {
        sIsTooltipNull = var0;
    }

    public static void seslSetTooltipPosition(int var0, int var1, int var2) {
        sLayoutDirection = var2;
        sPosX = var0;
        sPosY = var1;
        sIsCustomTooltipPosition = true;
    }

    public static void setPendingHandler(SeslTooltip var0) {
        SeslTooltip var1 = sPendingHandler;
        if (var1 != null) {
            var1.cancelPendingShow();
        }

        sPendingHandler = var0;
        var0 = sPendingHandler;
        if (var0 != null) {
            var0.scheduleShow();
        }

    }

    public static void setTooltipText(View var0, CharSequence var1) {
        if (var0 == null) {
            LogUtils.d(TAG, "view is null");
        } else {
            SeslTooltip var2 = sPendingHandler;
            if (var2 != null && var2.mAnchor == var0) {
                setPendingHandler(null);
            }

            if (TextUtils.isEmpty(var1)) {
                SeslTooltip var3 = sActiveHandler;
                if (var3 != null && var3.mAnchor == var0) {
                    var3.hide();
                }

                var0.setOnLongClickListener(null);
                var0.setLongClickable(false);
                var0.setOnHoverListener(null);
            } else {
                var2 = sActiveHandler;
                if (var2 != null && var2.mAnchor == var0) {
                    var2.hide();
                } else {
                    new SeslTooltip(var0, var1);
                }
            }

        }
    }

    private void showPenPointEffect(MotionEvent var1, boolean var2) {
        if (var1.getToolType(0) == 2) {
            if (var2) {
                Utils.genericInvokeMethod(InputManager.class, Build.VERSION.SDK_INT >= 29 ? "hidden_setPointerIconType" : "setPointerIconType", (int) 0x4e2a);
                this.mIsSPenPointChanged = true;
            } else if (this.mIsSPenPointChanged) {
                Utils.genericInvokeMethod(InputManager.class, Build.VERSION.SDK_INT >= 29 ? "hidden_setPointerIconType" : "setPointerIconType", (int) 0x4e21);
                this.mIsSPenPointChanged = false;
            }
        }

    }

    private void update(CharSequence var1) {
        this.mPopup.updateContent(var1);
    }

    private boolean updateAnchorPos(MotionEvent var1) {
        int var2 = (int)var1.getX();
        int var3 = (int)var1.getY();
        if (Math.abs(var2 - this.mAnchorX) <= this.mHoverSlop && Math.abs(var3 - this.mAnchorY) <= this.mHoverSlop) {
            return false;
        } else {
            this.mAnchorX = var2;
            this.mAnchorY = var3;
            return true;
        }
    }

    public void hide() {
        if (sActiveHandler == this) {
            sActiveHandler = null;
            SeslTooltipPopup var1 = this.mPopup;
            if (var1 != null) {
                var1.hide();
                this.mPopup = null;
                this.clearAnchorPos();
                this.mAnchor.removeOnAttachStateChangeListener(this);
            } else {
                LogUtils.e(TAG, "sActiveHandler.mPopup == null");
            }
        }

        this.mIsShowRunnablePostDelayed = false;
        if (sPendingHandler == this) {
            setPendingHandler(null);
        }

        this.mAnchor.removeCallbacks(this.mHideRunnable);
        sPosX = 0;
        sPosY = 0;
        sIsTooltipNull = false;
        sIsCustomTooltipPosition = false;
    }

    public boolean isSPenHoveringSettingsEnabled() {
        return System.getInt(mAnchor.getContext().getContentResolver(), "pen_hovering", 0) == 1;
    }

    public boolean onHover(View var1, MotionEvent var2) {
        if (this.mPopup != null && this.mFromTouch) {
            return false;
        } else if (this.mAnchor == null) {
            LogUtils.d(TAG, "TooltipCompat Anchor view is null");
            return false;
        } else {
            Context var3 = var1.getContext();
            if (var2.isFromSource(16386) && !this.isSPenHoveringSettingsEnabled()) {
                if (this.mAnchor.isEnabled() && this.mPopup != null && var3 != null) {
                    Utils.genericInvokeMethod(View.class, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetPointerIcon" : "semSetPointerIcon", var1, 2, PointerIcon.getSystemIcon(var3, 0x4e21));
                }

                return false;
            } else {
                AccessibilityManager var4 = (AccessibilityManager)this.mAnchor.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
                if (var4.isEnabled() && var4.isTouchExplorationEnabled()) {
                    return false;
                } else {
                    int var5 = var2.getAction();
                    if (var5 != 7) {
                        if (var5 != 9) {
                            if (var5 == 10) {
                                LogUtils.d(TAG, "MotionEvent.ACTION_HOVER_EXIT : hide SeslTooltipPopup");
                                if (this.mAnchor.isEnabled() && this.mPopup != null && var3 != null) {
                                    Utils.genericInvokeMethod(View.class, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetPointerIcon" : "semSetPointerIcon", var1, 2, PointerIcon.getSystemIcon(var3, 0x4e21));
                                }

                                this.hide();
                            }
                        } else if (this.mAnchor.isEnabled() && this.mPopup == null && var3 != null) {
                            Utils.genericInvokeMethod(View.class, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetPointerIcon" : "semSetPointerIcon", var1, 2, PointerIcon.getSystemIcon(var3, 0x4e2a));
                        }
                    } else if (this.mAnchor.isEnabled() && this.mPopup == null) {
                        this.mAnchorX = (int)var2.getX();
                        this.mAnchorY = (int)var2.getY();
                        if (!this.mIsShowRunnablePostDelayed) {
                            setPendingHandler(this);
                            this.mIsShowRunnablePostDelayed = true;
                        }
                    }

                    return false;
                }
            }
        }
    }

    public boolean onLongClick(View var1) {
        this.mAnchorX = var1.getWidth() / 2;
        this.mAnchorY = var1.getHeight() / 2;
        this.show(true);
        return true;
    }

    public void onViewAttachedToWindow(View var1) {
    }

    public void onViewDetachedFromWindow(View var1) {
        this.hide();
    }

    public void show(boolean var1) {
        if (ViewCompat.isAttachedToWindow(this.mAnchor)) {
            setPendingHandler(null);
            SeslTooltip var2 = sActiveHandler;
            if (var2 != null) {
                var2.hide();
            }

            sActiveHandler = this;
            this.mFromTouch = var1;
            this.mPopup = new SeslTooltipPopup(this.mAnchor.getContext());
            if (sIsCustomTooltipPosition) {
                mIsForceBelow = false;
                mIsForceActionBarX = false;
                if (sIsTooltipNull && !var1) {
                    return;
                }

                this.mPopup.showActionItemTooltip(sPosX, sPosY, sLayoutDirection, this.mTooltipText);
                sIsCustomTooltipPosition = false;
            } else {
                if (sIsTooltipNull) {
                    return;
                }

                if (!mIsForceBelow && !mIsForceActionBarX) {
                    mIsForceBelow = false;
                    mIsForceActionBarX = false;
                    this.mPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
                } else {
                    this.mPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText, mIsForceBelow, mIsForceActionBarX);
                }
            }

            this.mAnchor.addOnAttachStateChangeListener(this);
            long var3;
            if (this.mFromTouch) {
                var3 = 2500L;
            } else {
                int var5;
                if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 1) == 1) {
                    var3 = 3000L;
                    var5 = ViewConfiguration.getLongPressTimeout();
                } else {
                    var3 = 15000L;
                    var5 = ViewConfiguration.getLongPressTimeout();
                }

                var3 -= (long)var5;
            }

            this.mAnchor.removeCallbacks(this.mHideRunnable);
            this.mAnchor.postDelayed(this.mHideRunnable, var3);
        }
    }
}
