package com.samsung.android.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;
import com.samsung.android.ui.widget.SeslSeekBar;

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

public class SeslSeekBarPreference extends SeslPreference {
    public boolean mAdjustable;
    public int mMax;
    public int mMin;
    public SeslSeekBar mSeekBar;
    public SeslSeekBar.OnSeekBarChangeListener mSeekBarChangeListener;
    public int mSeekBarIncrement;
    public View.OnKeyListener mSeekBarKeyListener;
    public int mSeekBarValue;
    public boolean mShowSeekBarValue;
    public boolean mTrackingTouch;

    public SeslSeekBarPreference(Context var1, AttributeSet var2) {
        this(var1, var2, R.attr.seekBarPreferenceStyle);
    }

    public SeslSeekBarPreference(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }

    public SeslSeekBarPreference(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.mSeekBarChangeListener = new SeslSeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeslSeekBar var1, int var2, boolean var3) {
                if (var3) {
                    SeslSeekBarPreference var4 = SeslSeekBarPreference.this;
                    if (!var4.mTrackingTouch) {
                        try {
                            var4.syncValueInternal(var1);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }

            }

            public void onStartTrackingTouch(SeslSeekBar var1) {
                SeslSeekBarPreference.this.mTrackingTouch = true;
            }

            public void onStopTrackingTouch(SeslSeekBar var1) {
                SeslSeekBarPreference.this.mTrackingTouch = false;
                int var2 = var1.getProgress();
                SeslSeekBarPreference var3 = SeslSeekBarPreference.this;
                if (var2 + var3.mMin != var3.mSeekBarValue) {
                    try {
                        var3.syncValueInternal(var1);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }

            }
        };
        this.mSeekBarKeyListener = new View.OnKeyListener() {
            public boolean onKey(View var1, int var2, KeyEvent var3) {
                if (var3.getAction() != 0) {
                    return false;
                } else if (!SeslSeekBarPreference.this.mAdjustable && (var2 == 21 || var2 == 22)) {
                    return false;
                } else if (var2 != 23 && var2 != 66) {
                    if (SeslSeekBarPreference.this.mSeekBar == null) {
                        LogUtils.e("SeslSeekBarPreference", "SeekBar view is null and hence cannot be adjusted.");
                        return false;
                    } else {
                        return SeslSeekBarPreference.this.mSeekBar.onKeyDown(var2, var3);
                    }
                } else {
                    return false;
                }
            }
        };
        TypedArray var5 = var1.obtainStyledAttributes(var2, R.styleable.SeslSeekBarPreference, var3, var4);
        this.mMin = var5.getInt(R.styleable.SeslSeekBarPreference_min, 0);
        this.setMax(var5.getInt(R.styleable.SeslSeekBarPreference_android_max, 100));
        this.setSeekBarIncrement(var5.getInt(R.styleable.SeslSeekBarPreference_seekBarIncrement, 0));
        this.mAdjustable = var5.getBoolean(R.styleable.SeslSeekBarPreference_adjustable, true);
        this.mShowSeekBarValue = var5.getBoolean(R.styleable.SeslSeekBarPreference_showSeekBarValue, true);
        var5.recycle();
    }

    public void onBindViewHolder(PreferenceViewHolder var1) {
        super.onBindViewHolder(var1);
        var1.itemView.setOnKeyListener(this.mSeekBarKeyListener);
        this.mSeekBar = (SeslSeekBar)var1.findViewById(R.id.seekbar);
        SeslSeekBar var3 = this.mSeekBar;
        if (var3 == null) {
            LogUtils.e("SeslSeekBarPreference", "SeekBar view is null in onBindViewHolder.");
        } else {
            var3.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
            try {
                this.mSeekBar.setMax(this.mMax - this.mMin);
                int var2 = this.mSeekBarIncrement;
                if (var2 != 0) {
                    this.mSeekBar.setKeyProgressIncrement(var2);
                } else {
                    this.mSeekBarIncrement = this.mSeekBar.getKeyProgressIncrement();
                }

                this.mSeekBar.setProgress(this.mSeekBarValue - this.mMin);
                this.mSeekBar.setEnabled(this.isEnabled());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public Object onGetDefaultValue(TypedArray var1, int var2) {
        return var1.getInt(var2, 0);
    }

    public void onRestoreInstanceState(Parcelable var1) {
        if (!var1.getClass().equals(SeslSeekBarPreference.SavedState.class)) {
            super.onRestoreInstanceState(var1);
        } else {
            SeslSeekBarPreference.SavedState var2 = (SeslSeekBarPreference.SavedState)var1;
            super.onRestoreInstanceState(var2.getSuperState());
            this.mSeekBarValue = var2.mSeekBarValue;
            this.mMin = var2.mMin;
            this.mMax = var2.mMax;
            this.notifyChanged();
        }
    }

    public Parcelable onSaveInstanceState() {
        Parcelable var1 = super.onSaveInstanceState();
        if (this.isPersistent()) {
            return var1;
        } else {
            SeslSeekBarPreference.SavedState var2 = new SeslSeekBarPreference.SavedState(var1);
            var2.mSeekBarValue = this.mSeekBarValue;
            var2.mMin = this.mMin;
            var2.mMax = this.mMax;
            return var2;
        }
    }

    public void onSetInitialValue(Object var1) {
        Object var2 = var1;
        if (var1 == null) {
            var2 = 0;
        }

        this.setValue(this.getPersistedInt((Integer)var2));
    }

    public final void setMax(int var1) {
        int var2 = this.mMin;
        int var3 = var1;
        if (var1 < var2) {
            var3 = var2;
        }

        if (var3 != this.mMax) {
            this.mMax = var3;
            this.notifyChanged();
        }

    }

    public final void setSeekBarIncrement(int var1) {
        if (var1 != this.mSeekBarIncrement) {
            this.mSeekBarIncrement = Math.min(this.mMax - this.mMin, Math.abs(var1));
            this.notifyChanged();
        }

    }

    public void setValue(int var1) {
        this.setValueInternal(var1, true);
    }

    public final void setValueInternal(int var1, boolean var2) {
        int var3 = this.mMin;
        int var4 = var1;
        if (var1 < var3) {
            var4 = var3;
        }

        var3 = this.mMax;
        var1 = var4;
        if (var4 > var3) {
            var1 = var3;
        }

        if (var1 != this.mSeekBarValue) {
            this.mSeekBarValue = var1;
            this.persistInt(var1);
            if (var2) {
                this.notifyChanged();
            }
        }

    }

    public final void syncValueInternal(SeslSeekBar var1) throws Throwable {
        int var2 = this.mMin + var1.getProgress();
        if (var2 != this.mSeekBarValue) {
            if (this.callChangeListener(var2)) {
                this.setValueInternal(var2, false);
            } else {
                var1.setProgress(this.mSeekBarValue - this.mMin);
            }
        }

    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SeslSeekBarPreference.SavedState> CREATOR = new Creator<SeslSeekBarPreference.SavedState>() {
            public SeslSeekBarPreference.SavedState createFromParcel(Parcel var1) {
                return new SeslSeekBarPreference.SavedState(var1);
            }

            public SeslSeekBarPreference.SavedState[] newArray(int var1) {
                return new SeslSeekBarPreference.SavedState[var1];
            }
        };
        public int mMax;
        public int mMin;
        public int mSeekBarValue;

        public SavedState(Parcel var1) {
            super(var1);
            this.mSeekBarValue = var1.readInt();
            this.mMin = var1.readInt();
            this.mMax = var1.readInt();
        }

        public SavedState(Parcelable var1) {
            super(var1);
        }

        public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.mSeekBarValue);
            var1.writeInt(this.mMin);
            var1.writeInt(this.mMax);
        }
    }
}
