package cherry.android.ptr;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Scroller;

import static cherry.android.ptr.Common.STATE_COMPLETE;
import static cherry.android.ptr.Common.STATE_IDLE;
import static cherry.android.ptr.Common.STATE_PULL_TO_REFRESH;
import static cherry.android.ptr.Common.STATE_REFRESHING;
import static cherry.android.ptr.Common.STATE_RELEASE_TO_REFRESH;
import static cherry.android.ptr.NestedPullRefreshLayout.OverScrollHandler.MSG_START_COMPUTE_SCROLL;

/**
 * Created by LHEE on 2017/6/25.
 */

public class NestedPullRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild, IPullToRefresh {

    private static final String TAG = "NestedPullRefreshLayout";
    private static final int DEFAULT_SCROLL_DURATION = 1000;
    private static final int OVER_SCROLL_MIN_VX = 3000;

    @Common.State
    private int mState;

    private IRefreshHeader mRefreshHeader;
    private View mTarget;
    private Scroller mScroller;

    private int mLastScrollY = 0;
    private float mCurrentOffset;
    private float mTouchDistance;
    private float mMotionDownY;

    private int mTouchSlop;

    private float mTotalUnconsumed;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    private OnChildScrollUpCallback<NestedPullRefreshLayout> mOnChildScrollUpCallback;
    private OnRefreshListener mOnRefreshListener;

    public NestedPullRefreshLayout(Context context) {
        this(context, null);
    }

    public NestedPullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedPullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        mScroller = new Scroller(context);
        setState(STATE_IDLE);
        onStateChanged(mState);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0)
            return;
        if (mTarget == null)
            ensureTarget();
        if (mTarget == null)
            return;
        final int offset = (int) mCurrentOffset;
        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        //layout offset
        child.layout(childLeft, childTop + offset, childWidth, childHeight + offset);
        if (mRefreshHeader != null && mRefreshHeader.getView() != null) {
            final View header = mRefreshHeader.getView();
            final int headerWidth = header.getMeasuredWidth();
            final int headerHeight = header.getMeasuredHeight();
            //layout offset
            header.layout(childLeft, childTop + offset - headerHeight,
                    headerWidth, childTop + offset);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null)
            ensureTarget();
        if (mTarget == null)
            return;
        mTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        if (mRefreshHeader != null && mRefreshHeader.getView() != null) {
            final View header = mRefreshHeader.getView();
            measureChild(header, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int offset = mLastScrollY - mScroller.getCurrY();
            mLastScrollY = mScroller.getCurrY();
            mCurrentOffset += offset;
            mTotalUnconsumed = mCurrentOffset * 2.0f;
            offsetViewTopAndBottom(offset);
            ensureState();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                reset();
                mMotionDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                mTouchDistance = ev.getY() - mMotionDownY;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled()
                && mRefreshHeader != null && mRefreshHeader.getView() != null
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
        if (mState != STATE_REFRESHING)
            setState(STATE_IDLE);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.i(TAG, "onNestedPreScroll: " + dx + ", " + dy);
        Log.i(TAG, "consumed=" + consumed[0] + ", " + consumed[1]);
        Log.i("Test", "onPreScroll=" + mTotalUnconsumed + ",dy=" + dy);
        //上滑时的处理
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = (int) (dy - mTotalUnconsumed);
                mTotalUnconsumed = 0;
            } else {
                consumed[1] = dy;
                mTotalUnconsumed -= dy;
            }
            startDragTarget(mTotalUnconsumed);
            ensureState();
        }
        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "dxConsumed=" + dxConsumed + ",dyConsumed=" + dyConsumed
                + ",dxUnconsumed=" + dxUnconsumed + ", dyUnconsumed=" + dyUnconsumed);
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);
        //下滑时处理
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        Log.d("Test", dyUnconsumed + "====dy=" + dy + ", !canChildScrollUp=" + (!canChildScrollUp()));
        if (dy < 0 && !canChildScrollUp()) {
            Log.e(TAG, "startDrag " + mTotalUnconsumed);
            Log.d("Test", "onNestedScroll:: " + mTotalUnconsumed + ", dy=" + dy);
            mTotalUnconsumed += (-dy);
            startDragTarget(mTotalUnconsumed);
            ensureState();
        }

    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d(TAG, "[onStopNestedScroll]");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mLastScrollY = 0;
        if (mTotalUnconsumed > 0) {
            finishDragTarget();
        }
        stopNestedScroll();
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //处理Fling事件 velocityY > 0 向上快速滑动
        Log.d(TAG, "[onNestedPreFling] = " + target + ", velocityX=" + velocityX + ",velocityY=" + velocityY);
        Log.d(TAG, "mTouchDistance=" + mTouchDistance + " && slop=" + mTouchSlop);
        if (Math.abs(mTouchDistance) > mTouchSlop) {
            if (velocityY < 0 && !canChildScrollUp())
                return dispatchNestedPreFling(velocityX, velocityY);
            if (Math.abs(velocityY) > OVER_SCROLL_MIN_VX)
                mHandler.obtainMessage(MSG_START_COMPUTE_SCROLL, velocityY).sendToTarget();
        }
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    //NestedScrollingChild
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(getWindowToken());
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        View headerView = null;
        if (mRefreshHeader != null) {
            headerView = mRefreshHeader.getView();
        }
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(headerView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    protected boolean canChildScrollUp() {
        if (mOnChildScrollUpCallback != null) {
            return mOnChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }


    private void startDragTarget(float overScrollTop) {
        mCurrentOffset = overScrollTop / 2.0f;
        final int offset = (int) (mCurrentOffset - mTarget.getTop());
        offsetViewTopAndBottom(offset);
    }

    private void offsetViewTopAndBottom(int offset) {
        mRefreshHeader.getView().bringToFront();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            ViewCompat.offsetTopAndBottom(child, offset);
        }
        requestLayout();
        Log.e("Test", "current offset=" + mCurrentOffset
                + ", total=" + mTotalUnconsumed);
    }

    private void finishDragTarget() {
        final int threshold = getThresholdDistance();
        if (mState == STATE_REFRESHING) {
            if (mCurrentOffset >= threshold)
                mScroller.startScroll(0, 0, 0, (int) (mCurrentOffset - threshold), currentScrollDuration());
            else
                mScroller.startScroll(0, 0, 0, (int) mCurrentOffset, currentScrollDuration());
        } else if (mState == STATE_RELEASE_TO_REFRESH) {
            setState(STATE_REFRESHING);
            mScroller.startScroll(0, 0, 0, (int) (mCurrentOffset - threshold), currentScrollDuration());
        } else {
            mScroller.startScroll(0, 0, 0, (int) mCurrentOffset, currentScrollDuration());
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private int getThresholdDistance() {
        return mRefreshHeader.getRefreshThreshold();
    }

    private int currentScrollDuration() {
        int duration;
        final int threshold = getThresholdDistance();
        float offset = mCurrentOffset;
        if (offset > threshold)
            offset -= threshold;
        if (offset > threshold)
            return DEFAULT_SCROLL_DURATION;
        duration = (int) (offset / threshold * DEFAULT_SCROLL_DURATION);
        return duration;
    }

    private void reset() {
        mHandler.removeCallbacksAndMessages(getWindowToken());
        mTarget.clearAnimation();
    }

    private void overScroll(float velocityY, long delay) {
        final int headerThreshold = getThresholdDistance();
        int overHeight = (int) Math.abs(velocityY / delay / 2);
        final int realOverHeight = overHeight > headerThreshold ? headerThreshold : overHeight;
        final int duration = realOverHeight < 50 ? 345 : (int) (0.9f * realOverHeight + 300);
        Log.e(TAG, "[overScroll] " + realOverHeight + ", duration=" + duration);
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, realOverHeight);
        ta.setDuration(duration);
        ta.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) (1.2f * Math.sin(input * Math.PI));
            }
        });
        mTarget.startAnimation(ta);
        //mRefreshHeader.getView().startAnimation(ta);
    }

    private void ensureState() {
        if (mState == STATE_REFRESHING || mState == STATE_COMPLETE)
            return;
        final int threshold = getThresholdDistance();
        if (mCurrentOffset > threshold) {
            setState(STATE_RELEASE_TO_REFRESH);
        } else {
            setState(STATE_PULL_TO_REFRESH);
        }
    }

    public void setState(@Common.State int state) {
        if (mState != state) {
            mState = state;
            onStateChanged(mState);
        }
    }

    private void onStateChanged(@Common.State int state) {
        if (mState == STATE_REFRESHING && mOnRefreshListener != null)
            mOnRefreshListener.onRefresh();
        if (mRefreshHeader == null || mRefreshHeader.getStateChangedListener() == null)
            return;
        final OnStateChangedListener listener = mRefreshHeader.getStateChangedListener();
        listener.onStateChanged(state);
    }

    public void setRefreshHeader(@NonNull IRefreshHeader refreshHeader) {
        if (mRefreshHeader != null)
            removeView(mRefreshHeader.getView());
        if (refreshHeader.getView() == null)
            throw new NullPointerException("header view should not be Null");
        mRefreshHeader = refreshHeader;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(mRefreshHeader.getView(), 0, params);
        ensureTarget();
    }

    public void refreshComplete() {
        setState(STATE_COMPLETE);
        mLastScrollY = 0;
        if (mCurrentOffset <= 0)
            return;
        mScroller.startScroll(0, 0, 0, (int) mCurrentOffset, DEFAULT_SCROLL_DURATION);
        postInvalidate();
    }

    public void setOnChildScrollUpCallback(OnChildScrollUpCallback callback) {
        this.mOnChildScrollUpCallback = callback;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    private OverScrollHandler mHandler = new OverScrollHandler(this);

    static class OverScrollHandler extends Handler {
        static final int MSG_START_COMPUTE_SCROLL = 0;
        static final int MSG_CONTINUE_COMPUTE_SCROLL = 1;
        static final int DEFAULT_DELAY = 60;
        NestedPullRefreshLayout layout;

        long currentDelayTime;

        public OverScrollHandler(NestedPullRefreshLayout layout) {
            this.layout = layout;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            float velocityY = (float) msg.obj;
            switch (msg.what) {
                case MSG_START_COMPUTE_SCROLL:
                    currentDelayTime = -1;
                case MSG_CONTINUE_COMPUTE_SCROLL:
                    currentDelayTime++;
                    if (!layout.canChildScrollUp()) {
                        layout.overScroll(velocityY, currentDelayTime);
                        currentDelayTime = DEFAULT_DELAY;
                    }
                    if (currentDelayTime < DEFAULT_DELAY)
                        sendMessageDelayed(obtainMessage(MSG_CONTINUE_COMPUTE_SCROLL, velocityY), 10);
                    break;
            }
        }
    }
}
