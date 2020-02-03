package com.samsung.android.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

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

public class SeslSwitchBar extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    public static final int SWITCH_OFF_STRING_RESOURCE_ID;
    public static final int SWITCH_ON_STRING_RESOURCE_ID;
    public LinearLayout mBackground;
    public int mBackgroundActivatedColor;
    public int mBackgroundColor;
    public String mLabel;
    public int mOffTextColor;
    public int mOffTextId;
    public int mOnTextColor;
    public int mOnTextId;
    public SeslProgressBar mProgressBar;
    public SeslToggleSwitch mSwitch;
    public final List<OnSwitchChangeListener> mSwitchChangeListeners;
    public TextView mTextView;

    static {
        SWITCH_ON_STRING_RESOURCE_ID = R.string.sesl_switch_on;
        SWITCH_OFF_STRING_RESOURCE_ID = R.string.sesl_switch_off;
    }

    public SeslSwitchBar(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public SeslSwitchBar(Context var1, AttributeSet var2) {
        this(var1, var2, R.attr.seslSwitchBarStyle);
    }

    public SeslSwitchBar(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }

    public SeslSwitchBar(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.mSwitchChangeListeners = new ArrayList();
        LayoutInflater.from(var1).inflate(R.layout.sesl_switchbar, this);
        boolean var5 = true;
        this.setFocusable(true);
        this.setClickable(true);
        Resources var6 = this.getResources();
        TypedArray var8 = var1.obtainStyledAttributes(var2, R.styleable.SeslSwitchBar, var3, var4);
        this.mBackgroundColor = var8.getColor(R.styleable.SeslSwitchBar_seslSwitchBarBackgroundColor, var6.getColor(R.color.sesl_switchbar_off_background_color_light, null));
        var4 = var8.getColor(R.styleable.SeslSwitchBar_seslSwitchBarBackgroundActivatedColor, var6.getColor(R.color.sesl_switchbar_on_background_color, null));
        this.mOnTextColor = var8.getColor(R.styleable.SeslSwitchBar_seslSwitchBarTextActivatedColor, var6.getColor(R.color.sesl_switchbar_on_text_color_light, null));
        this.mOffTextColor = var8.getColor(R.styleable.SeslSwitchBar_seslSwitchBarTextColor, var6.getColor(R.color.sesl_switchbar_off_text_color_light, null));
        var8.recycle();
        TypedValue var9 = new TypedValue();
        var1.getTheme().resolveAttribute(R.attr.isLightTheme, var9, true);
        boolean var10;
        if (var9.data != 0) {
            var10 = var5;
        } else {
            var10 = false;
        }

        if (var10) {
            var3 = 16777215 & var4;
            var4 = -872415232;
        } else {
            var3 = 16777215 & var4;
            var4 = 1711276032;
        }

        this.mBackgroundActivatedColor = var3 | var4;
        this.mProgressBar = (SeslProgressBar)this.findViewById(R.id.sesl_switchbar_progress);
        this.mBackground = (LinearLayout)this.findViewById(R.id.sesl_switchbar_container);
        this.mBackground.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                if (SeslSwitchBar.this.mSwitch != null && SeslSwitchBar.this.mSwitch.isEnabled()) {
                    SeslSwitchBar var3 = SeslSwitchBar.this;
                    boolean var2;
                    if (!var3.mSwitch.isChecked()) {
                        var2 = true;
                    } else {
                        var2 = false;
                    }

                    var3.setChecked(var2);
                }

            }
        });
        var4 = (int)var6.getDimension(R.dimen.sesl_switchbar_margin_start);
        var3 = (int)var6.getDimension(R.dimen.sesl_switchbar_margin_end);
        this.mOnTextId = SWITCH_ON_STRING_RESOURCE_ID;
        this.mOffTextId = SWITCH_OFF_STRING_RESOURCE_ID;
        this.mTextView = (TextView)this.findViewById(R.id.sesl_switchbar_text);
        ((MarginLayoutParams)this.mTextView.getLayoutParams()).setMarginStart(var4);
        this.mSwitch = (SeslToggleSwitch)this.findViewById(R.id.sesl_switchbar_switch);
        this.mSwitch.setSaveEnabled(false);
        this.mSwitch.setOnCheckedChangeListener(this);
        this.setSwitchBarText(R.string.sesl_switch_on, R.string.sesl_switch_off);
        this.addOnSwitchChangeListener(new SeslSwitchBar.OnSwitchChangeListener() {
            public void onSwitchChanged(SeslSwitch var1, boolean var2) {
                SeslSwitchBar.this.setTextViewLabelAndBackground(var2);
            }
        });
        ((MarginLayoutParams)this.mSwitch.getLayoutParams()).setMarginEnd(var3);
        SeslSwitchBar.SwitchBarDelegate var7 = new SeslSwitchBar.SwitchBarDelegate(this);
        ViewCompat.setAccessibilityDelegate(this.mBackground, var7);
        ViewCompat.setAccessibilityDelegate(this.mSwitch, var7);
        var7.setSessionName(this.getActivityTitle());
    }

    private String getActivityTitle() {
        Context var1 = this.getContext();

        while(true) {
            boolean var2 = var1 instanceof ContextWrapper;
            String var3 = "";
            if (!var2) {
                return "";
            }

            if (var1 instanceof Activity) {
                CharSequence var4 = ((Activity)var1).getTitle();
                String var5 = var3;
                if (var4 != null) {
                    var5 = var4.toString();
                }

                return var5;
            }

            var1 = ((ContextWrapper)var1).getBaseContext();
        }
    }

    private void propagateChecked(boolean var1) {
        int var2 = this.mSwitchChangeListeners.size();

        for(int var3 = 0; var3 < var2; ++var3) {
            ((SeslSwitchBar.OnSwitchChangeListener)this.mSwitchChangeListeners.get(var3)).onSwitchChanged(this.mSwitch, var1);
        }

    }

    public void addOnSwitchChangeListener(SeslSwitchBar.OnSwitchChangeListener var1) {
        if (!this.mSwitchChangeListeners.contains(var1)) {
            this.mSwitchChangeListeners.add(var1);
        } else {
            throw new IllegalStateException("Cannot add twice the same OnSwitchChangeListener");
        }
    }

    public final SeslToggleSwitch getSwitch() {
        return this.mSwitch;
    }

    public void hide() {
        if (this.isShowing()) {
            this.setVisibility(View.GONE);
            this.mSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)null);
        }

    }

    public boolean isChecked() {
        return this.mSwitch.isChecked();
    }

    public boolean isShowing() {
        boolean var1;
        if (this.getVisibility() == View.VISIBLE) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    public void onCheckedChanged(CompoundButton var1, boolean var2) {
        this.propagateChecked(var2);
    }

    public void onRestoreInstanceState(Parcelable var1) {
        SeslSwitchBar.SavedState var4 = (SeslSwitchBar.SavedState)var1;
        super.onRestoreInstanceState(var4.getSuperState());
        this.mSwitch.setCheckedInternal(var4.checked);
        this.setTextViewLabelAndBackground(var4.checked);
        byte var2;
        if (var4.visible) {
            var2 = 0;
        } else {
            var2 = 8;
        }

        this.setVisibility(var2);
        SeslToggleSwitch var3 = this.mSwitch;
        SeslSwitchBar var5;
        if (var4.visible) {
            var5 = this;
        } else {
            var5 = null;
        }

        var3.setOnCheckedChangeListener(var5);
        this.requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SeslSwitchBar.SavedState var1 = new SeslSwitchBar.SavedState(super.onSaveInstanceState());
        var1.checked = this.mSwitch.isChecked();
        var1.visible = this.isShowing();
        return var1;
    }

    public boolean performClick() {
        return this.mSwitch.performClick();
    }

    public void removeOnSwitchChangeListener(SeslSwitchBar.OnSwitchChangeListener var1) {
        if (this.mSwitchChangeListeners.contains(var1)) {
            this.mSwitchChangeListeners.remove(var1);
        } else {
            throw new IllegalStateException("Cannot remove OnSwitchChangeListener");
        }
    }

    public void setChecked(boolean var1) {
        this.setTextViewLabelAndBackground(var1);
        this.mSwitch.setChecked(var1);
    }

    public void setCheckedInternal(boolean var1) {
        this.setTextViewLabelAndBackground(var1);
        this.mSwitch.setCheckedInternal(var1);
    }

    public void setEnabled(boolean var1) {
        super.setEnabled(var1);
        this.mTextView.setEnabled(var1);
        this.mSwitch.setEnabled(var1);
        this.setTextViewLabelAndBackground(this.isChecked());
    }

    public void setProgressBarVisible(boolean param1) {
        // $FF: Couldn't be decompiled
    }

    public void setSwitchBarText(int var1, int var2) {
        this.mOnTextId = var1;
        this.mOffTextId = var2;
        this.setTextViewLabelAndBackground(this.isChecked());
    }

    public void setTextViewLabel(boolean var1) {
        Resources var2 = this.getResources();
        int var3;
        if (var1) {
            var3 = R.string.sesl_switch_on;
        } else {
            var3 = R.string.sesl_switch_off;
        }

        this.mLabel = var2.getString(var3);
        this.mTextView.setText(this.mLabel);
    }

    public void setTextViewLabelAndBackground(boolean var1) {
        Resources var2 = this.getResources();
        int var3;
        if (var1) {
            var3 = this.mOnTextId;
        } else {
            var3 = this.mOffTextId;
        }

        this.mLabel = var2.getString(var3);
        Drawable var4 = DrawableCompat.wrap(this.mBackground.getBackground()).mutate();
        if (var1) {
            var3 = this.mBackgroundActivatedColor;
        } else {
            var3 = this.mBackgroundColor;
        }

        DrawableCompat.setTintList(var4, ColorStateList.valueOf(var3));
        TextView var5 = this.mTextView;
        if (var1) {
            var3 = this.mOnTextColor;
        } else {
            var3 = this.mOffTextColor;
        }

        var5.setTextColor(var3);
        if (this.isEnabled()) {
            this.mTextView.setAlpha(1.0F);
            this.mBackground.getBackground().setAlpha(255);
        } else {
            this.mTextView.setAlpha(0.4F);
            this.mBackground.getBackground().setAlpha(102);
        }

        String var6 = this.mLabel;
        if (var6 == null || !var6.contentEquals(this.mTextView.getText())) {
            this.mTextView.setText(this.mLabel);
        }
    }

    public void show() {
        if (!this.isShowing()) {
            this.setVisibility(View.VISIBLE);
            this.mSwitch.setOnCheckedChangeListener(this);
        }

    }

    public interface OnSwitchChangeListener {
        void onSwitchChanged(SeslSwitch var1, boolean var2);
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SeslSwitchBar.SavedState> CREATOR = new Creator<SeslSwitchBar.SavedState>() {
            public SeslSwitchBar.SavedState createFromParcel(Parcel var1) {
                return new SeslSwitchBar.SavedState(var1);
            }

            public SeslSwitchBar.SavedState[] newArray(int var1) {
                return new SeslSwitchBar.SavedState[var1];
            }
        };
        public boolean checked;
        public boolean visible;

        public SavedState(Parcel var1) {
            super(var1);
            this.checked = (Boolean)var1.readValue((ClassLoader)null);
            this.visible = (Boolean)var1.readValue((ClassLoader)null);
        }

        public SavedState(Parcelable var1) {
            super(var1);
        }

        public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("SeslSwitchBar.SavedState{");
            var1.append(Integer.toHexString(System.identityHashCode(this)));
            var1.append(" checked=");
            var1.append(this.checked);
            var1.append(" visible=");
            var1.append(this.visible);
            var1.append("}");
            return var1.toString();
        }

        public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeValue(this.checked);
            var1.writeValue(this.visible);
        }
    }

    private static class SwitchBarDelegate extends AccessibilityDelegateCompat {
        public String mSessionName = "";
        public SeslToggleSwitch mSwitch;
        public TextView mText;

        public SwitchBarDelegate(View var1) {
            this.mText = (TextView)var1.findViewById(R.id.sesl_switchbar_text);
            this.mSwitch = (SeslToggleSwitch)var1.findViewById(R.id.sesl_switchbar_switch);
        }

        public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            boolean var3 = this.mSwitch.isChecked();
            Resources var4 = var1.getContext().getResources();
            int var5;
            if (var3) {
                var5 = SeslSwitchBar.SWITCH_ON_STRING_RESOURCE_ID;
            } else {
                var5 = SeslSwitchBar.SWITCH_OFF_STRING_RESOURCE_ID;
            }

            String var9 = var4.getString(var5);
            StringBuilder var6 = new StringBuilder();
            CharSequence var7 = this.mText.getText();
            var2.setClassName(SeslSwitch.class.getName());
            if (!TextUtils.isEmpty(this.mSessionName)) {
                var6.append(this.mSessionName);
                var6.append(", ");
            }

            if (!TextUtils.equals(var9, var7) && !TextUtils.isEmpty(var7)) {
                var6.append(var7);
                var6.append(", ");
            }

            if (var1 instanceof SeslToggleSwitch) {
                var2.setText(var6.toString());
            } else if (var1 instanceof LinearLayout) {
                String var8 = var1.getContext().getResources().getString(R.string.sesl_switch);
                var6.append(var9);
                var6.append(", ");
                var6.append(var8);
                var1.setContentDescription(var6.toString());
            }

        }

        public void setSessionName(String var1) {
            this.mSessionName = var1;
        }
    }
}
