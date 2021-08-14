package com.mesalabs.cerberus.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.Collections;

import com.mesalabs.ten.update.R;
import com.mesalabs.cerberus.utils.Utils;
import com.samsung.android.ui.picker.app.SeslColorPickerDialog;
import com.samsung.android.ui.preference.internal.SeslPreferenceImageView;
import com.samsung.android.ui.preference.PreferenceViewHolder;
import com.samsung.android.ui.preference.SeslPreference;

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

public class ColorPickerPreference extends SeslPreference implements SeslPreference.OnPreferenceClickListener, SeslColorPickerDialog.OnColorSetListener {
    PreferenceViewHolder mViewHolder;
    SeslColorPickerDialog mDialog;
    SeslPreferenceImageView mPreview;
    private int mValue = Color.BLACK;
    private boolean mAlphaSliderEnabled = false;
    private boolean mFirstColor = true;
    private boolean mIsNightMode;
    private ArrayList<Integer> mUsedColors = new ArrayList();

    public ColorPickerPreference(Context context) {
        this(context, null);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        int colorInt;
        String mHexDefaultValue = a.getString(index);
        if (mHexDefaultValue != null && mHexDefaultValue.startsWith("#")) {
            colorInt = convertToColorInt(mHexDefaultValue);
            return colorInt;
        } else {
            return a.getColor(index, Color.BLACK);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        onColorSet(restoreValue ? getPersistedInt(mValue) : (Integer) defaultValue);
    }

    private void init(Context context, AttributeSet attrs) {
        setWidgetLayoutResource(R.layout.mesa_preference_colorpickerpref_widget_layout);

        setOnPreferenceClickListener(this);

        mIsNightMode = Utils.isNightMode(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);
        mAlphaSliderEnabled = a.getBoolean(R.styleable.ColorPickerPreference_showAlphaSlider, false);
        a.recycle();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mViewHolder = holder;
        mPreview = (SeslPreferenceImageView) holder.findViewById(R.id.mesa_imageview_colorpickerpref);
        setPreviewColor();
    }

    private void setPreviewColor() {
        if (mPreview == null) {
            return;
        }

        GradientDrawable drawable = (GradientDrawable) getContext().getDrawable(mIsNightMode ? R.drawable.sesl_color_picker_used_color_item_slot_dark : R.drawable.sesl_color_picker_used_color_item_slot_light);
        drawable.setColor(mValue);

        mPreview.setBackground(drawable);
    }

    @Override
    public void onColorSet(int color) {
        if (isPersistent()) {
            persistInt(color);
        }
        mValue = color;

        callChangeListener(color);

        if (!mFirstColor)
            addRecentColor(color);
        else
            mFirstColor = false;
        setPreviewColor();
    }

    @Override
    public boolean onPreferenceClick(SeslPreference preference) {
        showDialog(null);
        return false;
    }

    private void showDialog(Bundle state) {
        try {
            mDialog = new SeslColorPickerDialog(getContext(), this, mValue, getRecentColors());
            mDialog.setNewColor(mValue);
            mDialog.setTransparencyControlEnabled(mAlphaSliderEnabled);
            if (state != null) {
                mDialog.onRestoreInstanceState(state);
            }
            mDialog.show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setAlphaSliderEnabled(boolean enable) {
        mAlphaSliderEnabled = enable;
    }

    public static int convertToColorInt(String argb) throws IllegalArgumentException {
        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }

    private void addRecentColor(int color) {
        for (int i = 0; i < mUsedColors.size(); i++) {
            if (mUsedColors.get(i) == color)
                mUsedColors.remove(i);
        }

        if (mUsedColors.size() > 5) {
            mUsedColors.remove(0);
        }

        mUsedColors.add(color);
    }

    private int[] getRecentColors() {
        int[] usedColors = new int[mUsedColors.size()];
        ArrayList<Integer> reverseUsedColor = new ArrayList<>(mUsedColors);

        Collections.reverse(reverseUsedColor);

        for (int i = 0; i < reverseUsedColor.size(); i++) {
            usedColors[i] = reverseUsedColor.get(i);
        }
        return usedColors;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mDialog == null || !mDialog.isShowing()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.dialogBundle = mDialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        showDialog(myState.dialogBundle);
    }

    public String toString(ArrayList<Integer> arrList) {
        int n = arrList.size();
        if (n == 0)
            return "0";

        String str = "" ;
        for (int i = arrList.size()-1; i > 0; i--){
            int nums= arrList.get(i);
            str+= nums ;
        }

        return str;
    }


    private static class SavedState extends BaseSavedState {
        Bundle dialogBundle;

        public SavedState(Parcel source) {
            super(source);
            dialogBundle = source.readBundle();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
