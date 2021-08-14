package com.mesalabs.cerberus.ui.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.mesalabs.ten.update.R;
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

public class TipsCardViewPreference extends SeslPreference {
    private TipsCardListener mTipsCardListener;
    private int mTextColor;

    public TipsCardViewPreference(Context context) {
        this(context, null);
    }

    public TipsCardViewPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsCardViewPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.mesa_preference_tipcardviewpref_layout);

        mTextColor = ContextCompat.getColor(context, R.color.sesl_primary_text_color_dark);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);

        ((TextView) preferenceViewHolder.itemView.findViewById(android.R.id.title)).setTextColor(mTextColor);
        ((TextView) preferenceViewHolder.itemView.findViewById(android.R.id.summary)).setTextColor(mTextColor);

        preferenceViewHolder.itemView.findViewById(R.id.mesa_cancelbutton_tipscardviewpref).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                mTipsCardListener.onCancelClicked(view);
            }
        });
    }

    public void setTipsCardListener(TipsCardListener listener) {
        mTipsCardListener = listener;
    }

    public interface TipsCardListener {
        void onCancelClicked(View view);
    }

}