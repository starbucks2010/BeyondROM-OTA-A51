package com.samsung.android.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.FocusFinder;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.core.view.ViewCompat.TYPE_NON_TOUCH;
import static androidx.core.view.ViewCompat.TYPE_TOUCH;
import androidx.annotation.CallSuper;
import androidx.core.os.TraceCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;
import com.samsung.android.ui.util.SeslSubheaderRoundedCorner;
import com.samsung.android.ui.widget.SeslRecyclerView.ItemAnimator.ItemHolderInfo;

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

public class SeslRecyclerView extends ViewGroup implements NestedScrollingChild2, ScrollingView {
    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC = Build.VERSION.SDK_INT >= 23;
    private static final boolean ALLOW_THREAD_GAP_WORK = Build.VERSION.SDK_INT >= 21;
    private static final int[] CLIP_TO_PADDING_ATTR = {android.R.attr.clipToPadding};
    static final boolean DEBUG = false;
    static final int DEFAULT_ORIENTATION = 1;
    static final boolean DISPATCH_TEMP_DETACH = false;
    private static final int FOCUS_MOVE_DOWN = 1;
    private static final int FOCUS_MOVE_FULL_DOWN = 3;
    private static final int FOCUS_MOVE_FULL_UP = 2;
    private static final int FOCUS_MOVE_UP = 0;
    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION = Build.VERSION.SDK_INT <= 15;
    static final boolean FORCE_INVALIDATE_DISPLAY_LIST = (Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20);
    static final long FOREVER_NS = Long.MAX_VALUE;
    private static final int GTP_STATE_NONE = 0;
    private static final int GTP_STATE_PRESSED = 2;
    private static final int GTP_STATE_SHOWN = 1;
    public static final int HORIZONTAL = 0;
    private static final int HOVERSCROLL_DOWN = 2;
    private static final int HOVERSCROLL_HEIGHT_BOTTOM_DP = 25;
    private static final int HOVERSCROLL_HEIGHT_TOP_DP = 25;
    private static final int HOVERSCROLL_UP = 1;
    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD = Build.VERSION.SDK_INT <= 15;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class, int.class, int.class};
    static final int MAX_SCROLL_DURATION = 2000;
    private static final int MOTION_EVENT_ACTION_PEN_DOWN = 211;
    private static final int MOTION_EVENT_ACTION_PEN_MOVE = 213;
    private static final int MOTION_EVENT_ACTION_PEN_UP = 212;
    private static final int MSG_HOVERSCROLL_MOVE = 0;
    private static final int[] NESTED_SCROLLING_ATTRS = {android.R.attr.nestedScrollingEnabled};
    public static final long NO_ID = -1;
    public static final int NO_POSITION = -1;
    static final boolean POST_UPDATES_ON_ANIMATION = Build.VERSION.SDK_INT >= 16;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static final String TAG = "SeslRecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    static final String TRACE_PREFETCH_TAG = "RV Prefetch";
    static final String TRACE_SCROLL_TAG = "RV Scroll";
    static final boolean VERBOSE_TRACING = false;
    public static final int VERTICAL = 1;
    private int GO_TO_TOP_HIDE = 2500;
    private int HOVERSCROLL_DELAY = 0;
    private float HOVERSCROLL_SPEED = 10.0f;
    SeslRecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    Adapter mAdapter;
    SeslAdapterHelper mAdapterHelper;
    boolean mAdapterUpdateDuringMeasure;
    private boolean mAlwaysDisableHoverHighlight = false;
    int mAnimatedBlackTop = -1;
    int mBlackTop = -1;
    private SeslEdgeEffect mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    public SeslChildHelper mChildHelper;
    boolean mClipToPadding;
    private View mCloseChildByBottom = null;
    private View mCloseChildByTop = null;
    private int mCloseChildPositionByBottom = -1;
    private int mCloseChildPositionByTop = -1;
    private Context mContext;
    boolean mDataSetHasChangedAfterLayout = false;
    boolean mDispatchItemsChangedEvent = false;
    private int mDispatchScrollCounter = 0;
    private int mDistanceFromCloseChildBottom = 0;
    private int mDistanceFromCloseChildTop = 0;
    boolean mDrawLastItemOutlineStoke = false;
    boolean mDrawLastOutLineStroke = true;
    boolean mDrawOutlineStroke = true;
    boolean mDrawRect = false;
    boolean mDrawReverse = false;
    boolean mDrawWhiteTheme = true;
    private int mEatenAccessibilityChangeFlags;
    boolean mEnableFastScroller;
    private boolean mEnableGoToTop = false;
    private int mExtraPaddingInBottomHoverArea = 0;
    private int mExtraPaddingInTopHoverArea = 0;
    private SeslRecyclerViewFastScroller mFastScroller;
    private boolean mFastScrollerEnabled = false;
    private SeslFastScrollerEventListener mFastScrollerEventListener;
    boolean mFirstLayoutComplete;
    SeslGapWorker mGapWorker;
    private int mGoToTopBottomPadding;
    private ValueAnimator mGoToTopFadeInAnimator;
    private ValueAnimator mGoToTopFadeOutAnimator;
    private boolean mGoToTopFadeOutStart = false;
    private Drawable mGoToTopImage;
    private Drawable mGoToTopImageLight;
    private int mGoToTopLastState = 0;
    private boolean mGoToTopMoved = false;
    private Rect mGoToTopRect = new Rect();
    private int mGoToTopSize;
    private int mGoToTopState = 0;
    private boolean mGoToToping = false;
    boolean mHasFixedSize;
    private boolean mHasNestedScrollRange = false;
    public boolean mHoverAreaEnter = false;
    private int mHoverBottomAreaHeight = 0;
    private long mHoverRecognitionCurrentTime = 0;
    private long mHoverRecognitionDurationTime = 0;
    private long mHoverRecognitionStartTime = 0;
    private int mHoverScrollDirection = -1;
    private boolean mHoverScrollEnable = true;
    private int mHoverScrollSpeed = 0;
    private long mHoverScrollStartTime = 0;
    private boolean mHoverScrollStateChanged = false;
    private int mHoverScrollStateForListener = 0;
    private long mHoverScrollTimeInterval = 300;
    private int mHoverTopAreaHeight = 0;
    private boolean mHoveringEnabled = true;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTopOffsetOfScreen = 0;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mInterceptRequestLayoutDepth = 0;
    private boolean mIsArrowKeyPressed;
    boolean mIsAttached;
    private boolean mIsCloseChildSetted = false;
    private boolean mIsCtrlKeyPressed = false;
    private boolean mIsCtrlMultiSelection = false;
    private boolean mIsEnabledPaddingInHoverScroll = false;
    private boolean mIsFirstMultiSelectionMove = true;
    private boolean mIsFirstPenMoveEvent = true;
    private boolean mIsGoToTopShown = false;
    private boolean mIsHoverOverscrolled = false;
    private boolean mIsLongPressMultiSelection = false;
    private boolean mIsMouseWheel = false;
    private boolean mIsNeedPenSelectIconSet = false;
    private boolean mIsNeedPenSelection = false;
    private boolean mIsPenDragBlockEnabled = true;
    private boolean mIsPenHovered = false;
    private boolean mIsPenPressed = false;
    private boolean mIsPenSelectPointerSetted = false;
    private boolean mIsPenSelectionEnabled = true;
    private boolean mIsSendHoverScrollState = false;
    ItemAnimator mItemAnimator = new DefaultItemAnimator();
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener = new ItemAnimatorRestoreListener();
    final ArrayList<ItemDecoration> mItemDecorations = new ArrayList<>();
    boolean mItemsAddedOrRemoved = false;
    boolean mItemsChanged = false;
    int mLastBlackTop = -1;
    private int mLastTouchX;
    private int mLastTouchY;
    LayoutManager mLayout;
    boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter = 0;
    boolean mLayoutWasDefered;
    private SeslEdgeEffect mLeftGlow;
    Rect mListPadding = new Rect();
    private SeslLongPressMultiSelectionListener mLongPressMultiSelectionListener;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions = new int[2];
    private int mNavigationBarHeight;
    private boolean mNeedsHoverScroll = false;
    private final int[] mNestedOffsets = new int[2];
    private boolean mNestedScroll = false;
    private int mNestedScrollRange = 0;
    private boolean mNewTextViewHoverState = false;
    private final RecyclerViewDataObserver mObserver = new RecyclerViewDataObserver();
    private int mOldHoverScrollDirection = -1;
    private boolean mOldTextViewHoverState = false;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private OnFlingListener mOnFlingListener;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners = new ArrayList<>();
    private SeslOnMultiSelectedListener mOnMultiSelectedListener;
    private int mPenDistanceFromTrackedChildTop = 0;
    private int mPenDragBlockBottom = 0;
    private Drawable mPenDragBlockImage;
    private int mPenDragBlockLeft = 0;
    private Rect mPenDragBlockRect = new Rect();
    private int mPenDragBlockRight = 0;
    private int mPenDragBlockTop = 0;
    private int mPenDragEndX = 0;
    private int mPenDragEndY = 0;
    private long mPenDragScrollTimeInterval = 500;
    private ArrayList<Integer> mPenDragSelectedItemArray;
    private int mPenDragSelectedViewPosition = -1;
    private int mPenDragStartX = 0;
    private int mPenDragStartY = 0;
    private View mPenTrackedChild = null;
    private int mPenTrackedChildPosition = -1;
    private SavedState mPendingSavedState;
    boolean mPostedAnimatorRunner = false;
    SeslGapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry = ALLOW_THREAD_GAP_WORK ? new SeslGapWorker.LayoutPrefetchRegistryImpl() : null;
    private boolean mPreserveFocusAfterLayout = true;
    private int mRectColor;
    private Paint mRectPaint = new Paint();
    final Recycler mRecycler = new Recycler();
    RecyclerListener mRecyclerListener;
    private int mRemainNestedScrollRange = 0;
    private SeslEdgeEffect mRightGlow;
    private View mRootViewCheckForDialog = null;
    private float mScaledHorizontalScrollFactor = Float.MIN_VALUE;
    private float mScaledVerticalScrollFactor = Float.MIN_VALUE;
    private final int[] mScrollConsumed = new int[2];
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset = new int[2];
    private int mScrollPointerId = -1;
    private int mScrollState = 0;
    private NestedScrollingChildHelper mScrollingChildHelper;
    Drawable mSelector;
    Rect mSelectorRect = new Rect();
    private SeslOnGoToTopClickListener mSeslOnGoToTopClickListener = null;
    private int mSeslPagingTouchSlop = 0;
    private SeslSubheaderRoundedCorner mSeslRoundedCorner;
    private int mSeslTouchSlop = 0;
    private int mShowFadeOutGTP = 0;
    boolean mShowGTPAtFirstTime = false;
    private boolean mSizeChnage = false;
    final State mState = new State();
    private int mStrokeColor;
    private int mStrokeHeight;
    private Paint mStrokePaint = new Paint();
    final Rect mTempRect = new Rect();
    private final Rect mTempRect2 = new Rect();
    final RectF mTempRectF = new RectF();
    private SeslEdgeEffect mTopGlow;
    private int mTouchSlop;
    private boolean mUsePagingTouchSlopForStylus = false;
    private VelocityTracker mVelocityTracker;
    final ViewFlinger mViewFlinger = new ViewFlinger();
    final SeslViewInfoStore mViewInfoStore = new SeslViewInfoStore();
    private final int[] mWindowOffsets = new int[2];

    final Runnable mUpdateChildViewsRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mFirstLayoutComplete || isLayoutRequested()) {
                return;
            }
            if (!mIsAttached) {
                requestLayout();
                return;
            }
            if (mLayoutFrozen) {
                mLayoutWasDefered = true;
                return;
            }
            consumePendingUpdateOperations();
        }
    };
    private Handler mHoverHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("WrongConstant")
        public void handleMessage(Message msg) {
            int offset;
            switch (msg.what) {
                case MSG_HOVERSCROLL_MOVE:
                    if (mAdapter == null) {
                        LogUtils.e(TAG, "No adapter attached; skipping MSG_HOVERSCROLL_MOVE");
                        return;
                    }
                    mHoverRecognitionCurrentTime = System.currentTimeMillis();
                    mHoverRecognitionDurationTime = (mHoverRecognitionCurrentTime - mHoverRecognitionStartTime) / 1000;
                    if (mIsPenHovered && mHoverRecognitionCurrentTime - mHoverScrollStartTime < mHoverScrollTimeInterval) {
                        return;
                    }
                    if (!mIsPenPressed || mHoverRecognitionCurrentTime - mHoverScrollStartTime >= mPenDragScrollTimeInterval) {
                        if (mIsPenHovered && !mIsSendHoverScrollState) {
                            if (mScrollListener != null) {
                                mHoverScrollStateForListener = 1;
                                mScrollListener.onScrollStateChanged(SeslRecyclerView.this, 1);
                            }
                            mIsSendHoverScrollState = true;
                        }
                        int count = getChildCount();
                        boolean canScrollDown = findFirstChildPosition() + count < mAdapter.getItemCount();
                        if (!canScrollDown && count > 0) {
                            View child = getChildAt(count - 1);
                            canScrollDown = child.getBottom() > getBottom() - mListPadding.bottom || child.getBottom() > getHeight() - mListPadding.bottom;
                        }
                        boolean canScrollUp = findFirstChildPosition() > 0;
                        if (!canScrollUp && getChildCount() > 0) {
                            canScrollUp = getChildAt(0).getTop() < mListPadding.top;
                        }
                        mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, HOVERSCROLL_SPEED, mContext.getResources().getDisplayMetrics()) + 0.5f);
                        if (mHoverRecognitionDurationTime > 2 && mHoverRecognitionDurationTime < 4) {
                            mHoverScrollSpeed = mHoverScrollSpeed + ((int) (((double) mHoverScrollSpeed) * 0.1d));
                        } else if (mHoverRecognitionDurationTime >= 4 && mHoverRecognitionDurationTime < 5) {
                            mHoverScrollSpeed = mHoverScrollSpeed + ((int) (((double) mHoverScrollSpeed) * 0.2d));
                        } else if (mHoverRecognitionDurationTime >= 5) {
                            mHoverScrollSpeed = mHoverScrollSpeed + ((int) (((double) mHoverScrollSpeed) * 0.3d));
                        }
                        if (mHoverScrollDirection == 2) {
                            offset = mHoverScrollSpeed * -1;
                            if ((mPenTrackedChild == null && mCloseChildByBottom != null) || (mOldHoverScrollDirection != mHoverScrollDirection && mIsCloseChildSetted)) {
                                mPenTrackedChild = mCloseChildByBottom;
                                mPenDistanceFromTrackedChildTop = mDistanceFromCloseChildBottom;
                                mPenTrackedChildPosition = mCloseChildPositionByBottom;
                                mOldHoverScrollDirection = mHoverScrollDirection;
                                mIsCloseChildSetted = true;
                            }
                        } else {
                            offset = mHoverScrollSpeed;
                            if ((mPenTrackedChild == null && mCloseChildByTop != null) || (mOldHoverScrollDirection != mHoverScrollDirection && mIsCloseChildSetted)) {
                                mPenTrackedChild = mCloseChildByTop;
                                mPenDistanceFromTrackedChildTop = mDistanceFromCloseChildTop;
                                mPenTrackedChildPosition = mCloseChildPositionByTop;
                                mOldHoverScrollDirection = mHoverScrollDirection;
                                mIsCloseChildSetted = true;
                            }
                        }
                        if (getChildAt(getChildCount() - 1) == null) {
                            return;
                        }
                        if ((offset >= 0 || !canScrollUp) && (offset <= 0 || !canScrollDown)) {
                            int overscrollMode = getOverScrollMode();
                            boolean canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && !contentFits());
                            if (canOverscroll && !mIsHoverOverscrolled) {
                                ensureTopGlow();
                                ensureBottomGlow();
                                if (mHoverScrollDirection == 2) {
                                    mTopGlow.setSize(getWidth(), getHeight());
                                    mTopGlow.onPullCallOnRelease(0.4f, 0.5f, 0);
                                    if (!mBottomGlow.isFinished()) {
                                        mBottomGlow.onRelease();
                                    }
                                } else if (mHoverScrollDirection == 1) {
                                    mBottomGlow.setSize(getWidth(), getHeight());
                                    mBottomGlow.onPullCallOnRelease(0.4f, 0.5f, 0);
                                    setupGoToTop(1);
                                    autoHide(1);
                                    if (!mTopGlow.isFinished()) {
                                        mTopGlow.onRelease();
                                    }
                                }
                                invalidate();
                                mIsHoverOverscrolled = true;
                            }
                            if (mScrollState == 1) {
                                setScrollState(0);
                            }
                            if (!canOverscroll && !mIsHoverOverscrolled) {
                                mIsHoverOverscrolled = true;
                                return;
                            }
                            return;
                        }
                        startNestedScroll(2, TYPE_NON_TOUCH);
                        if (!dispatchNestedPreScroll(0, offset, null, null, 1)) {
                            scrollByInternal(0, offset, null);
                            setScrollState(1);
                            if (mIsLongPressMultiSelection) {
                                updateLongPressMultiSelection(mPenDragEndX, mPenDragEndY, false);
                            }
                        } else {
                            adjustNestedScrollRangeBy(offset);
                        }
                        mHoverHandler.sendEmptyMessageDelayed(0, (long) HOVERSCROLL_DELAY);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    final List<ViewHolder> mPendingAccessibilityImportanceChange = new ArrayList();
    private Runnable mItemAnimatorRunner = new Runnable() {
        @Override
        public void run() {
            if (mItemAnimator != null) {
                mItemAnimator.runPendingAnimations();
            }
            mPostedAnimatorRunner = false;
        }
    };
    private final SeslViewInfoStore.ProcessCallback mViewInfoProcessCallback = new SeslViewInfoStore.ProcessCallback() {
        @Override
        public void processDisappeared(ViewHolder viewHolder, ItemHolderInfo info, ItemHolderInfo postInfo) {
            mRecycler.unscrapView(viewHolder);
            animateDisappearance(viewHolder, info, postInfo);
        }
        @Override
        public void processAppeared(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo info) {
            animateAppearance(viewHolder, preInfo, info);
        }
        @Override
        public void processPersistent(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo) {
            viewHolder.setIsRecyclable(false);
            if (mDataSetHasChangedAfterLayout) {
                if (mItemAnimator.animateChange(viewHolder, viewHolder, preInfo, postInfo)) {
                    postAnimationRunner();
                }
            } else if (mItemAnimator.animatePersistence(viewHolder, preInfo, postInfo)) {
                postAnimationRunner();
            }
        }
        @Override
        public void unused(ViewHolder viewHolder) {
            mLayout.removeAndRecycleView(viewHolder.itemView, mRecycler);
        }
    };
    private final Runnable mGoToToFadeInRunnable = new Runnable() {
        @Override
        public void run() {
            playGotoToFadeOut();
        }
    };
    private final Runnable mGoToToFadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            playGotoToFadeIn();
        }
    };
    private final Runnable mAutoHide = new Runnable() {
        @Override
        public void run() {
            setupGoToTop(0);
        }
    };

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {}

    public SeslRecyclerView(Context context) {
        this(context, null);
    }

    public SeslRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, CLIP_TO_PADDING_ATTR, defStyle, 0);
            mClipToPadding = a.getBoolean(0, true);
            a.recycle();
        } else {
            mClipToPadding = true;
        }
        setScrollContainer(true);
        setFocusableInTouchMode(true);

        seslInitConfigurations(context);
        setWillNotDraw(getOverScrollMode() == View.OVER_SCROLL_NEVER);

        mItemAnimator.setListener(mItemAnimatorListener);
        initAdapterManager();
        initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility(this) == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        mAccessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        setAccessibilityDelegateCompat(new SeslRecyclerViewAccessibilityDelegate(this));

        boolean nestedScrollingEnabled = true;

        if (attrs != null) {
            int defStyleRes = 0;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslRecyclerView, defStyle, defStyleRes);
            String layoutManagerName = a.getString(R.styleable.SeslRecyclerView_layoutManager);
            int descendantFocusability = a.getInt(R.styleable.SeslRecyclerView_android_descendantFocusability, -1);
            if (descendantFocusability == -1) {
                setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            }
            a.recycle();
            createLayoutManager(context, layoutManagerName, attrs, defStyle, defStyleRes);

            if (Build.VERSION.SDK_INT >= 21) {
                a = context.obtainStyledAttributes(attrs, NESTED_SCROLLING_ATTRS, defStyle, defStyleRes);
                nestedScrollingEnabled = a.getBoolean(0, true);
                a.recycle();
            }
        } else {
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        }

        Resources r = context.getResources();
        TypedValue value = new TypedValue();
        mPenDragBlockImage = r.getDrawable(R.drawable.sesl_pen_block_selection, context.getTheme());
        if (context.getTheme().resolveAttribute(R.attr.goToTopStyle, value, true)) {
            mGoToTopImageLight = r.getDrawable(value.resourceId, context.getTheme());
        }
        context.getTheme().resolveAttribute(R.attr.roundedCornerColor, value, true);
        if (value.resourceId > 0) {
            mRectColor = r.getColor(value.resourceId, context.getTheme());
        }
        mRectPaint.setColor(mRectColor);
        mRectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        context.getTheme().resolveAttribute(R.attr.roundedCornerStrokeColor, value, true);
        if (value.resourceId > 0) {
            mStrokeColor = r.getColor(value.resourceId, context.getTheme());
        }
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mItemAnimator.setHostView(this);
        mSeslRoundedCorner = new SeslSubheaderRoundedCorner(getContext());
        mSeslRoundedCorner.setRoundedCorners(12);

        setNestedScrollingEnabled(nestedScrollingEnabled);
    }

    public void seslInitConfigurations(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        Resources r = context.getResources();
        mTouchSlop = vc.getScaledTouchSlop();
        mSeslTouchSlop = vc.getScaledTouchSlop();
        mSeslPagingTouchSlop = vc.getScaledPagingTouchSlop();
        mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(vc, context);
        mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(vc, context);
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mHoverTopAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, r.getDisplayMetrics()) + 0.5f);
        mHoverBottomAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, r.getDisplayMetrics()) + 0.5f);
        mGoToTopBottomPadding = r.getDimensionPixelSize(R.dimen.sesl_go_to_top_scrollable_view_gap);
        mGoToTopSize = r.getDimensionPixelSize(R.dimen.sesl_go_to_top_scrollable_view_size);
        mNavigationBarHeight = r.getDimensionPixelSize(R.dimen.sesl_navigation_bar_height);
        mStrokeHeight = r.getDimensionPixelSize(R.dimen.sesl_round_stroke_height);
    }

    String exceptionLabel() {
        return " " + super.toString()
                + ", adapter:" + mAdapter
                + ", layout:" + mLayout
                + ", context:" + getContext();
    }

    public SeslRecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return mAccessibilityDelegate;
    }

    public void setAccessibilityDelegateCompat(SeslRecyclerViewAccessibilityDelegate accessibilityDelegate) {
        mAccessibilityDelegate = accessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this, mAccessibilityDelegate);
    }

    private void createLayoutManager(Context context, String className, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (className != null) {
            className = className.trim();
            if (!className.isEmpty()) {
                className = getFullClassName(context, className);
                try {
                    ClassLoader classLoader;
                    if (isInEditMode()) {
                        classLoader = this.getClass().getClassLoader();
                    } else {
                        classLoader = context.getClassLoader();
                    }
                    Class<? extends LayoutManager> layoutManagerClass = classLoader.loadClass(className).asSubclass(LayoutManager.class);
                    Constructor<? extends LayoutManager> constructor;
                    Object[] constructorArgs = null;
                    try {
                        constructor = layoutManagerClass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        constructorArgs = new Object[]{context, attrs, defStyleAttr, defStyleRes};
                    } catch (NoSuchMethodException e) {
                        try {
                            constructor = layoutManagerClass.getConstructor();
                        } catch (NoSuchMethodException e1) {
                            e1.initCause(e);
                            throw new IllegalStateException(attrs.getPositionDescription() + ": Error creating LayoutManager " + className, e1);
                        }
                    }
                    constructor.setAccessible(true);
                    setLayoutManager(constructor.newInstance(constructorArgs));
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Unable to find LayoutManager " + className, e);
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className, e);
                } catch (InstantiationException e) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className, e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Cannot access non-public constructor " + className, e);
                } catch (ClassCastException e) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Class is not a LayoutManager " + className, e);
                }
            }
        }
    }

    private String getFullClassName(Context context, String className) {
        if (className.charAt(0) == '.') {
            return context.getPackageName() + className;
        }
        if (className.contains(".")) {
            return className;
        }
        return SeslRecyclerView.class.getPackage().getName() + '.' + className;
    }

    private void initChildrenHelper() {
        mChildHelper = new SeslChildHelper(new SeslChildHelper.Callback() {
            @Override
            public int getChildCount() {
                return SeslRecyclerView.this.getChildCount();
            }

            @Override
            public void addView(View child, int index) {
                if (VERBOSE_TRACING) {
                    TraceCompat.beginSection("RV addView");
                }
                SeslRecyclerView.this.addView(child, index);
                if (VERBOSE_TRACING) {
                    TraceCompat.endSection();
                }
                dispatchChildAttached(child);
            }

            @Override
            public int indexOfChild(View view) {
                return SeslRecyclerView.this.indexOfChild(view);
            }

            @Override
            public void removeViewAt(int index) {
                final View child = SeslRecyclerView.this.getChildAt(index);
                if (child != null) {
                    dispatchChildDetached(child);

                    child.clearAnimation();
                }
                if (VERBOSE_TRACING) {
                    TraceCompat.beginSection("RV removeViewAt");
                }
                SeslRecyclerView.this.removeViewAt(index);
                if (VERBOSE_TRACING) {
                    TraceCompat.endSection();
                }
            }

            @Override
            public View getChildAt(int offset) {
                return SeslRecyclerView.this.getChildAt(offset);
            }

            @Override
            public void removeAllViews() {
                final int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    dispatchChildDetached(child);

                    child.clearAnimation();
                }
                SeslRecyclerView.this.removeAllViews();
            }

            @Override
            public ViewHolder getChildViewHolder(View view) {
                return getChildViewHolderInt(view);
            }

            @Override
            public void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams) {
                final ViewHolder vh = getChildViewHolderInt(child);
                if (vh != null) {
                    if (!vh.isTmpDetached() && !vh.shouldIgnore()) {
                        throw new IllegalArgumentException("Called attach on a child which is not" + " detached: " + vh + exceptionLabel());
                    }
                    if (DEBUG) {
                        LogUtils.d(TAG, "reAttach " + vh);
                    }
                    vh.clearTmpDetachFlag();
                }
                SeslRecyclerView.this.attachViewToParent(child, index, layoutParams);
            }

            @Override
            public void detachViewFromParent(int offset) {
                final View view = getChildAt(offset);
                if (view != null) {
                    final ViewHolder vh = getChildViewHolderInt(view);
                    if (vh != null) {
                        if (vh.isTmpDetached() && !vh.shouldIgnore()) {
                            throw new IllegalArgumentException("called detach on an already" + " detached child " + vh + exceptionLabel());
                        }
                        if (DEBUG) {
                            LogUtils.d(TAG, "tmpDetach " + vh);
                        }
                        vh.addFlags(ViewHolder.FLAG_TMP_DETACHED);
                    }
                }
                SeslRecyclerView.this.detachViewFromParent(offset);
            }

            @Override
            public void onEnteredHiddenState(View child) {
                final ViewHolder vh = getChildViewHolderInt(child);
                if (vh != null) {
                    vh.onEnteredHiddenState(SeslRecyclerView.this);
                }
            }

            @Override
            public void onLeftHiddenState(View child) {
                final ViewHolder vh = getChildViewHolderInt(child);
                if (vh != null) {
                    vh.onLeftHiddenState(SeslRecyclerView.this);
                }
            }
        });
    }

    void initAdapterManager() {
        mAdapterHelper = new SeslAdapterHelper(new SeslAdapterHelper.Callback() {
            @Override
            public ViewHolder findViewHolder(int position) {
                final ViewHolder vh = findViewHolderForPosition(position, true);
                if (vh == null) {
                    return null;
                }
                if (mChildHelper.isHidden(vh.itemView)) {
                    if (DEBUG) {
                        LogUtils.d(TAG, "assuming view holder cannot be find because it is hidden");
                    }
                    return null;
                }
                return vh;
            }

            @Override
            public void offsetPositionsForRemovingInvisible(int start, int count) {
                offsetPositionRecordsForRemove(start, count, true);
                mItemsAddedOrRemoved = true;
                mState.mDeletedInvisibleItemCountSincePreviousLayout += count;
            }

            @Override
            public void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount) {
                offsetPositionRecordsForRemove(positionStart, itemCount, false);
                mItemsAddedOrRemoved = true;
            }


            @Override
            public void markViewHoldersUpdated(int positionStart, int itemCount, Object payload) {
                viewRangeUpdate(positionStart, itemCount, payload);
                mItemsChanged = true;
            }

            @Override
            public void onDispatchFirstPass(SeslAdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            void dispatchUpdate(SeslAdapterHelper.UpdateOp op) {
                switch (op.cmd) {
                    case SeslAdapterHelper.UpdateOp.ADD:
                        mLayout.onItemsAdded(SeslRecyclerView.this, op.positionStart, op.itemCount);
                        break;
                    case SeslAdapterHelper.UpdateOp.REMOVE:
                        mLayout.onItemsRemoved(SeslRecyclerView.this, op.positionStart, op.itemCount);
                        break;
                    case SeslAdapterHelper.UpdateOp.UPDATE:
                        mLayout.onItemsUpdated(SeslRecyclerView.this, op.positionStart, op.itemCount, op.payload);
                        break;
                    case SeslAdapterHelper.UpdateOp.MOVE:
                        mLayout.onItemsMoved(SeslRecyclerView.this, op.positionStart, op.itemCount, 1);
                        break;
                }
            }

            @Override
            public void onDispatchSecondPass(SeslAdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            @Override
            public void offsetPositionsForAdd(int positionStart, int itemCount) {
                offsetPositionRecordsForInsert(positionStart, itemCount);
                mItemsAddedOrRemoved = true;
            }

            @Override
            public void offsetPositionsForMove(int from, int to) {
                offsetPositionRecordsForMove(from, to);
                mItemsAddedOrRemoved = true;
            }
        });
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mHasFixedSize = hasFixedSize;
    }

    public boolean hasFixedSize() {
        return mHasFixedSize;
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        if (clipToPadding != mClipToPadding) {
            invalidateGlows();
        }
        mClipToPadding = clipToPadding;
        super.setClipToPadding(clipToPadding);
        if (mFirstLayoutComplete) {
            requestLayout();
        }
    }

    @Override
    public boolean getClipToPadding() {
        return mClipToPadding;
    }

    public void setScrollingTouchSlop(int slopConstant) {
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        LogUtils.d(TAG, "setScrollingTouchSlop(): slopConstant[" + slopConstant + "]");
        seslSetPagingTouchSlopForStylus(false);
        switch (slopConstant) {
            default:
                LogUtils.w(TAG, "setScrollingTouchSlop(): bad argument constant " + slopConstant + "; using default value");
            case TOUCH_SLOP_DEFAULT:
                mTouchSlop = vc.getScaledTouchSlop();
                break;
            case TOUCH_SLOP_PAGING:
                mTouchSlop = vc.getScaledPagingTouchSlop();
                break;
        }
    }

    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
        processDataSetCompletelyChanged(true);
        requestLayout();
    }

    public void setAdapter(Adapter adapter) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, false, true);
        processDataSetCompletelyChanged(false);
        requestLayout();
    }

    void removeAndRecycleViews() {
        if (mItemAnimator != null) {
            mItemAnimator.endAnimations();
        }
        if (mLayout != null) {
            mLayout.removeAndRecycleAllViews(mRecycler);
            mLayout.removeAndRecycleScrapInt(mRecycler);
        }
        mRecycler.clear();
    }

    private void setAdapterInternal(Adapter adapter, boolean compatibleWithPrevious, boolean removeAndRecycleViews) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
            mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!compatibleWithPrevious || removeAndRecycleViews) {
            removeAndRecycleViews();
        }
        mAdapterHelper.reset();
        final Adapter oldAdapter = mAdapter;
        mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        if (mLayout != null) {
            mLayout.onAdapterChanged(oldAdapter, mAdapter);
        }
        mRecycler.onAdapterChanged(oldAdapter, mAdapter, compatibleWithPrevious);
        mState.mStructureChanged = true;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setRecyclerListener(RecyclerListener listener) {
        mRecyclerListener = listener;
    }

    @Override
    public int getBaseline() {
        if (mLayout != null) {
            return mLayout.getBaseline();
        } else {
            return super.getBaseline();
        }
    }

    public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener listener) {
        if (mOnChildAttachStateListeners == null) {
            mOnChildAttachStateListeners = new ArrayList<>();
        }
        mOnChildAttachStateListeners.add(listener);
    }

    public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener listener) {
        if (mOnChildAttachStateListeners == null) {
            return;
        }
        mOnChildAttachStateListeners.remove(listener);
    }

    public void clearOnChildAttachStateChangeListeners() {
        if (mOnChildAttachStateListeners != null) {
            mOnChildAttachStateListeners.clear();
        }
    }

    public void setLayoutManager(LayoutManager layout) {
        if (layout == mLayout) {
            return;
        }
        final boolean isLinearLayoutManager = layout instanceof SeslLinearLayoutManager;
        mDrawRect = mDrawRect && isLinearLayoutManager;
        mDrawOutlineStroke = mDrawOutlineStroke || isLinearLayoutManager;
        mDrawLastOutLineStroke = mDrawLastOutLineStroke || isLinearLayoutManager;
        stopScroll();
        if (mLayout != null) {
            if (mItemAnimator != null) {
                mItemAnimator.endAnimations();
            }
            mLayout.removeAndRecycleAllViews(mRecycler);
            mLayout.removeAndRecycleScrapInt(mRecycler);
            mRecycler.clear();

            if (mIsAttached) {
                mLayout.dispatchDetachedFromWindow(this, mRecycler);
            }
            mLayout.setRecyclerView(null);
            mLayout = null;
        } else {
            mRecycler.clear();
        }
        mChildHelper.removeAllViewsUnfiltered();
        mLayout = layout;
        if (layout != null) {
            if (layout.mRecyclerView != null) {
                throw new IllegalArgumentException("LayoutManager " + layout + " is already attached to a RecyclerView:" + layout.mRecyclerView.exceptionLabel());
            }
            mLayout.setRecyclerView(this);
            if (mIsAttached) {
                mLayout.dispatchAttachedToWindow(this);
            }
        }
        mRecycler.updateViewCacheSize();
        requestLayout();
    }

    public void setOnFlingListener(OnFlingListener onFlingListener) {
        mOnFlingListener = onFlingListener;
    }

    public OnFlingListener getOnFlingListener() {
        return mOnFlingListener;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        if (mPendingSavedState != null) {
            state.copyFrom(mPendingSavedState);
        } else if (mLayout != null) {
            state.mLayoutState = mLayout.onSaveInstanceState();
        } else {
            state.mLayoutState = null;
        }

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        mPendingSavedState = (SavedState) state;
        super.onRestoreInstanceState(mPendingSavedState.getSuperState());
        if (mLayout != null && mPendingSavedState.mLayoutState != null) {
            mLayout.onRestoreInstanceState(mPendingSavedState.mLayoutState);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    private void addAnimatingView(ViewHolder viewHolder) {
        final View view = viewHolder.itemView;
        final boolean alreadyParented = view.getParent() == this;
        mRecycler.unscrapView(getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
        } else if (!alreadyParented) {
            mChildHelper.addView(view, true);
        } else {
            mChildHelper.hide(view);
        }
    }

    boolean removeAnimatingView(View view) {
        startInterceptRequestLayout();
        final boolean removed = mChildHelper.removeViewIfHidden(view);
        if (removed) {
            final ViewHolder viewHolder = getChildViewHolderInt(view);
            mRecycler.unscrapView(viewHolder);
            mRecycler.recycleViewHolderInternal(viewHolder);
            if (DEBUG) {
                LogUtils.d(TAG, "after removing animated view: " + view + ", " + this);
            }
        }
        stopInterceptRequestLayout(!removed);
        return removed;
    }

    public LayoutManager getLayoutManager() {
        return mLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return mRecycler.getRecycledViewPool();
    }

    public void setRecycledViewPool(RecycledViewPool pool) {
        mRecycler.setRecycledViewPool(pool);
    }

    public void setViewCacheExtension(ViewCacheExtension extension) {
        mRecycler.setViewCacheExtension(extension);
    }

    public void setItemViewCacheSize(int size) {
        mRecycler.setViewCacheSize(size);
    }

    public int getScrollState() {
        return mScrollState;
    }

    void setScrollState(int state) {
        if (state == mScrollState) {
            return;
        }
        if (DEBUG) {
            LogUtils.d(TAG, "setting scroll state to " + state + " from " + mScrollState, new Exception());
        }
        mScrollState = state;
        if (state != SCROLL_STATE_SETTLING) {
            stopScrollersInternal();
        }
        dispatchOnScrollStateChanged(state);
    }

    public void addItemDecoration(ItemDecoration decor, int index) {
        if (mLayout != null) {
            mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or"
                    + " layout");
        }
        if (mItemDecorations.isEmpty()) {
            setWillNotDraw(false);
        }
        if (index < 0) {
            mItemDecorations.add(decor);
        } else {
            mItemDecorations.add(index, decor);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void addItemDecoration(ItemDecoration decor) {
        addItemDecoration(decor, -1);
    }

    public ItemDecoration getItemDecorationAt(int index) {
        final int size = getItemDecorationCount();
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index + " is an invalid index for size " + size);
        }

        return mItemDecorations.get(index);
    }

    public int getItemDecorationCount() {
        return mItemDecorations.size();
    }

    public void removeItemDecorationAt(int index) {
        final int size = getItemDecorationCount();
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index + " is an invalid index for size " + size);
        }

        removeItemDecoration(getItemDecorationAt(index));
    }

    public void removeItemDecoration(ItemDecoration decor) {
        if (mLayout != null) {
            mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or"
                    + " layout");
        }
        mItemDecorations.remove(decor);
        if (mItemDecorations.isEmpty()) {
            setWillNotDraw(getOverScrollMode() == View.OVER_SCROLL_NEVER);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        if (childDrawingOrderCallback == mChildDrawingOrderCallback) {
            return;
        }
        mChildDrawingOrderCallback = childDrawingOrderCallback;
        setChildrenDrawingOrderEnabled(mChildDrawingOrderCallback != null);
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (mScrollListeners == null) {
            mScrollListeners = new ArrayList<>();
        }
        mScrollListeners.add(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        if (mScrollListeners != null) {
            mScrollListeners.remove(listener);
        }
    }

    public void clearOnScrollListeners() {
        if (mScrollListeners != null) {
            mScrollListeners.clear();
        }
    }

    public void scrollToPosition(int position) {
        if (mLayoutFrozen) {
            return;
        }
        stopScroll();
        if (mLayout == null) {
            LogUtils.e(TAG, "Cannot scroll to position a LayoutManager set. "
                    + "Call setLayoutManager with a non-null argument.");
            return;
        }
        mLayout.scrollToPosition(position);
        awakenScrollBars();
        if (mFastScroller != null && mAdapter != null) {
            mFastScroller.onScroll(findFirstVisibleItemPosition(), getChildCount(), mAdapter.getItemCount());
        }
    }

    void jumpToPositionForSmoothScroller(int position) {
        if (mLayout == null) {
            return;
        }
        mLayout.scrollToPosition(position);
        awakenScrollBars();
    }

    public void smoothScrollToPosition(int position) {
        if (mLayoutFrozen) {
            return;
        }
        if (mLayout == null) {
            LogUtils.e(TAG, "Cannot smooth scroll without a LayoutManager set. "
                    + "Call setLayoutManager with a non-null argument.");
            return;
        }
        mLayout.smoothScrollToPosition(this, mState, position);
    }

    @Override
    public void scrollTo(int x, int y) {
        LogUtils.w(TAG, "RecyclerView does not support scrolling to an absolute position. "
                + "Use scrollToPosition instead");
    }

    @Override
    public void scrollBy(int x, int y) {
        if (mLayout == null) {
            LogUtils.e(TAG, "Cannot scroll without a LayoutManager set. "
                    + "Call setLayoutManager with a non-null argument.");
            return;
        }
        if (mLayoutFrozen) {
            return;
        }
        final boolean canScrollHorizontal = mLayout.canScrollHorizontally();
        final boolean canScrollVertical = mLayout.canScrollVertically();
        if (canScrollHorizontal || canScrollVertical) {
            scrollByInternal(canScrollHorizontal ? x : 0, canScrollVertical ? y : 0, null);
        }
    }

    void consumePendingUpdateOperations() {
        if (!mFirstLayoutComplete || mDataSetHasChangedAfterLayout) {
            TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
            dispatchLayout();
            TraceCompat.endSection();
            return;
        }
        if (!mAdapterHelper.hasPendingUpdates()) {
            return;
        }

        if (mAdapterHelper.hasAnyUpdateTypes(SeslAdapterHelper.UpdateOp.UPDATE) && !mAdapterHelper.hasAnyUpdateTypes(SeslAdapterHelper.UpdateOp.ADD | SeslAdapterHelper.UpdateOp.REMOVE | SeslAdapterHelper.UpdateOp.MOVE)) {
            TraceCompat.beginSection(TRACE_HANDLE_ADAPTER_UPDATES_TAG);
            startInterceptRequestLayout();
            onEnterLayoutOrScroll();
            mAdapterHelper.preProcess();
            if (!mLayoutWasDefered) {
                if (hasUpdatedView()) {
                    dispatchLayout();
                } else {
                    mAdapterHelper.consumePostponedUpdates();
                }
            }
            stopInterceptRequestLayout(true);
            onExitLayoutOrScroll();
            TraceCompat.endSection();
        } else if (mAdapterHelper.hasPendingUpdates()) {
            TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
            dispatchLayout();
            TraceCompat.endSection();
        }
    }

    private boolean hasUpdatedView() {
        final int childCount = mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getChildAt(i));
            if (holder == null || holder.shouldIgnore()) {
                continue;
            }
            if (holder.isUpdated()) {
                return true;
            }
        }
        return false;
    }

    boolean scrollByInternal(int x, int y, MotionEvent ev) {
        int unconsumedX = 0, unconsumedY = 0;
        int consumedX = 0, consumedY = 0;

        consumePendingUpdateOperations();
        if (mAdapter != null) {
            startInterceptRequestLayout();
            onEnterLayoutOrScroll();
            TraceCompat.beginSection(TRACE_SCROLL_TAG);
            fillRemainingScrollValues(mState);
            if (x != 0) {
                consumedX = mLayout.scrollHorizontallyBy(x, mRecycler, mState);
                unconsumedX = x - consumedX;
            }
            if (y != 0) {
                consumedY = mLayout.scrollVerticallyBy(y, mRecycler, mState);
                unconsumedY = y - consumedY;
            }
            if (mGoToTopState == 0) {
                setupGoToTop(1);
                autoHide(1);
            }
            TraceCompat.endSection();
            repositionShadowingViews();
            onExitLayoutOrScroll();
            stopInterceptRequestLayout(false);
        }
        if (!mItemDecorations.isEmpty()) {
            invalidate();
        }

        final boolean needNestedScroll = mIsMouseWheel || unconsumedY >= 0;
        final boolean isMouse = MotionEventCompat.isFromSource(ev, InputDevice.SOURCE_MOUSE);

        if (!needNestedScroll || dispatchNestedScroll(consumedX, consumedY, unconsumedX, unconsumedY, mScrollOffset, isMouse ? TYPE_NON_TOUCH : TYPE_TOUCH)) {
            mLastTouchX -= mScrollOffset[0];
            mLastTouchY -= mScrollOffset[1];
            if (ev != null) {
                ev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
            }
            mNestedOffsets[0] += mScrollOffset[0];
            mNestedOffsets[1] += mScrollOffset[1];
        } else if (getOverScrollMode() != View.OVER_SCROLL_NEVER) {
            if (ev != null && !isMouse) {
                pullGlows(ev.getX(), unconsumedX, ev.getY(), unconsumedY);
            }
            considerReleasingGlowsOnScroll(x, y);
        }
        if (consumedX != 0 || consumedY != 0) {
            dispatchOnScrolled(consumedX, consumedY);
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
        // TODO merge Sammy StaggeredGridLayoutManager
        //if (mLayout instanceof StaggeredGridLayoutManager) {
        //    if (!canScrollVertically(-1) || !canScrollVertically(1)) {
        //        mLayout.onScrollStateChanged(0);
        //    }
        //}
        return consumedX != 0 || consumedY != 0;
    }

    @Override
    public int computeHorizontalScrollOffset() {
        if (mLayout == null) {
            return 0;
        }
        return mLayout.canScrollHorizontally() ? mLayout.computeHorizontalScrollOffset(mState) : 0;
    }

    @Override
    public int computeHorizontalScrollExtent() {
        if (mLayout == null) {
            return 0;
        }
        return mLayout.canScrollHorizontally() ? mLayout.computeHorizontalScrollExtent(mState) : 0;
    }

    @Override
    public int computeHorizontalScrollRange() {
        if (mLayout == null) {
            return 0;
        }
        return mLayout.canScrollHorizontally() ? mLayout.computeHorizontalScrollRange(mState) : 0;
    }

    @Override
    public int computeVerticalScrollOffset() {
        if (mLayout == null) {
            return 0;
        }
        return mLayout.canScrollVertically() ? mLayout.computeVerticalScrollOffset(mState) : 0;
    }

    @Override
    public int computeVerticalScrollExtent() {
        if (mLayout == null) {
            return 0;
        }
        return mLayout.canScrollVertically() ? mLayout.computeVerticalScrollExtent(mState) : 0;
    }

    @Override
    public int computeVerticalScrollRange() {
        if (mLayout == null) {
            return 0;
        }
        return mLayout.canScrollVertically() ? mLayout.computeVerticalScrollRange(mState) : 0;
    }

    void startInterceptRequestLayout() {
        mInterceptRequestLayoutDepth++;
        if (mInterceptRequestLayoutDepth == 1 && !mLayoutFrozen) {
            mLayoutWasDefered = false;
        }
    }

    void stopInterceptRequestLayout(boolean performLayoutChildren) {
        if (mInterceptRequestLayoutDepth < 1) {
            mInterceptRequestLayoutDepth = 1;
        }
        if (!performLayoutChildren && !mLayoutFrozen) {
            mLayoutWasDefered = false;
        }
        if (mInterceptRequestLayoutDepth == 1) {
            if (performLayoutChildren && mLayoutWasDefered && !mLayoutFrozen && mLayout != null && mAdapter != null) {
                dispatchLayout();
            }
            if (!mLayoutFrozen) {
                mLayoutWasDefered = false;
            }
        }
        mInterceptRequestLayoutDepth--;
    }

    public void setLayoutFrozen(boolean frozen) {
        if (frozen != mLayoutFrozen) {
            assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
            if (!frozen) {
                mLayoutFrozen = false;
                if (mLayoutWasDefered && mLayout != null && mAdapter != null) {
                    requestLayout();
                }
                mLayoutWasDefered = false;
            } else {
                final long now = SystemClock.uptimeMillis();
                MotionEvent cancelEvent = MotionEvent.obtain(now, now, MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0);
                onTouchEvent(cancelEvent);
                mLayoutFrozen = true;
                mIgnoreMotionEventTillDown = true;
                stopScroll();
            }
        }
    }

    public boolean isLayoutFrozen() {
        return mLayoutFrozen;
    }

    public void smoothScrollBy(int dx, int dy) {
        smoothScrollBy(dx, dy, null);
    }

    public void smoothScrollBy(int dx, int dy, Interpolator interpolator) {
        if (mLayout == null) {
            LogUtils.e(TAG, "Cannot smooth scroll without a LayoutManager set. "
                    + "Call setLayoutManager with a non-null argument.");
            return;
        }
        if (mLayoutFrozen) {
            return;
        }
        if (!mLayout.canScrollHorizontally()) {
            dx = 0;
        }
        if (!mLayout.canScrollVertically()) {
            dy = 0;
        }
        if (dx != 0 || dy != 0) {
            mViewFlinger.smoothScrollBy(dx, dy, interpolator);
            showGoToTop();
        }
    }

    public boolean fling(int velocityX, int velocityY) {
        if (mLayout == null) {
            LogUtils.e(TAG, "Cannot fling without a LayoutManager set. "
                    + "Call setLayoutManager with a non-null argument.");
            return false;
        }
        if (mLayoutFrozen) {
            return false;
        }

        final boolean canScrollHorizontal = mLayout.canScrollHorizontally();
        final boolean canScrollVertical = mLayout.canScrollVertically();

        if (!canScrollHorizontal || Math.abs(velocityX) < mMinFlingVelocity) {
            velocityX = 0;
        }
        if (!canScrollVertical || Math.abs(velocityY) < mMinFlingVelocity) {
            velocityY = 0;
        }
        if (velocityX == 0 && velocityY == 0) {
            return false;
        }

        final boolean canScroll = canScrollHorizontal || canScrollVertical;
        dispatchNestedFling(velocityX, velocityY, canScroll);

        if (mOnFlingListener != null && mOnFlingListener.onFling(velocityX, velocityY)) {
            return true;
        }

        if (canScroll) {
            int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
            if (canScrollHorizontal) {
                nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;
            }
            if (canScrollVertical) {
                nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
            }
            startNestedScroll(nestedScrollAxis, TYPE_NON_TOUCH);

            velocityX = Math.max(-mMaxFlingVelocity, Math.min(velocityX, mMaxFlingVelocity));
            velocityY = Math.max(-mMaxFlingVelocity, Math.min(velocityY, mMaxFlingVelocity));
            mViewFlinger.fling(velocityX, velocityY);
            return true;
        }

        return false;
    }

    public void stopScroll() {
        setScrollState(SCROLL_STATE_IDLE);
        stopScrollersInternal();
    }

    private void stopScrollersInternal() {
        mViewFlinger.stop();
        if (mLayout != null) {
            mLayout.stopSmoothScroller();
        }
    }

    public int getMinFlingVelocity() {
        return mMinFlingVelocity;
    }

    public int getMaxFlingVelocity() {
        return mMaxFlingVelocity;
    }

    private void pullGlows(float x, float overscrollX, float y, float overscrollY) {
        boolean invalidate = false;
        if (overscrollX < 0) {
            ensureLeftGlow();
            mLeftGlow.onPull(-overscrollX / getWidth(), 1f - y  / getHeight());
            invalidate = true;
        } else if (overscrollX > 0) {
            ensureRightGlow();
            mRightGlow.onPull(overscrollX / getWidth(), y / getHeight());
            invalidate = true;
        }

        if (overscrollY < 0) {
            ensureTopGlow();
            mTopGlow.onPull(-overscrollY / getHeight(), x / getWidth());
            invalidate = true;
        } else if (overscrollY > 0) {
            ensureBottomGlow();
            mBottomGlow.onPull(overscrollY / getHeight(), 1f - x / getWidth());
            invalidate = true;
        }

        if (invalidate || overscrollX != 0 || overscrollY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void releaseGlows() {
        boolean needsInvalidate = false;
        if (mLeftGlow != null) {
            mLeftGlow.onRelease();
            needsInvalidate = mLeftGlow.isFinished();
        }
        if (mTopGlow != null) {
            mTopGlow.onRelease();
            needsInvalidate |= mTopGlow.isFinished();
        }
        if (mRightGlow != null) {
            mRightGlow.onRelease();
            needsInvalidate |= mRightGlow.isFinished();
        }
        if (mBottomGlow != null) {
            mBottomGlow.onRelease();
            needsInvalidate |= mBottomGlow.isFinished();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void considerReleasingGlowsOnScroll(int dx, int dy) {
        boolean needsInvalidate = false;
        if (mLeftGlow != null && !mLeftGlow.isFinished() && dx > 0) {
            mLeftGlow.onRelease();
            needsInvalidate = mLeftGlow.isFinished();
        }
        if (mRightGlow != null && !mRightGlow.isFinished() && dx < 0) {
            mRightGlow.onRelease();
            needsInvalidate |= mRightGlow.isFinished();
        }
        if (mTopGlow != null && !mTopGlow.isFinished() && dy > 0) {
            mTopGlow.onRelease();
            needsInvalidate |= mTopGlow.isFinished();
        }
        if (mBottomGlow != null && !mBottomGlow.isFinished() && dy < 0) {
            mBottomGlow.onRelease();
            needsInvalidate |= mBottomGlow.isFinished();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void absorbGlows(int velocityX, int velocityY) {
        if (velocityX < 0) {
            ensureLeftGlow();
            mLeftGlow.onAbsorb(-velocityX);
        } else if (velocityX > 0) {
            ensureRightGlow();
            mRightGlow.onAbsorb(velocityX);
        }

        if (velocityY < 0) {
            ensureTopGlow();
            mTopGlow.onAbsorb(-velocityY);
        } else if (velocityY > 0) {
            ensureBottomGlow();
            mBottomGlow.onAbsorb(velocityY);
        }

        if (velocityX != 0 || velocityY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void ensureLeftGlow() {
        if (mLeftGlow != null) {
            return;
        }
        mLeftGlow = new SeslEdgeEffect(getContext());
        mLeftGlow.setSeslHostView(this);
        if (mClipToPadding) {
            mLeftGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
        } else {
            mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
        }
    }

    void ensureRightGlow() {
        if (mRightGlow != null) {
            return;
        }
        mRightGlow = new SeslEdgeEffect(getContext());
        mRightGlow.setSeslHostView(this);
        if (mClipToPadding) {
            mRightGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
        } else {
            mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
        }
    }

    void ensureTopGlow() {
        if (mTopGlow != null) {
            return;
        }
        mTopGlow = new SeslEdgeEffect(getContext());
        mTopGlow.setSeslHostView(this);
        if (mClipToPadding) {
            mTopGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
        } else {
            mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    void ensureBottomGlow() {
        if (mBottomGlow != null) {
            return;
        }
        mBottomGlow = new SeslEdgeEffect(getContext());
        mBottomGlow.setSeslHostView(this);
        if (mClipToPadding) {
            mBottomGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
        } else {
            mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    void invalidateGlows() {
        mLeftGlow = mRightGlow = mTopGlow = mBottomGlow = null;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        View result = mLayout.onInterceptFocusSearch(focused, direction);
        if (result != null) {
            return result;
        }
        final boolean canRunFocusFailure = mAdapter != null && mLayout != null && !isComputingLayout() && !mLayoutFrozen;

        final FocusFinder ff = FocusFinder.getInstance();
        if (canRunFocusFailure && (direction == View.FOCUS_FORWARD || direction == View.FOCUS_BACKWARD)) {
            boolean needsFocusFailureLayout = false;
            if (mLayout.canScrollVertically()) {
                final int absDir = direction == View.FOCUS_FORWARD ? View.FOCUS_DOWN : View.FOCUS_UP;
                final View found = ff.findNextFocus(this, focused, absDir);
                needsFocusFailureLayout = found == null;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    direction = absDir;
                }
            }
            if (!needsFocusFailureLayout && mLayout.canScrollHorizontally()) {
                boolean rtl = mLayout.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL;
                final int absDir = (direction == View.FOCUS_FORWARD) ^ rtl ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
                final View found = ff.findNextFocus(this, focused, absDir);
                needsFocusFailureLayout = found == null;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    direction = absDir;
                }
            }
            if (needsFocusFailureLayout) {
                consumePendingUpdateOperations();
                final View focusedItemView = findContainingItemView(focused);
                if (focusedItemView == null) {
                    return null;
                }
                startInterceptRequestLayout();
                mLayout.onFocusSearchFailed(focused, direction, mRecycler, mState);
                stopInterceptRequestLayout(false);
            }
            result = ff.findNextFocus(this, focused, direction);
        } else {
            result = ff.findNextFocus(this, focused, direction);
            if (result == null && canRunFocusFailure) {
                consumePendingUpdateOperations();
                final View focusedItemView = findContainingItemView(focused);
                if (focusedItemView == null) {
                    return null;
                }
                startInterceptRequestLayout();
                result = mLayout.onFocusSearchFailed(focused, direction, mRecycler, mState);
                stopInterceptRequestLayout(false);
            }
        }
        if (result != null && !result.hasFocusable()) {
            if (getFocusedChild() == null) {
                return super.focusSearch(focused, direction);
            }
            requestChildOnScreen(result, null);
            return focused;
        } else {
            if (!isPreferredNextFocus(focused, result, direction)) {
                result = super.focusSearch(focused, direction);
            }
            // TODO merge Sammy StaggeredGridLayoutManager
            //if (mIsArrowKeyPressed && result == null && (mLayout instanceof StaggeredGridLayoutManager)) {
            //    int distance = 0;
            //    if (direction == 130) {
            //        distance = getFocusedChild().getBottom() - getBottom();
            //    } else if (direction == 33) {
            //        distance = getFocusedChild().getTop() - getTop();
            //    }
            //    ((StaggeredGridLayoutManager) mLayout).scrollBy(distance, mRecycler, mState);
            //    mIsArrowKeyPressed = false;
            //}
            return result;
        }
    }

    private boolean isPreferredNextFocus(View focused, View next, int direction) {
        if (next == null || next == this || focused == next) {
            return false;
        }
        if (findContainingItemView(next) == null) {
            return false;
        }
        if (focused == null || findContainingItemView(focused) == null) {
            return true;
        }

        mTempRect.set(0, 0, focused.getWidth(), focused.getHeight());
        mTempRect2.set(0, 0, next.getWidth(), next.getHeight());
        offsetDescendantRectToMyCoords(focused, mTempRect);
        offsetDescendantRectToMyCoords(next, mTempRect2);
        final int rtl = mLayout.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL ? -1 : 1;
        int rightness = 0;
        if ((mTempRect.left < mTempRect2.left || mTempRect.right <= mTempRect2.left) && mTempRect.right < mTempRect2.right) {
            rightness = 1;
        } else if ((mTempRect.right > mTempRect2.right || mTempRect.left >= mTempRect2.right) && mTempRect.left > mTempRect2.left) {
            rightness = -1;
        }
        int downness = 0;
        if ((mTempRect.top < mTempRect2.top || mTempRect.bottom <= mTempRect2.top) && mTempRect.bottom < mTempRect2.bottom) {
            downness = 1;
        } else if ((mTempRect.bottom > mTempRect2.bottom || mTempRect.top >= mTempRect2.bottom) && mTempRect.top > mTempRect2.top) {
            downness = -1;
        }
        switch (direction) {
            case View.FOCUS_LEFT:
                return rightness < 0;
            case View.FOCUS_RIGHT:
                return rightness > 0;
            case View.FOCUS_UP:
                return downness < 0;
            case View.FOCUS_DOWN:
                return downness > 0;
            case View.FOCUS_FORWARD:
                return downness > 0 || (downness == 0 && rightness * rtl >= 0);
            case View.FOCUS_BACKWARD:
                return downness < 0 || (downness == 0 && rightness * rtl <= 0);
        }
        throw new IllegalArgumentException("Invalid direction: " + direction + exceptionLabel());
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (!mLayout.onRequestChildFocus(this, mState, child, focused) && focused != null) {
            requestChildOnScreen(child, focused);
        }
        super.requestChildFocus(child, focused);
    }

    private void requestChildOnScreen(View child, View focused) {
        View rectView = (focused != null) ? focused : child;
        mTempRect.set(0, 0, rectView.getWidth(), rectView.getHeight());

        final ViewGroup.LayoutParams focusedLayoutParams = rectView.getLayoutParams();
        if (focusedLayoutParams instanceof LayoutParams) {
            final LayoutParams lp = (LayoutParams) focusedLayoutParams;
            if (!lp.mInsetsDirty) {
                final Rect insets = lp.mDecorInsets;
                mTempRect.left -= insets.left;
                mTempRect.right += insets.right;
                mTempRect.top -= insets.top;
                mTempRect.bottom += insets.bottom;
            }
        }

        if (focused != null) {
            offsetDescendantRectToMyCoords(focused, mTempRect);
            offsetRectIntoDescendantCoords(child, mTempRect);
        }
        mLayout.requestChildRectangleOnScreen(this, child, mTempRect, !mFirstLayoutComplete, (focused == null));
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        return mLayout.requestChildRectangleOnScreen(this, child, rect, immediate);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (mLayout == null || !mLayout.onAddFocusables(this, views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (isComputingLayout()) {
            return false;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLayoutOrScrollCounter = 0;
        mIsAttached = true;
        mFirstLayoutComplete = mFirstLayoutComplete && !isLayoutRequested();
        if (mLayout != null) {
            mLayout.dispatchAttachedToWindow(this);
        }
        mPostedAnimatorRunner = false;

        if (ALLOW_THREAD_GAP_WORK) {
            mGapWorker = SeslGapWorker.sGapWorker.get();
            if (mGapWorker == null) {
                mGapWorker = new SeslGapWorker();

                Display display = ViewCompat.getDisplay(this);
                float refreshRate = 60.0f;
                if (!isInEditMode() && display != null) {
                    float displayRefreshRate = display.getRefreshRate();
                    if (displayRefreshRate >= 30.0f) {
                        refreshRate = displayRefreshRate;
                    }
                }
                mGapWorker.mFrameIntervalNs = (long) (1000000000 / refreshRate);
                SeslGapWorker.sGapWorker.set(mGapWorker);
            }
            mGapWorker.add(this);
            if (mLayout != null && mLayout.getLayoutDirection() == 1 && mFastScroller != null) {
                mFastScroller.setScrollbarPosition(getVerticalScrollbarPosition());
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mItemAnimator != null) {
            mItemAnimator.endAnimations();
        }
        stopScroll();
        mIsAttached = false;
        if (mLayout != null) {
            mLayout.dispatchDetachedFromWindow(this, mRecycler);
        }
        mPendingAccessibilityImportanceChange.clear();
        removeCallbacks(mItemAnimatorRunner);
        mViewInfoStore.onDetach();

        if (ALLOW_THREAD_GAP_WORK && mGapWorker != null) {
            mGapWorker.remove(this);
            mGapWorker = null;
        }
    }

    @Override
    public boolean isAttachedToWindow() {
        return mIsAttached;
    }

    void assertInLayoutOrScroll(String message) {
        if (!isComputingLayout()) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method unless RecyclerView is "
                        + "computing a layout or scrolling" + exceptionLabel());
            }
            throw new IllegalStateException(message + exceptionLabel());

        }
    }

    void assertNotInLayoutOrScroll(String message) {
        if (isComputingLayout()) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method while RecyclerView is "
                        + "computing a layout or scrolling" + exceptionLabel());
            }
            throw new IllegalStateException(message);
        }
        if (mDispatchScrollCounter > 0) {
            LogUtils.w(TAG, "Cannot call this method in a scroll callback. Scroll callbacks might"
                            + "be run during a measure & layout pass where you cannot change the"
                            + "RecyclerView data. Any method call that might change the structure"
                            + "of the RecyclerView or the adapter contents should be postponed to"
                            + "the next frame.",
                    new IllegalStateException("" + exceptionLabel()));
        }
    }

    private void adjustNestedScrollRange() {
        getLocationInWindow(mWindowOffsets);
        mRemainNestedScrollRange = mNestedScrollRange - (mInitialTopOffsetOfScreen - mWindowOffsets[1]);
        if (mInitialTopOffsetOfScreen - mWindowOffsets[1] < 0) {
            mNestedScrollRange = mRemainNestedScrollRange;
            mInitialTopOffsetOfScreen = mWindowOffsets[1];
        }
    }

    private void adjustNestedScrollRangeBy(int offset) {
        if (!mHasNestedScrollRange) {
            return;
        }
        if (!canScrollUp() || mRemainNestedScrollRange != 0) {
            mRemainNestedScrollRange -= offset;
            if (mRemainNestedScrollRange < 0) {
                mRemainNestedScrollRange = 0;
            } else if (mRemainNestedScrollRange > mNestedScrollRange) {
                mRemainNestedScrollRange = mNestedScrollRange;
            }
        }
    }

    public void seslSetGoToTopEnabled(boolean enable) {
        seslSetGoToTopEnabled(enable, true);
    }

    public void seslSetGoToTopEnabled(boolean enable, boolean isWhite) {
        mGoToTopImage = isWhite ? mGoToTopImageLight : mContext.getResources().getDrawable(R.drawable.sesl_list_go_to_top_dark, mContext.getTheme());
        if (mGoToTopImage != null) {
            mEnableGoToTop = enable;
            mGoToTopImage.setCallback(enable ? this : null);
            mGoToTopFadeInAnimator = ValueAnimator.ofInt(new int[]{0, 255});
            mGoToTopFadeInAnimator.setDuration(333);
            mGoToTopFadeInAnimator.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
            mGoToTopFadeInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        mGoToTopImage.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                    } catch (Exception e) { }
                }
            });
            mGoToTopFadeOutAnimator = ValueAnimator.ofInt(new int[]{0, 255});
            mGoToTopFadeOutAnimator.setDuration(333);
            mGoToTopFadeOutAnimator.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
            mGoToTopFadeOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        mGoToTopImage.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                        invalidate();
                    } catch (Exception e) { }
                }
            });
            mGoToTopFadeOutAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    try {
                        mShowFadeOutGTP = 1;
                    } catch (Exception e) { }
                }

                public void onAnimationEnd(Animator animation) {
                    try {
                        mShowFadeOutGTP = 2;
                        setupGoToTop(0);
                    } catch (Exception e) { }
                }

                public void onAnimationRepeat(Animator animation) { }

                public void onAnimationCancel(Animator animation) { }
            });
        }
    }

    void showGoToTop() {
        if (mEnableGoToTop && canScrollUp() && mGoToTopState != 2) {
            setupGoToTop(1);
            autoHide(1);
        }
    }

    public void seslSetOutlineStrokeEnabled(boolean draw) {
        seslSetOutlineStrokeEnabled(draw, draw);
    }

    public void seslSetOutlineStrokeEnabled(boolean outline, boolean isWhite) {
        if (mLayout instanceof SeslLinearLayoutManager) {
            mDrawOutlineStroke = outline;
            mDrawWhiteTheme = isWhite;
            mSeslRoundedCorner = new SeslSubheaderRoundedCorner(getContext(), isWhite);
            mSeslRoundedCorner.setRoundedCorners(12);
            if (!mDrawWhiteTheme) {
                mRectPaint.setColor(getResources().getColor(R.color.sesl_round_and_bgcolor_dark, mContext.getTheme()));
            }
            requestLayout();
        }
    }

    public void seslSetLastItemOutlineStrokeEnabled(boolean outline) {
        if (mLayout instanceof SeslLinearLayoutManager) {
            mDrawLastItemOutlineStoke = outline;
            requestLayout();
        }
    }

    public void seslSetLastOutlineStrokeEnabled(boolean draw) {
        mDrawLastOutLineStroke = draw;
    }

    public void seslSetFillBottomEnabled(boolean draw) {
        if (mLayout instanceof SeslLinearLayoutManager) {
            mDrawRect = draw;
            if (!mDrawWhiteTheme) {
                mRectPaint.setColor(getResources().getColor(R.color.sesl_round_and_bgcolor_dark, mContext.getTheme()));
            }
            requestLayout();
        }
    }

    public void seslSetFillBottomColor(int color) {
        mRectPaint.setColor(color);
        if (!mDrawWhiteTheme) {
            mSeslRoundedCorner.setRoundedCornerColor(15, color);
        }
    }

    public boolean verifyDrawable(Drawable dr) {
        return mGoToTopImage == dr || super.verifyDrawable(dr);
    }

    private boolean isTalkBackIsRunning() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            String enabledServices = Settings.Secure.getString(getContext().getContentResolver(), "enabled_accessibility_services");
            if (enabledServices != null && (enabledServices.matches("(?i).*com.samsung.accessibility/com.samsung.android.app.talkback.TalkBackService.*") || enabledServices.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*") || enabledServices.matches("(?i).*com.samsung.accessibility/com.samsung.accessibility.universalswitch.UniversalSwitchService.*"))) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupportGotoTop() {
        return !isTalkBackIsRunning() && mEnableGoToTop;
    }

    private void playGotoToFadeOut() {
        if (!mGoToTopFadeOutAnimator.isRunning()) {
            if (mGoToTopFadeInAnimator.isRunning()) {
                mGoToTopFadeOutAnimator.cancel();
            }
            mGoToTopFadeOutAnimator.setIntValues(new int[]{mGoToTopImage.getAlpha(), 0});
            mGoToTopFadeOutAnimator.start();
        }
    }

    private void playGotoToFadeIn() {
        if (!mGoToTopFadeInAnimator.isRunning()) {
            if (mGoToTopFadeOutAnimator.isRunning()) {
                mGoToTopFadeOutAnimator.cancel();
            }
            mGoToTopFadeInAnimator.setIntValues(new int[]{mGoToTopImage.getAlpha(), 255});
            mGoToTopFadeInAnimator.start();
        }
    }

    private void autoHide(int when) {
        if (mEnableGoToTop) {
            if (when == 0) {
                if (!seslIsFastScrollerEnabled()) {
                    removeCallbacks(mAutoHide);
                    postDelayed(mAutoHide, (long) GO_TO_TOP_HIDE);
                }
            } else if (when == 1) {
                removeCallbacks(mAutoHide);
                postDelayed(mAutoHide, (long) GO_TO_TOP_HIDE);
            }
        }
    }

    private boolean isNavigationBarHide(Context context) {
        return !ViewUtils.isSupportSoftNavigationBar(context) || ViewUtils.isVisibleNaviBar(context);
    }

    private void setupGoToTop(int where) {
        if (mEnableGoToTop) {
            removeCallbacks(mAutoHide);

            if (where == 1 && !canScrollUp()) {
                where = 0;
            }
            if (where != -1 || !mSizeChnage) {
                if (where == -1 && (canScrollUp() || canScrollDown())) {
                    where = 1;
                }
            } else if (canScrollUp() || canScrollDown()) {
                where = mGoToTopLastState;
            } else {
                where = 0;
            }
            if (where != 0) {
                removeCallbacks(mGoToToFadeOutRunnable);
            } else if (where != 1) {
                removeCallbacks(mGoToToFadeInRunnable);
            }
            if (mShowFadeOutGTP == 0 && where == 0 && mGoToTopLastState != 0) {
                post(mGoToToFadeOutRunnable);
            }
            if (where != 2) {
                mGoToTopImage.setState(StateSet.NOTHING);
            }
            mGoToTopState = where;
            int w = getWidth();
            int h = getHeight();
            int centerX = getPaddingLeft() + (((w - getPaddingLeft()) - getPaddingRight()) / 2);
            switch (where) {
                case 0:
                    if (mShowFadeOutGTP == 2) {
                        mGoToTopRect = new Rect(0, 0, 0, 0);
                    }
                    break;
                case 1:
                case 2:
                    removeCallbacks(mGoToToFadeOutRunnable);
                    mGoToTopRect = new Rect(centerX - (mGoToTopSize / 2), (h - mGoToTopSize) - mGoToTopBottomPadding, (mGoToTopSize / 2) + centerX, h - mGoToTopBottomPadding);
                    break;
            }
            if (mShowFadeOutGTP == 2) {
                mShowFadeOutGTP = 0;
            }
            mGoToTopImage.setBounds(mGoToTopRect);
            if (where == 1 && (mGoToTopLastState == 0 || mGoToTopImage.getAlpha() == 0 || mSizeChnage)) {
                post(mGoToToFadeInRunnable);
            }
            mSizeChnage = false;
            mGoToTopLastState = mGoToTopState;
        }
    }

    private void drawGoToTop(Canvas canvas) {
        int scrollY = getScrollY();
        int restoreCount = canvas.save();
        canvas.translate(0.0f, (float) scrollY);
        if (mGoToTopState != 0 && !canScrollUp()) {
            setupGoToTop(0);
        }
        mGoToTopImage.draw(canvas);
        canvas.restoreToCount(restoreCount);
    }

    public int seslGetHoverBottomPadding() {
        return mHoverBottomAreaHeight;
    }

    public void seslSetHoverBottomPadding(int padding) {
        mHoverBottomAreaHeight = padding;
    }

    public int seslGetHoverTopPadding() {
        return mHoverTopAreaHeight;
    }

    public void seslSetHoverTopPadding(int padding) {
        mHoverTopAreaHeight = padding;
    }

    public int seslGetGoToTopBottomPadding() {
        return mGoToTopBottomPadding;
    }

    public void seslSetGoToTopBottomPadding(int padding) {
        mGoToTopBottomPadding = padding;
        if (mGoToTopState == 1) {
            setupGoToTop(-1);
            autoHide(1);
        }
    }

    public void seslSetOnGoToTopClickListener(SeslOnGoToTopClickListener listener) {
        mSeslOnGoToTopClickListener = listener;
    }

    public void addOnItemTouchListener(OnItemTouchListener listener) {
        mOnItemTouchListeners.add(listener);
    }

    public void removeOnItemTouchListener(OnItemTouchListener listener) {
        mOnItemTouchListeners.remove(listener);
        if (mActiveOnItemTouchListener == listener) {
            mActiveOnItemTouchListener = null;
        }
    }

    private boolean dispatchOnItemTouchIntercept(MotionEvent e) {
        final int action = e.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_DOWN) {
            mActiveOnItemTouchListener = null;
        }

        final int listenerCount = mOnItemTouchListeners.size();
        for (int i = 0; i < listenerCount; i++) {
            final OnItemTouchListener listener = mOnItemTouchListeners.get(i);
            if (listener.onInterceptTouchEvent(this, e) && action != MotionEvent.ACTION_CANCEL) {
                mActiveOnItemTouchListener = listener;
                return true;
            }
        }
        return false;
    }

    private boolean dispatchOnItemTouch(MotionEvent e) {
        final int action = e.getAction();
        if (mActiveOnItemTouchListener != null) {
            if (action == MotionEvent.ACTION_DOWN) {
                mActiveOnItemTouchListener = null;
            } else {
                mActiveOnItemTouchListener.onTouchEvent(this, e);
                if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                    mActiveOnItemTouchListener = null;
                }
                return true;
            }
        }

        if (action != MotionEvent.ACTION_DOWN) {
            final int listenerCount = mOnItemTouchListeners.size();
            for (int i = 0; i < listenerCount; i++) {
                final OnItemTouchListener listener = mOnItemTouchListeners.get(i);
                if (listener.onInterceptTouchEvent(this, e)) {
                    mActiveOnItemTouchListener = listener;
                    return true;
                }
            }
        }
        return false;
    }

    public void seslStartLongPressMultiSelection() {
        mIsLongPressMultiSelection = true;
    }

    public void seslSetCtrlkeyPressed(boolean pressed) {
        mIsCtrlKeyPressed = pressed;
    }

    private void updateLongPressMultiSelection(int x, int y, boolean fromUserTouch) {
        if (mIsFirstMultiSelectionMove) {
            mPenDragStartX = x;
            mPenDragStartY = y;
            mPenTrackedChild = findChildViewUnder((float) x, (float) y);
            if (mPenTrackedChild == null) {
                mPenTrackedChild = seslFindNearChildViewUnder((float) x, (float) y);
                if (mPenTrackedChild == null) {
                    LogUtils.e(TAG, "updateLongPressMultiSelection, mPenTrackedChild is NULL");
                    mIsFirstMultiSelectionMove = false;
                    return;
                }
            }
            if (mLongPressMultiSelectionListener != null) {
                mLongPressMultiSelectionListener.onLongPressMultiSelectionStarted(x, y);
            }
            mPenTrackedChildPosition = getChildLayoutPosition(mPenTrackedChild);
            mPenDragSelectedViewPosition = mPenTrackedChildPosition;
            mPenDistanceFromTrackedChildTop = mPenDragStartY - mPenTrackedChild.getTop();
            mIsFirstMultiSelectionMove = false;
        }

        int contentTop;
        int contentBottom;
        int startPosition;
        int endPosition;
        if (mIsEnabledPaddingInHoverScroll) {
            contentTop = mListPadding.top;
            contentBottom = getHeight() - mListPadding.bottom;
        } else {
            contentTop = 0;
            contentBottom = getHeight();
        }

        mPenDragEndX = x;
        mPenDragEndY = y;
        if (mPenDragEndY < 0) {
            mPenDragEndY = 0;
        } else if (mPenDragEndY > contentBottom) {
            mPenDragEndY = contentBottom;
        }

        View touchedView = findChildViewUnder((float) mPenDragEndX, (float) mPenDragEndY);
        if (touchedView == null) {
            touchedView = seslFindNearChildViewUnder((float) mPenDragEndX, (float) mPenDragEndY);
            if (touchedView == null) {
                LogUtils.e(TAG, "updateLongPressMultiSelection, touchedView is NULL");
                return;
            }
        }
        int touchedPosition = getChildLayoutPosition(touchedView);
        if (touchedPosition == NO_POSITION) {
            LogUtils.e(TAG, "touchedPosition is NO_POSITION");
            return;
        }
        mPenDragSelectedViewPosition = touchedPosition;
        if (mPenTrackedChildPosition < mPenDragSelectedViewPosition) {
            startPosition = mPenTrackedChildPosition;
            endPosition = mPenDragSelectedViewPosition;
        } else {
            startPosition = mPenDragSelectedViewPosition;
            endPosition = mPenTrackedChildPosition;
        }
        mPenDragBlockLeft = mPenDragStartX < mPenDragEndX ? mPenDragStartX : mPenDragEndX;
        mPenDragBlockTop = mPenDragStartY < mPenDragEndY ? mPenDragStartY : mPenDragEndY;
        mPenDragBlockRight = mPenDragEndX > mPenDragStartX ? mPenDragEndX : mPenDragStartX;
        mPenDragBlockBottom = mPenDragEndX > mPenDragStartX ? mPenDragEndX : mPenDragStartX;

        int count = mChildHelper.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child != null) {
                mPenDragSelectedViewPosition = getChildLayoutPosition(child);
                if (child.getVisibility() == View.VISIBLE) {
                    boolean needSelected = false;
                    if (startPosition <= mPenDragSelectedViewPosition && mPenDragSelectedViewPosition <= endPosition && mPenDragSelectedViewPosition != mPenTrackedChildPosition) {
                        needSelected = true;
                    }
                    if (needSelected) {
                        if (mPenDragSelectedViewPosition != -1 && !mPenDragSelectedItemArray.contains(mPenDragSelectedViewPosition)) {
                            mPenDragSelectedItemArray.add(mPenDragSelectedViewPosition);
                            if (mLongPressMultiSelectionListener != null) {
                                mLongPressMultiSelectionListener.onItemSelected(this, child, mPenDragSelectedViewPosition, getChildItemId(child));
                            }
                        }
                    } else if (mPenDragSelectedViewPosition != -1 && mPenDragSelectedItemArray.contains(mPenDragSelectedViewPosition)) {
                        mPenDragSelectedItemArray.remove(Integer.valueOf(mPenDragSelectedViewPosition));
                        if (mLongPressMultiSelectionListener != null) {
                            mLongPressMultiSelectionListener.onItemSelected(this, child, mPenDragSelectedViewPosition, getChildItemId(child));
                        }
                    }
                }
            }
        }

        if (fromUserTouch) {
            if (y <= mHoverTopAreaHeight + contentTop) {
                if (!mHoverAreaEnter) {
                    mHoverAreaEnter = true;
                    mHoverScrollStartTime = System.currentTimeMillis();
                    if (mScrollListener != null) {
                        mScrollListener.onScrollStateChanged(this, 1);
                    }
                }
                if (!mHoverHandler.hasMessages(0)) {
                    mHoverRecognitionStartTime = System.currentTimeMillis();
                    mHoverScrollDirection = 2;
                    mHoverHandler.sendEmptyMessage(0);
                }
            } else if (y >= (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange) {
                if (!mHoverAreaEnter) {
                    mHoverAreaEnter = true;
                    mHoverScrollStartTime = System.currentTimeMillis();
                    if (mScrollListener != null) {
                        mScrollListener.onScrollStateChanged(this, 1);
                    }
                }
                if (!mHoverHandler.hasMessages(0)) {
                    mHoverRecognitionStartTime = System.currentTimeMillis();
                    mHoverScrollDirection = 1;
                    mHoverHandler.sendEmptyMessage(0);
                }
            } else {
                if (mHoverAreaEnter && mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(this, 0);
                }
                mHoverScrollStartTime = 0;
                mHoverRecognitionStartTime = 0;
                mHoverAreaEnter = false;
                if (mHoverHandler.hasMessages(0)) {
                    mHoverHandler.removeMessages(0);
                    if (mScrollState == 1) {
                        setScrollState(0);
                    }
                }
                mIsHoverOverscrolled = false;
            }
        }
        invalidate();
    }

    private void multiSelection(int x, int y, int contentTop, int contentBottom, boolean needToScroll) {
        if (mIsNeedPenSelection) {
            if (mIsFirstPenMoveEvent) {
                mPenDragStartX = x;
                mPenDragStartY = y;
                mIsPenPressed = true;
                mPenTrackedChild = findChildViewUnder((float) x, (float) y);
                if (mPenTrackedChild == null) {
                    mPenTrackedChild = seslFindNearChildViewUnder((float) x, (float) y);
                    if (mPenTrackedChild == null) {
                        LogUtils.e(TAG, "multiSelection, mPenTrackedChild is NULL");
                        mIsPenPressed = false;
                        mIsFirstPenMoveEvent = false;
                        return;
                    }
                }
                if (mOnMultiSelectedListener != null) {
                    mOnMultiSelectedListener.onMultiSelectStart(x, y);
                }
                mPenTrackedChildPosition = getChildLayoutPosition(mPenTrackedChild);
                mPenDistanceFromTrackedChildTop = mPenDragStartY - mPenTrackedChild.getTop();
                mIsFirstPenMoveEvent = false;
            }
            if (mPenDragStartX == 0 && mPenDragStartY == 0) {
                mPenDragStartX = x;
                mPenDragStartY = y;
                if (mOnMultiSelectedListener != null) {
                    mOnMultiSelectedListener.onMultiSelectStart(x, y);
                }
                mIsPenPressed = true;
            }
            mPenDragEndX = x;
            mPenDragEndY = y;
            if (mPenDragEndY < 0) {
                mPenDragEndY = 0;
            } else if (mPenDragEndY > contentBottom) {
                mPenDragEndY = contentBottom;
            }
            mPenDragBlockLeft = mPenDragStartX < mPenDragEndX ? mPenDragStartX : mPenDragEndX;
            mPenDragBlockTop = mPenDragStartY < mPenDragEndY ? mPenDragStartY : mPenDragEndY;
            mPenDragBlockRight = mPenDragEndX > mPenDragStartX ? mPenDragEndX : mPenDragStartX;
            mPenDragBlockBottom = mPenDragEndY > mPenDragStartY ? mPenDragEndY : mPenDragStartY;
            needToScroll = true;
        }
        if (needToScroll) {
            if (y <= mHoverTopAreaHeight + contentTop) {
                if (!mHoverAreaEnter) {
                    mHoverAreaEnter = true;
                    mHoverScrollStartTime = System.currentTimeMillis();
                    if (mScrollListener != null) {
                        mScrollListener.onScrollStateChanged(this, 1);
                    }
                }
                if (!mHoverHandler.hasMessages(0)) {
                    mHoverRecognitionStartTime = System.currentTimeMillis();
                    mHoverScrollDirection = 2;
                    mHoverHandler.sendEmptyMessage(0);
                }
            } else if (y >= (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange) {
                if (!mHoverAreaEnter) {
                    mHoverAreaEnter = true;
                    mHoverScrollStartTime = System.currentTimeMillis();
                    if (mScrollListener != null) {
                        mScrollListener.onScrollStateChanged(this, 1);
                    }
                }
                if (!mHoverHandler.hasMessages(0)) {
                    mHoverRecognitionStartTime = System.currentTimeMillis();
                    mHoverScrollDirection = 1;
                    mHoverHandler.sendEmptyMessage(0);
                }
            } else {
                if (mHoverAreaEnter && mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(this, 0);
                }
                mHoverScrollStartTime = 0;
                mHoverRecognitionStartTime = 0;
                mHoverAreaEnter = false;
                if (mHoverHandler.hasMessages(0)) {
                    mHoverHandler.removeMessages(0);
                    if (mScrollState == 1) {
                        setScrollState(0);
                    }
                }
                mIsHoverOverscrolled = false;
            }
            if (mIsPenDragBlockEnabled) {
                invalidate();
            }
        }
    }

    private void multiSelectionEnd(int x, int y) {
        if (mIsPenPressed && mOnMultiSelectedListener != null) {
            mOnMultiSelectedListener.onMultiSelectStop(x, y);
        }
        mIsPenPressed = false;
        mIsFirstPenMoveEvent = true;
        mPenDragSelectedViewPosition = -1;
        mPenDragSelectedItemArray.clear();
        mPenDragStartX = 0;
        mPenDragStartY = 0;
        mPenDragEndX = 0;
        mPenDragEndY = 0;
        mPenDragBlockLeft = 0;
        mPenDragBlockTop = 0;
        mPenDragBlockRight = 0;
        mPenDragBlockBottom = 0;
        mPenTrackedChild = null;
        mPenDistanceFromTrackedChildTop = 0;
        if (mIsPenDragBlockEnabled) {
            invalidate();
        }
        if (mHoverHandler.hasMessages(0)) {
            mHoverHandler.removeMessages(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mItemAnimator != null && !mItemAnimator.isRunning()) {
            if (mItemAnimator.getItemAnimationTypeInternal() == 2) {
                LogUtils.d(TAG, "dispatchTouchEvent : itemAnimator is running, return..");
                return true;
            }
        }

        if (mLayout == null) {
            LogUtils.d(TAG, "No layout manager attached; skipping gototop & multiselection");
            return super.dispatchTouchEvent(e);
        }

        final int action = e.getActionMasked();
        final int x = (int) (e.getX() + 0.5f);
        final int y = (int) (e.getY() + 0.5f);
        int contentTop = 0;
        int contentBottom = 0;
        final boolean needToScroll = false;

        if (mPenDragSelectedItemArray == null) {
            mPenDragSelectedItemArray = new ArrayList();
        }

        if (mIsEnabledPaddingInHoverScroll) {
            contentTop = mListPadding.top;
            contentBottom = getHeight() - mListPadding.bottom;
        } else {
            contentBottom = getHeight();
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (isSupportGotoTop()) {
                    mGoToTopMoved = false;
                    mGoToToping = false;
                }

                if (isSupportGotoTop() && mGoToTopState != 2 && mGoToTopRect.contains(x, y)) {
                    setupGoToTop(2);
                    mGoToTopImage.setHotspot((float) x, (float) y);
                    mGoToTopImage.setState(new int[] {android.R.attr.state_pressed, android.R.attr.state_enabled, android.R.attr.state_selected});
                    return true;
                }

                if (mIsCtrlKeyPressed && e.getToolType(0) == 3) {
                    mIsCtrlMultiSelection = true;
                    mIsNeedPenSelection = true;
                    multiSelection(x, y, contentTop, contentBottom, needToScroll);
                    return true;
                }

                if (mIsLongPressMultiSelection) {
                    mIsLongPressMultiSelection = false;
                }
            } break;

            case MOTION_EVENT_ACTION_PEN_DOWN: {
                if ((boolean) Utils.genericInvokeMethod(TextView.class, "semIsTextSelectionProgressing")) {
                    mIsNeedPenSelection = false;
                } else {
                    mIsNeedPenSelection = mIsPenSelectionEnabled;
                }

                if (mPenDragSelectedItemArray == null) {
                    mPenDragSelectedItemArray = new ArrayList();
                }
            } break;

            case MotionEvent.ACTION_MOVE: {
                if (mIsCtrlKeyPressed) {
                    multiSelection(x, y, contentTop, contentBottom, needToScroll);
                    return true;
                }

                if (mIsLongPressMultiSelection) {
                    updateLongPressMultiSelection(x, y, true);
                    return true;
                }

                if (isSupportGotoTop() && mGoToTopState == 2) {
                    if (!mGoToTopRect.contains(x, y)) {
                        mGoToTopState = 1;
                        mGoToTopImage.setState(StateSet.NOTHING);
                        autoHide(1);
                        mGoToTopMoved = true;
                    }
                    return true;
                }
            } break;

            case MOTION_EVENT_ACTION_PEN_MOVE: {
                multiSelection(x, y, contentTop, contentBottom, needToScroll);
            } break;

            case MotionEvent.ACTION_CANCEL: {
                if (isSupportGotoTop() && mGoToTopState == 0) {
                    mGoToTopImage.setState(StateSet.NOTHING);
                }
            }

            case MotionEvent.ACTION_UP: {
                if (mIsCtrlMultiSelection) {
                    multiSelectionEnd(x, y);
                    mIsCtrlMultiSelection = false;
                    return true;
                }

                if (mIsLongPressMultiSelection) {
                    if (mLongPressMultiSelectionListener != null) {
                        mLongPressMultiSelectionListener.onLongPressMultiSelectionEnded(x ,y);
                    }

                    mIsFirstMultiSelectionMove = true;
                    mPenDragSelectedViewPosition = -1;

                    mPenDragStartX = 0;
                    mPenDragStartY = 0;
                    mPenDragEndX = 0;
                    mPenDragEndY = 0;
                    mPenDragBlockLeft = 0;
                    mPenDragBlockTop = 0;
                    mPenDragBlockRight = 0;
                    mPenDragBlockBottom = 0;

                    mPenDragSelectedItemArray.clear();

                    mPenTrackedChild = null;
                    mPenDistanceFromTrackedChildTop = 0;

                    if (mHoverHandler.hasMessages(0)) {
                        mHoverHandler.removeMessages(0);
                        if (mScrollState == 1) {
                            setScrollState(1);
                        }
                    }
                    mIsHoverOverscrolled = false;

                    invalidate();
                    mIsLongPressMultiSelection = false;
                }
            }

            case MOTION_EVENT_ACTION_PEN_UP: {
                if (isSupportGotoTop() && mGoToTopState == 2) {
                    if (canScrollUp()) {
                        if (mSeslOnGoToTopClickListener != null && mSeslOnGoToTopClickListener.onGoToTopClick(this)) {
                            return true;
                        }

                        LogUtils.d(TAG, " can scroll top ");
                        final int childCount = getChildCount();
                        if (computeVerticalScrollOffset() != 0) {
                            stopScroll();
                            // TODO merge Sammy StaggeredGridLayoutManager
                            //if (mLayout instanceof StaggeredGridLayoutManager) {
                            //    (StaggeredGridLayoutManager) mLayout.scrollToPositionWithOffset(0, 0);
                            //} else {
                                mGoToToping = true;
                                if (childCount > 0) {
                                    if (childCount < findFirstVisibleItemPosition()) {
                                        if (mLayout instanceof SeslLinearLayoutManager) {
                                            ((SeslLinearLayoutManager) mLayout).scrollToPositionWithOffset(childCount, 0);
                                        } else {
                                            scrollToPosition(childCount);
                                        }
                                    }
                                }

                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        smoothScrollToPosition(0);
                                    }
                                });
                            //}

                            if (mTopGlow != null) {
                                seslShowGoToTopEdge((float) getHeight() / 500f, (float) getWidth() / x, 0x96);
                            } else {
                                LogUtils.d(TAG, " There is no mTopGlow");
                            }
                        }
                    }
                    seslHideGoToTop();
                    playSoundEffect(0);
                    return true;
                }

                if (mGoToTopMoved) {
                    mGoToTopMoved = false;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.clear();
                    }
                }

                multiSelectionEnd(x, y);
            }
        }

        return super.dispatchTouchEvent(e);
    }

    public void seslShowGoToTopEdge(float deltaDistance, float displacement, int delayTime) {
        ensureTopGlow();
        mTopGlow.onPullCallOnRelease(deltaDistance, displacement, delayTime);
        invalidate(0, 0, getWidth(), 500);
    }

    public void seslHideGoToTop() {
        autoHide(0);
        mGoToTopImage.setState(StateSet.NOTHING);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mLayoutFrozen) {
            return false;
        }
        if (dispatchOnItemTouchIntercept(e)) {
            cancelTouch();
            return true;
        }

        if (mLayout == null) {
            return false;
        }

        final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = mLayout.canScrollVertically();
        final boolean isMouse = MotionEventCompat.isFromSource(e, 8194);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(e);

        final int action = e.getActionMasked();
        final int actionIndex = e.getActionIndex();
        final MotionEvent vtev = MotionEvent.obtain(e);

        if (mFastScroller != null && mFastScroller.onInterceptTouchEvent(e)) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mIgnoreMotionEventTillDown) {
                    mIgnoreMotionEventTillDown = false;
                }
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);

                if (mUsePagingTouchSlopForStylus) {
                    if (e.isFromSource(InputDeviceCompat.SOURCE_STYLUS)) {
                        mTouchSlop = mSeslPagingTouchSlop;
                    } else {
                        mTouchSlop = mSeslTouchSlop;
                    }
                }

                if (mScrollState == SCROLL_STATE_SETTLING) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                }

                mNestedOffsets[0] = mNestedOffsets[1] = 0;

                if (mHasNestedScrollRange) {
                    adjustNestedScrollRange();
                }

                int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                if (canScrollHorizontally) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
                }
                startNestedScroll(nestedScrollAxis, isMouse ? TYPE_NON_TOUCH : TYPE_TOUCH);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mScrollPointerId = e.getPointerId(actionIndex);
                mInitialTouchX = mLastTouchX = (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY(actionIndex) + 0.5f);
                break;

            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    LogUtils.e(TAG, "Error processing scroll; pointer index for id " + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;
                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > mTouchSlop) {
                        if (dx > 0) {
                            dx -= mTouchSlop;
                        } else {
                            dx += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }
                if (mScrollState == SCROLL_STATE_DRAGGING) {
                    mLastTouchX = x - mScrollOffset[0];
                    mLastTouchY = y - mScrollOffset[1];
                    if (!mGoToTopMoved && scrollByInternal(canScrollHorizontally ? dx : 0, canScrollVertically ? dx : 0,vtev)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (mGapWorker != null && dx != 0 && dy != 0) {
                        mGapWorker.postFromTraversal(this, dx, dy);
                    }
                }
                adjustNestedScrollRangeBy(dy);
            } break;

            case MotionEvent.ACTION_POINTER_UP: {
                onPointerUp(e);
            } break;

            case MotionEvent.ACTION_UP: {
                mVelocityTracker.clear();
                stopNestedScroll(isMouse ? TYPE_NON_TOUCH : TYPE_TOUCH);
            } break;

            case MotionEvent.ACTION_CANCEL: {
                cancelTouch();
            }
        }
        return mScrollState == SCROLL_STATE_DRAGGING;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final int listenerCount = mOnItemTouchListeners.size();
        for (int i = 0; i < listenerCount; i++) {
            final OnItemTouchListener listener = mOnItemTouchListeners.get(i);
            listener.onRequestDisallowInterceptTouchEvent(disallowIntercept);
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLayoutFrozen || mIgnoreMotionEventTillDown) {
            return false;
        }
        if (dispatchOnItemTouch(e)) {
            cancelTouch();
            return true;
        }
        if (mLayout == null) {
            return false;
        }

        mIsMouseWheel = false;

        final boolean canScrollHorizontally = mLayout.canScrollHorizontally();
        final boolean canScrollVertically = mLayout.canScrollVertically();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;

        final MotionEvent vtev = MotionEvent.obtain(e);
        final int action = e.getActionMasked();
        final int actionIndex = e.getActionIndex();

        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsets[0] = mNestedOffsets[1] = 0;
        }
        vtev.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);

        if (mFastScroller != null && mFastScroller.onTouchEvent(e)) {
            if (mFastScrollerEventListener != null) {
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    if (mFastScroller.getEffectState() == 1) {
                        mFastScrollerEventListener.onPressed(mFastScroller.getScrollY());
                    }
                }
                if (action == MotionEvent.ACTION_UP) {
                    if (mFastScroller.getEffectState() == 0) {
                        mFastScrollerEventListener.onReleased(mFastScroller.getScrollY());
                    }
                }
            }
            vtev.recycle();
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);

                if (!mHasNestedScrollRange) {
                    adjustNestedScrollRange();
                }

                int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                if (canScrollHorizontally) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
                }
                startNestedScroll(nestedScrollAxis, TYPE_TOUCH);
            } break;

            case MotionEvent.ACTION_POINTER_DOWN: {
                mScrollPointerId = e.getPointerId(actionIndex);
                mInitialTouchX = mLastTouchX = (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY(actionIndex) + 0.5f);
            } break;

            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    LogUtils.e(TAG, "Error processing scroll; pointer index for id " + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;

                if (dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset, TYPE_TOUCH)) {
                    dx -= mScrollConsumed[0];
                    dy -= mScrollConsumed[1];
                    vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                    mNestedOffsets[0] += mScrollOffset[0];
                    mNestedOffsets[1] += mScrollOffset[1];
                    adjustNestedScrollRangeBy(mScrollConsumed[1]);
                } else {
                    adjustNestedScrollRangeBy(dy);
                }

                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > mTouchSlop) {
                        if (dx > 0) {
                            dx -= mTouchSlop;
                        } else {
                            dx += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }

                if (mScrollState == SCROLL_STATE_DRAGGING) {
                    mLastTouchX = x - mScrollOffset[0];
                    mLastTouchY = y - mScrollOffset[1];

                    if (!mGoToTopMoved && scrollByInternal(canScrollHorizontally ? dx : 0, canScrollVertically ? dy : 0, vtev)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (mGapWorker != null && (dx != 0 || dy != 0)) {
                        mGapWorker.postFromTraversal(this, dx, dy);
                    }
                }
            } break;

            case MotionEvent.ACTION_POINTER_UP: {
                onPointerUp(e);
            } break;

            case MotionEvent.ACTION_UP: {
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                final float xvel = canScrollHorizontally ? -mVelocityTracker.getXVelocity(mScrollPointerId) : 0;
                final float yvel = canScrollVertically ? -mVelocityTracker.getYVelocity(mScrollPointerId) : 0;
                if (!((xvel != 0 || yvel != 0) && fling((int) xvel, (int) yvel))) {
                    setScrollState(SCROLL_STATE_IDLE);
                }
                LogUtils.d(TAG, "onTouchUp() velocity : " + yvel);
                resetTouch();
            } break;

            case MotionEvent.ACTION_CANCEL: {
                cancelTouch();
            } break;
        }

        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }

        vtev.recycle();
        return true;
    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
        stopNestedScroll(TYPE_TOUCH);
        releaseGlows();
    }

    private void cancelTouch() {
        resetTouch();
        setScrollState(SCROLL_STATE_IDLE);
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == mScrollPointerId) {
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = e.getPointerId(newIndex);
            mInitialTouchX = mLastTouchX = (int) (e.getX(newIndex) + 0.5f);
            mInitialTouchY = mLastTouchY = (int) (e.getY(newIndex) + 0.5f);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mLayout == null) {
            return false;
        }
        if (mLayoutFrozen) {
            return false;
        }
        if (event.getAction() == 8 /*MotionEventCompat.ACTION_SCROLL*/) {
            final float vScroll, hScroll;
            mIsMouseWheel = true;
            if ((event.getSource() & InputDeviceCompat.SOURCE_CLASS_POINTER) != 0) {
                if (mLayout.canScrollVertically()) {
                    vScroll = -event.getAxisValue(MotionEvent.AXIS_VSCROLL);
                } else {
                    vScroll = 0f;
                }
                if (mLayout.canScrollHorizontally()) {
                    hScroll = event.getAxisValue(MotionEvent.AXIS_HSCROLL);
                } else {
                    hScroll = 0f;
                }
            } else if ((event.getSource() & InputDeviceCompat.SOURCE_ROTARY_ENCODER) != 0) {
                final float axisScroll = event.getAxisValue(MotionEventCompat.AXIS_SCROLL);
                if (mLayout.canScrollVertically()) {
                    vScroll = -axisScroll;
                    hScroll = 0f;
                } else if (mLayout.canScrollHorizontally()) {
                    vScroll = 0f;
                    hScroll = axisScroll;
                } else {
                    vScroll = 0f;
                    hScroll = 0f;
                }
            } else {
                vScroll = 0f;
                hScroll = 0f;
            }

            if (vScroll != 0 || hScroll != 0) {
                final int axis = vScroll == 0 ? 1 : 2;
                startNestedScroll(axis, TYPE_NON_TOUCH);
                if (!dispatchNestedPreScroll((int) (hScroll * mScaledHorizontalScrollFactor), (int) (vScroll * mScaledVerticalScrollFactor), null, null, TYPE_NON_TOUCH)) {
                    scrollByInternal((int) (hScroll * mScaledHorizontalScrollFactor), (int) (vScroll * mScaledVerticalScrollFactor), event);
                }
            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mLayout == null) {
            defaultOnMeasure(widthSpec, heightSpec);
            return;
        }
        Rect listPadding = mListPadding;
        listPadding.left = getPaddingLeft();
        listPadding.right = getPaddingRight();
        listPadding.top = getPaddingTop();
        listPadding.bottom = getPaddingBottom();
        if (getResources().getDisplayMetrics().heightPixels < getMeasuredHeight()) {
            LogUtils.d(TAG, "h = " + getMeasuredHeight() + "auto = " + mLayout.isAutoMeasureEnabled() + ", fixedSize = " + mHasFixedSize);
            if (getParent() != null) {
                LogUtils.d(TAG, "p = " + getParent() + ", ph =" + ((View) getParent()).getMeasuredHeight());
            }
        }
        if (mLayout.isAutoMeasureEnabled()) {
            final int widthMode = MeasureSpec.getMode(widthSpec);
            final int heightMode = MeasureSpec.getMode(heightSpec);
            final boolean measureSpecModeIsExactly = widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY;
            mLayout.onMeasure(mRecycler, mState, widthSpec, heightSpec);
            if (measureSpecModeIsExactly || mAdapter == null) {
                return;
            }
            if (mState.mLayoutStep == State.STEP_START) {
                dispatchLayoutStep1();
            }
            mLayout.setMeasureSpecs(widthSpec, heightSpec);
            mState.mIsMeasuring = true;
            dispatchLayoutStep2();

            mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);

            if (mLayout.shouldMeasureTwice()) {
                mLayout.setMeasureSpecs(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
                mState.mIsMeasuring = true;
                dispatchLayoutStep2();
                mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
            }
        } else {
            if (mHasFixedSize) {
                mLayout.onMeasure(mRecycler, mState, widthSpec, heightSpec);
                return;
            }
            if (mAdapterUpdateDuringMeasure) {
                startInterceptRequestLayout();
                onEnterLayoutOrScroll();
                processAdapterUpdatesAndSetAnimationFlags();
                onExitLayoutOrScroll();

                if (mState.mRunPredictiveAnimations) {
                    mState.mInPreLayout = true;
                } else {
                    mAdapterHelper.consumeUpdatesInOnePass();
                    mState.mInPreLayout = false;
                }
                mAdapterUpdateDuringMeasure = false;
                stopInterceptRequestLayout(false);
            } else if (mState.mRunPredictiveAnimations) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
                return;
            }

            if (mAdapter != null) {
                mState.mItemCount = mAdapter.getItemCount();
            } else {
                mState.mItemCount = 0;
            }
            startInterceptRequestLayout();
            mLayout.onMeasure(mRecycler, mState, widthSpec, heightSpec);
            stopInterceptRequestLayout(false);
            mState.mInPreLayout = false;
        }
    }

    void defaultOnMeasure(int widthSpec, int heightSpec) {
        final int width = LayoutManager.chooseSize(widthSpec, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth(this));
        final int height = LayoutManager.chooseSize(heightSpec, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight(this));

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            invalidateGlows();
        }
        if (mFastScroller != null) {
            mFastScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void setItemAnimator(ItemAnimator animator) {
        if (mItemAnimator != null) {
            mItemAnimator.endAnimations();
            mItemAnimator.setListener(null);
        }
        mItemAnimator = animator;
        if (mItemAnimator != null) {
            mItemAnimator.setListener(mItemAnimatorListener);
        }
    }

    void onEnterLayoutOrScroll() {
        mLayoutOrScrollCounter++;
    }

    void onExitLayoutOrScroll() {
        onExitLayoutOrScroll(true);
    }

    void onExitLayoutOrScroll(boolean enableChangeEvents) {
        mLayoutOrScrollCounter--;
        if (mLayoutOrScrollCounter < 1) {
            if (DEBUG && mLayoutOrScrollCounter < 0) {
                throw new IllegalStateException("layout or scroll counter cannot go below zero." + "Some calls are not matching" + exceptionLabel());
            }
            mLayoutOrScrollCounter = 0;
            if (enableChangeEvents) {
                dispatchContentChangedIfNecessary();
                dispatchPendingImportantForAccessibilityChanges();
            }
        }
    }

    boolean isAccessibilityEnabled() {
        return mAccessibilityManager != null && mAccessibilityManager.isEnabled();
    }

    private void dispatchContentChangedIfNecessary() {
        final int flags = mEatenAccessibilityChangeFlags;
        mEatenAccessibilityChangeFlags = 0;
        if (flags != 0 && isAccessibilityEnabled()) {
            final AccessibilityEvent event = AccessibilityEvent.obtain();
            event.setEventType(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
            AccessibilityEventCompat.setContentChangeTypes(event, flags);
            sendAccessibilityEventUnchecked(event);
        }
    }

    public boolean isComputingLayout() {
        return mLayoutOrScrollCounter > 0;
    }

    boolean shouldDeferAccessibilityEvent(AccessibilityEvent event) {
        if (isComputingLayout()) {
            int type = 0;
            if (event != null) {
                type = AccessibilityEventCompat.getContentChangeTypes(event);
            }
            if (type == 0) {
                type = AccessibilityEventCompat.CONTENT_CHANGE_TYPE_UNDEFINED;
            }
            mEatenAccessibilityChangeFlags |= type;
            return true;
        }
        return false;
    }

    @Override
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (shouldDeferAccessibilityEvent(event)) {
            return;
        }
        super.sendAccessibilityEventUnchecked(event);
    }

    public ItemAnimator getItemAnimator() {
        return mItemAnimator;
    }

    void postAnimationRunner() {
        if (!mPostedAnimatorRunner && mIsAttached) {
            ViewCompat.postOnAnimation(this, mItemAnimatorRunner);
            mPostedAnimatorRunner = true;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        return (mItemAnimator != null && mLayout.supportsPredictiveItemAnimations());
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (mDataSetHasChangedAfterLayout) {
            mAdapterHelper.reset();
            if (mDispatchItemsChangedEvent) {
                mLayout.onItemsChanged(this);
            }
        }
        if (predictiveItemAnimationsEnabled()) {
            mAdapterHelper.preProcess();
        } else {
            mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean animationTypeSupported = mItemsAddedOrRemoved || mItemsChanged;
        mState.mRunSimpleAnimations = mFirstLayoutComplete && mItemAnimator != null && (mDataSetHasChangedAfterLayout || animationTypeSupported || mLayout.mRequestedSimpleAnimations) && (!mDataSetHasChangedAfterLayout || mAdapter.hasStableIds());
        mState.mRunPredictiveAnimations = mState.mRunSimpleAnimations && animationTypeSupported && !mDataSetHasChangedAfterLayout && predictiveItemAnimationsEnabled();
    }

    void dispatchLayout() {
        if (mAdapter == null) {
            LogUtils.e(TAG, "No adapter attached; skipping layout");
            return;
        }
        if (mLayout == null) {
            LogUtils.e(TAG, "No layout manager attached; skipping layout");
            return;
        }
        mState.mIsMeasuring = false;
        if (mState.mLayoutStep == State.STEP_START) {
            dispatchLayoutStep1();
            mLayout.setExactMeasureSpecsFrom(this);
            dispatchLayoutStep2();
        } else if (mAdapterHelper.hasUpdates() || mLayout.getWidth() != getWidth() || mLayout.getHeight() != getHeight()) {
            mLayout.setExactMeasureSpecsFrom(this);
            dispatchLayoutStep2();
        } else {
            mLayout.setExactMeasureSpecsFrom(this);
        }
        dispatchLayoutStep3();
    }

    private void saveFocusInfo() {
        View child = null;
        if (mPreserveFocusAfterLayout && hasFocus() && mAdapter != null) {
            child = getFocusedChild();
        }

        final ViewHolder focusedVh = child == null ? null : findContainingViewHolder(child);
        if (focusedVh == null) {
            resetFocusInfo();
        } else {
            mState.mFocusedItemId = mAdapter.hasStableIds() ? focusedVh.getItemId() : NO_ID;
            mState.mFocusedItemPosition = mDataSetHasChangedAfterLayout ? NO_POSITION : (focusedVh.isRemoved() ? focusedVh.mOldPosition : focusedVh.getAdapterPosition());
            mState.mFocusedSubChildId = getDeepestFocusedViewWithId(focusedVh.itemView);
        }
    }

    private void resetFocusInfo() {
        mState.mFocusedItemId = NO_ID;
        mState.mFocusedItemPosition = NO_POSITION;
        mState.mFocusedSubChildId = View.NO_ID;
    }

    private View findNextViewToFocus() {
        int startFocusSearchIndex = mState.mFocusedItemPosition != -1 ? mState.mFocusedItemPosition : 0;
        ViewHolder nextFocus;
        final int itemCount = mState.getItemCount();
        for (int i = startFocusSearchIndex; i < itemCount; i++) {
            nextFocus = findViewHolderForAdapterPosition(i);
            if (nextFocus == null) {
                break;
            }
            if (nextFocus.itemView.hasFocusable()) {
                return nextFocus.itemView;
            }
        }
        final int limit = Math.min(itemCount, startFocusSearchIndex);
        for (int i = limit - 1; i >= 0; i--) {
            nextFocus = findViewHolderForAdapterPosition(i);
            if (nextFocus == null) {
                return null;
            }
            if (nextFocus.itemView.hasFocusable()) {
                return nextFocus.itemView;
            }
        }
        return null;
    }

    private void recoverFocusFromState() {
        if (!mPreserveFocusAfterLayout || mAdapter == null || !hasFocus() || getDescendantFocusability() == FOCUS_BLOCK_DESCENDANTS || (getDescendantFocusability() == FOCUS_BEFORE_DESCENDANTS && isFocused())) {
            return;
        }
        if (!isFocused()) {
            final View focusedChild = getFocusedChild();
            if (IGNORE_DETACHED_FOCUSED_CHILD && (focusedChild.getParent() == null || !focusedChild.hasFocus())) {
                if (mChildHelper.getChildCount() == 0) {
                    requestFocus();
                    return;
                }
            } else if (!mChildHelper.isHidden(focusedChild)) {
                return;
            }
        }
        ViewHolder focusTarget = null;
        if (mState.mFocusedItemId != NO_ID && mAdapter.hasStableIds()) {
            focusTarget = findViewHolderForItemId(mState.mFocusedItemId);
        }
        View viewToFocus = null;
        if (focusTarget == null || mChildHelper.isHidden(focusTarget.itemView) || !focusTarget.itemView.hasFocusable()) {
            if (mChildHelper.getChildCount() > 0) {
                viewToFocus = findNextViewToFocus();
            }
        } else {
            viewToFocus = focusTarget.itemView;
        }

        if (viewToFocus != null) {
            if (mState.mFocusedSubChildId != NO_ID) {
                View child = viewToFocus.findViewById(mState.mFocusedSubChildId);
                if (child != null && child.isFocusable()) {
                    viewToFocus = child;
                }
            }
            viewToFocus.requestFocus();
        }
    }

    private int getDeepestFocusedViewWithId(View view) {
        int lastKnownId = view.getId();
        while (!view.isFocused() && view instanceof ViewGroup && view.hasFocus()) {
            view = ((ViewGroup) view).getFocusedChild();
            final int id = view.getId();
            if (id != View.NO_ID) {
                lastKnownId = view.getId();
            }
        }
        return lastKnownId;
    }

    final void fillRemainingScrollValues(State state) {
        if (getScrollState() == SCROLL_STATE_SETTLING) {
            final SeslOverScroller scroller = mViewFlinger.mScroller;
            state.mRemainingScrollHorizontal = scroller.getFinalX() - scroller.getCurrX();
            state.mRemainingScrollVertical = scroller.getFinalY() - scroller.getCurrY();
        } else {
            state.mRemainingScrollHorizontal = 0;
            state.mRemainingScrollVertical = 0;
        }
    }

    private void dispatchLayoutStep1() {
        mState.assertLayoutStep(State.STEP_START);
        fillRemainingScrollValues(mState);
        mState.mIsMeasuring = false;
        startInterceptRequestLayout();
        mViewInfoStore.clear();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        saveFocusInfo();
        mState.mTrackOldChangeHolders = mState.mRunSimpleAnimations && mItemsChanged;
        mItemsAddedOrRemoved = mItemsChanged = false;
        mState.mInPreLayout = mState.mRunPredictiveAnimations;
        mState.mItemCount = mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(mMinMaxLayoutPositions);

        if (mState.mRunSimpleAnimations) {
            int count = mChildHelper.getChildCount();
            for (int i = 0; i < count; ++i) {
                final ViewHolder holder = getChildViewHolderInt(mChildHelper.getChildAt(i));
                if (holder.shouldIgnore() || (holder.isInvalid() && !mAdapter.hasStableIds())) {
                    continue;
                }
                final ItemHolderInfo animationInfo = mItemAnimator.recordPreLayoutInformation(mState, holder, ItemAnimator.buildAdapterChangeFlagsForAnimations(holder), holder.getUnmodifiedPayloads());
                mViewInfoStore.addToPreLayout(holder, animationInfo);
                if (mState.mTrackOldChangeHolders && holder.isUpdated() && !holder.isRemoved() && !holder.shouldIgnore() && !holder.isInvalid()) {
                    long key = getChangedHolderKey(holder);
                    mViewInfoStore.addToOldChangeHolders(key, holder);
                }
            }
        }
        if (mState.mRunPredictiveAnimations) {
            saveOldPositions();
            final boolean didStructureChange = mState.mStructureChanged;
            mState.mStructureChanged = false;
            mLayout.onLayoutChildren(mRecycler, mState);
            mState.mStructureChanged = didStructureChange;

            for (int i = 0; i < mChildHelper.getChildCount(); ++i) {
                final View child = mChildHelper.getChildAt(i);
                final ViewHolder viewHolder = getChildViewHolderInt(child);
                if (viewHolder.shouldIgnore()) {
                    continue;
                }
                if (!mViewInfoStore.isInPreLayout(viewHolder)) {
                    int flags = ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder);
                    boolean wasHidden = viewHolder.hasAnyOfTheFlags(ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST);
                    if (!wasHidden) {
                        flags |= ItemAnimator.FLAG_APPEARED_IN_PRE_LAYOUT;
                    }
                    final ItemHolderInfo animationInfo = mItemAnimator.recordPreLayoutInformation(mState, viewHolder, flags, viewHolder.getUnmodifiedPayloads());
                    if (wasHidden) {
                        recordAnimationInfoIfBouncedHiddenView(viewHolder, animationInfo);
                    } else {
                        mViewInfoStore.addToAppearedInPreLayoutHolders(viewHolder, animationInfo);
                    }
                }
            }
            clearOldPositions();
        } else {
            clearOldPositions();
        }
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        mState.mLayoutStep = State.STEP_LAYOUT;
    }

    private void dispatchLayoutStep2() {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        mState.assertLayoutStep(State.STEP_LAYOUT | State.STEP_ANIMATIONS);
        mAdapterHelper.consumeUpdatesInOnePass();
        mState.mItemCount = mAdapter.getItemCount();
        mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;

        mState.mInPreLayout = false;
        mLayout.onLayoutChildren(mRecycler, mState);

        mState.mStructureChanged = false;
        mPendingSavedState = null;

        mState.mRunSimpleAnimations = mState.mRunSimpleAnimations && mItemAnimator != null;
        mState.mLayoutStep = State.STEP_ANIMATIONS;
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
    }

    private void dispatchLayoutStep3() {
        mState.assertLayoutStep(State.STEP_ANIMATIONS);
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        mState.mLayoutStep = State.STEP_START;
        if (mState.mRunSimpleAnimations) {
            for (int i = mChildHelper.getChildCount() - 1; i >= 0; i--) {
                ViewHolder holder = getChildViewHolderInt(mChildHelper.getChildAt(i));
                if (holder.shouldIgnore()) {
                    continue;
                }
                long key = getChangedHolderKey(holder);
                final ItemHolderInfo animationInfo = mItemAnimator.recordPostLayoutInformation(mState, holder);
                ViewHolder oldChangeViewHolder = mViewInfoStore.getFromOldChangeHolders(key);
                if (oldChangeViewHolder != null && !oldChangeViewHolder.shouldIgnore()) {

                    final boolean oldDisappearing = mViewInfoStore.isDisappearing(oldChangeViewHolder);
                    final boolean newDisappearing = mViewInfoStore.isDisappearing(holder);
                    if (oldDisappearing && oldChangeViewHolder == holder) {
                        mViewInfoStore.addToPostLayout(holder, animationInfo);
                    } else {
                        final ItemHolderInfo preInfo = mViewInfoStore.popFromPreLayout(oldChangeViewHolder);
                        mViewInfoStore.addToPostLayout(holder, animationInfo);
                        ItemHolderInfo postInfo = mViewInfoStore.popFromPostLayout(holder);
                        if (preInfo == null) {
                            handleMissingPreInfoForChangeError(key, holder, oldChangeViewHolder);
                        } else {
                            animateChange(oldChangeViewHolder, holder, preInfo, postInfo, oldDisappearing, newDisappearing);
                        }
                    }
                } else {
                    mViewInfoStore.addToPostLayout(holder, animationInfo);
                }
            }
            mViewInfoStore.process(mViewInfoProcessCallback);
        }

        mLastBlackTop = mBlackTop;
        mBlackTop = -1;
        if (mDrawRect && !canScrollVertically(-1) && !canScrollVertically(1)) {
            int lastPosition = mAdapter.getItemCount() - 1;
            SeslLinearLayoutManager linearLayoutManager = (SeslLinearLayoutManager) mLayout;
            if (linearLayoutManager.mReverseLayout && linearLayoutManager.mStackFromEnd) {
                mDrawReverse = true;
                lastPosition = 0;
            } else if (linearLayoutManager.mReverseLayout || linearLayoutManager.mStackFromEnd) {
                mDrawRect = false;
                lastPosition = -1;
            }
            if (lastPosition >= 0 && lastPosition <= findLastVisibleItemPosition()) {
                View view = mChildHelper.getChildAt(lastPosition);
                if (view != null) {
                    mBlackTop = view.getBottom();
                    View tempView = getChildAt(lastPosition);
                    int bottom = -1;
                    if (tempView != null) {
                        bottom = tempView.getBottom();
                    }
                    LogUtils.d(TAG, "dispatchLayoutStep3, lastPosition : " + lastPosition + ", mBlackTop : " + mBlackTop + " tempView bottom : " + bottom + ", mDrawReverse : " + mDrawReverse);
                }
            }
        }

        mLayout.removeAndRecycleScrapInt(mRecycler);
        mState.mPreviousLayoutItemCount = mState.mItemCount;
        mDataSetHasChangedAfterLayout = false;
        mDispatchItemsChangedEvent = false;
        mState.mRunSimpleAnimations = false;

        mState.mRunPredictiveAnimations = false;
        mLayout.mRequestedSimpleAnimations = false;
        if (mRecycler.mChangedScrap != null) {
            mRecycler.mChangedScrap.clear();
        }
        if (mLayout.mPrefetchMaxObservedInInitialPrefetch) {
            mLayout.mPrefetchMaxCountObserved = 0;
            mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
            mRecycler.updateViewCacheSize();
        }

        mLayout.onLayoutCompleted(mState);
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        mViewInfoStore.clear();
        if (didChildRangeChange(mMinMaxLayoutPositions[0], mMinMaxLayoutPositions[1])) {
            dispatchOnScrolled(0, 0);
        }
        recoverFocusFromState();
        resetFocusInfo();
    }

    private void handleMissingPreInfoForChangeError(long key, ViewHolder holder, ViewHolder oldChangeViewHolder) {
        final int childCount = mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mChildHelper.getChildAt(i);
            ViewHolder other = getChildViewHolderInt(view);
            if (other == holder) {
                continue;
            }
            final long otherKey = getChangedHolderKey(other);
            if (otherKey == key) {
                if (mAdapter != null && mAdapter.hasStableIds()) {
                    throw new IllegalStateException("Two different ViewHolders have the same stable" + " ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT" + " change.\n ViewHolder 1:" + other + " \n View Holder 2:" + holder + exceptionLabel());
                } else {
                    throw new IllegalStateException("Two different ViewHolders have the same change" + " ID. This might happen due to inconsistent Adapter update events or" + " if the LayoutManager lays out the same View multiple times." + "\n ViewHolder 1:" + other + " \n View Holder 2:" + holder + exceptionLabel());
                }
            }
        }
        LogUtils.e(TAG, "Problem while matching changed view holders with the new" + "ones. The pre-layout information for the change holder " + oldChangeViewHolder + " cannot be found but it is necessary for " + holder + exceptionLabel());
    }

    void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemHolderInfo animationInfo) {
        viewHolder.setFlags(0, ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST);
        if (mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            long key = getChangedHolderKey(viewHolder);
            mViewInfoStore.addToOldChangeHolders(key, viewHolder);
        }
        mViewInfoStore.addToPreLayout(viewHolder, animationInfo);
    }

    private void findMinMaxChildLayoutPositions(int[] into) {
        final int count = mChildHelper.getChildCount();
        if (count == 0) {
            into[0] = NO_POSITION;
            into[1] = NO_POSITION;
            return;
        }
        int minPositionPreLayout = Integer.MAX_VALUE;
        int maxPositionPreLayout = Integer.MIN_VALUE;
        for (int i = 0; i < count; ++i) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getChildAt(i));
            if (holder.shouldIgnore()) {
                continue;
            }
            final int pos = holder.getLayoutPosition();
            if (pos < minPositionPreLayout) {
                minPositionPreLayout = pos;
            }
            if (pos > maxPositionPreLayout) {
                maxPositionPreLayout = pos;
            }
        }
        into[0] = minPositionPreLayout;
        into[1] = maxPositionPreLayout;
    }

    private boolean didChildRangeChange(int minPositionPreLayout, int maxPositionPreLayout) {
        findMinMaxChildLayoutPositions(mMinMaxLayoutPositions);
        return mMinMaxLayoutPositions[0] != minPositionPreLayout || mMinMaxLayoutPositions[1] != maxPositionPreLayout;
    }

    @Override
    protected void removeDetachedView(View child, boolean animate) {
        ViewHolder vh = getChildViewHolderInt(child);
        if (vh != null) {
            if (vh.isTmpDetached()) {
                vh.clearTmpDetachFlag();
            } else if (!vh.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which" + " is not flagged as tmp detached." + vh + exceptionLabel());
            }
        }

        child.clearAnimation();

        dispatchChildDetached(child);
        super.removeDetachedView(child, animate);
    }

    long getChangedHolderKey(ViewHolder holder) {
        return mAdapter.hasStableIds() ? holder.getItemId() : holder.mPosition;
    }

    void animateAppearance(ViewHolder itemHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        itemHolder.setIsRecyclable(false);
        if (mItemAnimator.animateAppearance(itemHolder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    void animateDisappearance(ViewHolder holder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        addAnimatingView(holder);
        holder.setIsRecyclable(false);
        if (mItemAnimator.animateDisappearance(holder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    private void animateChange(ViewHolder oldHolder, ViewHolder newHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo, boolean oldHolderDisappearing, boolean newHolderDisappearing) {
        oldHolder.setIsRecyclable(false);
        if (oldHolderDisappearing) {
            addAnimatingView(oldHolder);
        }
        if (oldHolder != newHolder) {
            if (newHolderDisappearing) {
                addAnimatingView(newHolder);
            }
            oldHolder.mShadowedHolder = newHolder;
            addAnimatingView(oldHolder);
            mRecycler.unscrapView(oldHolder);
            newHolder.setIsRecyclable(false);
            newHolder.mShadowingHolder = oldHolder;
        }
        if (mItemAnimator.animateChange(oldHolder, newHolder, preInfo, postInfo)) {
            postAnimationRunner();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        TraceCompat.beginSection(TRACE_ON_LAYOUT_TAG);
        dispatchLayout();
        TraceCompat.endSection();
        mFirstLayoutComplete = true;

        if (mFastScroller != null && mAdapter != null) {
            mFastScroller.onItemCountChanged(getChildCount(), mAdapter.getItemCount());
        }
        if (changed) {
            mSizeChnage = true;
            setupGoToTop(-1);
            autoHide(1);
            mHasNestedScrollRange = false;
            ViewParent vp = getParent();
            while (true) {
                if (vp == null || !(vp instanceof ViewGroup)) {
                    break;
                } else if (vp instanceof NestedScrollingParent2) {
                    ((ViewGroup) vp).getLocationInWindow(mWindowOffsets);
                    int coordinatorBottomY = mWindowOffsets[1] + ((ViewGroup) vp).getHeight();
                    getLocationInWindow(mWindowOffsets);
                    mInitialTopOffsetOfScreen = mWindowOffsets[1];
                    mRemainNestedScrollRange = getHeight() - (coordinatorBottomY - mInitialTopOffsetOfScreen);
                    if (mRemainNestedScrollRange < 0) {
                        mRemainNestedScrollRange = 0;
                    }
                    mNestedScrollRange = mRemainNestedScrollRange;
                    mHasNestedScrollRange = true;
                } else {
                    vp = vp.getParent();
                }
            }
            if (!mHasNestedScrollRange) {
                mInitialTopOffsetOfScreen = 0;
                mRemainNestedScrollRange = 0;
                mNestedScrollRange = 0;
            }
        }
    }

    @Override
    public void requestLayout() {
        if (mInterceptRequestLayoutDepth == 0 && !mLayoutFrozen) {
            super.requestLayout();
        } else {
            mLayoutWasDefered = true;
        }
    }

    void markItemDecorInsetsDirty() {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = mChildHelper.getUnfilteredChildAt(i);
            ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
        }
        mRecycler.markItemDecorInsetsDirty();
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);

        int count = mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            mItemDecorations.get(i).onDrawOver(c, this, mState);
        }
        boolean needsInvalidate = false;
        if (mLeftGlow != null && !mLeftGlow.isFinished()) {
            final int restore = c.save();
            final int padding = mClipToPadding ? getPaddingBottom() : 0;
            c.rotate(270);
            c.translate(-getHeight() + padding, 0);
            needsInvalidate = mLeftGlow != null && mLeftGlow.draw(c);
            c.restoreToCount(restore);
        }
        if (mTopGlow != null && !mTopGlow.isFinished()) {
            final int restore = c.save();
            if (mClipToPadding) {
                c.translate(getPaddingLeft(), getPaddingTop());
            }
            needsInvalidate |= mTopGlow != null && mTopGlow.draw(c);
            c.restoreToCount(restore);
        }
        if (mRightGlow != null && !mRightGlow.isFinished()) {
            final int restore = c.save();
            final int width = getWidth();
            final int padding = mClipToPadding ? getPaddingTop() : 0;
            c.rotate(90);
            c.translate(-padding, -width);
            needsInvalidate |= mRightGlow != null && mRightGlow.draw(c);
            c.restoreToCount(restore);
        }
        if (mBottomGlow != null && !mBottomGlow.isFinished()) {
            final int restore = c.save();
            c.rotate(180);
            if (mClipToPadding) {
                c.translate(-getWidth() + getPaddingRight(), -getHeight() + getPaddingBottom());
            } else {
                c.translate(-getWidth(), -getHeight());
            }
            needsInvalidate |= mBottomGlow != null && mBottomGlow.draw(c);
            c.restoreToCount(restore);
        }
        if (!needsInvalidate && mItemAnimator != null && mItemDecorations.size() > 0 && mItemAnimator.isRunning()) {
            needsInvalidate = true;
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (mEnableGoToTop) {
            drawGoToTop(c);
        }
        if (mIsPenDragBlockEnabled && !mIsLongPressMultiSelection && mLayout != null) {
            if (mPenDragBlockLeft != 0 || mPenDragBlockTop != 0) {
                int firstChildLayoutPosition = findFirstVisibleItemPosition();
                int lastChildLayoutPosition = (mLayout.getChildCount() + firstChildLayoutPosition) - 1;
                if (mPenTrackedChildPosition >= firstChildLayoutPosition && mPenTrackedChildPosition <= lastChildLayoutPosition) {
                    mPenTrackedChild = mLayout.getChildAt(mPenTrackedChildPosition - firstChildLayoutPosition);
                    int penTrackChildTop = 0;
                    if (mPenTrackedChild != null) {
                        penTrackChildTop = mPenTrackedChild.getTop();
                    }
                    mPenDragStartY = mPenDistanceFromTrackedChildTop + penTrackChildTop;
                }
                mPenDragBlockTop = mPenDragStartY < mPenDragEndY ? mPenDragStartY : mPenDragEndY;
                mPenDragBlockBottom = mPenDragEndY > mPenDragStartY ? mPenDragEndY : mPenDragStartY;
                mPenDragBlockRect.set(mPenDragBlockLeft, mPenDragBlockTop, mPenDragBlockRight, mPenDragBlockBottom);
                mPenDragBlockImage.setBounds(mPenDragBlockRect);
                mPenDragBlockImage.draw(c);
            }
        }
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        if (mDrawOutlineStroke) {
            c.drawRect(0.0f, 0.0f, (float) mStrokeHeight, (float) getBottom(), mStrokePaint);
            c.drawRect((float) (getRight() - mStrokeHeight), 0.0f, (float) getRight(), (float) getBottom(), mStrokePaint);
        }

        final int count = mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            mItemDecorations.get(i).onDraw(c, this, mState);
        }
    }

    @Override
    protected void dispatchDraw(Canvas c) {
        super.dispatchDraw(c);

        final int count = mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            mItemDecorations.get(i).seslOnDispatchDraw(c, this, mState);
        }
        if (mDrawRect && ((mBlackTop != -1 || mLastBlackTop != -1) && !canScrollVertically(-1) && !canScrollVertically(1))) {
            mAnimatedBlackTop = mBlackTop;
            if (isAnimating()) {
                View v = mDrawReverse ? mBlackTop != -1 ? mChildHelper.getChildAt(0) : getChildAt(0) : mBlackTop != -1 ? mChildHelper.getChildAt(mChildHelper.getChildCount() - 1) : getChildAt(getChildCount() - 1);
                if (v != null) {
                    mAnimatedBlackTop = Math.round(v.getY()) + v.getHeight();
                }
            }
            if (mBlackTop != -1 || mAnimatedBlackTop != mBlackTop) {
                c.drawRect(0.0f, (float) mAnimatedBlackTop, (float) getRight(), (float) getBottom(), mRectPaint);
                if (mDrawLastOutLineStroke) {
                    mSeslRoundedCorner.drawRoundedCorner(0, mAnimatedBlackTop, getRight(), getBottom(), c);
                }
            }
        } else if (mDrawRect && mDrawLastItemOutlineStoke && !canScrollVertically(1)) {
            mSeslRoundedCorner.drawRoundedCorner(0, getBottom(), getRight(), mSeslRoundedCorner.getRoundedCornerRadius() + getBottom(), c);
        }
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && mLayout.checkLayoutParams((LayoutParams) p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
        }
        return mLayout.generateDefaultLayoutParams();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        if (mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
        }
        return mLayout.generateLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
        }
        return mLayout.generateLayoutParams(p);
    }

    public boolean isAnimating() {
        return mItemAnimator != null && mItemAnimator.isRunning();
    }

    void saveOldPositions() {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (DEBUG && holder.mPosition == -1 && !holder.isRemoved()) {
                throw new IllegalStateException("view holder cannot have position -1 unless it" + " is removed" + exceptionLabel());
            }
            if (!holder.shouldIgnore()) {
                holder.saveOldPosition();
            }
        }
    }

    void clearOldPositions() {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.clearOldPosition();
            }
        }
        mRecycler.clearOldPositions();
    }

    void offsetPositionRecordsForMove(int from, int to) {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        final int start, end, inBetweenOffset;
        if (from < to) {
            start = from;
            end = to;
            inBetweenOffset = -1;
        } else {
            start = to;
            end = from;
            inBetweenOffset = 1;
        }

        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder == null || holder.mPosition < start || holder.mPosition > end) {
                continue;
            }
            if (DEBUG) {
                LogUtils.d(TAG, "offsetPositionRecordsForMove attached child " + i + " holder " + holder);
            }
            if (holder.mPosition == from) {
                holder.offsetPosition(to - from, false);
            } else {
                holder.offsetPosition(inBetweenOffset, false);
            }

            mState.mStructureChanged = true;
        }
        mRecycler.offsetPositionRecordsForMove(from, to);
        requestLayout();
    }

    void offsetPositionRecordsForInsert(int positionStart, int itemCount) {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore() && holder.mPosition >= positionStart) {
                if (DEBUG) {
                    LogUtils.d(TAG, "offsetPositionRecordsForInsert attached child " + i + " holder " + holder + " now at position " + (holder.mPosition + itemCount));
                }
                holder.offsetPosition(itemCount, false);
                mState.mStructureChanged = true;
            }
        }
        mRecycler.offsetPositionRecordsForInsert(positionStart, itemCount);
        requestLayout();
    }

    void offsetPositionRecordsForRemove(int positionStart, int itemCount, boolean applyToPreLayout) {
        final int positionEnd = positionStart + itemCount;
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                if (holder.mPosition >= positionEnd) {
                    if (DEBUG) {
                        LogUtils.d(TAG, "offsetPositionRecordsForRemove attached child " + i + " holder " + holder + " now at position " + (holder.mPosition - itemCount));
                    }
                    holder.offsetPosition(-itemCount, applyToPreLayout);
                    mState.mStructureChanged = true;
                } else if (holder.mPosition >= positionStart) {
                    if (DEBUG) {
                        LogUtils.d(TAG, "offsetPositionRecordsForRemove attached child " + i + " holder " + holder + " now REMOVED");
                    }
                    holder.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, applyToPreLayout);
                    mState.mStructureChanged = true;
                }
            }
        }
        mRecycler.offsetPositionRecordsForRemove(positionStart, itemCount, applyToPreLayout);
        requestLayout();
    }

    void viewRangeUpdate(int positionStart, int itemCount, Object payload) {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        final int positionEnd = positionStart + itemCount;

        for (int i = 0; i < childCount; i++) {
            final View child = mChildHelper.getUnfilteredChildAt(i);
            final ViewHolder holder = getChildViewHolderInt(child);
            if (holder == null || holder.shouldIgnore()) {
                continue;
            }
            if (holder.mPosition >= positionStart && holder.mPosition < positionEnd) {
                holder.addFlags(ViewHolder.FLAG_UPDATE);
                holder.addChangePayload(payload);
                ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
            }
        }
        mRecycler.viewRangeUpdate(positionStart, itemCount);
    }

    boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        return mItemAnimator == null || mItemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }

    void processDataSetCompletelyChanged(boolean dispatchItemsChanged) {
        mDispatchItemsChangedEvent |= dispatchItemsChanged;
        mDataSetHasChangedAfterLayout = true;
        markKnownViewsInvalid();
    }

    void markKnownViewsInvalid() {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                holder.addFlags(ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_INVALID);
            }
        }
        markItemDecorInsetsDirty();
        mRecycler.markKnownViewsInvalid();
    }

    public void invalidateItemDecorations() {
        if (mItemDecorations.size() == 0) {
            return;
        }
        if (mLayout != null) {
            mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll" + " or layout");
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public boolean getPreserveFocusAfterLayout() {
        return mPreserveFocusAfterLayout;
    }

    public void setPreserveFocusAfterLayout(boolean preserveFocusAfterLayout) {
        mPreserveFocusAfterLayout = preserveFocusAfterLayout;
    }

    public ViewHolder getChildViewHolder(View child) {
        final ViewParent parent = child.getParent();
        if (parent != null && parent != this) {
            throw new IllegalArgumentException("View " + child + " is not a direct child of " + this);
        }
        return getChildViewHolderInt(child);
    }

    public View findContainingItemView(View view) {
        ViewParent parent = view.getParent();
        while (parent != null && parent != this && parent instanceof View) {
            view = (View) parent;
            parent = view.getParent();
        }
        return parent == this ? view : null;
    }

    public ViewHolder findContainingViewHolder(View view) {
        View itemView = findContainingItemView(view);
        return itemView == null ? null : getChildViewHolder(itemView);
    }

    static ViewHolder getChildViewHolderInt(View child) {
        if (child == null) {
            return null;
        }
        return ((LayoutParams) child.getLayoutParams()).mViewHolder;
    }

    @Deprecated
    public int getChildPosition(View child) {
        return getChildAdapterPosition(child);
    }

    public int getChildAdapterPosition(View child) {
        final ViewHolder holder = getChildViewHolderInt(child);
        return holder != null ? holder.getAdapterPosition() : NO_POSITION;
    }

    public int getChildLayoutPosition(View child) {
        final ViewHolder holder = getChildViewHolderInt(child);
        return holder != null ? holder.getLayoutPosition() : NO_POSITION;
    }

    public long getChildItemId(View child) {
        if (mAdapter == null || !mAdapter.hasStableIds()) {
            return NO_ID;
        }
        final ViewHolder holder = getChildViewHolderInt(child);
        return holder != null ? holder.getItemId() : NO_ID;
    }

    @Deprecated
    public ViewHolder findViewHolderForPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    public ViewHolder findViewHolderForLayoutPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    public ViewHolder findViewHolderForAdapterPosition(int position) {
        if (mDataSetHasChangedAfterLayout) {
            return null;
        }
        final int childCount = mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved() && getAdapterPositionFor(holder) == position) {
                if (mChildHelper.isHidden(holder.itemView)) {
                    hidden = holder;
                } else {
                    return holder;
                }
            }
        }
        return hidden;
    }

    ViewHolder findViewHolderForPosition(int position, boolean checkNewPosition) {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved()) {
                if (checkNewPosition) {
                    if (holder.mPosition != position) {
                        continue;
                    }
                } else if (holder.getLayoutPosition() != position) {
                    continue;
                }
                if (mChildHelper.isHidden(holder.itemView)) {
                    hidden = holder;
                } else {
                    return holder;
                }
            }
        }
        return hidden;
    }

    public ViewHolder findViewHolderForItemId(long id) {
        if (mAdapter == null || !mAdapter.hasStableIds()) {
            return null;
        }
        final int childCount = mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved() && holder.getItemId() == id) {
                if (mChildHelper.isHidden(holder.itemView)) {
                    hidden = holder;
                } else {
                    return holder;
                }
            }
        }
        return hidden;
    }

    public View findChildViewUnder(float x, float y) {
        final int count = mChildHelper.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = mChildHelper.getChildAt(i);
            final float translationX = child.getTranslationX();
            final float translationY = child.getTranslationY();
            if (x >= child.getLeft() + translationX && x <= child.getRight() + translationX && y >= child.getTop() + translationY && y <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }

    public View seslFindNearChildViewUnder(float x, float y) {
        int count = mChildHelper.getChildCount();
        int positionX = (int) (0.5f + x);
        int positionY = (int) (0.5f + y);
        int adjustY = positionY;
        int oldDistanceY = Integer.MAX_VALUE;
        int previousChildCenter = 0;
        for (int i = count - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child != null) {
                int childCenter = (child.getTop() + child.getBottom()) / 2;
                if (previousChildCenter != childCenter) {
                    previousChildCenter = childCenter;
                    int newDistanceY = Math.abs(positionY - childCenter);
                    if (newDistanceY >= oldDistanceY) {
                        break;
                    }
                    oldDistanceY = newDistanceY;
                    adjustY = childCenter;
                }
            }
        }
        int oldDistanceFromLeft = 0;
        int oldDistanceFromRight = 0;
        int closeIndexByLeft = 0;
        int closeIndexByRight = 0;
        for (int i = count - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child != null) {
                int childTop = child.getTop();
                int childBottom = child.getBottom();
                int childLeft = child.getLeft();
                int childRight = child.getRight();
                if (i == count - 1) {
                    closeIndexByLeft = count - 1;
                    closeIndexByRight = count - 1;
                    oldDistanceFromLeft = Math.abs(positionX - childLeft);
                    oldDistanceFromRight = Math.abs(positionX - childRight);
                }
                if (adjustY >= childTop && adjustY <= childBottom) {
                    int newDistanceFromLeft = Math.abs(positionX - childLeft);
                    int newDistanceFromRight = Math.abs(positionX - childRight);
                    if (newDistanceFromLeft <= oldDistanceFromLeft) {
                        closeIndexByLeft = i;
                        oldDistanceFromLeft = newDistanceFromLeft;
                    }
                    if (newDistanceFromRight <= oldDistanceFromRight) {
                        closeIndexByRight = i;
                        oldDistanceFromRight = newDistanceFromRight;
                    }
                }
                if (adjustY > childBottom || i == 0) {
                    if (oldDistanceFromLeft < oldDistanceFromRight) {
                        return mChildHelper.getChildAt(closeIndexByLeft);
                    }
                    return mChildHelper.getChildAt(closeIndexByRight);
                }
            }
        }
        LogUtils.e(TAG, "findNearChildViewUnder didn't find valid child view! " + x + ", " + y);
        return null;
    }



    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    public void offsetChildrenVertical(int dy) {
        final int childCount = mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mChildHelper.getChildAt(i).offsetTopAndBottom(dy);
        }
    }

    public void onChildAttachedToWindow(View child) { }

    public void onChildDetachedFromWindow(View child) { }

    public void offsetChildrenHorizontal(int dx) {
        final int childCount = mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mChildHelper.getChildAt(i).offsetLeftAndRight(dx);
        }
    }

    public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
        getDecoratedBoundsWithMarginsInt(view, outBounds);
    }

    static void getDecoratedBoundsWithMarginsInt(View view, Rect outBounds) {
        final LayoutParams lp = (LayoutParams) view.getLayoutParams();
        final Rect insets = lp.mDecorInsets;
        outBounds.set(view.getLeft() - insets.left - lp.leftMargin, view.getTop() - insets.top - lp.topMargin, view.getRight() + insets.right + lp.rightMargin, view.getBottom() + insets.bottom + lp.bottomMargin);
    }

    Rect getItemDecorInsetsForChild(View child) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (!lp.mInsetsDirty) {
            return lp.mDecorInsets;
        }

        if (mState.isPreLayout() && (lp.isItemChanged() || lp.isViewInvalid())) {
            return lp.mDecorInsets;
        }
        final Rect insets = lp.mDecorInsets;
        insets.set(0, 0, 0, 0);
        final int decorCount = mItemDecorations.size();
        for (int i = 0; i < decorCount; i++) {
            mTempRect.set(0, 0, 0, 0);
            mItemDecorations.get(i).getItemOffsets(mTempRect, child, this, mState);
            insets.left += mTempRect.left;
            insets.top += mTempRect.top;
            insets.right += mTempRect.right;
            insets.bottom += mTempRect.bottom;
        }
        lp.mInsetsDirty = false;
        return insets;
    }

    public void onScrolled(int dx, int dy) { }

    void dispatchOnScrolled(int hresult, int vresult) {
        mDispatchScrollCounter++;
        final int scrollX = getScrollX();
        final int scrollY = getScrollY();
        onScrollChanged(scrollX, scrollY, scrollX, scrollY);

        onScrolled(hresult, vresult);

        if (mFastScroller != null && mAdapter != null) {
            mFastScroller.onScroll(findFirstVisibleItemPosition(), getChildCount(), mAdapter.getItemCount());
        }
        if (mScrollListener != null) {
            mScrollListener.onScrolled(this, hresult, vresult);
        }
        if (mScrollListeners != null) {
            for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                mScrollListeners.get(i).onScrolled(this, hresult, vresult);
            }
        }
        mDispatchScrollCounter--;
    }

    public void onScrollStateChanged(int state) { }

    void dispatchOnScrollStateChanged(int state) {
        if (mLayout != null) {
            mLayout.onScrollStateChanged(state);
        }

        onScrollStateChanged(state);

        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(this, state);
        }
        if (mScrollListeners != null) {
            for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                mScrollListeners.get(i).onScrollStateChanged(this, state);
            }
        }
    }

    public boolean hasPendingAdapterUpdates() {
        return !mFirstLayoutComplete || mDataSetHasChangedAfterLayout || mAdapterHelper.hasPendingUpdates();
    }

    void repositionShadowingViews() {
        int count = mChildHelper.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mChildHelper.getChildAt(i);
            ViewHolder holder = getChildViewHolder(view);
            if (holder != null && holder.mShadowingHolder != null) {
                View shadowingView = holder.mShadowingHolder.itemView;
                int left = view.getLeft();
                int top = view.getTop();
                if (left != shadowingView.getLeft() ||  top != shadowingView.getTop()) {
                    shadowingView.layout(left, top, left + shadowingView.getWidth(), top + shadowingView.getHeight());
                }
            }
        }
    }

    static SeslRecyclerView findNestedRecyclerView(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof SeslRecyclerView) {
            return (SeslRecyclerView) view;
        }
        final ViewGroup parent = (ViewGroup) view;
        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final SeslRecyclerView descendant = findNestedRecyclerView(child);
            if (descendant != null) {
                return descendant;
            }
        }
        return null;
    }

    static void clearNestedRecyclerViewIfNotNested(ViewHolder holder) {
        if (holder.mNestedRecyclerView != null) {
            View item = holder.mNestedRecyclerView.get();
            while (item != null) {
                if (item == holder.itemView) {
                    return;
                }

                ViewParent parent = item.getParent();
                if (parent instanceof View) {
                    item = (View) parent;
                } else {
                    item = null;
                }
            }
            holder.mNestedRecyclerView = null;
        }
    }

    long getNanoTime() {
        if (ALLOW_THREAD_GAP_WORK) {
            return System.nanoTime();
        } else {
            return 0;
        }
    }

    void dispatchChildDetached(View child) {
        final ViewHolder viewHolder = getChildViewHolderInt(child);
        onChildDetachedFromWindow(child);
        if (mAdapter != null && viewHolder != null) {
            mAdapter.onViewDetachedFromWindow(viewHolder);
        }
        if (mOnChildAttachStateListeners != null) {
            final int cnt = mOnChildAttachStateListeners.size();
            for (int i = cnt - 1; i >= 0; i--) {
                mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(child);
            }
        }
    }

    void dispatchChildAttached(View child) {
        final ViewHolder viewHolder = getChildViewHolderInt(child);
        onChildAttachedToWindow(child);
        if (mAdapter != null && viewHolder != null) {
            mAdapter.onViewAttachedToWindow(viewHolder);
        }
        if (mOnChildAttachStateListeners != null) {
            final int cnt = mOnChildAttachStateListeners.size();
            for (int i = cnt - 1; i >= 0; i--) {
                mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(child);
            }
        }
    }

    boolean setChildImportantForAccessibilityInternal(ViewHolder viewHolder, int importantForAccessibility) {
        if (isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = importantForAccessibility;
            mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        ViewCompat.setImportantForAccessibility(viewHolder.itemView, importantForAccessibility);
        return true;
    }

    @SuppressLint("WrongConstant")
    void dispatchPendingImportantForAccessibilityChanges() {
        for (int i = mPendingAccessibilityImportanceChange.size() - 1; i >= 0; i--) {
            ViewHolder viewHolder = mPendingAccessibilityImportanceChange.get(i);
            if (viewHolder.itemView.getParent() != this || viewHolder.shouldIgnore()) {
                continue;
            }
            int state = viewHolder.mPendingAccessibilityState;
            if (state != ViewHolder.PENDING_ACCESSIBILITY_STATE_NOT_SET) {
                ViewCompat.setImportantForAccessibility(viewHolder.itemView, state);
                viewHolder.mPendingAccessibilityState = ViewHolder.PENDING_ACCESSIBILITY_STATE_NOT_SET;
            }
        }
        mPendingAccessibilityImportanceChange.clear();
    }

    int getAdapterPositionFor(ViewHolder viewHolder) {
        if (viewHolder.hasAnyOfTheFlags(ViewHolder.FLAG_INVALID | ViewHolder.FLAG_REMOVED | ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN) || !viewHolder.isBound()) {
            return SeslRecyclerView.NO_POSITION;
        }
        return mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
    }

    void initFastScroller(StateListDrawable verticalThumbDrawable, Drawable verticalTrackDrawable, StateListDrawable horizontalThumbDrawable, Drawable horizontalTrackDrawable) {
        if (verticalThumbDrawable == null || verticalTrackDrawable == null || horizontalThumbDrawable == null || horizontalTrackDrawable == null) {
            throw new IllegalArgumentException("Trying to set fast scroller without both required drawables." + exceptionLabel());
        }

        Resources resources = getContext().getResources();
        new FastScroller(this, verticalThumbDrawable, verticalTrackDrawable, horizontalThumbDrawable, horizontalTrackDrawable, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return getScrollingChildHelper().startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        getScrollingChildHelper().stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return getScrollingChildHelper().hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mChildDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(childCount, i);
        } else {
            return mChildDrawingOrderCallback.onGetChildDrawingOrder(childCount, i);
        }
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return mScrollingChildHelper;
    }

    public void seslSetFastScrollerEnabled(boolean enabled) {
        if (mFastScroller != null) {
            mFastScroller.setEnabled(enabled);
        } else if (enabled) {
            mFastScroller = new SeslRecyclerViewFastScroller(this, 1);
            mFastScroller.setEnabled(true);
            mFastScroller.setScrollbarPosition(getVerticalScrollbarPosition());
        }
        mFastScrollerEnabled = enabled;
        if (mFastScroller != null) {
            mFastScroller.updateLayout();
        }
    }

    public boolean seslIsFastScrollerEnabled() {
        return mFastScrollerEnabled;
    }

    public boolean isVerticalScrollBarEnabled() {
        return !mFastScrollerEnabled && super.isVerticalScrollBarEnabled();
    }

    protected boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && (p instanceof ViewGroup)) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    public void seslSetFastScrollerEventListener(SeslFastScrollerEventListener l) {
        mFastScrollerEventListener = l;
    }

    private boolean contentFits() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        if (childCount != mAdapter.getItemCount()) {
            return false;
        }
        if (getChildAt(0).getTop() < mListPadding.top || getChildAt(childCount - 1).getBottom() > getHeight() - mListPadding.bottom) {
            return false;
        }
        return true;
    }

    private boolean isInDialog() {
        boolean isInDialog = false;
        if (mRootViewCheckForDialog == null) {
            mRootViewCheckForDialog = getRootView();
            if (mRootViewCheckForDialog == null) {
                return false;
            }
            Context context = mRootViewCheckForDialog.getContext();
            isInDialog = !(context instanceof Activity) || ((Activity) context).getWindow().getAttributes().type != 1;
        }
        return isInDialog;
    }

    private boolean showPointerIcon(MotionEvent ev, int iconId) {
        Utils.genericInvokeMethod(InputDevice.class, Build.VERSION.SDK_INT >= 28 ? "semSetPointerDevice" : "setPointerDevice", ev.getDevice(), iconId);
        return true;
    }

    private boolean canScrollUp() {
        boolean canScrollUp;
        canScrollUp = findFirstChildPosition() > 0;
        if (canScrollUp || getChildCount() <= 0) {
            return canScrollUp;
        }
        return getChildAt(0).getTop() < getPaddingTop();
    }

    private boolean canScrollDown() {
        boolean canScrollDown;
        int count = getChildCount();
        if (mAdapter == null) {
            LogUtils.e(TAG, "No adapter attached; skipping canScrollDown");
            return false;
        }
        canScrollDown = findFirstChildPosition() + count < mAdapter.getItemCount();
        if (!canScrollDown && count > 0) {
            canScrollDown = getChildAt(count - 1).getBottom() > getBottom() - mListPadding.bottom;
        }
        return canScrollDown;
    }

    private int findFirstChildPosition() {
        int firstPosition = 0;
        if (mLayout instanceof SeslLinearLayoutManager) {
            firstPosition = ((SeslLinearLayoutManager) mLayout).findFirstVisibleItemPosition();
        // TODO merge Sammy StaggeredGridLayoutManager
        //} else if (mLayout instanceof StaggeredGridLayoutManager) {
        //    firstPosition = ((StaggeredGridLayoutManager) mLayout).findFirstVisibleItemPositions(null)[mLayout.getLayoutDirection() == 1 ? ((StaggeredGridLayoutManager) mLayout).getSpanCount() - 1 : 0];
        }
        if (firstPosition == -1) {
            return 0;
        }
        return firstPosition;
    }

    public boolean isLockScreenMode() {
        return ((KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode();
    }

    public void seslSetHoverScrollEnabled(boolean enabled) {
        mHoverScrollEnable = enabled;
        mHoverScrollStateChanged = true;
    }

    public int findFirstVisibleItemPosition() {
        if (mLayout instanceof SeslLinearLayoutManager) {
            return ((SeslLinearLayoutManager) mLayout).findFirstVisibleItemPosition();
        }
        // TODO merge Sammy StaggeredGridLayoutManager
        //if (mLayout instanceof StaggeredGridLayoutManager) {
        //    return ((StaggeredGridLayoutManager) mLayout).findFirstVisibleItemPositions(null)[0];
        //}
        return -1;
    }

    public int findLastVisibleItemPosition() {
        if (mLayout instanceof SeslLinearLayoutManager) {
            return ((SeslLinearLayoutManager) mLayout).findLastVisibleItemPosition();
        }
        // TODO merge Sammy StaggeredGridLayoutManager
        //if (mLayout instanceof StaggeredGridLayoutManager) {
        //    return ((StaggeredGridLayoutManager) mLayout).findLastVisibleItemPositions(null)[0];
        //}
        return -1;
    }

    private boolean pageScroll(int direction) {
        int pos;
        if (mAdapter == null) {
            LogUtils.e(TAG, "No adapter attached; skipping pageScroll");
            return false;
        }
        int itemCount = mAdapter.getItemCount();
        if (itemCount <= 0) {
            return false;
        }
        switch (direction) {
            case 0:
                pos = findFirstVisibleItemPosition() - getChildCount();
                break;
            case 1:
                pos = findLastVisibleItemPosition() + getChildCount();
                break;
            case 2:
                pos = 0;
                break;
            case 3:
                pos = itemCount - 1;
                break;
            default:
                return false;
        }
        if (pos > itemCount - 1) {
            pos = itemCount - 1;
        } else if (pos < 0) {
            pos = 0;
        }
        mLayout.mRecyclerView.scrollToPosition(pos);
        mLayout.mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                View view = getChildAt(0);
                if (view != null) {
                    view.requestFocus();
                }
            }
        });
        return true;
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent ev) {
        int contentTop;
        int contentBottom;
        if (mAdapter == null) {
            LogUtils.d(TAG, "No adapter attached; skipping hover scroll");
            return super.dispatchHoverEvent(ev);
        }
        int action = ev.getAction();
        int toolType = ev.getToolType(0);
        mIsMouseWheel = false;
        if ((action == MotionEvent.ACTION_HOVER_MOVE || action == MotionEvent.ACTION_HOVER_ENTER) && toolType == MotionEvent.TOOL_TYPE_STYLUS) {
            mIsPenHovered = true;
        } else if (action == MotionEvent.ACTION_HOVER_EXIT) {
            mIsPenHovered = false;
        }
        mNewTextViewHoverState = (boolean) Utils.genericInvokeMethod(TextView.class, "semIsTextViewHovered");
        if (mNewTextViewHoverState || !mOldTextViewHoverState || !mIsPenDragBlockEnabled || !(ev.getButtonState() == 32 || ev.getButtonState() == 2)) {
            mIsNeedPenSelectIconSet = false;
        } else {
            mIsNeedPenSelectIconSet = true;
        }
        mOldTextViewHoverState = mNewTextViewHoverState;
        if (action == MotionEvent.ACTION_HOVER_ENTER || mHoverScrollStateChanged) {
            mNeedsHoverScroll = true;
            mHoverScrollStateChanged = false;
            if (mHasNestedScrollRange) {
                adjustNestedScrollRange();
            }
            if (!(boolean) Utils.genericInvokeMethod(View.class, this, "isHoveringUIEnabled") || !mHoverScrollEnable) {
                mNeedsHoverScroll = false;
            }
            if (mNeedsHoverScroll && toolType == MotionEvent.TOOL_TYPE_STYLUS) {
                boolean isHoveringOn = Settings.System.getInt(mContext.getContentResolver(), "pen_hovering", 0) == 1;
                boolean isCarModeOn = false;
                try {
                    isCarModeOn = Settings.System.getInt(mContext.getContentResolver(), "car_mode_on") == 1;
                } catch (Settings.SettingNotFoundException e) {
                    LogUtils.i(TAG, "dispatchHoverEvent car_mode_on SettingNotFoundException");
                }
                if (!isHoveringOn || isCarModeOn) {
                    mNeedsHoverScroll = false;
                }
                if (isHoveringOn && mIsPenDragBlockEnabled && !mIsPenSelectPointerSetted && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) {
                    showPointerIcon(ev, 0x4e35);
                    mIsPenSelectPointerSetted = true;
                }
            }
            if (mNeedsHoverScroll && toolType == MotionEvent.TOOL_TYPE_MOUSE) {
                mNeedsHoverScroll = false;
            }
        } else if (action == MotionEvent.ACTION_HOVER_MOVE) {
            if ((mIsPenDragBlockEnabled && !mIsPenSelectPointerSetted && ev.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) || mIsNeedPenSelectIconSet) {
                showPointerIcon(ev, 0x4e35);
                mIsPenSelectPointerSetted = true;
            } else if (mIsPenDragBlockEnabled && mIsPenSelectPointerSetted && ev.getButtonState() != 32 && ev.getButtonState() != 2) {
                showPointerIcon(ev, 0x4e21);
                mIsPenSelectPointerSetted = false;
            }
        } else if (action == 10 && mIsPenSelectPointerSetted) {
            showPointerIcon(ev, 0x4e21);
            mIsPenSelectPointerSetted = false;
        }
        if (!mNeedsHoverScroll) {
            return super.dispatchHoverEvent(ev);
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int childCount = getChildCount();
        if (mIsEnabledPaddingInHoverScroll) {
            contentTop = mListPadding.top;
            contentBottom = getHeight() - mListPadding.bottom;
        } else {
            contentTop = mExtraPaddingInTopHoverArea;
            contentBottom = getHeight() - mExtraPaddingInBottomHoverArea;
        }
        boolean canScrollDown = findFirstChildPosition() + childCount < mAdapter.getItemCount();
        if (!canScrollDown && childCount > 0) {
            View child = getChildAt(childCount - 1);
            canScrollDown = child.getBottom() > getBottom() - mListPadding.bottom || child.getBottom() > getHeight() - mListPadding.bottom;
        }
        boolean canScrollUp = findFirstChildPosition() > 0;
        if (!canScrollUp && childCount > 0) {
            canScrollUp = getChildAt(0).getTop() < mListPadding.top;
        }
        boolean isPossibleTooltype = ev.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS;
        if ((y <= mHoverTopAreaHeight + contentTop || y >= (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange) && x > 0 && x <= getRight() && ((canScrollUp || canScrollDown) && ((y < contentTop || y > mHoverTopAreaHeight + contentTop || canScrollUp || !mIsHoverOverscrolled) && ((y < (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange || y > contentBottom - mRemainNestedScrollRange || canScrollDown || !mIsHoverOverscrolled) && ((!isPossibleTooltype || !(ev.getButtonState() == 32 || ev.getButtonState() == 2)) && isPossibleTooltype && !isLockScreenMode()))))) {
            if (mHasNestedScrollRange && mRemainNestedScrollRange > 0 && mRemainNestedScrollRange != mNestedScrollRange) {
                adjustNestedScrollRange();
            }
            if (!mHoverAreaEnter) {
                mHoverScrollStartTime = System.currentTimeMillis();
            }
            switch (action) {
                case MotionEvent.ACTION_HOVER_MOVE:
                    if (mHoverAreaEnter) {
                        if (y >= contentTop && y <= mHoverTopAreaHeight + contentTop) {
                            if (!mHoverHandler.hasMessages(0)) {
                                mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!mIsHoverOverscrolled || mHoverScrollDirection == 1) {
                                    showPointerIcon(ev, 0x4e2b);
                                }
                                mHoverScrollDirection = 2;
                                mHoverHandler.sendEmptyMessage(0);
                                break;
                            }
                        } else if (y >= (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange && y <= contentBottom - mRemainNestedScrollRange) {
                            if (!mHoverHandler.hasMessages(0)) {
                                mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!mIsHoverOverscrolled || mHoverScrollDirection == 2) {
                                    showPointerIcon(ev, 0x4e2f);
                                }
                                mHoverScrollDirection = 1;
                                mHoverHandler.sendEmptyMessage(0);
                                break;
                            }
                        } else {
                            if (mHoverHandler.hasMessages(0)) {
                                mHoverHandler.removeMessages(0);
                                if (mScrollState == 1) {
                                    setScrollState(0);
                                }
                            }
                            showPointerIcon(ev, 0x4e21);
                            mHoverRecognitionStartTime = 0;
                            mHoverScrollStartTime = 0;
                            mIsHoverOverscrolled = false;
                            mHoverAreaEnter = false;
                            mIsSendHoverScrollState = false;
                            break;
                        }
                    } else {
                        mHoverAreaEnter = true;
                        ev.setAction(MotionEvent.ACTION_HOVER_EXIT);
                        return super.dispatchHoverEvent(ev);
                    }
                    break;
                case 9:
                    mHoverAreaEnter = true;
                    if (y >= contentTop && y <= mHoverTopAreaHeight + contentTop) {
                        if (!mHoverHandler.hasMessages(0)) {
                            mHoverRecognitionStartTime = System.currentTimeMillis();
                            showPointerIcon(ev, 0x4e2b);
                            mHoverScrollDirection = 2;
                            mHoverHandler.sendEmptyMessage(0);
                            break;
                        }
                    } else if (y >= (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange && y <= contentBottom - mRemainNestedScrollRange && !mHoverHandler.hasMessages(0)) {
                        mHoverRecognitionStartTime = System.currentTimeMillis();
                        showPointerIcon(ev, 0x4e2f);
                        mHoverScrollDirection = 1;
                        mHoverHandler.sendEmptyMessage(0);
                        break;
                    }
                    break;
                case 10:
                    if (mHoverHandler.hasMessages(0)) {
                        mHoverHandler.removeMessages(0);
                    }
                    if (mScrollState == 1) {
                        setScrollState(0);
                    }
                    showPointerIcon(ev, 0x4e21);
                    mHoverRecognitionStartTime = 0;
                    mHoverScrollStartTime = 0;
                    mIsHoverOverscrolled = false;
                    mHoverAreaEnter = false;
                    mIsSendHoverScrollState = false;
                    if (mHoverScrollStateForListener != 0) {
                        mHoverScrollStateForListener = 0;
                        if (mScrollListener != null) {
                            mScrollListener.onScrollStateChanged(this, 0);
                        }
                    }
                    return super.dispatchHoverEvent(ev);
            }
            return true;
        }
        if (mHasNestedScrollRange && mRemainNestedScrollRange > 0 && mRemainNestedScrollRange != mNestedScrollRange) {
            adjustNestedScrollRange();
        }
        if (mHoverHandler.hasMessages(0)) {
            mHoverHandler.removeMessages(0);
            showPointerIcon(ev, 0x4e21);
            if (mScrollState == 1) {
                setScrollState(0);
            }
        }
        if ((y > mHoverTopAreaHeight + contentTop && y < (contentBottom - mHoverBottomAreaHeight) - mRemainNestedScrollRange) || x <= 0 || x > getRight()) {
            mIsHoverOverscrolled = false;
        }
        if (mHoverAreaEnter || mHoverScrollStartTime != 0) {
            showPointerIcon(ev, 0x4e21);
        }
        mHoverRecognitionStartTime = 0;
        mHoverScrollStartTime = 0;
        mHoverAreaEnter = false;
        mIsSendHoverScrollState = false;
        if (action == MotionEvent.ACTION_HOVER_EXIT && mHoverScrollStateForListener != 0) {
            mHoverScrollStateForListener = 0;
            if (mScrollListener != null) {
                mScrollListener.onScrollStateChanged(this, 0);
            }
        }
        return super.dispatchHoverEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_PAGE_UP:
                if (event.hasNoModifiers()) {
                    pageScroll(0);
                }
                break;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                if (event.hasNoModifiers()) {
                    pageScroll(1);
                }
                break;
            case KeyEvent.KEYCODE_CTRL_LEFT:
            case KeyEvent.KEYCODE_CTRL_RIGHT:
                mIsCtrlKeyPressed = true;
                break;
            case KeyEvent.KEYCODE_MOVE_HOME:
                if (event.hasNoModifiers()) {
                    pageScroll(2);
                }
                break;
            case KeyEvent.KEYCODE_MOVE_END:
                if (event.hasNoModifiers()) {
                    pageScroll(3);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CTRL_LEFT:
            case KeyEvent.KEYCODE_CTRL_RIGHT:
                mIsCtrlKeyPressed = false;
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void seslSetOnMultiSelectedListener(SeslOnMultiSelectedListener listener) {
        mOnMultiSelectedListener = listener;
    }

    public final SeslOnMultiSelectedListener seslGetOnMultiSelectedListener() {
        return mOnMultiSelectedListener;
    }

    public void seslSetPenSelectionEnabled(boolean enabled) {
        mIsPenSelectionEnabled = enabled;
    }

    public void seslSetLongPressMultiSelectionListener(SeslLongPressMultiSelectionListener listener) {
        mLongPressMultiSelectionListener = listener;
    }

    public final SeslLongPressMultiSelectionListener getLongPressMultiSelectionListener() {
        return mLongPressMultiSelectionListener;
    }

    public void seslSetSmoothScrollEnabled(boolean enabled) {
        if (mViewFlinger != null) {
            mViewFlinger.mScroller.setSmoothScrollEnabled(enabled);
        }
    }

    public void seslSetRegulationEnabled(boolean enabled) {
        if (mViewFlinger != null) {
            mViewFlinger.mScroller.setRegulationEnabled(enabled);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    mIsArrowKeyPressed = true;
                }
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public void seslSetPagingTouchSlopForStylus(boolean enabled) {
        mUsePagingTouchSlopForStylus = enabled;
    }

    public boolean seslIsPagingTouchSlopForStylusEnabled() {
        return mUsePagingTouchSlopForStylus;
    }


    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets = new Rect();
        boolean mInsetsDirty = true;
        boolean mPendingInvalidate = false;
        ViewHolder mViewHolder;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }

        public boolean viewNeedsUpdate() {
            return mViewHolder.needsUpdate();
        }

        public boolean isViewInvalid() {
            return mViewHolder.isInvalid();
        }

        public boolean isItemRemoved() {
            return mViewHolder.isRemoved();
        }

        public boolean isItemChanged() {
            return mViewHolder.isUpdated();
        }

        @Deprecated
        public int getViewPosition() {
            return mViewHolder.getPosition();
        }

        public int getViewLayoutPosition() {
            return mViewHolder.getLayoutPosition();
        }

        public int getViewAdapterPosition() {
            return mViewHolder.getAdapterPosition();
        }
    }

    public interface OnChildAttachStateChangeListener {
        void onChildViewAttachedToWindow(View view);

        void onChildViewDetachedFromWindow(View view);
    }

    public static abstract class OnFlingListener {
        public abstract boolean onFling(int velocityX, int velocityY);
    }

    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent(SeslRecyclerView rv, MotionEvent e);

        void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept);

        void onTouchEvent(SeslRecyclerView rv, MotionEvent e);
    }

    public static abstract class OnScrollListener {
        public void onScrollStateChanged(SeslRecyclerView recyclerView, int newState) { }

        public void onScrolled(SeslRecyclerView recyclerView, int dx, int dy) { }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() { }

        public void onItemRangeChanged(int positionStart, int itemCount) { }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) { }

        public void onItemRangeRemoved(int positionStart, int itemCount) { }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) { }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount, null);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private boolean mHasStableIds = false;
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public abstract int getItemCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public void onBindViewHolder(VH holder, int position, List<Object> list) {
            onBindViewHolder(holder, position);
        }

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            try {
                TraceCompat.beginSection(TRACE_CREATE_VIEW_TAG);
                VH holder = onCreateViewHolder(parent, viewType);
                if (holder.itemView.getParent() != null) {
                    throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
                }
                holder.mItemViewType = viewType;
                return holder;
            } finally {
                TraceCompat.endSection();
            }
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.mPosition = position;
            if (hasStableIds()) {
                holder.mItemId = getItemId(position);
            }
            holder.setFlags(ViewHolder.FLAG_BOUND, ViewHolder.FLAG_BOUND | ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_INVALID | ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN);
            TraceCompat.beginSection(TRACE_BIND_VIEW_TAG);
            onBindViewHolder(holder, position, holder.getUnmodifiedPayloads());
            holder.clearPayload();
            final ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof SeslRecyclerView.LayoutParams) {
                ((LayoutParams) layoutParams).mInsetsDirty = true;
            }
            TraceCompat.endSection();
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public void setHasStableIds(boolean hasStableIds) {
            if (hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            mHasStableIds = hasStableIds;
        }

        public long getItemId(int position) {
            return NO_ID;
        }

        public final boolean hasStableIds() {
            return mHasStableIds;
        }

        public void onViewRecycled(VH vh) { }

        public boolean onFailedToRecycleView(VH vh) {
            return false;
        }

        public void onViewAttachedToWindow(VH vh) { }

        public void onViewDetachedFromWindow(VH vh) { }

        public final boolean hasObservers() {
            return mObservable.hasObservers();
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        public void onAttachedToRecyclerView(SeslRecyclerView recyclerView) { }

        public void onDetachedFromRecyclerView(SeslRecyclerView recyclerView) { }

        public final void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int position) {
            mObservable.notifyItemRangeChanged(position, 1);
        }

        public final void notifyItemChanged(int position, Object payload) {
            mObservable.notifyItemRangeChanged(position, 1, payload);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount) {
            mObservable.notifyItemRangeChanged(positionStart, itemCount);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        public final void notifyItemInserted(int position) {
            mObservable.notifyItemRangeInserted(position, 1);
        }

        public final void notifyItemMoved(int fromPosition, int toPosition) {
            mObservable.notifyItemMoved(fromPosition, toPosition);
        }

        public final void notifyItemRangeInserted(int positionStart, int itemCount) {
            mObservable.notifyItemRangeInserted(positionStart, itemCount);
        }

        public final void notifyItemRemoved(int position) {
            mObservable.notifyItemRangeRemoved(position, 1);
        }

        public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
            mObservable.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public interface ChildDrawingOrderCallback {
        int onGetChildDrawingOrder(int i, int i2);
    }

    public static class State {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout = false;
        boolean mIsMeasuring = false;
        int mItemCount = 0;
        int mLayoutStep = STEP_START;
        int mPreviousLayoutItemCount = 0;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations = false;
        boolean mRunSimpleAnimations = false;
        boolean mStructureChanged = false;
        private int mTargetPosition = SeslRecyclerView.NO_POSITION;
        boolean mTrackOldChangeHolders = false;

        @Retention(RetentionPolicy.SOURCE)
        @interface LayoutState { }

        void assertLayoutStep(int accepted) {
            if ((mLayoutStep & accepted) == 0) {
                throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(accepted) + " but it is " + Integer.toBinaryString(mLayoutStep));
            }
        }

        State reset() {
            mTargetPosition = SeslRecyclerView.NO_POSITION;
            if (mData != null) {
                mData.clear();
            }
            mItemCount = 0;
            mStructureChanged = false;
            mIsMeasuring = false;
            return this;
        }

        void prepareForNestedPrefetch(Adapter adapter) {
            mLayoutStep = STEP_START;
            mItemCount = adapter.getItemCount();
            mInPreLayout = false;
            mTrackOldChangeHolders = false;
            mIsMeasuring = false;
        }

        public boolean isMeasuring() {
            return mIsMeasuring;
        }

        public boolean isPreLayout() {
            return mInPreLayout;
        }

        public boolean willRunPredictiveAnimations() {
            return mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return mRunSimpleAnimations;
        }

        public void remove(int resourceId) {
            if (mData == null) {
                return;
            }
            mData.remove(resourceId);
        }

        @SuppressWarnings("TypeParameterUnusedInFormals")
        public <T> T get(int resourceId) {
            if (mData == null) {
                return null;
            }
            return (T) mData.get(resourceId);
        }

        public void put(int resourceId, Object data) {
            if (mData == null) {
                mData = new SparseArray<>();
            }
            mData.put(resourceId, data);
        }

        public int getTargetScrollPosition() {
            return mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            return mTargetPosition != SeslRecyclerView.NO_POSITION;
        }

        public boolean didStructureChange() {
            return mStructureChanged;
        }

        public int getItemCount() {
            return mInPreLayout ? mPreviousLayoutItemCount - mDeletedInvisibleItemCountSincePreviousLayout : mItemCount;
        }

        public int getRemainingScrollHorizontal() {
            return mRemainingScrollHorizontal;
        }

        public int getRemainingScrollVertical() {
            return mRemainingScrollVertical;
        }

        public String toString() {
            return "State{mTargetPosition=" + mTargetPosition + ", mData=" + mData + ", mItemCount=" + mItemCount + ", mIsMeasuring=" + mIsMeasuring + ", mPreviousLayoutItemCount=" + mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + mStructureChanged + ", mInPreLayout=" + mInPreLayout + ", mRunSimpleAnimations=" + mRunSimpleAnimations + ", mRunPredictiveAnimations=" + mRunPredictiveAnimations + '}';
        }
    }

    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler recycler, int i, int i2);
    }

    private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
        ItemAnimatorRestoreListener() { }

        @Override
        public void onAnimationFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.mShadowedHolder != null && item.mShadowingHolder == null) {
                item.mShadowedHolder = null;
            }
            item.mShadowingHolder = null;
            if (!item.shouldBeKeptAsChild()) {
                if (!removeAnimatingView(item.itemView) && item.isTmpDetached()) {
                    removeDetachedView(item.itemView, false);
                }
            }
        }
    }

    public static abstract class ItemDecoration {
        @Deprecated
        public void onDraw(Canvas c, SeslRecyclerView parent) { }

        public void onDraw(Canvas c, SeslRecyclerView parent, State state) {
            onDraw(c, parent);
        }

        @Deprecated
        public void onDrawOver(Canvas c, SeslRecyclerView parent) { }

        public void onDrawOver(Canvas c, SeslRecyclerView parent, State state) {
            onDrawOver(c, parent);
        }

        public void seslOnDispatchDraw(Canvas c, SeslRecyclerView parent, State state) { }

        @Deprecated
        public void getItemOffsets(Rect outRect, int itemPosition, SeslRecyclerView parent) {
            outRect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect outRect, View view, SeslRecyclerView parent, State state) {
            getItemOffsets(outRect, ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition(), parent);
        }
    }

    public static abstract class LayoutManager {
        boolean mAutoMeasure = false;
        SeslChildHelper mChildHelper;
        private int mHeight;
        private int mHeightMode;
        private int mWidth;
        private int mWidthMode;
        boolean mIsAttachedToWindow = false;
        private boolean mItemPrefetchEnabled = true;
        private boolean mMeasurementCacheEnabled = true;
        int mPrefetchMaxCountObserved;
        boolean mPrefetchMaxObservedInInitialPrefetch;
        SeslRecyclerView mRecyclerView;
        boolean mRequestedSimpleAnimations = false;
        SmoothScroller mSmoothScroller;

        private final SeslViewBoundsCheck.Callback mHorizontalBoundCheckCallback = new SeslViewBoundsCheck.Callback() {
            @Override
            public int getChildCount() {
                return LayoutManager.this.getChildCount();
            }

            @Override
            public View getParent() {
                return mRecyclerView;
            }

            @Override
            public View getChildAt(int index) {
                return LayoutManager.this.getChildAt(index);
            }

            @Override
            public int getParentStart() {
                return LayoutManager.this.getPaddingLeft();
            }

            @Override
            public int getParentEnd() {
                return LayoutManager.this.getWidth() - LayoutManager.this.getPaddingRight();
            }

            @Override
            public int getChildStart(View view) {
                final SeslRecyclerView.LayoutParams params = (SeslRecyclerView.LayoutParams) view.getLayoutParams();
                return LayoutManager.this.getDecoratedLeft(view) - params.leftMargin;
            }

            @Override
            public int getChildEnd(View view) {
                final SeslRecyclerView.LayoutParams params = (SeslRecyclerView.LayoutParams) view.getLayoutParams();
                return LayoutManager.this.getDecoratedRight(view) + params.rightMargin;
            }
        };

        private final SeslViewBoundsCheck.Callback mVerticalBoundCheckCallback = new SeslViewBoundsCheck.Callback() {
            @Override
            public int getChildCount() {
                return LayoutManager.this.getChildCount();
            }

            @Override
            public View getParent() {
                return mRecyclerView;
            }

            @Override
            public View getChildAt(int index) {
                return LayoutManager.this.getChildAt(index);
            }

            @Override
            public int getParentStart() {
                return LayoutManager.this.getPaddingTop();
            }

            @Override
            public int getParentEnd() {
                return LayoutManager.this.getHeight() - LayoutManager.this.getPaddingBottom();
            }

            @Override
            public int getChildStart(View view) {
                final SeslRecyclerView.LayoutParams params = (SeslRecyclerView.LayoutParams) view.getLayoutParams();
                return LayoutManager.this.getDecoratedTop(view) - params.topMargin;
            }

            @Override
            public int getChildEnd(View view) {
                final SeslRecyclerView.LayoutParams params = (SeslRecyclerView.LayoutParams) view.getLayoutParams();
                return LayoutManager.this.getDecoratedBottom(view) + params.bottomMargin;
            }
        };

        SeslViewBoundsCheck mHorizontalBoundCheck = new SeslViewBoundsCheck(mHorizontalBoundCheckCallback);
        SeslViewBoundsCheck mVerticalBoundCheck = new SeslViewBoundsCheck(mVerticalBoundCheckCallback);

        public interface LayoutPrefetchRegistry {
            void addPosition(int layoutPosition, int pixelDistance);
        }

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        void setRecyclerView(SeslRecyclerView recyclerView) {
            if (recyclerView == null) {
                mRecyclerView = null;
                mChildHelper = null;
                mWidth = 0;
                mHeight = 0;
            } else {
                mRecyclerView = recyclerView;
                mChildHelper = recyclerView.mChildHelper;
                mWidth = recyclerView.getWidth();
                mHeight = recyclerView.getHeight();
            }
            mWidthMode = MeasureSpec.EXACTLY;
            mHeightMode = MeasureSpec.EXACTLY;
        }

        void setMeasureSpecs(int wSpec, int hSpec) {
            mWidth = MeasureSpec.getSize(wSpec);
            mWidthMode = MeasureSpec.getMode(wSpec);
            if (mWidthMode == MeasureSpec.UNSPECIFIED && !SeslRecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                mWidth = 0;
            }
            mHeight = MeasureSpec.getSize(hSpec);
            mHeightMode = MeasureSpec.getMode(hSpec);
            if (mHeightMode == MeasureSpec.UNSPECIFIED && !SeslRecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                mHeight = 0;
            }
        }

        void setMeasuredDimensionFromChildren(int widthSpec, int heightSpec) {
            final int count = getChildCount();
            if (count == 0) {
                mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
                return;
            }
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                final Rect bounds = mRecyclerView.mTempRect;
                getDecoratedBoundsWithMargins(child, bounds);
                if (bounds.left < minX) {
                    minX = bounds.left;
                }
                if (bounds.right > maxX) {
                    maxX = bounds.right;
                }
                if (bounds.top < minY) {
                    minY = bounds.top;
                }
                if (bounds.bottom > maxY) {
                    maxY = bounds.bottom;
                }
            }
            mRecyclerView.mTempRect.set(minX, minY, maxX, maxY);
            setMeasuredDimension(mRecyclerView.mTempRect, widthSpec, heightSpec);
        }

        public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
            int usedWidth = childrenBounds.width() + getPaddingLeft() + getPaddingRight();
            int usedHeight = childrenBounds.height() + getPaddingTop() + getPaddingBottom();
            int width = chooseSize(wSpec, usedWidth, getMinimumWidth());
            int height = chooseSize(hSpec, usedHeight, getMinimumHeight());
            setMeasuredDimension(width, height);}

        public void requestLayout() {
            if (mRecyclerView != null) {
                mRecyclerView.requestLayout();
            }
        }

        public void assertInLayoutOrScroll(String message) {
            if (mRecyclerView != null) {
                mRecyclerView.assertInLayoutOrScroll(message);
            }
        }

        public static int chooseSize(int spec, int desired, int min) {
            final int mode = MeasureSpec.getMode(spec);
            final int size = MeasureSpec.getSize(spec);
            switch (mode) {
                case View.MeasureSpec.EXACTLY:
                    return size;
                case View.MeasureSpec.AT_MOST:
                    return Math.min(size, Math.max(desired, min));
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    return Math.max(desired, min);
            }
        }

        public void assertNotInLayoutOrScroll(String message) {
            if (mRecyclerView != null) {
                mRecyclerView.assertNotInLayoutOrScroll(message);
            }
        }

        @Deprecated
        public void setAutoMeasureEnabled(boolean enabled) {
            mAutoMeasure = enabled;
        }

        public boolean isAutoMeasureEnabled() {
            return mAutoMeasure;
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public final void setItemPrefetchEnabled(boolean enabled) {
            if (enabled != mItemPrefetchEnabled) {
                mItemPrefetchEnabled = enabled;
                mPrefetchMaxCountObserved = 0;
                if (mRecyclerView != null) {
                    mRecyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }

        public final boolean isItemPrefetchEnabled() {
            return mItemPrefetchEnabled;
        }

        public void collectAdjacentPrefetchPositions(int dx, int dy, State state, LayoutPrefetchRegistry layoutPrefetchRegistry) { }

        public void collectInitialPrefetchPositions(int adapterItemCount, LayoutPrefetchRegistry layoutPrefetchRegistry) { }

        void dispatchAttachedToWindow(SeslRecyclerView view) {
            mIsAttachedToWindow = true;
            onAttachedToWindow(view);
        }

        void dispatchDetachedFromWindow(SeslRecyclerView view, Recycler recycler) {
            mIsAttachedToWindow = false;
            onDetachedFromWindow(view, recycler);
        }

        public boolean isAttachedToWindow() {
            return mIsAttachedToWindow;
        }

        public void postOnAnimation(Runnable action) {
            if (mRecyclerView != null) {
                ViewCompat.postOnAnimation(mRecyclerView, action);
            }
        }

        public boolean removeCallbacks(Runnable action) {
            if (mRecyclerView != null) {
                return mRecyclerView.removeCallbacks(action);
            }
            return false;
        }

        public void onAttachedToWindow(SeslRecyclerView view) { }

        @Deprecated
        public void onDetachedFromWindow(SeslRecyclerView view) { }

        @CallSuper
        public void onDetachedFromWindow(SeslRecyclerView view, Recycler recycler) {
            onDetachedFromWindow(view);
        }

        public boolean getClipToPadding() {
            return mRecyclerView != null && mRecyclerView.mClipToPadding;
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            LogUtils.e(TAG, "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onLayoutCompleted(State state) { }

        public boolean checkLayoutParams(LayoutParams lp) {
            return lp != null;
        }

        public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof MarginLayoutParams) {
                return new LayoutParams((MarginLayoutParams) lp);
            }
            return new LayoutParams(lp);
        }

        public LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
            return new LayoutParams(c, attrs);
        }

        public int scrollHorizontallyBy(int dx, Recycler recycler, State state) {
            return 0;
        }

        public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
            return 0;
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public void scrollToPosition(int position) {
            if (DEBUG) {
                LogUtils.e(TAG, "You MUST implement scrollToPosition. It will soon become abstract");
            }
        }

        public void smoothScrollToPosition(SeslRecyclerView recyclerView, State state, int position) {
            LogUtils.e(TAG, "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            if (mSmoothScroller != null && smoothScroller != mSmoothScroller && mSmoothScroller.isRunning()) {
                mSmoothScroller.stop();
            }
            mSmoothScroller = smoothScroller;
            mSmoothScroller.start(mRecyclerView, this);
        }

        public boolean isSmoothScrolling() {
            return mSmoothScroller != null && mSmoothScroller.isRunning();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection(mRecyclerView);
        }

        public void endAnimation(View view) {
            if (mRecyclerView.mItemAnimator != null) {
                mRecyclerView.mItemAnimator.endAnimation(getChildViewHolderInt(view));
            }
        }

        public void addDisappearingView(View child) {
            addDisappearingView(child, -1);
        }

        public void addDisappearingView(View child, int index) {
            addViewInt(child, index, true);
        }

        public void addView(View child) {
            addView(child, -1);
        }

        public void addView(View child, int index) {
            addViewInt(child, index, false);
        }

        private void addViewInt(View child, int index, boolean disappearing) {
            final ViewHolder holder = getChildViewHolderInt(child);
            if (disappearing || holder.isRemoved()) {
                mRecyclerView.mViewInfoStore.addToDisappearedInLayout(holder);
            } else {
                mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(holder);
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (holder.wasReturnedFromScrap() || holder.isScrap()) {
                if (holder.isScrap()) {
                    holder.unScrap();
                } else {
                    holder.clearReturnedFromScrapFlag();
                }
                mChildHelper.attachViewToParent(child, index, child.getLayoutParams(), false);
                if (DISPATCH_TEMP_DETACH) {
                    ViewCompat.dispatchFinishTemporaryDetach(child);
                }
            } else if (child.getParent() == mRecyclerView) {
                int currentIndex = mChildHelper.indexOfChild(child);
                if (index == -1) {
                    index = mChildHelper.getChildCount();
                }
                if (currentIndex == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + mRecyclerView.indexOfChild(child) + mRecyclerView.exceptionLabel());
                }
                if (currentIndex != index) {
                    mRecyclerView.mLayout.moveView(currentIndex, index);
                }
            } else {
                mChildHelper.addView(child, index, false);
                lp.mInsetsDirty = true;
                if (mSmoothScroller != null && mSmoothScroller.isRunning()) {
                    mSmoothScroller.onChildAttachedToWindow(child);
                }
            }
            if (lp.mPendingInvalidate) {
                if (DEBUG) {
                    LogUtils.d(TAG, "consuming pending invalidate on child " + lp.mViewHolder);
                }
                holder.itemView.invalidate();
                lp.mPendingInvalidate = false;
            }
        }

        public void removeView(View child) {
            mChildHelper.removeView(child);
        }

        public void removeViewAt(int index) {
            final View child = getChildAt(index);
            if (child != null) {
                mChildHelper.removeViewAt(index);
            }
        }

        public void removeAllViews() {
            final int childCount = getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                mChildHelper.removeViewAt(i);
            }
        }

        public int getBaseline() {
            return -1;
        }

        public int getPosition(View view) {
            return ((SeslRecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        }

        public int getItemViewType(View view) {
            return getChildViewHolderInt(view).getItemViewType();
        }

        public View findContainingItemView(View view) {
            if (mRecyclerView == null) {
                return null;
            }
            View found = mRecyclerView.findContainingItemView(view);
            if (found == null) {
                return null;
            }
            if (mChildHelper.isHidden(found)) {
                return null;
            }
            return found;
        }

        public View findViewByPosition(int position) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                ViewHolder vh = getChildViewHolderInt(child);
                if (vh == null) {
                    continue;
                }
                if (vh.getLayoutPosition() == position && !vh.shouldIgnore() && (mRecyclerView.mState.isPreLayout() || !vh.isRemoved())) {
                    return child;
                }
            }
            return null;
        }

        public void detachView(View child) {
            final int ind = mChildHelper.indexOfChild(child);
            if (ind >= 0) {
                detachViewInternal(ind, child);
            }
        }

        public void detachViewAt(int index) {
            detachViewInternal(index, getChildAt(index));
        }

        private void detachViewInternal(int index, View view) {
            if (DISPATCH_TEMP_DETACH) {
                ViewCompat.dispatchStartTemporaryDetach(view);
            }
            mChildHelper.detachViewFromParent(index);
        }

        public void attachView(View child, int index, LayoutParams lp) {
            ViewHolder vh = getChildViewHolderInt(child);
            if (vh.isRemoved()) {
                mRecyclerView.mViewInfoStore.addToDisappearedInLayout(vh);
            } else {
                mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(vh);
            }
            mChildHelper.attachViewToParent(child, index, lp, vh.isRemoved());
            if (DISPATCH_TEMP_DETACH)  {
                ViewCompat.dispatchFinishTemporaryDetach(child);
            }
        }

        public void attachView(View child, int index) {
            attachView(child, index, (LayoutParams) child.getLayoutParams());
        }

        public void attachView(View child) {
            attachView(child, -1);
        }

        public void removeDetachedView(View child) {
            mRecyclerView.removeDetachedView(child, false);
        }

        public void moveView(int fromIndex, int toIndex) {
            View view = getChildAt(fromIndex);
            if (view == null) {
                throw new IllegalArgumentException("Cannot move a child from non-existing index:" + fromIndex + mRecyclerView.toString());
            }
            detachViewAt(fromIndex);
            attachView(view, toIndex);
        }

        public void detachAndScrapView(View child, Recycler recycler) {
            int index = mChildHelper.indexOfChild(child);
            scrapOrRecycleView(recycler, index, child);
        }

        public void detachAndScrapViewAt(int index, Recycler recycler) {
            final View child = getChildAt(index);
            scrapOrRecycleView(recycler, index, child);
        }

        public void removeAndRecycleView(View child, Recycler recycler) {
            removeView(child);
            recycler.recycleView(child);
        }

        public void removeAndRecycleViewAt(int index, Recycler recycler) {
            final View view = getChildAt(index);
            removeViewAt(index);
            recycler.recycleView(view);
        }

        public int getChildCount() {
            return mChildHelper != null ? mChildHelper.getChildCount() : 0;
        }

        public View getChildAt(int index) {
            return mChildHelper != null ? mChildHelper.getChildAt(index) : null;
        }

        public int getWidthMode() {
            return mWidthMode;
        }

        public int getHeightMode() {
            return mHeightMode;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public int getPaddingLeft() {
            return mRecyclerView != null ? mRecyclerView.getPaddingLeft() : 0;
        }

        public int getPaddingTop() {
            return mRecyclerView != null ? mRecyclerView.getPaddingTop() : 0;
        }

        public int getPaddingRight() {
            return mRecyclerView != null ? mRecyclerView.getPaddingRight() : 0;
        }

        public int getPaddingBottom() {
            return mRecyclerView != null ? mRecyclerView.getPaddingBottom() : 0;
        }

        public int getPaddingStart() {
            return mRecyclerView != null ? ViewCompat.getPaddingStart(mRecyclerView) : 0;
        }

        public int getPaddingEnd() {
            return mRecyclerView != null ? ViewCompat.getPaddingEnd(mRecyclerView) : 0;
        }

        public boolean isFocused() {
            return mRecyclerView != null && mRecyclerView.isFocused();
        }

        public boolean hasFocus() {
            return mRecyclerView != null && mRecyclerView.hasFocus();
        }

        public View getFocusedChild() {
            if (mRecyclerView == null) {
                return null;
            }
            final View focused = mRecyclerView.getFocusedChild();
            if (focused == null || mChildHelper.isHidden(focused)) {
                return null;
            }
            return focused;
        }

        public int getItemCount() {
            final Adapter a = mRecyclerView != null ? mRecyclerView.getAdapter() : null;
            return a != null ? a.getItemCount() : 0;
        }

        public void offsetChildrenHorizontal(int dx) {
            if (mRecyclerView != null) {
                mRecyclerView.offsetChildrenHorizontal(dx);
            }
        }

        public void offsetChildrenVertical(int dy) {
            if (mRecyclerView != null) {
                mRecyclerView.offsetChildrenVertical(dy);
            }
        }

        public void ignoreView(View view) {
            if (view.getParent() != mRecyclerView || mRecyclerView.indexOfChild(view) == -1) {
                throw new IllegalArgumentException("View should be fully attached to be ignored" + mRecyclerView.exceptionLabel());
            }
            ViewHolder vh = getChildViewHolderInt(view);
            vh.addFlags(ViewHolder.FLAG_IGNORE);
            mRecyclerView.mViewInfoStore.removeViewHolder(vh);
        }

        public void stopIgnoringView(View view) {
            final ViewHolder vh = getChildViewHolderInt(view);
            vh.stopIgnoring();
            vh.resetInternal();
            vh.addFlags(ViewHolder.FLAG_INVALID);
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            final int childCount = getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View v = getChildAt(i);
                scrapOrRecycleView(recycler, i, v);
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int index, View view) {
            final ViewHolder viewHolder = getChildViewHolderInt(view);
            if (viewHolder.shouldIgnore()) {
                if (DEBUG) {
                    LogUtils.d(TAG, "ignoring view " + viewHolder);
                }
                return;
            }
            if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !mRecyclerView.mAdapter.hasStableIds()) {
                removeViewAt(index);
                recycler.recycleViewHolderInternal(viewHolder);
            } else {
                detachViewAt(index);
                recycler.scrapView(view);
                mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
            }
        }

        void removeAndRecycleScrapInt(Recycler recycler) {
            final int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; i--) {
                final View scrap = recycler.getScrapViewAt(i);
                final ViewHolder vh = getChildViewHolderInt(scrap);
                if (vh.shouldIgnore()) {
                    continue;
                }
                vh.setIsRecyclable(false);
                if (vh.isTmpDetached()) {
                    mRecyclerView.removeDetachedView(scrap, false);
                }
                if (mRecyclerView.mItemAnimator != null) {
                    mRecyclerView.mItemAnimator.endAnimation(vh);
                }
                vh.setIsRecyclable(true);
                recycler.quickRecycleScrapView(scrap);
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                mRecyclerView.invalidate();
            }
        }

        public void measureChild(View child, int widthUsed, int heightUsed) {
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
            widthUsed += insets.left + insets.right;
            heightUsed += insets.top + insets.bottom;
            final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + widthUsed, lp.width, canScrollHorizontally());
            final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + heightUsed, lp.height, canScrollVertically());
            if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
                child.measure(widthSpec, heightSpec);
            }
        }

        boolean shouldReMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return !mMeasurementCacheEnabled || !isMeasurementUpToDate(child.getMeasuredWidth(), widthSpec, lp.width) || !isMeasurementUpToDate(child.getMeasuredHeight(), heightSpec, lp.height);
        }

        boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return child.isLayoutRequested() || !mMeasurementCacheEnabled || !isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width) || !isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height);
        }

        public boolean isMeasurementCacheEnabled() {
            return mMeasurementCacheEnabled;
        }

        public void setMeasurementCacheEnabled(boolean measurementCacheEnabled) {
            mMeasurementCacheEnabled = measurementCacheEnabled;
        }

        private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
            final int specMode = MeasureSpec.getMode(spec);
            final int specSize = MeasureSpec.getSize(spec);
            if (dimension > 0 && childSize != dimension) {
                return false;
            }
            switch (specMode) {
                case MeasureSpec.UNSPECIFIED:
                    return true;
                case MeasureSpec.AT_MOST:
                    return specSize >= childSize;
                case MeasureSpec.EXACTLY:
                    return  specSize == childSize;
            }
            return false;
        }

        public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
            int heightUsed2 = heightUsed + insets.top + insets.bottom;
            int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed + insets.left + insets.right, lp.width, canScrollHorizontally());
            int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed2, lp.height, canScrollVertically());
            if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
                child.measure(widthSpec, heightSpec);
            }
        }

        @Deprecated
        public static int getChildMeasureSpec(int parentSize, int padding, int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else {
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
            } else {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
            }
            return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        public static int getChildMeasureSpec(int parentSize, int parentMode, int padding, int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    switch (parentMode) {
                        case MeasureSpec.AT_MOST:
                        case MeasureSpec.EXACTLY:
                            resultSize = size;
                            resultMode = parentMode;
                            break;
                        case MeasureSpec.UNSPECIFIED:
                            resultSize = 0;
                            resultMode = MeasureSpec.UNSPECIFIED;
                            break;
                    }
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
            } else {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = parentMode;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = size;
                    if (parentMode == MeasureSpec.AT_MOST || parentMode == MeasureSpec.EXACTLY) {
                        resultMode = MeasureSpec.AT_MOST;
                    } else {
                        resultMode = MeasureSpec.UNSPECIFIED;
                    }

                }
            }
            return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        public int getDecoratedMeasuredWidth(View child) {
            final Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredWidth() + insets.left + insets.right;
        }

        public int getDecoratedMeasuredHeight(View child) {
            final Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredHeight() + insets.top + insets.bottom;
        }

        public void layoutDecorated(View child, int left, int top, int right, int bottom) {
            final Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            child.layout(left + insets.left, top + insets.top, right - insets.right, bottom - insets.bottom);
        }

        public void layoutDecoratedWithMargins(View child, int left, int top, int right, int bottom) {
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final Rect insets = lp.mDecorInsets;
            child.layout(left + insets.left + lp.leftMargin, top + insets.top + lp.topMargin, right - insets.right - lp.rightMargin, bottom - insets.bottom - lp.bottomMargin);
        }

        public void getTransformedBoundingBox(View child, boolean includeDecorInsets, Rect out) {
            if (includeDecorInsets) {
                Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
                out.set(-insets.left, -insets.top,
                        child.getWidth() + insets.right, child.getHeight() + insets.bottom);
            } else {
                out.set(0, 0, child.getWidth(), child.getHeight());
            }

            if (mRecyclerView != null) {
                final Matrix childMatrix = child.getMatrix();
                if (childMatrix != null && !childMatrix.isIdentity()) {
                    final RectF tempRectF = mRecyclerView.mTempRectF;
                    tempRectF.set(out);
                    childMatrix.mapRect(tempRectF);
                    out.set((int) Math.floor(tempRectF.left), (int) Math.floor(tempRectF.top), (int) Math.ceil(tempRectF.right), (int) Math.ceil(tempRectF.bottom));
                }
            }
            out.offset(child.getLeft(), child.getTop());
        }

        public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
            SeslRecyclerView.getDecoratedBoundsWithMarginsInt(view, outBounds);
        }

        public int getDecoratedLeft(View child) {
            return child.getLeft() - getLeftDecorationWidth(child);
        }

        public int getDecoratedTop(View child) {
            return child.getTop() - getTopDecorationHeight(child);
        }

        public int getDecoratedRight(View child) {
            return child.getRight() + getRightDecorationWidth(child);
        }

        public int getDecoratedBottom(View child) {
            return child.getBottom() + getBottomDecorationHeight(child);
        }

        public void calculateItemDecorationsForChild(View child, Rect outRect) {
            if (mRecyclerView == null) {
                outRect.set(0, 0, 0, 0);
                return;
            }
            Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
            outRect.set(insets);
        }

        public int getTopDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.top;
        }

        public int getBottomDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.bottom;
        }

        public int getLeftDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.left;
        }

        public int getRightDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.right;
        }

        public View onFocusSearchFailed(View focused, int direction, Recycler recycler, State state) {
            return null;
        }

        public View onInterceptFocusSearch(View focused, int direction) {
            return null;
        }

        private int[] getChildRectangleOnScreenScrollAmount(SeslRecyclerView parent, View child, Rect rect, boolean immediate) {
            int[] out = new int[2];
            final int parentLeft = getPaddingLeft();
            final int parentTop = getPaddingTop();
            final int parentRight = getWidth() - getPaddingRight();
            final int parentBottom = getHeight() - getPaddingBottom();
            final int childLeft = child.getLeft() + rect.left - child.getScrollX();
            final int childTop = child.getTop() + rect.top - child.getScrollY();
            final int childRight = childLeft + rect.width();
            final int childBottom = childTop + rect.height();

            final int offScreenLeft = Math.min(0, childLeft - parentLeft);
            final int offScreenTop = Math.min(0, childTop - parentTop);
            final int offScreenRight = Math.max(0, childRight - parentRight);
            final int offScreenBottom = Math.max(0, childBottom - parentBottom);

            final int dx;
            if (getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft : Math.min(childLeft - parentLeft, offScreenRight);
            }

            final int dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
            out[0] = dx;
            out[1] = dy;
            return out;
        }

        public boolean requestChildRectangleOnScreen(SeslRecyclerView parent, View child, Rect rect, boolean immediate) {
            return requestChildRectangleOnScreen(parent, child, rect, immediate, false);
        }

        public boolean requestChildRectangleOnScreen(SeslRecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
            int[] scrollAmount = getChildRectangleOnScreenScrollAmount(parent, child, rect, immediate);
            int dx = scrollAmount[0];
            int dy = scrollAmount[1];
            if (!focusedChildVisible || isFocusedChildVisibleAfterScrolling(parent, dx, dy)) {
                if (dx != 0 || dy != 0) {
                    if (immediate) {
                        parent.scrollBy(dx, dy);
                    } else {
                        parent.smoothScrollBy(dx, dy);
                    }
                    return true;
                }
            }
            return false;
        }

        public boolean isViewPartiallyVisible(View child, boolean completelyVisible, boolean acceptEndPointInclusion) {
            int boundsFlag = (SeslViewBoundsCheck.FLAG_CVS_GT_PVS | SeslViewBoundsCheck.FLAG_CVS_EQ_PVS | SeslViewBoundsCheck.FLAG_CVE_LT_PVE | SeslViewBoundsCheck.FLAG_CVE_EQ_PVE);
            boolean isViewFullyVisible = mHorizontalBoundCheck.isViewWithinBoundFlags(child, boundsFlag) && mVerticalBoundCheck.isViewWithinBoundFlags(child, boundsFlag);
            if (completelyVisible) {
                return isViewFullyVisible;
            } else {
                return !isViewFullyVisible;
            }
        }

        private boolean isFocusedChildVisibleAfterScrolling(SeslRecyclerView parent, int dx, int dy) {
            final View focusedChild = parent.getFocusedChild();
            if (focusedChild == null) {
                return false;
            }
            final int parentLeft = getPaddingLeft();
            final int parentTop = getPaddingTop();
            final int parentRight = getWidth() - getPaddingRight();
            final int parentBottom = getHeight() - getPaddingBottom();
            final Rect bounds = mRecyclerView.mTempRect;
            getDecoratedBoundsWithMargins(focusedChild, bounds);

            if (bounds.left - dx >= parentRight || bounds.right - dx <= parentLeft || bounds.top - dy >= parentBottom || bounds.bottom - dy <= parentTop) {
                return false;
            }
            return true;
        }

        @Deprecated
        public boolean onRequestChildFocus(SeslRecyclerView parent, View child, View focused) {
            return isSmoothScrolling() || parent.isComputingLayout();
        }

        public boolean onRequestChildFocus(SeslRecyclerView parent, State state, View child, View focused) {
            return onRequestChildFocus(parent, child, focused);
        }

        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter) { }

        public boolean onAddFocusables(SeslRecyclerView recyclerView, ArrayList<View> arrayList, int direction, int focusableMode) {
            return false;
        }

        public void onItemsChanged(SeslRecyclerView recyclerView) { }

        public void onItemsAdded(SeslRecyclerView recyclerView, int positionStart, int itemCount) { }

        public void onItemsRemoved(SeslRecyclerView recyclerView, int positionStart, int itemCount) { }

        public void onItemsUpdated(SeslRecyclerView recyclerView, int positionStart, int itemCount) { }

        public void onItemsUpdated(SeslRecyclerView recyclerView, int positionStart, int itemCount, Object payload) {
            onItemsUpdated(recyclerView, positionStart, itemCount);
        }

        public void onItemsMoved(SeslRecyclerView recyclerView, int from, int to, int itemCount) { }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
            mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
        }

        public void setMeasuredDimension(int widthSize, int heightSize) {
            mRecyclerView.setMeasuredDimension(widthSize, heightSize);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth(mRecyclerView);
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight(mRecyclerView);
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) { }

        void stopSmoothScroller() {
            if (mSmoothScroller != null) {
                mSmoothScroller.stop();
            }
        }

        void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (mSmoothScroller == smoothScroller) {
                mSmoothScroller = null;
            }
        }

        public void onScrollStateChanged(int state) { }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                final View view = getChildAt(i);
                if (!getChildViewHolderInt(view).shouldIgnore()) {
                    removeAndRecycleViewAt(i, recycler);
                }
            }
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
            onInitializeAccessibilityNodeInfo(mRecyclerView.mRecycler, mRecyclerView.mState, info);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat info) {
            if (mRecyclerView.canScrollVertically(-1) || mRecyclerView.canScrollHorizontally(-1)) {
                info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
                info.setScrollable(true);
            }
            if (mRecyclerView.canScrollVertically(1) || mRecyclerView.canScrollHorizontally(1)) {
                info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
                info.setScrollable(true);
            }
            final AccessibilityNodeInfoCompat.CollectionInfoCompat collectionInfo = AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state));
            info.setCollectionInfo(collectionInfo);
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            onInitializeAccessibilityEvent(mRecyclerView.mRecycler, mRecyclerView.mState, event);
        }

        public void onInitializeAccessibilityEvent(Recycler recycler, State state, AccessibilityEvent event) {
            if (mRecyclerView == null || event == null) {
                return;
            }
            event.setScrollable(mRecyclerView.canScrollVertically(1) || mRecyclerView.canScrollVertically(-1) || mRecyclerView.canScrollHorizontally(-1) || mRecyclerView.canScrollHorizontally(1));

            if (mRecyclerView.mAdapter != null) {
                event.setItemCount(mRecyclerView.mAdapter.getItemCount());
            }
        }

        void onInitializeAccessibilityNodeInfoForItem(View host, AccessibilityNodeInfoCompat info) {
            final ViewHolder vh = getChildViewHolderInt(host);
            if (vh != null && !vh.isRemoved() && !mChildHelper.isHidden(vh.itemView)) {
                onInitializeAccessibilityNodeInfoForItem(mRecyclerView.mRecycler, mRecyclerView.mState, host, info);
            }
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View host, AccessibilityNodeInfoCompat info) {
            int rowIndexGuess = canScrollVertically() ? getPosition(host) : 0;
            int columnIndexGuess = canScrollHorizontally() ? getPosition(host) : 0;
            final AccessibilityNodeInfoCompat.CollectionItemInfoCompat itemInfo = AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndexGuess, 1, columnIndexGuess, 1, false, false);
            info.setCollectionItemInfo(itemInfo);
        }

        public void requestSimpleAnimationsInNextLayout() {
            mRequestedSimpleAnimations = true;
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_NONE;
        }

        public int getRowCountForAccessibility(Recycler recycler, State state) {
            if (mRecyclerView == null || mRecyclerView.mAdapter == null) {
                return 1;
            }
            return canScrollVertically() ? mRecyclerView.mAdapter.getItemCount() : 1;
        }

        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            if (mRecyclerView == null || mRecyclerView.mAdapter == null) {
                return 1;
            }
            return canScrollHorizontally() ? mRecyclerView.mAdapter.getItemCount() : 1;
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        boolean performAccessibilityAction(int action, Bundle args) {
            return performAccessibilityAction(mRecyclerView.mRecycler, mRecyclerView.mState, action, args);
        }

        public boolean performAccessibilityAction(Recycler recycler, State state, int action, Bundle args) {
            if (mRecyclerView == null) {
                return false;
            }
            int vScroll = 0, hScroll = 0;
            switch (action) {
                case AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD:
                    if (mRecyclerView.canScrollVertically(-1)) {
                        vScroll = -(getHeight() - getPaddingTop() - getPaddingBottom());
                    }
                    if (mRecyclerView.canScrollHorizontally(-1)) {
                        hScroll = -(getWidth() - getPaddingLeft() - getPaddingRight());
                    }
                    break;
                case AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD:
                    if (mRecyclerView.canScrollVertically(1)) {
                        vScroll = getHeight() - getPaddingTop() - getPaddingBottom();
                    }
                    if (mRecyclerView.canScrollHorizontally(1)) {
                        hScroll = getWidth() - getPaddingLeft() - getPaddingRight();
                    }
                    break;
            }
            if (vScroll == 0 && hScroll == 0) {
                return false;
            }
            mRecyclerView.scrollBy(hScroll, vScroll);
            return true;
        }

        boolean performAccessibilityActionForItem(View view, int action, Bundle args) {
            return performAccessibilityActionForItem(mRecyclerView.mRecycler, mRecyclerView.mState, view, action, args);
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int action, Bundle args) {
            return false;
        }

        public static Properties getProperties(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            Properties properties = new Properties();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslRecyclerView, defStyleAttr, defStyleRes);
            properties.orientation = a.getInt(R.styleable.SeslRecyclerView_android_orientation, VERTICAL);
            properties.spanCount = a.getInt(R.styleable.SeslRecyclerView_spanCount, 1);
            properties.reverseLayout = a.getBoolean(R.styleable.SeslRecyclerView_reverseLayout, false);
            properties.stackFromEnd = a.getBoolean(R.styleable.SeslRecyclerView_stackFromEnd, false);
            a.recycle();
            return properties;
        }

        void setExactMeasureSpecsFrom(SeslRecyclerView recyclerView) {
            setMeasureSpecs(MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), MeasureSpec.EXACTLY));
        }

        boolean shouldMeasureTwice() {
            return false;
        }

        boolean hasFlexibleChildInBothOrientations() {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final ViewGroup.LayoutParams lp = child.getLayoutParams();
                if (lp.width < 0 && lp.height < 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount = 0;

        static class ScrapData {
            long mBindRunningAverageNs = 0;
            long mCreateRunningAverageNs = 0;
            int mMaxScrap = 5;
            final ArrayList<ViewHolder> mScrapHeap = new ArrayList<>();

            ScrapData() {
            }
        }
        SparseArray<ScrapData> mScrap = new SparseArray<>();

        public void clear() {
            for (int i = 0; i < mScrap.size(); i++) {
                ScrapData data = mScrap.valueAt(i);
                data.mScrapHeap.clear();
            }
        }

        public void setMaxRecycledViews(int viewType, int max) {
            ScrapData scrapData = getScrapDataForType(viewType);
            scrapData.mMaxScrap = max;
            final ArrayList<ViewHolder> scrapHeap = scrapData.mScrapHeap;
            while (scrapHeap.size() > max) {
                scrapHeap.remove(scrapHeap.size() - 1);
            }
        }

        public int getRecycledViewCount(int viewType) {
            return getScrapDataForType(viewType).mScrapHeap.size();
        }

        public ViewHolder getRecycledView(int viewType) {
            final ScrapData scrapData = mScrap.get(viewType);
            if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
                final ArrayList<ViewHolder> scrapHeap = scrapData.mScrapHeap;
                return scrapHeap.remove(scrapHeap.size() - 1);
            }
            return null;
        }

        int size() {
            int count = 0;
            for (int i = 0; i < mScrap.size(); i++) {
                ArrayList<ViewHolder> viewHolders = mScrap.valueAt(i).mScrapHeap;
                if (viewHolders != null) {
                    count += viewHolders.size();
                }
            }
            return count;
        }

        public void putRecycledView(ViewHolder scrap) {
            final int viewType = scrap.getItemViewType();
            final ArrayList<ViewHolder> scrapHeap = getScrapDataForType(viewType).mScrapHeap;
            if (mScrap.get(viewType).mMaxScrap <= scrapHeap.size()) {
                return;
            }
            if (DEBUG && scrapHeap.contains(scrap)) {
                throw new IllegalArgumentException("this scrap item already exists");
            }
            scrap.resetInternal();
            scrapHeap.add(scrap);
        }

        long runningAverage(long oldAverage, long newValue) {
            if (oldAverage == 0) {
                return newValue;
            }
            return (oldAverage / 4 * 3) + (newValue / 4);
        }

        void factorInCreateTime(int viewType, long createTimeNs) {
            ScrapData scrapData = getScrapDataForType(viewType);
            scrapData.mCreateRunningAverageNs = runningAverage(scrapData.mCreateRunningAverageNs, createTimeNs);
        }

        void factorInBindTime(int viewType, long bindTimeNs) {
            ScrapData scrapData = getScrapDataForType(viewType);
            scrapData.mBindRunningAverageNs = runningAverage(scrapData.mBindRunningAverageNs, bindTimeNs);
        }

        boolean willCreateInTime(int viewType, long approxCurrentNs, long deadlineNs) {
            long expectedDurationNs = getScrapDataForType(viewType).mCreateRunningAverageNs;
            return expectedDurationNs == 0 || (approxCurrentNs + expectedDurationNs < deadlineNs);
        }

        boolean willBindInTime(int viewType, long approxCurrentNs, long deadlineNs) {
            long expectedDurationNs = getScrapDataForType(viewType).mBindRunningAverageNs;
            return expectedDurationNs == 0 || (approxCurrentNs + expectedDurationNs < deadlineNs);
        }

        void attach(Adapter adapter) {
            mAttachCount++;
        }

        void detach() {
            mAttachCount--;
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            if (oldAdapter != null) {
                detach();
            }
            if (!compatibleWithPrevious && mAttachCount == 0) {
                clear();
            }
            if (newAdapter != null) {
                attach(newAdapter);
            }
        }

        private ScrapData getScrapDataForType(int viewType) {
            ScrapData scrapData = mScrap.get(viewType);
            if (scrapData == null) {
                scrapData = new ScrapData();
                mScrap.put(viewType, scrapData);
            }
            return scrapData;
        }
    }

    public static abstract class SmoothScroller {
        private int mTargetPosition = SeslRecyclerView.NO_POSITION;
        private SeslRecyclerView mRecyclerView;
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private boolean mRunning;
        private View mTargetView;
        private final Action mRecyclingAction;

        public SmoothScroller() {
            mRecyclingAction = new Action(0, 0);
        }

        void start(SeslRecyclerView recyclerView, LayoutManager layoutManager) {
            mRecyclerView = recyclerView;
            mLayoutManager = layoutManager;
            if (mTargetPosition == SeslRecyclerView.NO_POSITION) {
                throw new IllegalArgumentException("Invalid target position");
            }
            mRecyclerView.mState.mTargetPosition = mTargetPosition;
            mRunning = true;
            mPendingInitialRun = true;
            mTargetView = findViewByPosition(getTargetPosition());
            onStart();
            mRecyclerView.mViewFlinger.postOnAnimation();
        }

        public void setTargetPosition(int targetPosition) {
            mTargetPosition = targetPosition;
        }

        public LayoutManager getLayoutManager() {
            return mLayoutManager;
        }

        protected final void stop() {
            if (!mRunning) {
                return;
            }
            mRunning = false;
            onStop();
            mRecyclerView.mState.mTargetPosition = SeslRecyclerView.NO_POSITION;
            mTargetView = null;
            mTargetPosition = SeslRecyclerView.NO_POSITION;
            mPendingInitialRun = false;
            mLayoutManager.onSmoothScrollerStopped(this);
            mLayoutManager = null;
            mRecyclerView = null;
        }

        public boolean isPendingInitialRun() {
            return mPendingInitialRun;
        }

        public boolean isRunning() {
            return mRunning;
        }

        public int getTargetPosition() {
            return mTargetPosition;
        }

        private void onAnimation(int dx, int dy) {
            final SeslRecyclerView recyclerView = mRecyclerView;
            if (!mRunning || mTargetPosition == SeslRecyclerView.NO_POSITION || recyclerView == null) {
                stop();
            }
            mPendingInitialRun = false;
            if (mTargetView != null) {
                if (getChildPosition(mTargetView) == mTargetPosition) {
                    onTargetFound(mTargetView, recyclerView.mState, mRecyclingAction);
                    mRecyclingAction.runIfNecessary(recyclerView);
                    stop();
                } else {
                    LogUtils.e(TAG, "Passed over target position while smooth scrolling.");
                    mTargetView = null;
                }
            }
            if (mRunning) {
                onSeekTargetStep(dx, dy, recyclerView.mState, mRecyclingAction);
                boolean hadJumpTarget = mRecyclingAction.hasJumpTarget();
                mRecyclingAction.runIfNecessary(recyclerView);
                if (hadJumpTarget) {
                    if (mRunning) {
                        mPendingInitialRun = true;
                        recyclerView.mViewFlinger.postOnAnimation();
                    } else {
                        stop();
                    }
                }
            }
        }

        public int getChildPosition(View view) {
            return mRecyclerView.getChildLayoutPosition(view);
        }

        public int getChildCount() {
            return mRecyclerView.mLayout.getChildCount();
        }

        public View findViewByPosition(int position) {
            return mRecyclerView.mLayout.findViewByPosition(position);
        }

        @Deprecated
        public void instantScrollToPosition(int position) {
            mRecyclerView.scrollToPosition(position);
        }

        protected void onChildAttachedToWindow(View child) {
            if (getChildPosition(child) == getTargetPosition()) {
                mTargetView = child;
                if (DEBUG) {
                    LogUtils.d(TAG, "smooth scroll target view has been attached");
                }
            }
        }

        protected void normalize(PointF scrollVector) {
            final float magnitude = (float) Math.sqrt(scrollVector.x * scrollVector.x + scrollVector.y * scrollVector.y);
            scrollVector.x /= magnitude;
            scrollVector.y /= magnitude;
        }

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onSeekTargetStep(int dx, int dy, State state, Action action);

        protected abstract void onTargetFound(View view, State state, Action action);

        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private int mDx;
            private int mDy;
            private int mDuration;
            private int mJumpToPosition = NO_POSITION;
            private Interpolator mInterpolator;
            private boolean mChanged = false;
            private int mConsecutiveUpdates = 0;

            public Action(int dx, int dy) {
                this(dx, dy, UNDEFINED_DURATION, null);
            }

            public Action(int dx, int dy, int duration) {
                this(dx, dy, duration, null);
            }

            public Action(int dx, int dy, int duration, Interpolator interpolator) {
                mDx = dx;
                mDy = dy;
                mDuration = duration;
                mInterpolator = interpolator;
            }

            public void jumpTo(int targetPosition) {
                mJumpToPosition = targetPosition;
            }

            boolean hasJumpTarget() {
                return mJumpToPosition >= 0;
            }

            void runIfNecessary(SeslRecyclerView recyclerView) {
                if (mJumpToPosition >= 0) {
                    final int position = mJumpToPosition;
                    mJumpToPosition = NO_POSITION;
                    recyclerView.jumpToPositionForSmoothScroller(position);
                    mChanged = false;
                    return;
                }
                if (mChanged) {
                    validate();
                    if (mInterpolator == null) {
                        if (mDuration == UNDEFINED_DURATION) {
                            recyclerView.mViewFlinger.smoothScrollBy(mDx, mDy);
                        } else {
                            recyclerView.mViewFlinger.smoothScrollBy(mDx, mDy, mDuration);
                        }
                    } else {
                        recyclerView.mViewFlinger.smoothScrollBy(
                                mDx, mDy, mDuration, mInterpolator);
                    }
                    mConsecutiveUpdates++;
                    if (mConsecutiveUpdates > 10) {
                        LogUtils.e(TAG, "Smooth Scroll action is being updated too frequently. Make sure"
                                + " you are not changing it unless necessary");
                    }
                    mChanged = false;
                } else {
                    mConsecutiveUpdates = 0;
                }
            }

            private void validate() {
                if (mInterpolator != null && mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must"
                            + " set a positive duration");
                } else if (mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            public int getDx() {
                return mDx;
            }

            public void setDx(int dx) {
                mChanged = true;
                mDx = dx;
            }

            public int getDy() {
                return mDy;
            }

            public void setDy(int dy) {
                mChanged = true;
                mDy = dy;
            }

            public int getDuration() {
                return mDuration;
            }

            public void setDuration(int duration) {
                mChanged = true;
                mDuration = duration;
            }

            public Interpolator getInterpolator() {
                return mInterpolator;
            }

            public void setInterpolator(Interpolator interpolator) {
                mChanged = true;
                mInterpolator = interpolator;
            }

            public void update(int dx, int dy, int duration, Interpolator interpolator) {
                mDx = dx;
                mDy = dy;
                mDuration = duration;
                mInterpolator = interpolator;
                mChanged = true;
            }
        }

        public interface ScrollVectorProvider {
            PointF computeScrollVectorForPosition(int i);
        }
    }

    public final class Recycler {
        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();
        ArrayList<ViewHolder> mChangedScrap = null;
        final ArrayList<ViewHolder> mCachedViews = new ArrayList<ViewHolder>();
        private final List<ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(mAttachedScrap);
        private int mRequestedCacheMax = DEFAULT_CACHE_SIZE;
        int mViewCacheMax = DEFAULT_CACHE_SIZE;
        RecycledViewPool mRecyclerPool;
        private ViewCacheExtension mViewCacheExtension;
        static final int DEFAULT_CACHE_SIZE = 2;

        public void clear() {
            mAttachedScrap.clear();
            recycleAndClearCachedViews();
        }

        public void setViewCacheSize(int viewCount) {
            mRequestedCacheMax = viewCount;
            updateViewCacheSize();
        }

        void updateViewCacheSize() {
            int extraCache = mLayout != null ? mLayout.mPrefetchMaxCountObserved : 0;
            mViewCacheMax = mRequestedCacheMax + extraCache;

            for (int i = mCachedViews.size() - 1; i >= 0 && mCachedViews.size() > mViewCacheMax; i--) {
                recycleCachedViewAt(i);
            }
        }

        public List<ViewHolder> getScrapList() {
            return mUnmodifiableAttachedScrap;
        }

        boolean validateViewHolderForOffsetPosition(ViewHolder holder) {
            if (holder.isRemoved()) {
                if (DEBUG && !mState.isPreLayout()) {
                    throw new IllegalStateException("should not receive a removed view unless it"
                            + " is pre layout" + exceptionLabel());
                }
                return mState.isPreLayout();
            }
            if (holder.mPosition < 0 || holder.mPosition >= mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder "
                        + "adapter position" + holder + exceptionLabel());
            }
            if (!mState.isPreLayout()) {
                final int type = mAdapter.getItemViewType(holder.mPosition);
                if (type != holder.getItemViewType()) {
                    return false;
                }
            }
            if (mAdapter.hasStableIds()) {
                return holder.getItemId() == mAdapter.getItemId(holder.mPosition);
            }
            return true;
        }

        private boolean tryBindViewHolderByDeadline(ViewHolder holder, int offsetPosition, int position, long deadlineNs) {
            holder.mOwnerRecyclerView = SeslRecyclerView.this;
            final int viewType = holder.getItemViewType();
            long startBindNs = getNanoTime();
            if (deadlineNs != FOREVER_NS && !mRecyclerPool.willBindInTime(viewType, startBindNs, deadlineNs)) {
                return false;
            }
            mAdapter.bindViewHolder(holder, offsetPosition);
            long endBindNs = getNanoTime();
            mRecyclerPool.factorInBindTime(holder.getItemViewType(), endBindNs - startBindNs);
            attachAccessibilityDelegateOnBind(holder);
            if (mState.isPreLayout()) {
                holder.mPreLayoutPosition = position;
            }
            return true;
        }

        public void bindViewToPosition(View view, int position) {
            ViewHolder holder = getChildViewHolderInt(view);
            if (holder == null) {
                throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot"
                        + " pass arbitrary views to this method, they should be created by the "
                        + "Adapter" + exceptionLabel());
            }
            final int offsetPosition = mAdapterHelper.findPositionOffset(position);
            if (offsetPosition < 0 || offsetPosition >= mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item "
                        + "position " + position + "(offset:" + offsetPosition + ")."
                        + "state:" + mState.getItemCount() + exceptionLabel());
            }
            tryBindViewHolderByDeadline(holder, offsetPosition, position, FOREVER_NS);

            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            final LayoutParams rvLayoutParams;
            if (lp == null) {
                rvLayoutParams = (LayoutParams) generateDefaultLayoutParams();
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else if (!checkLayoutParams(lp)) {
                rvLayoutParams = (LayoutParams) generateLayoutParams(lp);
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else {
                rvLayoutParams = (LayoutParams) lp;
            }

            rvLayoutParams.mInsetsDirty = true;
            rvLayoutParams.mViewHolder = holder;
            rvLayoutParams.mPendingInvalidate = holder.itemView.getParent() == null;
        }

        public int convertPreLayoutPositionToPostLayout(int position) {
            if (position < 0 || position >= mState.getItemCount()) {
                throw new IndexOutOfBoundsException("invalid position " + position + ". State "
                        + "item count is " + mState.getItemCount() + exceptionLabel());
            }
            if (!mState.isPreLayout()) {
                return position;
            }
            return mAdapterHelper.findPositionOffset(position);
        }

        public View getViewForPosition(int position) {
            return getViewForPosition(position, false);
        }

        View getViewForPosition(int position, boolean dryRun) {
            return tryGetViewHolderForPositionByDeadline(position, dryRun, FOREVER_NS).itemView;
        }

        ViewHolder tryGetViewHolderForPositionByDeadline(int position, boolean dryRun, long deadlineNs) {
            if (position < 0 || position >= mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + position
                        + "(" + position + "). Item count:" + mState.getItemCount()
                        + exceptionLabel());
            }
            boolean fromScrapOrHiddenOrCache = false;
            ViewHolder holder = null;
            if (mState.isPreLayout()) {
                holder = getChangedScrapViewForPosition(position);
                fromScrapOrHiddenOrCache = holder != null;
            }
            if (holder == null) {
                holder = getScrapOrHiddenOrCachedHolderForPosition(position, dryRun);
                if (holder != null) {
                    if (!validateViewHolderForOffsetPosition(holder)) {
                        if (!dryRun) {
                            holder.addFlags(ViewHolder.FLAG_INVALID);
                            if (holder.isScrap()) {
                                removeDetachedView(holder.itemView, false);
                                holder.unScrap();
                            } else if (holder.wasReturnedFromScrap()) {
                                holder.clearReturnedFromScrapFlag();
                            }
                            recycleViewHolderInternal(holder);
                        }
                        holder = null;
                    } else {
                        fromScrapOrHiddenOrCache = true;
                    }
                }
            }
            if (holder == null) {
                final int offsetPosition = mAdapterHelper.findPositionOffset(position);
                if (offsetPosition < 0 || offsetPosition >= mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item "
                            + "position " + position + "(offset:" + offsetPosition + ")."
                            + "state:" + mState.getItemCount() + exceptionLabel());
                }

                final int type = mAdapter.getItemViewType(offsetPosition);
                if (mAdapter.hasStableIds()) {
                    holder = getScrapOrCachedViewForId(mAdapter.getItemId(offsetPosition), type, dryRun);
                    if (holder != null) {
                        holder.mPosition = offsetPosition;
                        fromScrapOrHiddenOrCache = true;
                    }
                }
                if (holder == null && mViewCacheExtension != null) {
                    final View view = mViewCacheExtension.getViewForPositionAndType(this, position, type);
                    if (view != null) {
                        holder = getChildViewHolder(view);
                        if (holder == null) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned"
                                    + " a view which does not have a ViewHolder"
                                    + exceptionLabel());
                        } else if (holder.shouldIgnore()) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned"
                                    + " a view that is ignored. You must call stopIgnoring before"
                                    + " returning this view." + exceptionLabel());
                        }
                    }
                }
                if (holder == null) {
                    if (DEBUG) {
                        LogUtils.d(TAG, "tryGetViewHolderForPositionByDeadline("
                                + position + ") fetching from shared pool");
                    }
                    holder = getRecycledViewPool().getRecycledView(type);
                    if (holder != null) {
                        holder.resetInternal();
                        if (FORCE_INVALIDATE_DISPLAY_LIST) {
                            invalidateDisplayListInt(holder);
                        }
                    }
                }
                if (holder == null) {
                    long start = getNanoTime();
                    if (deadlineNs != FOREVER_NS && !mRecyclerPool.willCreateInTime(type, start, deadlineNs)) {
                        return null;
                    }
                    holder = mAdapter.createViewHolder(SeslRecyclerView.this, type);
                    if (ALLOW_THREAD_GAP_WORK) {
                        SeslRecyclerView innerView = findNestedRecyclerView(holder.itemView);
                        if (innerView != null) {
                            holder.mNestedRecyclerView = new WeakReference<>(innerView);
                        }
                    }

                    long end = getNanoTime();
                    mRecyclerPool.factorInCreateTime(type, end - start);
                    if (DEBUG) {
                        LogUtils.d(TAG, "tryGetViewHolderForPositionByDeadline created new ViewHolder");
                    }
                }
            }

            if (fromScrapOrHiddenOrCache && !mState.isPreLayout() && holder.hasAnyOfTheFlags(ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST)) {
                holder.setFlags(0, ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST);
                if (mState.mRunSimpleAnimations) {
                    int changeFlags = ItemAnimator.buildAdapterChangeFlagsForAnimations(holder);
                    changeFlags |= ItemAnimator.FLAG_APPEARED_IN_PRE_LAYOUT;
                    final ItemHolderInfo info = mItemAnimator.recordPreLayoutInformation(mState, holder, changeFlags, holder.getUnmodifiedPayloads());
                    recordAnimationInfoIfBouncedHiddenView(holder, info);
                }
            }

            boolean bound = false;
            if (mState.isPreLayout() && holder.isBound()) {
                holder.mPreLayoutPosition = position;
            } else if (!holder.isBound() || holder.needsUpdate() || holder.isInvalid()) {
                if (DEBUG && holder.isRemoved()) {
                    throw new IllegalStateException("Removed holder should be bound and it should"
                            + " come here only in pre-layout. Holder: " + holder
                            + exceptionLabel());
                }
                final int offsetPosition = mAdapterHelper.findPositionOffset(position);
                bound = tryBindViewHolderByDeadline(holder, offsetPosition, position, deadlineNs);
            }

            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            final LayoutParams rvLayoutParams;
            if (lp == null) {
                rvLayoutParams = (LayoutParams) generateDefaultLayoutParams();
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else if (!checkLayoutParams(lp)) {
                rvLayoutParams = (LayoutParams) generateLayoutParams(lp);
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else {
                rvLayoutParams = (LayoutParams) lp;
            }
            rvLayoutParams.mViewHolder = holder;
            rvLayoutParams.mPendingInvalidate = fromScrapOrHiddenOrCache && bound;
            return holder;
        }

        private void attachAccessibilityDelegateOnBind(ViewHolder holder) {
            if (isAccessibilityEnabled()) {
                final View itemView = holder.itemView;
                if (ViewCompat.getImportantForAccessibility(itemView) == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
                    ViewCompat.setImportantForAccessibility(itemView, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
                }
                if (mAccessibilityDelegate == null) {
                    setAccessibilityDelegateCompat(new SeslRecyclerViewAccessibilityDelegate(SeslRecyclerView.this));
                    LogUtils.d(TAG, "attachAccessibilityDelegate: mAccessibilityDelegate is null, so re create");
                }
                if (mAccessibilityDelegate != null && !ViewCompat.hasAccessibilityDelegate(itemView)) {
                    holder.addFlags(ViewHolder.FLAG_SET_A11Y_ITEM_DELEGATE);
                    ViewCompat.setAccessibilityDelegate(itemView, mAccessibilityDelegate.getItemDelegate());
                }
            }
        }

        private void invalidateDisplayListInt(ViewHolder holder) {
            if (holder.itemView instanceof ViewGroup) {
                invalidateDisplayListInt((ViewGroup) holder.itemView, false);
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean invalidateThis) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                final View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    invalidateDisplayListInt((ViewGroup) view, true);
                }
            }
            if (!invalidateThis) {
                return;
            }
            if (viewGroup.getVisibility() == View.INVISIBLE) {
                viewGroup.setVisibility(View.VISIBLE);
                viewGroup.setVisibility(View.INVISIBLE);
            } else {
                final int visibility = viewGroup.getVisibility();
                viewGroup.setVisibility(View.INVISIBLE);
                viewGroup.setVisibility(visibility);
            }
        }

        public void recycleView(View view) {
            ViewHolder holder = getChildViewHolderInt(view);
            if (holder.isTmpDetached()) {
                removeDetachedView(view, false);
            }
            if (holder.isScrap()) {
                holder.unScrap();
            } else if (holder.wasReturnedFromScrap()) {
                holder.clearReturnedFromScrapFlag();
            }
            recycleViewHolderInternal(holder);
        }

        void recycleViewInternal(View view) {
            recycleViewHolderInternal(getChildViewHolderInt(view));
        }

        void recycleAndClearCachedViews() {
            final int count = mCachedViews.size();
            for (int i = count - 1; i >= 0; i--) {
                recycleCachedViewAt(i);
            }
            mCachedViews.clear();
            if (ALLOW_THREAD_GAP_WORK) {
                mPrefetchRegistry.clearPrefetchPositions();
            }
        }

        void recycleCachedViewAt(int cachedViewIndex) {
            if (DEBUG) {
                LogUtils.d(TAG, "Recycling cached view at index " + cachedViewIndex);
            }
            ViewHolder viewHolder = mCachedViews.get(cachedViewIndex);
            if (DEBUG) {
                LogUtils.d(TAG, "CachedViewHolder to be recycled: " + viewHolder);
            }
            addViewHolderToRecycledViewPool(viewHolder, true);
            mCachedViews.remove(cachedViewIndex);
        }

        void recycleViewHolderInternal(ViewHolder holder) {
            if (holder.isScrap() || holder.itemView.getParent() != null) {
                throw new IllegalArgumentException(
                        "Scrapped or attached views may not be recycled. isScrap:"
                                + holder.isScrap() + " isAttached:"
                                + (holder.itemView.getParent() != null) + exceptionLabel());
            }

            if (holder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed "
                        + "from RecyclerView before it can be recycled: " + holder
                        + exceptionLabel());
            }

            if (holder.shouldIgnore()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You"
                        + " should first call stopIgnoringView(view) before calling recycle."
                        + exceptionLabel());
            }
            final boolean transientStatePreventsRecycling = holder.doesTransientStatePreventRecycling();
            final boolean forceRecycle = mAdapter != null && transientStatePreventsRecycling && mAdapter.onFailedToRecycleView(holder);
            boolean cached = false;
            boolean recycled = false;
            if (DEBUG && mCachedViews.contains(holder)) {
                throw new IllegalArgumentException("cached view received recycle internal? "
                        + holder + exceptionLabel());
            }
            if (forceRecycle || holder.isRecyclable()) {
                if (mViewCacheMax > 0 && !holder.hasAnyOfTheFlags(ViewHolder.FLAG_INVALID | ViewHolder.FLAG_REMOVED | ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN)) {
                    int cachedViewSize = mCachedViews.size();
                    if (cachedViewSize >= mViewCacheMax && cachedViewSize > 0) {
                        recycleCachedViewAt(0);
                        cachedViewSize--;
                    }

                    int targetCacheIndex = cachedViewSize;
                    if (ALLOW_THREAD_GAP_WORK && cachedViewSize > 0 && !mPrefetchRegistry.lastPrefetchIncludedPosition(holder.mPosition)) {
                        int cacheIndex = cachedViewSize - 1;
                        while (cacheIndex >= 0) {
                            int cachedPos = mCachedViews.get(cacheIndex).mPosition;
                            if (!mPrefetchRegistry.lastPrefetchIncludedPosition(cachedPos)) {
                                break;
                            }
                            cacheIndex--;
                        }
                        targetCacheIndex = cacheIndex + 1;
                    }
                    mCachedViews.add(targetCacheIndex, holder);
                    cached = true;
                }
                if (!cached) {
                    addViewHolderToRecycledViewPool(holder, true);
                    recycled = true;
                }
            } else {
                if (DEBUG) {
                    LogUtils.d(TAG, "trying to recycle a non-recycleable holder. Hopefully, it will "
                            + "re-visit here. We are still removing it from animation lists"
                            + exceptionLabel());
                }
            }
            mViewInfoStore.removeViewHolder(holder);
            if (!cached && !recycled && transientStatePreventsRecycling) {
                holder.mOwnerRecyclerView = null;
            }
        }

        void addViewHolderToRecycledViewPool(ViewHolder holder, boolean dispatchRecycled) {
            clearNestedRecyclerViewIfNotNested(holder);
            if (holder.hasAnyOfTheFlags(ViewHolder.FLAG_SET_A11Y_ITEM_DELEGATE)) {
                holder.setFlags(0, ViewHolder.FLAG_SET_A11Y_ITEM_DELEGATE);
                ViewCompat.setAccessibilityDelegate(holder.itemView, null);
            }
            if (dispatchRecycled) {
                dispatchViewRecycled(holder);
            }
            holder.mOwnerRecyclerView = null;
            getRecycledViewPool().putRecycledView(holder);
        }

        void quickRecycleScrapView(View view) {
            final ViewHolder holder = getChildViewHolderInt(view);
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
            recycleViewHolderInternal(holder);
        }

        void scrapView(View view) {
            final ViewHolder holder = getChildViewHolderInt(view);
            if (holder.hasAnyOfTheFlags(ViewHolder.FLAG_REMOVED | ViewHolder.FLAG_INVALID) || !holder.isUpdated() || canReuseUpdatedViewHolder(holder)) {
                if (holder.isInvalid() && !holder.isRemoved() && !mAdapter.hasStableIds()) {
                    throw new IllegalArgumentException("Called scrap view with an invalid view."
                            + " Invalid views cannot be reused from scrap, they should rebound from"
                            + " recycler pool." + exceptionLabel());
                }
                holder.setScrapContainer(this, false);
                mAttachedScrap.add(holder);
            } else {
                if (mChangedScrap == null) {
                    mChangedScrap = new ArrayList<ViewHolder>();
                }
                holder.setScrapContainer(this, true);
                mChangedScrap.add(holder);
            }
        }

        void unscrapView(ViewHolder holder) {
            if (holder.mInChangeScrap) {
                mChangedScrap.remove(holder);
            } else {
                mAttachedScrap.remove(holder);
            }
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
        }

        int getScrapCount() {
            return mAttachedScrap.size();
        }

        View getScrapViewAt(int index) {
            return mAttachedScrap.get(index).itemView;
        }

        void clearScrap() {
            mAttachedScrap.clear();
            if (mChangedScrap != null) {
                mChangedScrap.clear();
            }
        }

        ViewHolder getChangedScrapViewForPosition(int position) {
            final int changedScrapSize;
            if (mChangedScrap == null || (changedScrapSize = mChangedScrap.size()) == 0) {
                return null;
            }
            for (int i = 0; i < changedScrapSize; i++) {
                final ViewHolder holder = mChangedScrap.get(i);
                if (!holder.wasReturnedFromScrap() && holder.getLayoutPosition() == position) {
                    holder.addFlags(ViewHolder.FLAG_RETURNED_FROM_SCRAP);
                    return holder;
                }
            }
            if (mAdapter.hasStableIds()) {
                final int offsetPosition = mAdapterHelper.findPositionOffset(position);
                if (offsetPosition > 0 && offsetPosition < mAdapter.getItemCount()) {
                    final long id = mAdapter.getItemId(offsetPosition);
                    for (int i = 0; i < changedScrapSize; i++) {
                        final ViewHolder holder = mChangedScrap.get(i);
                        if (!holder.wasReturnedFromScrap() && holder.getItemId() == id) {
                            holder.addFlags(ViewHolder.FLAG_RETURNED_FROM_SCRAP);
                            return holder;
                        }
                    }
                }
            }
            return null;
        }

        ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int position, boolean dryRun) {
            final int scrapCount = mAttachedScrap.size();

            for (int i = 0; i < scrapCount; i++) {
                final ViewHolder holder = mAttachedScrap.get(i);
                if (!holder.wasReturnedFromScrap() && holder.getLayoutPosition() == position && !holder.isInvalid() && (mState.mInPreLayout || !holder.isRemoved())) {
                    holder.addFlags(ViewHolder.FLAG_RETURNED_FROM_SCRAP);
                    return holder;
                }
            }

            if (!dryRun) {
                View view = mChildHelper.findHiddenNonRemovedView(position);
                if (view != null) {
                    final ViewHolder vh = getChildViewHolderInt(view);
                    mChildHelper.unhide(view);
                    int layoutIndex = mChildHelper.indexOfChild(view);
                    if (layoutIndex == SeslRecyclerView.NO_POSITION) {
                        throw new IllegalStateException("layout index should not be -1 after "
                                + "unhiding a view:" + vh + exceptionLabel());
                    }
                    mChildHelper.detachViewFromParent(layoutIndex);
                    scrapView(view);
                    vh.addFlags(ViewHolder.FLAG_RETURNED_FROM_SCRAP | ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST);
                    return vh;
                }
            }

            final int cacheSize = mCachedViews.size();
            for (int i = 0; i < cacheSize; i++) {
                final ViewHolder holder = mCachedViews.get(i);
                if (!holder.isInvalid() && holder.getLayoutPosition() == position) {
                    if (!dryRun) {
                        mCachedViews.remove(i);
                    }
                    if (DEBUG) {
                        LogUtils.d(TAG, "getScrapOrHiddenOrCachedHolderForPosition(" + position
                                + ") found match in cache: " + holder);
                    }
                    return holder;
                }
            }
            return null;
        }

        ViewHolder getScrapOrCachedViewForId(long id, int type, boolean dryRun) {
            final int count = mAttachedScrap.size();
            for (int i = count - 1; i >= 0; i--) {
                final ViewHolder holder = mAttachedScrap.get(i);
                if (holder.getItemId() == id && !holder.wasReturnedFromScrap()) {
                    if (type == holder.getItemViewType()) {
                        holder.addFlags(ViewHolder.FLAG_RETURNED_FROM_SCRAP);
                        if (holder.isRemoved()) {
                            if (!mState.isPreLayout()) {
                                holder.setFlags(ViewHolder.FLAG_UPDATE, ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_INVALID | ViewHolder.FLAG_REMOVED);
                            }
                        }
                        return holder;
                    } else if (!dryRun) {
                        mAttachedScrap.remove(i);
                        removeDetachedView(holder.itemView, false);
                        quickRecycleScrapView(holder.itemView);
                    }
                }
            }

            final int cacheSize = mCachedViews.size();
            for (int i = cacheSize - 1; i >= 0; i--) {
                final ViewHolder holder = mCachedViews.get(i);
                if (holder.getItemId() == id) {
                    if (type == holder.getItemViewType()) {
                        if (!dryRun) {
                            mCachedViews.remove(i);
                        }
                        return holder;
                    } else if (!dryRun) {
                        recycleCachedViewAt(i);
                        return null;
                    }
                }
            }
            return null;
        }

        void dispatchViewRecycled(ViewHolder holder) {
            if (mRecyclerListener != null) {
                mRecyclerListener.onViewRecycled(holder);
            }
            if (mAdapter != null) {
                mAdapter.onViewRecycled(holder);
            }
            if (mState != null) {
                mViewInfoStore.removeViewHolder(holder);
            }
            if (DEBUG) LogUtils.d(TAG, "dispatchViewRecycled: " + holder);
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            clear();
            getRecycledViewPool().onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious);
        }

        void offsetPositionRecordsForMove(int from, int to) {
            final int start, end, inBetweenOffset;
            if (from < to) {
                start = from;
                end = to;
                inBetweenOffset = -1;
            } else {
                start = to;
                end = from;
                inBetweenOffset = 1;
            }
            final int cachedCount = mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                final ViewHolder holder = mCachedViews.get(i);
                if (holder == null || holder.mPosition < start || holder.mPosition > end) {
                    continue;
                }
                if (holder.mPosition == from) {
                    holder.offsetPosition(to - from, false);
                } else {
                    holder.offsetPosition(inBetweenOffset, false);
                }
                if (DEBUG) {
                    LogUtils.d(TAG, "offsetPositionRecordsForMove cached child " + i + " holder "
                            + holder);
                }
            }
        }

        void offsetPositionRecordsForInsert(int insertedAt, int count) {
            final int cachedCount = mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                final ViewHolder holder = mCachedViews.get(i);
                if (holder != null && holder.mPosition >= insertedAt) {
                    if (DEBUG) {
                        LogUtils.d(TAG, "offsetPositionRecordsForInsert cached " + i + " holder "
                                + holder + " now at position " + (holder.mPosition + count));
                    }
                    holder.offsetPosition(count, true);
                }
            }
        }

        void offsetPositionRecordsForRemove(int removedFrom, int count, boolean applyToPreLayout) {
            final int removedEnd = removedFrom + count;
            final int cachedCount = mCachedViews.size();
            for (int i = cachedCount - 1; i >= 0; i--) {
                final ViewHolder holder = mCachedViews.get(i);
                if (holder != null) {
                    if (holder.mPosition >= removedEnd) {
                        if (DEBUG) {
                            LogUtils.d(TAG, "offsetPositionRecordsForRemove cached " + i
                                    + " holder " + holder + " now at position "
                                    + (holder.mPosition - count));
                        }
                        holder.offsetPosition(-count, applyToPreLayout);
                    } else if (holder.mPosition >= removedFrom) {
                        holder.addFlags(ViewHolder.FLAG_REMOVED);
                        recycleCachedViewAt(i);
                    }
                }
            }
        }

        void setViewCacheExtension(ViewCacheExtension extension) {
            mViewCacheExtension = extension;
        }

        void setRecycledViewPool(RecycledViewPool pool) {
            if (mRecyclerPool != null) {
                mRecyclerPool.detach();
            }
            mRecyclerPool = pool;
            if (pool != null) {
                mRecyclerPool.attach(getAdapter());
            }
        }

        RecycledViewPool getRecycledViewPool() {
            if (mRecyclerPool == null) {
                mRecyclerPool = new RecycledViewPool();
            }
            return mRecyclerPool;
        }

        void viewRangeUpdate(int positionStart, int itemCount) {
            final int positionEnd = positionStart + itemCount;
            final int cachedCount = mCachedViews.size();
            for (int i = cachedCount - 1; i >= 0; i--) {
                final ViewHolder holder = mCachedViews.get(i);
                if (holder == null) {
                    continue;
                }

                final int pos = holder.mPosition;
                if (pos >= positionStart && pos < positionEnd) {
                    holder.addFlags(ViewHolder.FLAG_UPDATE);
                    recycleCachedViewAt(i);
                }
            }
        }

        void markKnownViewsInvalid() {
            if (mAdapter != null && mAdapter.hasStableIds()) {
                final int cachedCount = mCachedViews.size();
                for (int i = 0; i < cachedCount; i++) {
                    final ViewHolder holder = mCachedViews.get(i);
                    if (holder != null) {
                        holder.addFlags(ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_INVALID);
                        holder.addChangePayload(null);
                    }
                }
            } else {
                recycleAndClearCachedViews();
            }
        }

        void clearOldPositions() {
            final int cachedCount = mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                final ViewHolder holder = mCachedViews.get(i);
                holder.clearOldPosition();
            }
            final int scrapCount = mAttachedScrap.size();
            for (int i = 0; i < scrapCount; i++) {
                mAttachedScrap.get(i).clearOldPosition();
            }
            if (mChangedScrap != null) {
                final int changedScrapCount = mChangedScrap.size();
                for (int i = 0; i < changedScrapCount; i++) {
                    mChangedScrap.get(i).clearOldPosition();
                }
            }
        }

        void markItemDecorInsetsDirty() {
            final int cachedCount = mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                final ViewHolder holder = mCachedViews.get(i);
                LayoutParams layoutParams = (LayoutParams) holder.itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
    }

    class ViewFlinger implements Runnable {
        private int mLastFlingX;
        private int mLastFlingY;
        private SeslOverScroller mScroller;
        Interpolator mInterpolator = sQuinticInterpolator;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        ViewFlinger() {
            mScroller = new SeslOverScroller(getContext(), sQuinticInterpolator);
        }

        @Override
        public void run() {
            if (mLayout == null) {
                stop();
                return;
            }
            disableRunOnAnimationRequests();
            consumePendingUpdateOperations();
            final SeslOverScroller scroller = mScroller;
            final SmoothScroller smoothScroller = mLayout.mSmoothScroller;
            if (scroller.computeScrollOffset()) {
                final int[] scrollConsumed = mScrollConsumed;
                final int x = scroller.getCurrX();
                final int y = scroller.getCurrY();
                int dx = x - mLastFlingX;
                int dy = y - mLastFlingY;
                int hresult = 0;
                int vresult = 0;
                mLastFlingX = x;
                mLastFlingY = y;
                int overscrollX = 0, overscrollY = 0;

                if (dispatchNestedPreScroll(dx, dy, scrollConsumed, null, TYPE_NON_TOUCH)) {
                    dx -= scrollConsumed[0];
                    dy -= scrollConsumed[1];
                    adjustNestedScrollRangeBy(scrollConsumed[1]);
                } else {
                    adjustNestedScrollRangeBy(dy);
                }

                if (mAdapter != null) {
                    startInterceptRequestLayout();
                    onEnterLayoutOrScroll();
                    TraceCompat.beginSection(TRACE_SCROLL_TAG);
                    fillRemainingScrollValues(mState);
                    if (dx != 0) {
                        hresult = mLayout.scrollHorizontallyBy(dx, mRecycler, mState);
                        overscrollX = dx - hresult;
                    }
                    if (dy != 0) {
                        vresult = mLayout.scrollVerticallyBy(dy, mRecycler, mState);
                        overscrollY = dy - vresult;
                        if (mGoToTopState == 0) {
                            setupGoToTop(1);
                            autoHide(1);
                        }
                    }
                    TraceCompat.endSection();
                    repositionShadowingViews();

                    onExitLayoutOrScroll();
                    stopInterceptRequestLayout(false);

                    if (smoothScroller != null && !smoothScroller.isPendingInitialRun() && smoothScroller.isRunning()) {
                        final int adapterSize = mState.getItemCount();
                        if (adapterSize == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.getTargetPosition() >= adapterSize) {
                            smoothScroller.setTargetPosition(adapterSize - 1);
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        } else {
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        }
                    }
                }
                if (mItemDecorations.isEmpty()) {
                    invalidate();
                }
                if (getOverScrollMode() != View.OVER_SCROLL_NEVER) {
                    considerReleasingGlowsOnScroll(dx, dy);
                }

                boolean nested = dispatchNestedScroll(hresult, vresult, overscrollX, overscrollY, null, TYPE_NON_TOUCH);
                if (nested) {
                    mScrollOffset[0] = 0;
                    mScrollOffset[1] = 0;
                }
                if (overscrollX != 0 || overscrollY != 0) {
                    final int vel = (int) scroller.getCurrVelocity();

                    int velX = 0;
                    if (overscrollX != x) {
                        velX = overscrollX < 0 ? -vel : overscrollX > 0 ? vel : 0;
                    }

                    int velY = 0;
                    if (overscrollY != y) {
                        velY = overscrollY < 0 ? -vel : overscrollY > 0 ? vel : 0;
                    }

                    if ((!nested || overscrollY >= 0) && getOverScrollMode() != View.OVER_SCROLL_NEVER) {
                        absorbGlows(velX, velY);
                    }
                    if ((!nested || overscrollY >= 0) && ((velX != 0 || overscrollX == x || scroller.getFinalX() == 0) && (velY != 0 || overscrollY == y || scroller.getFinalY() == 0))) {
                        scroller.abortAnimation();
                    }
                }
                if (hresult != 0 || vresult != 0) {
                    dispatchOnScrolled(hresult, vresult);
                }

                if (!awakenScrollBars()) {
                    invalidate();
                }

                final boolean fullyConsumedVertical = dy != 0 && mLayout.canScrollVertically() && vresult == dy;
                final boolean fullyConsumedHorizontal = dx != 0 && mLayout.canScrollHorizontally() && hresult == dx;
                final boolean fullyConsumedAny = (dx == 0 && dy == 0) || fullyConsumedHorizontal || fullyConsumedVertical;

                if (scroller.isFinished() || (!fullyConsumedAny && !hasNestedScrollingParent(TYPE_NON_TOUCH))) {
                    if (getOverScrollMode() != View.OVER_SCROLL_NEVER && mNestedScroll) {
                        pullGlows((float) dx, (float) overscrollX, (float) dy, (float) overscrollY);
                        considerReleasingGlowsOnScroll(x, y);
                    }
                    setScrollState(SCROLL_STATE_IDLE);
                    if (ALLOW_THREAD_GAP_WORK) {
                        mPrefetchRegistry.clearPrefetchPositions();
                    }
                    stopNestedScroll(TYPE_NON_TOUCH);
                } else {
                    postOnAnimation();
                    if (mGapWorker != null) {
                        mGapWorker.postFromTraversal(SeslRecyclerView.this, dx, dy);
                    }
                }
            }
            if (smoothScroller != null) {
                if (smoothScroller.isPendingInitialRun()) {
                    smoothScroller.onAnimation(0, 0);
                }
                if (!mReSchedulePostAnimationCallback) {
                    smoothScroller.stop();
                }
            }
            enableRunOnAnimationRequests();
        }

        private void disableRunOnAnimationRequests() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                removeCallbacks(this);
                ViewCompat.postOnAnimation(SeslRecyclerView.this, this);
            }
        }

        public void fling(int velocityX, int velocityY) {
            setScrollState(2);
            mLastFlingX = mLastFlingY = 0;
            mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void smoothScrollBy(int dx, int dy) {
            smoothScrollBy(dx, dy, 0, 0);
        }

        public void smoothScrollBy(int dx, int dy, int vx, int vy) {
            smoothScrollBy(dx, dy, computeScrollDuration(dx, dy, vx, vy));
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
        }

        private int computeScrollDuration(int dx, int dy, int vx, int vy) {
            final int absDx = Math.abs(dx);
            final int absDy = Math.abs(dy);
            final boolean horizontal = absDx > absDy;
            final int velocity = (int) Math.sqrt(vx * vx + vy * vy);
            final int delta = (int) Math.sqrt(dx * dx + dy * dy);
            final int containerSize = horizontal ? getWidth() : getHeight();
            final int halfContainerSize = containerSize / 2;
            final float distanceRatio = Math.min(1.f, 1.f * delta / containerSize);
            final float distance = halfContainerSize + halfContainerSize * distanceInfluenceForSnapDuration(distanceRatio);

            final int duration;
            if (velocity > 0) {
                duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
            } else {
                float absDelta = (float) (horizontal ? absDx : absDy);
                duration = (int) (((absDelta / containerSize) + 1) * 300);
            }
            return Math.min(duration, MAX_SCROLL_DURATION);
        }

        public void smoothScrollBy(int dx, int dy, int duration) {
            smoothScrollBy(dx, dy, duration, sQuinticInterpolator);
        }

        public void smoothScrollBy(int dx, int dy, Interpolator interpolator) {
            smoothScrollBy(dx, dy, computeScrollDuration(dx, dy, 0, 0), interpolator == null ? sQuinticInterpolator : interpolator);
        }

        @SuppressLint("WrongConstant")
        public void smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator) {
            int axis = dx != 0 ? 2 : 1;
            startNestedScroll(axis, TYPE_NON_TOUCH);
            if (!dispatchNestedPreScroll(dx, dy, null, null, TYPE_NON_TOUCH)) {
                if (mInterpolator != interpolator) {
                    mInterpolator = interpolator;
                    mScroller = new SeslOverScroller(getContext(), interpolator);
                }
                setScrollState(2);
                mLastFlingX = mLastFlingY = 0;
                mScroller.startScroll(0, 0, dx, dy, duration);
                if (Build.VERSION.SDK_INT < 23) {
                    mScroller.computeScrollOffset();
                }
                postOnAnimation();
            }
            adjustNestedScrollRangeBy(dy);
        }

        public void stop() {
            removeCallbacks(this);
            mScroller.abortAnimation();
        }
    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    private class RecyclerViewDataObserver extends AdapterDataObserver {
        RecyclerViewDataObserver() { }

        @Override
        public void onChanged() {
            assertNotInLayoutOrScroll(null);
            mState.mStructureChanged = true;
            processDataSetCompletelyChanged(true);
            if (!mAdapterHelper.hasPendingUpdates()) {
                requestLayout();
            }
            if (mFastScroller != null) {
                mFastScroller.onSectionsChanged();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeChanged(positionStart, itemCount, payload)) {
                triggerUpdateProcessor();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeInserted(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeRemoved(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            assertNotInLayoutOrScroll(null);
            if (mAdapterHelper.onItemRangeMoved(fromPosition, toPosition, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        void triggerUpdateProcessor() {
            if (POST_UPDATES_ON_ANIMATION && mHasFixedSize && mIsAttached) {
                ViewCompat.postOnAnimation(SeslRecyclerView.this, mUpdateChildViewsRunnable);
            } else {
                mAdapterUpdateDuringMeasure = true;
                requestLayout();
            }
        }
    }

    public static class SavedState extends AbsSavedState {
        Parcelable mLayoutState;

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            mLayoutState = in.readParcelable(loader != null ? loader : LayoutManager.class.getClassLoader());
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(mLayoutState, 0);
        }

        void copyFrom(SavedState other) {
            mLayoutState = other.mLayoutState;
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface SeslFastScrollerEventListener {
        void onPressed(float f);

        void onReleased(float f);
    }

    public interface SeslLongPressMultiSelectionListener {
        void onItemSelected(SeslRecyclerView recyclerView, View view, int i, long j);

        void onLongPressMultiSelectionEnded(int i, int i2);

        void onLongPressMultiSelectionStarted(int i, int i2);
    }

    public interface SeslOnGoToTopClickListener {
        boolean onGoToTopClick(SeslRecyclerView recyclerView);
    }

    public interface SeslOnMultiSelectedListener {
        void onMultiSelectStart(int i, int i2);

        void onMultiSelectStop(int i, int i2);

        void onMultiSelected(SeslRecyclerView recyclerView, View view, int i, long j);
    }

    public static class SimpleOnItemTouchListener implements SeslRecyclerView.OnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(SeslRecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(SeslRecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    }

    public static abstract class ItemAnimator {
        static final int ANIMATION_TYPE_DEFAULT = 1;
        static final int ANIMATION_TYPE_EXPAND_COLLAPSE = 2;
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration = 120;
        private int mAnimationType = 1;
        private long mChangeDuration = 250;
        private long mExpandCollapseDuration = 700;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<>();
        private ViewHolder mGroupViewHolder = null;
        private View mHostView = null;
        private ItemAnimatorListener mListener = null;
        private long mMoveDuration = 250;
        private long mRemoveDuration = 120;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges { }

        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        interface ItemAnimatorListener {
            void onAnimationFinished(ViewHolder item);
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo setFrom(ViewHolder holder) {
                return setFrom(holder, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder holder, int flags) {
                View view = holder.itemView;
                left = view.getLeft();
                top = view.getTop();
                right = view.getRight();
                bottom = view.getBottom();
                return this;
            }
        }

        public abstract boolean animateAppearance(ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo);

        public abstract boolean animateChange(ViewHolder oldHolder, ViewHolder newHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo);

        public abstract boolean animateDisappearance(ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo);

        public abstract boolean animatePersistence(ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo);

        public abstract void endAnimation(ViewHolder viewHolder);

        public abstract void endAnimations();

        public abstract boolean isRunning();

        public abstract void runPendingAnimations();

        public long getExpandCollapseDuration() {
            return mExpandCollapseDuration;
        }

        public int getItemAnimationTypeInternal() {
            return mAnimationType;
        }

        public void setItemAnimationTypeInternal(int animationType) {
            mAnimationType = animationType;
        }

        public void setHostView(View hostView) {
            mHostView = hostView;
        }

        public View getHostView() {
            return mHostView;
        }

        public void setGroupViewHolderInternal(ViewHolder vH) {
            mGroupViewHolder = vH;
        }

        public void clearGroupViewHolderInternal() {
            mGroupViewHolder = null;
        }

        public ViewHolder getGroupViewHolderInternal() {
            return mGroupViewHolder;
        }

        public long getMoveDuration() {
            return mMoveDuration;
        }

        public void setMoveDuration(long moveDuration) {
            mMoveDuration = moveDuration;
        }

        public long getAddDuration() {
            return mAddDuration;
        }

        public void setAddDuration(long addDuration) {
            mAddDuration = addDuration;
        }

        public long getRemoveDuration() {
            return mRemoveDuration;
        }

        public void setRemoveDuration(long removeDuration) {
            mRemoveDuration = removeDuration;
        }

        public long getChangeDuration() {
            return mChangeDuration;
        }

        public void setChangeDuration(long changeDuration) {
            mChangeDuration = changeDuration;
        }

        void setListener(ItemAnimatorListener listener) {
            mListener = listener;
        }

        public ItemHolderInfo recordPreLayoutInformation(State state, ViewHolder viewHolder, @AdapterChanges int changeFlags, List<Object> list) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        public ItemHolderInfo recordPostLayoutInformation(State state, ViewHolder viewHolder) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        @AdapterChanges static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int flags = viewHolder.mFlags & (FLAG_INVALIDATED | FLAG_REMOVED | FLAG_CHANGED);
            if (viewHolder.isInvalid()) {
                return FLAG_INVALIDATED;
            }
            if ((flags & FLAG_INVALIDATED) == 0) {
                final int oldPos = viewHolder.getOldPosition();
                final int pos = viewHolder.getAdapterPosition();
                if (oldPos != NO_POSITION && pos != NO_POSITION && oldPos != pos) {
                    flags |= FLAG_MOVED;
                }
            }
            return flags;
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            onAnimationFinished(viewHolder);
            if (mListener != null) {
                mListener.onAnimationFinished(viewHolder);
            }
        }

        public void onAnimationFinished(ViewHolder viewHolder) { }

        public final void dispatchAnimationStarted(ViewHolder viewHolder) {
            onAnimationStarted(viewHolder);
        }

        public void onAnimationStarted(ViewHolder viewHolder) { }

        public final boolean isRunning(ItemAnimatorFinishedListener listener) {
            boolean running = isRunning();
            if (listener != null) {
                if (!running) {
                    listener.onAnimationsFinished();
                } else {
                    mFinishedListeners.add(listener);
                }
            }
            return running;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
            return true;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder, List<Object> list) {
            return canReuseUpdatedViewHolder(viewHolder);
        }

        public final void dispatchAnimationsFinished() {
            final int count = mFinishedListeners.size();
            for (int i = 0; i < count; i++) {
                mFinishedListeners.get(i).onAnimationsFinished();
            }
            mFinishedListeners.clear();
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }
    }

    public static abstract class ViewHolder {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_SET_A11Y_ITEM_DELEGATE = 16384;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        public final View itemView;
        private int mFlags;
        private boolean mInChangeScrap = false;
        private int mIsRecyclableCount = 0;
        long mItemId = NO_ID;
        int mItemViewType = INVALID_TYPE;
        WeakReference<SeslRecyclerView> mNestedRecyclerView;
        int mOldPosition = NO_POSITION;
        SeslRecyclerView mOwnerRecyclerView;
        List<Object> mPayloads = null;
        int mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;
        int mPosition = NO_POSITION;
        int mPreLayoutPosition = NO_POSITION;
        private Recycler mScrapContainer = null;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        List<Object> mUnmodifiedPayloads = null;
        private int mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
            addFlags(ViewHolder.FLAG_REMOVED);
            offsetPosition(offset, applyToPreLayout);
            mPosition = mNewPosition;
        }

        void offsetPosition(int offset, boolean applyToPreLayout) {
            if (mOldPosition == NO_POSITION) {
                mOldPosition = mPosition;
            }
            if (mPreLayoutPosition == NO_POSITION) {
                mPreLayoutPosition = mPosition;
            }
            if (applyToPreLayout) {
                mPreLayoutPosition += offset;
            }
            mPosition += offset;
            if (itemView.getLayoutParams() != null) {
                ((LayoutParams) itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        void clearOldPosition() {
            mOldPosition = NO_POSITION;
            mPreLayoutPosition = NO_POSITION;
        }

        void saveOldPosition() {
            if (mOldPosition == NO_POSITION) {
                mOldPosition = mPosition;
            }
        }

        boolean shouldIgnore() {
            return (mFlags & FLAG_IGNORE) != 0;
        }

        @Deprecated
        public final int getPosition() {
            return mPreLayoutPosition == NO_POSITION ? mPosition : mPreLayoutPosition;
        }

        public final int getLayoutPosition() {
            return mPreLayoutPosition == NO_POSITION ? mPosition : mPreLayoutPosition;
        }

        public final int getAdapterPosition() {
            if (mOwnerRecyclerView == null) {
                return NO_POSITION;
            }
            return mOwnerRecyclerView.getAdapterPositionFor(this);
        }

        public final int getOldPosition() {
            return mOldPosition;
        }

        public final long getItemId() {
            return mItemId;
        }

        public final int getItemViewType() {
            return mItemViewType;
        }

        boolean isScrap() {
            return mScrapContainer != null;
        }

        void unScrap() {
            mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            return (mFlags & FLAG_RETURNED_FROM_SCRAP) != 0;
        }

        void clearReturnedFromScrapFlag() {
            mFlags = mFlags & ~FLAG_RETURNED_FROM_SCRAP;
        }

        void clearTmpDetachFlag() {
            mFlags = mFlags & ~FLAG_TMP_DETACHED;
        }

        void stopIgnoring() {
            mFlags = mFlags & ~FLAG_IGNORE;
        }

        void setScrapContainer(Recycler recycler, boolean isChangeScrap) {
            mScrapContainer = recycler;
            mInChangeScrap = isChangeScrap;
        }

        boolean isInvalid() {
            return (mFlags & FLAG_INVALID) != 0;
        }

        boolean needsUpdate() {
            return (mFlags & FLAG_UPDATE) != 0;
        }

        boolean isBound() {
            return (mFlags & FLAG_BOUND) != 0;
        }

        boolean isRemoved() {
            return (mFlags & FLAG_REMOVED) != 0;
        }

        boolean hasAnyOfTheFlags(int flags) {
            return (mFlags & flags) != 0;
        }

        boolean isTmpDetached() {
            return (mFlags & FLAG_TMP_DETACHED) != 0;
        }

        boolean isAdapterPositionUnknown() {
            return (mFlags & FLAG_ADAPTER_POSITION_UNKNOWN) != 0 || isInvalid();
        }

        void setFlags(int flags, int mask) {
            mFlags = (mFlags & ~mask) | (flags & mask);
        }

        void addFlags(int flags) {
            mFlags |= flags;
        }

        void addChangePayload(Object payload) {
            if (payload == null) {
                addFlags(FLAG_ADAPTER_FULLUPDATE);
            } else if ((mFlags & FLAG_ADAPTER_FULLUPDATE) == 0) {
                createPayloadsIfNeeded();
                mPayloads.add(payload);
            }
        }

        private void createPayloadsIfNeeded() {
            if (mPayloads == null) {
                mPayloads = new ArrayList<Object>();
                mUnmodifiedPayloads = Collections.unmodifiableList(mPayloads);
            }
        }

        void clearPayload() {
            if (mPayloads != null) {
                mPayloads.clear();
            }
            mFlags = mFlags & ~FLAG_ADAPTER_FULLUPDATE;
        }

        List<Object> getUnmodifiedPayloads() {
            if ((mFlags & FLAG_ADAPTER_FULLUPDATE) == 0) {
                if (mPayloads == null || mPayloads.size() == 0) {
                    return FULLUPDATE_PAYLOADS;
                }
                return mUnmodifiedPayloads;
            } else {
                return FULLUPDATE_PAYLOADS;
            }
        }

        void resetInternal() {
            mFlags = 0;
            mPosition = NO_POSITION;
            mOldPosition = NO_POSITION;
            mItemId = NO_ID;
            mPreLayoutPosition = NO_POSITION;
            mIsRecyclableCount = 0;
            mShadowedHolder = null;
            mShadowingHolder = null;
            clearPayload();
            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
            mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;
            clearNestedRecyclerViewIfNotNested(this);
        }

        private void onEnteredHiddenState(SeslRecyclerView parent) {
            if (mPendingAccessibilityState != -1) {
                mWasImportantForAccessibilityBeforeHidden = mPendingAccessibilityState;
            } else {
                mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(itemView);
            }
            parent.setChildImportantForAccessibilityInternal(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        }


        private void onLeftHiddenState(SeslRecyclerView parent) {
            parent.setChildImportantForAccessibilityInternal(this, mWasImportantForAccessibilityBeforeHidden);
            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ViewHolder{" + Integer.toHexString(hashCode()) + " position=" + mPosition + " id=" + mItemId + ", oldPos=" + mOldPosition + ", pLpos:" + mPreLayoutPosition);
            if (isScrap()) {
                sb.append(" scrap ").append(mInChangeScrap ? "[changeScrap]" : "[attachedScrap]");
            }
            if (isInvalid()) sb.append(" invalid");
            if (!isBound()) sb.append(" unbound");
            if (needsUpdate()) sb.append(" update");
            if (isRemoved()) sb.append(" removed");
            if (shouldIgnore()) sb.append(" ignored");
            if (isTmpDetached()) sb.append(" tmpDetached");
            if (!isRecyclable()) sb.append(" not recyclable(" + mIsRecyclableCount + ")");
            if (isAdapterPositionUnknown()) sb.append(" undefined adapter position");

            if (itemView.getParent() == null) sb.append(" no parent");
            sb.append("}");
            return sb.toString();
        }

        public final void setIsRecyclable(boolean recyclable) {
            mIsRecyclableCount = recyclable ? mIsRecyclableCount - 1 : mIsRecyclableCount + 1;
            if (mIsRecyclableCount < 0) {
                mIsRecyclableCount = 0;
                if (DEBUG) {
                    throw new RuntimeException("isRecyclable decremented below 0: " + "unmatched pair of setIsRecyable() calls for " + this);
                }
                LogUtils.e(VIEW_LOG_TAG, "isRecyclable decremented below 0: " + "unmatched pair of setIsRecyable() calls for " + this);
            } else if (!recyclable && mIsRecyclableCount == 1) {
                mFlags |= FLAG_NOT_RECYCLABLE;
            } else if (recyclable && mIsRecyclableCount == 0) {
                mFlags &= ~FLAG_NOT_RECYCLABLE;
            }
            if (DEBUG) {
                LogUtils.d(TAG, "setIsRecyclable val:" + recyclable + ":" + this);
            }
        }

        public final boolean isRecyclable() {
            return (mFlags & FLAG_NOT_RECYCLABLE) == 0 && !ViewCompat.hasTransientState(itemView);
        }

        private boolean shouldBeKeptAsChild() {
            return (mFlags & FLAG_NOT_RECYCLABLE) != 0;
        }

        private boolean doesTransientStatePreventRecycling() {
            return (mFlags & FLAG_NOT_RECYCLABLE) == 0 && ViewCompat.hasTransientState(itemView);
        }

        boolean isUpdated() {
            return (mFlags & FLAG_UPDATE) != 0;
        }
    }

}
