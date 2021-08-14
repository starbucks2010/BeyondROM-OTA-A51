package com.mesalabs.ten.update.ui.creditspage;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import com.mesalabs.cerberus.ui.callback.OnSingleClickListener;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.activity.aboutpage.CreditsActivity;
import com.samsung.android.ui.recyclerview.widget.SeslRecyclerView;

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

public class CreditsPageListAdapter extends SeslRecyclerView.Adapter<CreditsPageListViewHolder> {
    private CreditsActivity mActivity;
    private List<CreditsListViewModel> mModel;

    public CreditsPageListAdapter(CreditsActivity activity, List<CreditsListViewModel> model) {
        mActivity = activity;
        mModel = model;
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemViewType(final int position) {
        return position + 1 == getItemCount() ? 1 : 0;
    }

    @Override
    public CreditsPageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = viewType == 1 ? LayoutInflater.from(parent.getContext()).inflate(R.layout.mesa_view_bottom_spacing_layout, parent, false) : LayoutInflater.from(parent.getContext()).inflate(R.layout.mesa_view_creditspage_item_layout, parent, false);
        return new CreditsPageListViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(CreditsPageListViewHolder holder, final int position) {
        holder.onBindViewHolder(mModel.get(position));
        if (holder.getIsItem()) {
            holder.setItemOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    String webLink = mModel.get(position).getLibWebLink();

                    if (!webLink.isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(webLink));
                        mActivity.startActivity(intent);
                    }
                }
            });
        }
    }

}
