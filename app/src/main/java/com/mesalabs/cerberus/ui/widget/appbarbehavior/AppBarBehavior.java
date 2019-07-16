package com.mesalabs.cerberus.ui.widget.appbarbehavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import java.lang.ref.WeakReference;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AbsSavedState;
import android.support.v4.math.MathUtils;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;

import com.mesalabs.cerberus.CerberusApp;
import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
 * Originally coded by Samsung. All rights reserved to their respective owners.
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

public class AppBarBehavior extends HeaderBehavior<AppBarLayout> {
    static final Interpolator SINE_OUT_80_INTERPOLATOR = new PathInterpolator(0.17f, 0.17f, 0.2f, 1.0f);

    private static final int MAX_OFFSET_ANIMATION_DURATION = 700; // ms
    private static final int INVALID_POSITION = -1;

    public static abstract class DragCallback {
        public abstract boolean canDrag(@NonNull AppBarLayout appBarLayout);
    }

    private int lastStartedType;
    float mDefaultHeight = CerberusApp.getAppContext().getResources().getDimension(R.dimen.sesl_action_bar_default_height);
    private float mDiffY_Touch;
    private boolean mIsCollapsed;
    private boolean mIsFlingScrollDown = false;
    private boolean mIsFlingScrollUp = false;
    private boolean mIsScrollHold = false;
    boolean mIsSetStaticDuration = false;
    private float mLastMotionY_Touch;
    private WeakReference<View> mLastNestedScrollingChildRef;
    private ValueAnimator mOffsetAnimator;
    int mOffsetDelta;
    private int mOffsetToChildIndexOnLayout = INVALID_POSITION;
    private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
    private float mOffsetToChildIndexOnLayoutPerc;
    private DragCallback mOnDragCallback;
    private boolean mToolisMouse;
    private int mTouchSlop = INVALID_POSITION;
    private float mVelocity = 0.0f;
    private float touchX;
    private float touchY;

    public AppBarBehavior() {}

    public AppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        switch (ev.getAction()) {
            case 0:
                touchX = ev.getX();
                touchY = ev.getY();
                mLastMotionY_Touch = touchY;
                mDiffY_Touch = 0.0f;
                break;
            case 1:
            case 3:
                if (Math.abs(mDiffY_Touch) <= 21.0f) {
                    touchX = 0.0f;
                    touchY = 0.0f;
                    mIsFlingScrollUp = false;
                    mIsFlingScrollDown = false;
                    mLastMotionY_Touch = 0.0f;
                } else if (mDiffY_Touch < 0.0f) {
                    mIsFlingScrollUp = true;
                    mIsFlingScrollDown = false;
                } else if (mDiffY_Touch > 0.0f) {
                    mIsFlingScrollUp = false;
                    mIsFlingScrollDown = true;
                }
                snapToChildIfNeeded(parent, child);
                break;
            case 2:
                if (ev == null || !MotionEventCompat.isFromSource(ev, 8194))
                    mToolisMouse = false;
                else
                    mToolisMouse = true;
                float y = ev.getY();
                if (y - mLastMotionY_Touch != 0.0f)
                    mDiffY_Touch = y - mLastMotionY_Touch;
                if (Math.abs(mDiffY_Touch) > ((float) mTouchSlop))
                    mLastMotionY_Touch = y;
                break;
        }
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        final boolean started = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && (boolean) Utils.genericInvokeMethod(child, "hasScrollableChildren") && parent.getHeight() - directTargetChild.getHeight() <= child.getHeight();

        if (started && mOffsetAnimator != null && directTargetChild.getTop() == child.getBottom()) {
            mOffsetAnimator.cancel();
        }

        if (((float) child.getBottom()) <= mDefaultHeight) {
            mIsCollapsed = true;
        } else {
            mIsCollapsed = false;
        }

        mLastNestedScrollingChildRef = null;

        lastStartedType = type;

        return started;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (dy != 0) {
            int min, max;
            if (dy < 0) {
                min = -child.getTotalScrollRange();
                max = min + (int) Utils.genericInvokeMethod(child, "getDownNestedPreScrollRange");

                mIsFlingScrollDown = true;
                mIsFlingScrollUp = false;
                if (((double) child.getBottom()) >= ((double) child.getHeight()) * 0.52d) {
                    mIsSetStaticDuration = true;
                }
                if (dy < -30) {
                    mIsFlingScrollDown = true;
                } else {
                    mVelocity = 0.0f;
                    mIsFlingScrollDown = false;
                }
            } else {
                min = -(int) Utils.genericInvokeMethod(child, "getUpNestedPreScrollRange");
                max = 0;

                mIsFlingScrollDown = false;
                mIsFlingScrollUp = true;
                if (((double) child.getBottom()) <= ((double) child.getHeight()) * 0.43d) {
                    mIsSetStaticDuration = true;
                }
                if (dy > 30) {
                    mIsFlingScrollUp = true;
                } else {
                    mVelocity = 0.0f;
                    mIsFlingScrollUp = false;
                }
                if (getTopAndBottomOffset() == min) {
                    mIsScrollHold = true;
                }
            }
            if (min != max) {
                consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
            }
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (isScrollHoldMode(child)) {
            if (dyUnconsumed >= 0 || mIsScrollHold) {
                ViewCompat.stopNestedScroll(target, 1);
                return;
            }
            scroll(coordinatorLayout, child, dyUnconsumed, -(int) Utils.genericInvokeMethod(child, "getDownNestedScrollRange"), 0);
            stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
        } else if (dyUnconsumed < 0) {
            scroll(coordinatorLayout, child, dyUnconsumed, -(int) Utils.genericInvokeMethod(child, "getDownNestedScrollRange"), 0);
            stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        if (getLastInterceptTouchEventEvent() == 3 || getLastInterceptTouchEventEvent() == 1 || getLastTouchEventEvent() == 3 || getLastTouchEventEvent() == 1) {
            snapToChildIfNeeded(coordinatorLayout, abl);
        }

        if ((lastStartedType == 0 || type == 1) && mIsScrollHold) {
            mIsScrollHold = false;
        }

        mLastNestedScrollingChildRef = new WeakReference<>(target);
    }

    private void stopNestedScrollIfNeeded(int dy, AppBarLayout child, View target, int type) {
        if (type == 1) {
            int currOffset = getTopAndBottomOffset();
            if ((dy < 0 && currOffset == 0) || (dy > 0 && currOffset == (-(int) Utils.genericInvokeMethod(child, "getDownNestedScrollRange")))) {
                ViewCompat.stopNestedScroll(target, 1);
            }
        }
    }

    private boolean isScrollHoldMode(AppBarLayout abl) {
        if (mToolisMouse) {
            return false;
        }
        int offsetChildIndex = getChildIndexOnOffset(abl, getTopBottomOffsetForScrollingSibling());
        if (offsetChildIndex < 0 || (((AppBarLayout.LayoutParams) abl.getChildAt(offsetChildIndex).getLayoutParams()).getScrollFlags() & 0x91) != 0x91) {
            return true;
        }
        return false;
    }

    private void animateOffsetTo(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final int offset, float velocity) {
        /* final */ int duration;

        if (Math.abs(mVelocity) <= 0.0f || Math.abs(mVelocity) > 3000.0f) {
            duration = 250;
        } else {
            duration = (int) (((double) (3000.0f - Math.abs(mVelocity))) * 0.4d);
        }
        if (duration <= 250) {
            duration = 250;
        }
        if (mIsSetStaticDuration) {
            duration = 250;
            mIsSetStaticDuration = false;
        }
        if (mVelocity < 2000.0f) {
            animateOffsetWithDuration(coordinatorLayout, child, offset, duration);
        }

        mVelocity = 0.0f;
    }

    private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final int offset, final int duration) {
        final int currentOffset = getTopBottomOffsetForScrollingSibling();
        if (currentOffset == offset) {
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
                mOffsetAnimator.cancel();
            }
            return;
        }

        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(SINE_OUT_80_INTERPOLATOR);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setHeaderTopBottomOffset(coordinatorLayout, child, (int) animation.getAnimatedValue());
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }

        mOffsetAnimator.setDuration(Math.min(duration, MAX_OFFSET_ANIMATION_DURATION));
        mOffsetAnimator.setIntValues(currentOffset, offset);
        mOffsetAnimator.start();
    }

    private int getChildIndexOnOffset(AppBarLayout abl, final int offset) {
        for (int i = 0, count = abl.getChildCount(); i < count; i++) {
            View child = abl.getChildAt(i);
            if (child.getTop() <= -offset && child.getBottom() >= -offset) {
                return i;
            }
        }
        return -1;
    }

    private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, AppBarLayout abl) {
        final int offset = getTopBottomOffsetForScrollingSibling();
        final int offsetChildIndex = getChildIndexOnOffset(abl, offset);
        View childView = coordinatorLayout.getChildAt(1);
        if (offsetChildIndex >= 0) {
            final View offsetChild = abl.getChildAt(offsetChildIndex);
            final AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) offsetChild.getLayoutParams();
            final int flags = lp.getScrollFlags();

            if ((flags & 0x90) != 0x90) {
                int snapTop = -offsetChild.getTop();
                int snapBottom = -offsetChild.getBottom();

                if (offsetChildIndex == abl.getChildCount() - 1) {
                    snapBottom += (int) Utils.genericInvokeMethod(abl, "getTopInset");
                }

                if (checkFlag(flags, 0x2)) {
                    snapBottom += ViewCompat.getMinimumHeight(offsetChild);
                } else if (checkFlag(flags, 0x2)) {
                    final int seam = snapBottom + ViewCompat.getMinimumHeight(offsetChild);
                    if (offset < seam) {
                        snapTop = seam;
                    } else {
                        snapBottom = seam;
                    }
                }

                int newOffset = mIsCollapsed ? ((double) offset) >= ((double) (snapBottom + snapTop)) * 0.52d ? snapTop : snapBottom : ((double) offset) < ((double) (snapBottom + snapTop)) * 0.43d ? snapBottom : snapTop;

                if (isScrollHoldMode(abl)) {
                    if (mIsFlingScrollUp) {
                        newOffset = snapBottom;
                        mIsFlingScrollUp = false;
                        mIsFlingScrollDown = false;
                    }
                    if (mIsFlingScrollDown && childView != null && ((float) childView.getTop()) > mDefaultHeight) {
                        newOffset = snapTop;
                        mIsFlingScrollDown = false;
                    }
                } else {
                    if (mIsFlingScrollUp) {
                        newOffset = snapBottom;
                        mIsFlingScrollUp = false;
                        mIsFlingScrollDown = false;
                    }
                    if (mIsFlingScrollDown && childView != null && ((float) childView.getTop()) > mDefaultHeight) {
                        newOffset = snapTop;
                        mIsFlingScrollDown = false;
                    }
                }

                animateOffsetTo(coordinatorLayout, abl, MathUtils.clamp(newOffset, -abl.getTotalScrollRange(), 0), 0);
            }
        }
    }

    private static boolean checkFlag(final int flags, final int check) {
        return (flags & check) == check;
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, AppBarLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.WRAP_CONTENT) {
            parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, AppBarLayout.MeasureSpec.makeMeasureSpec(0, AppBarLayout.MeasureSpec.UNSPECIFIED), heightUsed);
            return true;
        }

        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);

        final int pendingAction = (int) Utils.genericInvokeMethod(abl, "getPendingAction");
        if (mOffsetToChildIndexOnLayout >= 0 && (pendingAction & 0x8) == 0) {
            View child = abl.getChildAt(mOffsetToChildIndexOnLayout);
            int offset = -child.getBottom();
            if (mOffsetToChildIndexOnLayoutIsMinHeight) {
                offset += ViewCompat.getMinimumHeight(child) + (int) Utils.genericInvokeMethod(abl, "getTopInset");
            } else {
                offset += Math.round(child.getHeight() * mOffsetToChildIndexOnLayoutPerc);
            }
            setHeaderTopBottomOffset(parent, abl, offset);
        } else if (pendingAction != 0x0) {
            final boolean animate = (pendingAction & 0x4) != 0;
            if ((pendingAction & 0x2) != 0) {
                final int offset = -(int) Utils.genericInvokeMethod(abl, "getUpNestedPreScrollRange");
                if (animate) {
                    animateOffsetTo(parent, abl, offset, 0);
                } else {
                    setHeaderTopBottomOffset(parent, abl, offset);
                }
            } else if ((pendingAction & 0x1) != 0) {
                if (animate) {
                    animateOffsetTo(parent, abl, 0, 0);
                } else {
                    setHeaderTopBottomOffset(parent, abl, 0);
                }
            }
        }

        Utils.genericInvokeMethod(abl, "resetPendingAction");
        mOffsetToChildIndexOnLayout = INVALID_POSITION;

        setTopAndBottomOffset(MathUtils.clamp(getTopAndBottomOffset(), -abl.getTotalScrollRange(), 0));

        updateAppBarLayoutDrawableState(parent, abl, getTopAndBottomOffset(), 0, true);

        Utils.genericInvokeMethod(abl, "dispatchOffsetUpdates", getTopAndBottomOffset());

        return handled;
    }

    @Override
    boolean canDragView(AppBarLayout view) {
        if (mOnDragCallback != null) {
            return mOnDragCallback.canDrag(view);
        }

        if (mLastNestedScrollingChildRef != null) {
            final View scrollingView = mLastNestedScrollingChildRef.get();
            return scrollingView != null && scrollingView.isShown() && !scrollingView.canScrollVertically(-1);
        } else {
            return true;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, abl, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, float velocityX, float velocityY) {
        mVelocity = velocityY;
        if (velocityY < -300.0f) {
            mIsFlingScrollDown = true;
            mIsFlingScrollUp = false;
        } else if (velocityY > 300.0f) {
            mIsFlingScrollDown = false;
            mIsFlingScrollUp = true;
        } else {
            mVelocity = 0.0f;
            mIsFlingScrollDown = false;
            mIsFlingScrollUp = false;
            return true;
        }
        return super.onNestedPreFling(coordinatorLayout, abl, target, velocityX, velocityY);
    }

    @Override
    int getMaxDragOffset(AppBarLayout view) {
        return -(int) Utils.genericInvokeMethod(view, "getDownNestedScrollRange");
    }

    @Override
    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int newOffset, int minOffset, int maxOffset) {
        final int curOffset = getTopBottomOffsetForScrollingSibling();
        int consumed = 0;

        if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
            newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);
            if (curOffset != newOffset) {
                final int interpolatedOffset = (boolean) Utils.genericInvokeMethod(appBarLayout, "hasChildWithInterpolator") ? interpolateOffset(appBarLayout, newOffset) : newOffset;

                final boolean offsetChanged = setTopAndBottomOffset(interpolatedOffset);

                consumed = curOffset - newOffset;
                mOffsetDelta = newOffset - interpolatedOffset;

                if (!offsetChanged && (boolean) Utils.genericInvokeMethod(appBarLayout, "hasChildWithInterpolator")) {
                    coordinatorLayout.dispatchDependentViewsChanged(appBarLayout);
                }

                Utils.genericInvokeMethod(appBarLayout, "dispatchOffsetUpdates", getTopAndBottomOffset());

                updateAppBarLayoutDrawableState(coordinatorLayout, appBarLayout, newOffset, newOffset < curOffset ? -1 : 1, false);
            }
        } else {
            mOffsetDelta = 0;
        }

        return consumed;
    }

    private int interpolateOffset(AppBarLayout layout, final int offset) {
        final int absOffset = Math.abs(offset);

        for (int i = 0, z = layout.getChildCount(); i < z; i++) {
            final View child = layout.getChildAt(i);
            final AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) child.getLayoutParams();
            final Interpolator interpolator = childLp.getScrollInterpolator();

            if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
                if (interpolator != null) {
                    int childScrollableHeight = 0;
                    final int flags = childLp.getScrollFlags();
                    if ((flags & 0x1) != 0) {
                        childScrollableHeight += child.getHeight() + childLp.topMargin + childLp.bottomMargin;

                        if ((flags & 0x2) != 0) {
                            childScrollableHeight -= ViewCompat.getMinimumHeight(child);
                        }
                    }

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        childScrollableHeight -= (int) Utils.genericInvokeMethod(layout, "getTopInset");
                    }

                    if (childScrollableHeight > 0) {
                        final int offsetForView = absOffset - child.getTop();
                        final int interpolatedDiff = Math.round(childScrollableHeight * interpolator.getInterpolation(offsetForView / (float) childScrollableHeight));

                        return Integer.signum(offset) * (child.getTop() + interpolatedDiff);
                    }
                }
                break;
            }
        }

        return offset;
    }

    private void updateAppBarLayoutDrawableState(final CoordinatorLayout parent, final AppBarLayout layout, final int offset, final int direction, final boolean forceJump) {
        final View child = getAppBarChildOnOffset(layout, offset);
        if (child != null) {
            final AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) child.getLayoutParams();
            final int flags = childLp.getScrollFlags();
            boolean lifted = false;

            if ((flags & 0x1) != 0) {
                final int minHeight = ViewCompat.getMinimumHeight(child);
                int topInset = (int) Utils.genericInvokeMethod(layout, "getTopInset");

                if (direction > 0 && (flags & 0x12) != 0) {
                    lifted = -offset >= child.getBottom() - minHeight - topInset;
                } else if ((flags & 0x2) != 0) {
                    lifted = -offset >= child.getBottom() - minHeight - topInset;
                }
            }

            if (layout.isLiftOnScroll()) {
                View scrollingChild = findFirstScrollingChild(parent);
                if (scrollingChild != null) {
                    lifted = scrollingChild.getScrollY() > 0;
                }
            }

            final boolean changed = (boolean) Utils.genericInvokeMethod(layout, "setLiftedState", lifted);

            if (forceJump || changed && shouldJumpElevationState(parent, layout)) {
                layout.jumpDrawablesToCurrentState();
            }
        }
    }

    private boolean shouldJumpElevationState(CoordinatorLayout parent, AppBarLayout layout) {
        return false;
    }

    private static View getAppBarChildOnOffset(final AppBarLayout layout, final int offset) {
        final int absOffset = Math.abs(offset);
        for (int i = 0, z = layout.getChildCount(); i < z; i++) {
            final View child = layout.getChildAt(i);
            if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    @Override
    int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset() + mOffsetDelta;
    }

    @Nullable
    private View findFirstScrollingChild(CoordinatorLayout parent) {
        int i = 0;

        for(int z = parent.getChildCount(); i < z; ++i) {
            View child = parent.getChildAt(i);
            if (child instanceof NestedScrollingChild) {
                return child;
            }
        }

        return null;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, AppBarLayout abl) {
        final Parcelable superState = super.onSaveInstanceState(parent, abl);
        final int offset = getTopAndBottomOffset();

        for (int i = 0, count = abl.getChildCount(); i < count; i++) {
            View child = abl.getChildAt(i);
            final int visBottom = child.getBottom() + offset;

            if (child.getTop() + offset <= 0 && visBottom >= 0) {
                final SavedState ss = new SavedState(superState);

                ss.firstVisibleChildIndex = i;
                ss.firstVisibleChildAtMinimumHeight = visBottom == (ViewCompat.getMinimumHeight(child) + (int) Utils.genericInvokeMethod(abl, "getTopInset"));
                ss.firstVisibleChildPercentageShown = visBottom / (float) child.getHeight();
                return ss;
            }
        }

        return superState;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, AppBarLayout appBarLayout, Parcelable state) {
        if (state instanceof SavedState) {
            final SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(parent, appBarLayout, ss.getSuperState());
            mOffsetToChildIndexOnLayout = ss.firstVisibleChildIndex;
            mOffsetToChildIndexOnLayoutPerc = ss.firstVisibleChildPercentageShown;
            mOffsetToChildIndexOnLayoutIsMinHeight = ss.firstVisibleChildAtMinimumHeight;
        } else {
            super.onRestoreInstanceState(parent, appBarLayout, state);
            mOffsetToChildIndexOnLayout = INVALID_POSITION;
        }
    }

    protected static class SavedState extends AbsSavedState {
        int firstVisibleChildIndex;
        float firstVisibleChildPercentageShown;
        boolean firstVisibleChildAtMinimumHeight;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            firstVisibleChildIndex = source.readInt();
            firstVisibleChildPercentageShown = source.readFloat();
            firstVisibleChildAtMinimumHeight = source.readByte() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(firstVisibleChildIndex);
            dest.writeFloat(firstVisibleChildPercentageShown);
            dest.writeByte((byte) (firstVisibleChildAtMinimumHeight ? 1 : 0));
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}