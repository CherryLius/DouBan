package cherry.android.ptr;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LHEE on 2017/6/25.
 */

public class NestedPullRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {

    public static final int STATE_IDLE = 0;
    public static final int STATE_PULL_TO_REFRESH = 1;
    public static final int STATE_RELEASE_TO_REFRESH = 2;
    public static final int STATE_REFRESHING = 3;
    public static final int STATE_COMPLETE = 4;

    @IntDef({STATE_IDLE,
            STATE_PULL_TO_REFRESH,
            STATE_RELEASE_TO_REFRESH,
            STATE_REFRESHING,
            STATE_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    private static final String TAG = "NestedPullRefreshLayout";
    private static final int DEFAULT_SCROLL_DURATION = 1000;

    @State
    private int mState;

    private IRefreshHeader mRefreshHeader;
    private View mTarget;
    private Scroller mScroller;

    private int mLastScrollY = 0;
    private float mCurrentOffset;

    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    public NestedPullRefreshLayout(Context context) {
        this(context, null);
    }

    public NestedPullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedPullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
            startDragTarget(offset);
            ensureState();
            ViewCompat.postInvalidateOnAnimation(this);
        }
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
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.i(TAG, "onNestedPreScroll: " + dx + ", " + dy);
        Log.i(TAG, "consumed=" + consumed[0] + ", " + consumed[1]);
        //上滑时的处理
        if (dy > 0 && mCurrentOffset > 0) {
            int drag;
            if (dy > mCurrentOffset) {
                consumed[1] = (int) (dy - mCurrentOffset);
                drag = -(int) mCurrentOffset;
                mCurrentOffset = 0;
            } else {
                consumed[1] = dy;
                drag = -dy;
                mCurrentOffset -= dy;
            }
            startDragTarget(drag);
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
        if (dy < 0 && !canChildScrollUp()) {
            Log.e(TAG, "startDrag " + mCurrentOffset);
            mCurrentOffset += (-dy);
            startDragTarget(-dy);
            ensureState();
        }

    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d(TAG, "[onStopNestedScroll]");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mLastScrollY = 0;
        if (mCurrentOffset > 0) {
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
//        if (mCallback != null) {
//            return mCallback.canChildScrollUp();
//        }
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


    private void startDragTarget(int offset) {
        if (mCurrentOffset <= 0)
            return;
        mRefreshHeader.getView().bringToFront();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            ViewCompat.offsetTopAndBottom(child, offset);
        }
        requestLayout();
    }

    private void finishDragTarget() {
        final int threshold = mRefreshHeader.getRefreshThreshold();
        if (mState == STATE_REFRESHING) {
            if (mCurrentOffset > threshold)
                mScroller.startScroll(0, 0, 0, (int) (mCurrentOffset - threshold), DEFAULT_SCROLL_DURATION);
            else
                mScroller.startScroll(0, 0, 0, (int) mCurrentOffset, DEFAULT_SCROLL_DURATION);
        } else if (mState == STATE_RELEASE_TO_REFRESH) {
            setState(STATE_REFRESHING);
            mScroller.startScroll(0, 0, 0, (int) (mCurrentOffset - threshold), DEFAULT_SCROLL_DURATION);
        } else {
            mScroller.startScroll(0, 0, 0, (int) mCurrentOffset, DEFAULT_SCROLL_DURATION);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

//    private void fling(float velocityY) {
//        mScroller.abortAnimation();
//        mScroller.fling(0, (int) mCurrentOffset, 0, (int) velocityY, 0, 0, 0, mRefreshHeader.getRefreshThreshold());
//        ViewCompat.postInvalidateOnAnimation(this);
//    }

    private void ensureState() {
        if (mState == STATE_REFRESHING || mState == STATE_COMPLETE)
            return;
        final int threshold = mRefreshHeader.getRefreshThreshold();
        if (mCurrentOffset > threshold) {
            setState(STATE_RELEASE_TO_REFRESH);
        } else {
            setState(STATE_PULL_TO_REFRESH);
        }
    }

    private void setState(@State int state) {
        if (mState != state) {
            mState = state;
            onStateChanged(mState);
        }
    }

    private void onStateChanged(@State int state) {
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
}
