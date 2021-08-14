package com.samsung.android.ui.preference;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;
import com.mesalabs.cerberus.utils.ViewUtils;

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

public class SeslSwitchPreferenceScreen extends SeslSwitchPreferenceCompat {
    private View.OnKeyListener mSwitchKeyListener;

    public SeslSwitchPreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mSwitchKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean handled = false;
                switch (event.getKeyCode()) {
                    case 21:
                        if (isChecked()) {
                            if (callChangeListener(false)) {
                                setChecked(false);
                            }
                            handled = true;
                        }
                        break;
                    case 22:
                        if (!isChecked()) {
                            if (callChangeListener(true)) {
                                setChecked(true);
                            }
                            handled = true;
                        }
                        break;
                }
                return handled;
            }
        };

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslPreference, defStyleAttr, defStyleRes);
        String fragment = a.getString(R.styleable.SeslPreference_android_fragment);
        if (fragment == null || fragment.equals("")) {
            LogUtils.w("SwitchPreferenceScreen", "SwitchPreferenceScreen should get fragment property. Fragment property does not exsit in SwitchPreferenceScreen");
        }
        Configuration conf = context.getResources().getConfiguration();
        if ((conf.screenWidthDp > 320 || conf.fontScale < 1.1F) && (conf.screenWidthDp >= 411 || conf.fontScale < 1.3F)) {
            setLayoutResource(R.layout.sesl_switch_preference_screen);
        } else {
            setLayoutResource(R.layout.sesl_switch_preference_screen_large);
        }
        setWidgetLayoutResource(R.layout.sesl_switch_preference_screen_widget_divider);
        a.recycle();
    }

    public SeslSwitchPreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslSwitchPreferenceScreen(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchPreferenceStyle);
    }

    @Override
    protected void onClick() { }

    @Override
    protected void callClickListener() { }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setOnKeyListener(mSwitchKeyListener);
        TextView titleView = (TextView) holder.findViewById(16908310);
        View switchView = holder.findViewById(16908352);
        if (titleView != null && switchView != null) {
            ViewUtils.semSetHoverPopupType(switchView, 0 /*SemHoverPopupWindow.TYPE_NONE*/);
            switchView.setContentDescription(titleView.getText().toString());
        }
    }
}
