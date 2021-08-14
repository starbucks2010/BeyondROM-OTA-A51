package com.mesalabs.cerberus.ui.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.res.TypedArrayUtils;

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

public class LayoutPreference extends SeslPreference {
    private boolean mAllowDividerAbove;
    private boolean mAllowDividerBelow;
    private final View.OnClickListener mClickListener;
    private int mDescendantFocusability;
    private boolean mIsRelativeLinkView;
    View mRootView;

    public LayoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mClickListener = new View.OnClickListener() {
            public final void onClick(View view) {
                performClick(view);
            }
        };
        mIsRelativeLinkView = false;
        mDescendantFocusability = -1;

        init(context, attrs, 0);
    }

    public LayoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mClickListener = new View.OnClickListener() {
            public final void onClick(View view) {
                performClick(view);
            }
        };
        mIsRelativeLinkView = false;
        mDescendantFocusability = -1;

        init(context, attrs, defStyleAttr);
    }

    public LayoutPreference(Context context, int resource) {
        this(context, LayoutInflater.from(context).inflate(resource, null, false));
    }

    public LayoutPreference(Context context, View view, boolean isRelativeLinkView) {
        super(context, null);

        mClickListener = new View.OnClickListener() {
            public final void onClick(View view) {
                performClick(view);
            }
        };
        mIsRelativeLinkView = false;
        mDescendantFocusability = -1;

        setView(view);
        mIsRelativeLinkView = isRelativeLinkView;
    }

    public LayoutPreference(Context context, View view, int descendantFocusability) {
        super(context, null);

        mClickListener = new View.OnClickListener() {
            public final void onClick(View view) {
                performClick(view);
            }
        };
        mIsRelativeLinkView = false;
        mDescendantFocusability = -1;

        setView(view);
        mDescendantFocusability = descendantFocusability;
    }

    public LayoutPreference(Context context, View view) {
        super(context, null);

        mClickListener = new View.OnClickListener() {
            public final void onClick(View view) {
                performClick(view);
            }
        };

        mIsRelativeLinkView = false;
        mDescendantFocusability = -1;
        setView(view);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslPreference);
        mAllowDividerAbove = TypedArrayUtils.getBoolean(a, R.styleable.SeslPreference_allowDividerAbove, R.styleable.SeslPreference_allowDividerAbove, false);
        mAllowDividerBelow = TypedArrayUtils.getBoolean(a, R.styleable.SeslPreference_allowDividerBelow, R.styleable.SeslPreference_allowDividerBelow, false);
        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.SeslPreference, defStyleAttr, 0);
        int layoutResource = a.getResourceId(R.styleable.SeslPreference_android_layout, 0);

        if (layoutResource == 0) {
            throw new IllegalArgumentException("LayoutPreference requires a layout to be defined");
        }

        a.recycle();
        setView(LayoutInflater.from(getContext()).inflate(layoutResource, null, false));
    }

    private void setView(View view) {
        setLayoutResource(R.layout.mesa_preference_layoutpref_frame_layout);

        ViewGroup allDetails = view.findViewById(R.id.mesa_alldetails_layoutpref);
        if (allDetails != null) {
            forceCustomPadding(allDetails, true);
        }

        mRootView = view;
        setShouldDisableView(false);
    }

    private void forceCustomPadding(View view, boolean additive) {
        Resources res = view.getResources();
        int paddingSide = res.getDimensionPixelSize(R.dimen.sesl_preference_item_padding_vertical);
        view.setPaddingRelative((additive ? view.getPaddingStart() : 0) + paddingSide, 0, (additive ? view.getPaddingEnd() : 0) + paddingSide, res.getDimensionPixelSize(R.dimen.mesa_preference_fragment_padding_bottom));
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        if (mIsRelativeLinkView) {
            holder.itemView.setOnClickListener(null);
            holder.itemView.setFocusable(false);
            holder.itemView.setClickable(false);
        } else {
            holder.itemView.setOnClickListener(mClickListener);
            boolean selectable = isSelectable();
            holder.itemView.setFocusable(selectable);
            holder.itemView.setClickable(selectable);
            holder.setDividerAllowedAbove(mAllowDividerAbove);
            holder.setDividerAllowedBelow(mAllowDividerBelow);
        }

        FrameLayout layout = (FrameLayout) holder.itemView;
        layout.removeAllViews();

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        layout.addView(mRootView);
        if (mDescendantFocusability != -1) {
            layout.setDescendantFocusability(mDescendantFocusability);
        }
    }

    public <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    public void setDescendantFocusability(int descendantFocusability) {
        mDescendantFocusability = descendantFocusability;
    }

    public boolean isRelativeLinkView() {
        return mIsRelativeLinkView;
    }
}
