//package cherry.android.ptr;
//
//import android.content.Context;
//import android.support.annotation.AttrRes;
//import android.support.annotation.IntDef;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.view.ViewCompat;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.widget.AbsListView;
//import android.widget.FrameLayout;
//import android.widget.Scroller;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
///**
// * Created by Administrator on 2017/6/23.
// */
//
//public class PullToRefreshLayout0 extends FrameLayout {
//
//    public static final int STATE_IDLE = 0;
//    public static final int STATE_PULL_TO_REFRESH = 1;
//    public static final int STATE_RELEASE_TO_REFRESH = 2;
//    public static final int STATE_REFRESHING = 3;
//    public static final int STATE_COMPLETE = 4;
//
//    @IntDef({STATE_IDLE,
//            STATE_PULL_TO_REFRESH,
//            STATE_RELEASE_TO_REFRESH,
//            STATE_REFRESHING,
//            STATE_COMPLETE})
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface State {
//
//    }
//
//    private static final float DEFAULT_OFFSET_RATIO = 1.5f;
//    private static final int DEFAULT_SCROLL_DURATION = 1000;
//
//    private int mTouchSlop;
//    private Scroller mScroller;
//    private IRefreshHeader mRefreshHeader;
//    @State
//    private int mState;
//
//    //滑动刷新控件
//    private View mTarget;
//    //触控点
//    private float mLastMotionY;
//    //偏移量
//    private int mOffset;
//    // Scroll偏移量
//    private int mLastScrollY;
//
//    private CanChildScrollUpCallback mCallback;
//    private OnRefreshListener mOnRefreshListener;
//
//    public PullToRefreshLayout0(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public PullToRefreshLayout0(@NonNull Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public PullToRefreshLayout0(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        mScroller = new Scroller(context);
//        mState = STATE_IDLE;
//    }
//
//    public void setRefreshHeader(@NonNull IRefreshHeader refreshHeader) {
//        if (mRefreshHeader != null)
//            removeView(mRefreshHeader.getView());
//        mRefreshHeader = refreshHeader;
//        addView(mRefreshHeader.getView(), 0);
//        ensureTarget();
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e("Test", "onInter] " + canChildScrollUp() + ",acton=" + ev.getAction());
//        if (mRefreshHeader == null) {
//            return super.onInterceptTouchEvent(ev);
//        }
////        if (canChildScrollUp() && mState == STATE_IDLE)
////            return super.onInterceptTouchEvent(ev);
//        final int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mLastMotionY = ev.getY();
//                if (mState != STATE_REFRESHING)
//                    setState(STATE_IDLE);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float delta = ev.getY() - mLastMotionY;
//                Log.d("Test", "delat=" + delta);
//                if (delta > mTouchSlop
//                        && !canChildScrollUp()
//                        && mState != STATE_REFRESHING) { //下拉拦截
//                    mLastMotionY = ev.getY();
//                    Log.e("Test", "拦截");
//                    return true;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("Test", "[onTouch] " + canChildScrollUp() + ", " + event.getAction());
//        if (mRefreshHeader == null) {
//            return super.onTouchEvent(event);
//        }
////        if (canChildScrollUp() && mState == STATE_IDLE) {
////            Log.i("Test", "canScrollUp=" + canChildScrollUp() + ",ret=" + super.onTouchEvent(event));
////            return super.onTouchEvent(event);
////        }
//        final int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mLastMotionY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float delta = event.getY() - mLastMotionY;
//                mLastMotionY = event.getY();
//                offsetViewTopAndBottom((int) (delta / DEFAULT_OFFSET_RATIO));
//                return true;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mLastScrollY = 0;
//                if (mState == STATE_RELEASE_TO_REFRESH) {
//                    setState(STATE_REFRESHING);
//                    mScroller.startScroll(0, 0, 0, mOffset - mRefreshHeader.getRefreshThreshold(), DEFAULT_SCROLL_DURATION);
//                } else {
//                    mScroller.startScroll(0, 0, 0, mOffset, DEFAULT_SCROLL_DURATION);
//                }
//                postInvalidate();
//                return true;
//        }
//
//        return super.onTouchEvent(event);
//    }
//
//    private void offsetViewTopAndBottom(int offset) {
//        mOffset += offset;
//        if (mOffset <= 0) return;
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = getChildAt(i);
//            ViewCompat.offsetTopAndBottom(child, offset);
//        }
//        requestLayout();
//        if (mState != STATE_REFRESHING && mState != STATE_COMPLETE) {
//            if (mOffset <= mRefreshHeader.getRefreshThreshold()) {
//                setState(STATE_PULL_TO_REFRESH);
//            } else {
//                setState(STATE_RELEASE_TO_REFRESH);
//            }
//        }
//        mRefreshHeader.onPositionChanged(mOffset / (float) mRefreshHeader.getRefreshThreshold(), mState);
//    }
//
//    @Override
//    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//        if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
//                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))) {
//            // Nope.
//        } else {
//            super.requestDisallowInterceptTouchEvent(disallowIntercept);
//        }
//    }
//
//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if (mScroller.computeScrollOffset()) {
//            int offset = mScroller.getCurrY() - mLastScrollY;
//            mLastScrollY = mScroller.getCurrY();
//            offsetViewTopAndBottom(-offset);
//            postInvalidate();
//        }
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        View headerView = null;
//        if (mRefreshHeader != null) {
//            headerView = mRefreshHeader.getView();
//            headerView.layout(left, mOffset - headerView.getMeasuredHeight(), right, mOffset);
//        }
//        ensureTarget();
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = getChildAt(i);
//            if (child != headerView) {
//                child.layout(left, mOffset, right, bottom + mOffset - top);
//            }
//        }
//    }
//
//    private void ensureTarget() {
//        // Don't bother getting the parent height if the parent hasn't been laid
//        // out yet.
//        View headerView = null;
//        if (mRefreshHeader != null) {
//            headerView = mRefreshHeader.getView();
//        }
//        if (mTarget == null) {
//            for (int i = 0; i < getChildCount(); i++) {
//                View child = getChildAt(i);
//                if (!child.equals(headerView)) {
//                    mTarget = child;
//                    break;
//                }
//            }
//        }
//    }
//
//    public boolean canChildScrollUp() {
//        if (mCallback != null) {
//            return mCallback.canChildScrollUp();
//        }
//        if (android.os.Build.VERSION.SDK_INT < 14) {
//            if (mTarget instanceof AbsListView) {
//                final AbsListView absListView = (AbsListView) mTarget;
//                return absListView.getChildCount() > 0
//                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
//                        .getTop() < absListView.getPaddingTop());
//            } else {
//                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
//            }
//        } else {
//            return ViewCompat.canScrollVertically(mTarget, -1);
//        }
//    }
//
//    private void setState(@State int state) {
//        if (mState != state) {
//            mState = state;
//            onStateChanged(state);
//        }
//    }
//
//    private void onStateChanged(@State int state) {
//        if (mOnRefreshListener != null && mState == STATE_REFRESHING)
//            mOnRefreshListener.onRefresh();
//        if (mRefreshHeader == null || mRefreshHeader.getStateChangedListener() == null)
//            return;
//        final OnStateChangedListener headerRefreshingListener = mRefreshHeader.getStateChangedListener();
//        switch (state) {
//            case STATE_IDLE:
//                headerRefreshingListener.onIdle();
//                break;
//            case STATE_PULL_TO_REFRESH:
//                headerRefreshingListener.onPullToRefresh();
//                break;
//            case STATE_RELEASE_TO_REFRESH:
//                headerRefreshingListener.onReleaseToRefresh();
//                break;
//            case STATE_REFRESHING:
//                headerRefreshingListener.onRefreshing();
//                break;
//            case STATE_COMPLETE:
//                headerRefreshingListener.onComplete();
//                break;
//        }
//    }
//
//    public void notifyRefreshComplete() {
//        setState(STATE_COMPLETE);
//        mLastScrollY = 0;
//        mScroller.startScroll(0, 0, 0, mOffset, DEFAULT_SCROLL_DURATION);
//        postInvalidate();
//    }
//
//    public void setOnRefreshListener(OnRefreshListener listener) {
//        mOnRefreshListener = listener;
//    }
//
//    public void setCanChildScrollUpCallback(CanChildScrollUpCallback callback) {
//        mCallback = callback;
//    }
//
//    public interface CanChildScrollUpCallback {
//        boolean canChildScrollUp();
//    }
//
//    public interface OnRefreshListener {
//        void onRefresh();
//    }
//}
