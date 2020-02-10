package com.samsung.android.ui.recyclerview.widget;

import androidx.annotation.VisibleForTesting;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pools;

import static com.samsung.android.ui.recyclerview.widget.SeslRecyclerView.ItemAnimator.ItemHolderInfo;
import static com.samsung.android.ui.recyclerview.widget.SeslRecyclerView.ViewHolder;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_APPEAR;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_APPEAR_AND_DISAPPEAR;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_APPEAR_PRE_AND_POST;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_DISAPPEARED;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_POST;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_PRE;
import static com.samsung.android.ui.recyclerview.widget.SeslViewInfoStore.InfoRecord.FLAG_PRE_AND_POST;

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

class SeslViewInfoStore {
    private static final boolean DEBUG = false;
    @VisibleForTesting
    final ArrayMap<ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap<>();
    @VisibleForTesting
    final LongSparseArray<ViewHolder> mOldChangedHolders = new LongSparseArray<>();

    void clear() {
        mLayoutHolderMap.clear();
        mOldChangedHolders.clear();
    }

    void addToPreLayout(ViewHolder holder, ItemHolderInfo info) {
        InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.preInfo = info;
        record.flags |= FLAG_PRE;
    }

    boolean isDisappearing(ViewHolder holder) {
        final InfoRecord record = mLayoutHolderMap.get(holder);
        return record != null && ((record.flags & FLAG_DISAPPEARED) != 0);
    }

    ItemHolderInfo popFromPreLayout(ViewHolder vh) {
        return popFromLayoutStep(vh, FLAG_PRE);
    }

    ItemHolderInfo popFromPostLayout(ViewHolder vh) {
        return popFromLayoutStep(vh, FLAG_POST);
    }

    private ItemHolderInfo popFromLayoutStep(ViewHolder vh, int flag) {
        int index = mLayoutHolderMap.indexOfKey(vh);
        if (index < 0) {
            return null;
        }
        final InfoRecord record = mLayoutHolderMap.valueAt(index);
        if (record != null && (record.flags & flag) != 0) {
            record.flags &= ~flag;
            final ItemHolderInfo info;
            if (flag == FLAG_PRE) {
                info = record.preInfo;
            } else if (flag == FLAG_POST) {
                info = record.postInfo;
            } else {
                throw new IllegalArgumentException("Must provide flag PRE or POST");
            }
            if ((record.flags & (FLAG_PRE | FLAG_POST)) == 0) {
                mLayoutHolderMap.removeAt(index);
                InfoRecord.recycle(record);
            }
            return info;
        }
        return null;
    }

    void addToOldChangeHolders(long key, ViewHolder holder) {
        mOldChangedHolders.put(key, holder);
    }

    void addToAppearedInPreLayoutHolders(ViewHolder holder, ItemHolderInfo info) {
        InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.flags |= FLAG_APPEAR;
        record.preInfo = info;
    }

    boolean isInPreLayout(ViewHolder viewHolder) {
        final InfoRecord record = mLayoutHolderMap.get(viewHolder);
        return record != null && (record.flags & FLAG_PRE) != 0;
    }

    ViewHolder getFromOldChangeHolders(long key) {
        return mOldChangedHolders.get(key);
    }

    void addToPostLayout(ViewHolder holder, ItemHolderInfo info) {
        InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.postInfo = info;
        record.flags |= FLAG_POST;
    }

    void addToDisappearedInLayout(ViewHolder holder) {
        InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            mLayoutHolderMap.put(holder, record);
        }
        record.flags |= FLAG_DISAPPEARED;
    }

    void removeFromDisappearedInLayout(ViewHolder holder) {
        InfoRecord record = mLayoutHolderMap.get(holder);
        if (record == null) {
            return;
        }
        record.flags &= ~FLAG_DISAPPEARED;
    }

    void process(ProcessCallback callback) {
        for (int index = mLayoutHolderMap.size() - 1; index >= 0; index--) {
            final ViewHolder viewHolder = mLayoutHolderMap.keyAt(index);
            final InfoRecord record = mLayoutHolderMap.removeAt(index);
            if ((record.flags & FLAG_APPEAR_AND_DISAPPEAR) == FLAG_APPEAR_AND_DISAPPEAR) {
                callback.unused(viewHolder);
            } else if ((record.flags & FLAG_DISAPPEARED) != 0) {
                if (record.preInfo == null) {
                    callback.unused(viewHolder);
                } else {
                    callback.processDisappeared(viewHolder, record.preInfo, record.postInfo);
                }
            } else if ((record.flags & FLAG_APPEAR_PRE_AND_POST) == FLAG_APPEAR_PRE_AND_POST) {
                callback.processAppeared(viewHolder, record.preInfo, record.postInfo);
            } else if ((record.flags & FLAG_PRE_AND_POST) == FLAG_PRE_AND_POST) {
                callback.processPersistent(viewHolder, record.preInfo, record.postInfo);
            } else if ((record.flags & FLAG_PRE) != 0) {
                callback.processDisappeared(viewHolder, record.preInfo, null);
            } else if ((record.flags & FLAG_POST) != 0) {
                callback.processAppeared(viewHolder, record.preInfo, record.postInfo);
            } else if ((record.flags & FLAG_APPEAR) != 0) {
            } else if (DEBUG) {
                throw new IllegalStateException("record without any reasonable flag combination:/");
            }
            InfoRecord.recycle(record);
        }
    }

    void removeViewHolder(ViewHolder holder) {
        for (int i = mOldChangedHolders.size() - 1; i >= 0; i--) {
            if (holder == mOldChangedHolders.valueAt(i)) {
                mOldChangedHolders.removeAt(i);
                break;
            }
        }
        final InfoRecord info = mLayoutHolderMap.remove(holder);
        if (info != null) {
            InfoRecord.recycle(info);
        }
    }

    void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }

    interface ProcessCallback {
        void processDisappeared(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo);
        void processAppeared(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo);
        void processPersistent(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo);
        void unused(ViewHolder holder);
    }

    static class InfoRecord {
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_APPEAR = 1 << 1;
        static final int FLAG_PRE = 1 << 2;
        static final int FLAG_POST = 1 << 3;
        static final int FLAG_APPEAR_AND_DISAPPEAR = FLAG_APPEAR | FLAG_DISAPPEARED;
        static final int FLAG_PRE_AND_POST = FLAG_PRE | FLAG_POST;
        static final int FLAG_APPEAR_PRE_AND_POST = FLAG_APPEAR | FLAG_PRE | FLAG_POST;
        int flags;
        ItemHolderInfo preInfo;
        ItemHolderInfo postInfo;
        static Pools.Pool<InfoRecord> sPool = new Pools.SimplePool<>(20);

        private InfoRecord() { }

        static InfoRecord obtain() {
            InfoRecord record = sPool.acquire();
            return record == null ? new InfoRecord() : record;
        }

        static void recycle(InfoRecord record) {
            record.flags = 0;
            record.preInfo = null;
            record.postInfo = null;
            sPool.release(record);
        }

        static void drainCache() {
            while (sPool.acquire() != null);
        }
    }
}
