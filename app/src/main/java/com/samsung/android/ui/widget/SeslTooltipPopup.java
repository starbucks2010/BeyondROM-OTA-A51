package com.samsung.android.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.mesalabs.cerberus.R;

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

class SeslTooltipPopup {
    private static final String TAG = "SeslTooltipPopup";
    private final View mContentView;
    private final Context mContext;
    private final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private final TextView mMessageView;
    private final Rect mTmpDisplayFrame = new Rect();
    private final int[] mTmpAnchorPos = new int[2];
    private final int[] mTmpAppPos = new int[2];

    SeslTooltipPopup(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.popupTheme, outValue, false);
        if (outValue.data != 0) {
            this.mContext = new ContextThemeWrapper(context, outValue.data);
        } else {
            this.mContext = context;
        }

        mContentView = LayoutInflater.from(mContext).inflate(R.layout.sesl_tooltip, null);
        mMessageView = (TextView) mContentView.findViewById(R.id.message);

        mContentView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    hide();
                    return false;
                } else if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) {
                    return false;
                } else {
                    hide();
                    return true;
                }
            }
        });

        mLayoutParams.setTitle(getClass().getSimpleName());
        mLayoutParams.packageName = mContext.getPackageName();

        mLayoutParams.type = 1002;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.windowAnimations = R.style.mesa_TooltipAnimStyle;
        mLayoutParams.flags = 262152;
    }

    void show(View anchorView, int anchorX, int anchorY, boolean fromTouch, CharSequence tooltipText) {
        if (isShowing()) {
            hide();
        }

        mMessageView.setText(tooltipText);

        computePosition(anchorView, anchorX, anchorY, fromTouch, mLayoutParams);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mContentView, mLayoutParams);
    }

    public void showActionItemTooltip(int x, int y, int layoutDirection, CharSequence tooltipText) {
        if (isShowing()) {
            hide();
        }

        mMessageView.setText(tooltipText);
        mLayoutParams.x = x;
        mLayoutParams.y = y;

        if (layoutDirection == 0) {
            mLayoutParams.gravity = 8388661;
        } else {
            mLayoutParams.gravity = 8388659;
        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mContentView, mLayoutParams);
    }

    void hide() {
        if (!isShowing()) {
            return;
        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(mContentView);
    }

    boolean isShowing() {
        return mContentView.getParent() != null;
    }

    void updateContent(CharSequence tooltipText) {
        mMessageView.setText(tooltipText);
    }

    private void computePosition(View anchorView, int anchorX, int anchorY, boolean fromTouch, WindowManager.LayoutParams outParams) {
        int statusBarHeight;
        int offsetX = anchorView.getWidth() / 2;
        if (anchorView.getHeight() >= mContext.getResources().getDimensionPixelOffset(R.dimen.sesl_tooltip_precise_anchor_threshold)) {
            int offsetExtra = mContext.getResources().getDimensionPixelOffset(R.dimen.sesl_tooltip_precise_anchor_extra_offset);
            int i = anchorY + offsetExtra;
            int i2 = anchorY - offsetExtra;
        } else {
            int offsetBelow = anchorView.getHeight();
        }

        outParams.gravity = 49;

        int dimensionPixelOffset = mContext.getResources().getDimensionPixelOffset(fromTouch ? R.dimen.sesl_tooltip_y_offset_touch : R.dimen.sesl_tooltip_y_offset_non_touch);

        View appView = getAppRootView(anchorView);
        if (appView == null) {
            Log.e(TAG, "Cannot find app view");
            return;
        }

        appView.getWindowVisibleDisplayFrame(mTmpDisplayFrame);
        if (mTmpDisplayFrame.left < 0 && mTmpDisplayFrame.top < 0) {
            Resources res = mContext.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId != 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            } else {
                statusBarHeight = 0;
            }
            DisplayMetrics metrics = res.getDisplayMetrics();
            mTmpDisplayFrame.set(0, statusBarHeight, metrics.widthPixels, metrics.heightPixels);
        }
        appView.getLocationOnScreen(mTmpAppPos);
        anchorView.getLocationOnScreen(mTmpAnchorPos);

        int[] iArr = mTmpAnchorPos;
        iArr[0] = iArr[0] - mTmpAppPos[0];
        int[] iArr2 = mTmpAnchorPos;
        iArr2[1] = iArr2[1] - mTmpAppPos[1];
        outParams.x = (mTmpAnchorPos[0] + offsetX) - (mTmpDisplayFrame.width() / 2);

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mContentView.measure(spec, spec);

        int tooltipHeight = mContentView.getMeasuredHeight();
        int tooltipWidth = mContentView.getMeasuredWidth();
        int yAbove = mTmpAnchorPos[1] - tooltipHeight;
        int yBelow = mTmpAnchorPos[1] + anchorView.getHeight();

        if (fromTouch) {
            if (anchorView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                outParams.x = ((mTmpAnchorPos[0] + offsetX) - ((mTmpDisplayFrame.width() + tooltipWidth) / 2)) + mContext.getResources().getDimensionPixelOffset(R.dimen.sesl_hover_tooltip_popup_right_margin);
            } else {
                outParams.x = ((mTmpAnchorPos[0] + offsetX) - ((mTmpDisplayFrame.width() - tooltipWidth) / 2)) - mContext.getResources().getDimensionPixelOffset(R.dimen.sesl_hover_tooltip_popup_left_margin);
            }
            if (yBelow + tooltipHeight > mTmpDisplayFrame.height()) {
                outParams.y = yAbove;
            } else {
                outParams.y = yBelow;
            }
        } else {
            outParams.x = (mTmpAnchorPos[0] + offsetX) - (mTmpDisplayFrame.width() / 2);
            if (yAbove >= 0) {
                outParams.y = yAbove;
            } else {
                outParams.y = yBelow;
            }
        }
    }

    private static View getAppRootView(View anchorView) {
        View rootView = anchorView.getRootView();
        Context context = anchorView.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context).getWindow().getDecorView();
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }

        return rootView;
    }
}
