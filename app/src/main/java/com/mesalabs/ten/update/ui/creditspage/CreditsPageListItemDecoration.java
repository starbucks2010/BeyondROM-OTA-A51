package com.mesalabs.ten.update.ui.creditspage;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.mesalabs.cerberus.utils.CerberusException;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.activity.aboutpage.CreditsActivity;
import com.samsung.android.ui.recyclerview.widget.SeslLinearLayoutManager;
import com.samsung.android.ui.recyclerview.widget.SeslRecyclerView;
import com.samsung.android.ui.util.SeslRoundedCorner;

/*
 * 십 Update
 *
 * Coded by BlackMesa123 @2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class CreditsPageListItemDecoration extends SeslRecyclerView.ItemDecoration {
    private CreditsActivity mListActivity;
    private SeslRoundedCorner mSeslListRoundedCorner;
    private SeslRoundedCorner mSeslRoundedCornerBottom;
    private Drawable mDivider;
    private int mDividerHeight;

    public CreditsPageListItemDecoration(CreditsActivity activity) {
        mListActivity = activity;

        if (mListActivity == null) {
            throw new CerberusException("Activity is null!!!");
        }

        mSeslListRoundedCorner = new SeslRoundedCorner(mListActivity.getContext(), true);
        mSeslListRoundedCorner.setRoundedCornerColor(15 /* all */,
                mListActivity.getResources().getColor(Utils.isNightMode(mListActivity.getContext()) ?
                        R.color.sesl_round_and_bgcolor_dark :
                        R.color.sesl_round_and_bgcolor_light,
                        null));
        mSeslListRoundedCorner.setRoundedCorners(3 /* top_left|top_right */);

        mSeslRoundedCornerBottom = new SeslRoundedCorner(mListActivity.getContext(), true);
        mSeslRoundedCornerBottom.setRoundedCornerColor(15 /* all */,
                mListActivity.getResources().getColor(Utils.isNightMode(mListActivity.getContext()) ?
                                R.color.sesl_round_and_bgcolor_dark :
                                R.color.sesl_round_and_bgcolor_light,
                        null));
        mSeslRoundedCornerBottom.setRoundedCorners(12 /* bottom_left|bottom_right */);
    }

    @Override
    public void seslOnDispatchDraw(Canvas canvas, SeslRecyclerView recyclerView, SeslRecyclerView.State state) {
        super.seslOnDispatchDraw(canvas, recyclerView, state);

        int childCount = getChildCount(recyclerView);
        int width = recyclerView.getWidth();

        // draw divider for each item
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int y = ((int) childAt.getY()) + childAt.getHeight();

            if (mDivider != null) {
                mDivider.setBounds(0, y, width, mDividerHeight + y);
                mDivider.draw(canvas);
            }
        }

        mSeslListRoundedCorner.drawRoundedCorner(canvas);
        mSeslRoundedCornerBottom.drawRoundedCorner(recyclerView.getChildAt(childCount), canvas);
    }

    private boolean canScrollUp(SeslRecyclerView recyclerView) {
        SeslRecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof SeslLinearLayoutManager)) {
            return false;
        }

        boolean isntFirstItem = ((SeslLinearLayoutManager) layoutManager).findFirstVisibleItemPosition() > 0;
        View childAt = recyclerView.getChildAt(0);

        if (isntFirstItem || childAt == null) {
            return isntFirstItem;
        }
        if (childAt.getTop() < recyclerView.getPaddingTop()) {
            return true;
        } else {
            return false;
        }
    }

    private int getChildCount(SeslRecyclerView recyclerView) {
        if (!((CreditsPageListViewHolder) recyclerView.findViewHolderForLayoutPosition(recyclerView.findLastVisibleItemPosition())).getIsItem()) {
            return recyclerView.getChildCount() - 2;
        } else {
            return recyclerView.getChildCount() - 1;
        }
    }

    public void setDivider(Drawable d) {
        mDivider = d;
        mDividerHeight = d.getIntrinsicHeight();
        mListActivity.getListView().invalidateItemDecorations();
    }

}
