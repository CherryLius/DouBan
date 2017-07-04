package cherry.android.banner;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class Banner extends ViewGroup {

    private static final String TAG = "Banner";

    public static final int NONE_INDICATOR = 0;
    public static final int CIRCLE_INDICATOR = 1;
    public static final int NUMBER_INDICATOR = 2;
    public static final int TITLE_NUMBER_INDICATOR = 3;
    public static final int TITLE_CIRCLE_INDICATOR = 4;
    public static final int TITLE_CIRCLE_INDICATOR_INSIDE = 5;

    @IntDef({NONE_INDICATOR,
            CIRCLE_INDICATOR,
            NUMBER_INDICATOR,
            TITLE_NUMBER_INDICATOR,
            TITLE_CIRCLE_INDICATOR,
            TITLE_CIRCLE_INDICATOR_INSIDE})
    @Retention(RetentionPolicy.SOURCE)
    @interface IndicatorStyle {

    }

    private LoopViewPager mLoopViewPager;
    private List<String> mTitles;

    private LinearLayout mTitleLayout;
    private TextView mTitleView;

    private CircleIndicator mCircleIndicator;
    private int mBannerDuration = 2000;

    private int mTitleHeight = 64;

    private boolean mIsAutoPlay;
    private Handler mHandler = new Handler();

    @IndicatorStyle
    private int mIndicatorStyle;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTitles = new ArrayList<>();
        mCircleIndicator = new CircleIndicator(getContext());
        mLoopViewPager = new LoopViewPager(getContext(), 800);
        mLoopViewPager.setBoundaryCaching(true);
        mLoopViewPager.addOnPageChangeListener(mPageChangeListener);
        addView(mLoopViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mCircleIndicator, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setAutoPlay(true);
    }

    public void setAutoPlay(boolean isAutoPlay) {
        this.mIsAutoPlay = isAutoPlay;
    }

    public void setAdapter(@NonNull PagerAdapter adapter) {
        mLoopViewPager.setAdapter(adapter);
        final int count = adapter.getCount();
        mCircleIndicator.setCount(count);
        mLoopViewPager.setCurrentItem(0);
        mCircleIndicator.onPageSelected(0, -1);
        Log.d(TAG, "setAdapter");
        if (mIsAutoPlay)
            startAutoPlay();
    }

    public <T> void setTitles(@NonNull List<T> source, Function<T, String> func) {
        mTitles.clear();
        for (int i = 0; i < source.size(); i++) {
            mTitles.add(func.apply(source.get(i)));
        }
        if (mTitles.size() != 0 && mTitleLayout == null) {
            mTitleLayout = new LinearLayout(getContext());
            mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(mTitleLayout, new LayoutParams(LayoutParams.MATCH_PARENT, mTitleHeight));
            mTitleView = new AppCompatTextView(getContext());
            mTitleLayout.addView(mTitleView, new LayoutParams(LayoutParams.MATCH_PARENT, mTitleHeight));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        final int pageCount = mLoopViewPager.getAdapter().getCount();
        if (pageCount == 0)
            return;
        if (getVisibility() == GONE
                || mLoopViewPager.getVisibility() == GONE)
            return;
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int parentWidth = getMeasuredWidth();
        final int parentHeight = getMeasuredHeight();
        Log.i(TAG, "[onLayout] " + paddingLeft + ", " + paddingRight + ", " + paddingTop + ", " + paddingBottom);
        Log.i(TAG, "[onLayout] " + parentWidth + "x" + parentHeight);
        Log.i(TAG, "viewPager " + mLoopViewPager.getMeasuredWidth() + "," + mLoopViewPager.getMeasuredHeight());
        mLoopViewPager.layout(paddingLeft, paddingTop,
                parentWidth - paddingRight + paddingLeft,
                parentHeight - paddingBottom + paddingTop);

        if (mTitleLayout != null) {
            int left = paddingLeft;
            int top = parentHeight - mTitleLayout.getMeasuredHeight() - paddingBottom;
            int right = parentWidth - paddingRight + paddingLeft;
            int bottom = top + mTitleLayout.getMeasuredHeight();
            mTitleLayout.layout(left, top, right, bottom);
        }

        int left = (parentWidth - mCircleIndicator.getMeasuredWidth()) / 2;
        int top = parentHeight - mCircleIndicator.getMeasuredHeight() - paddingBottom;
        int right = left + mCircleIndicator.getMeasuredWidth();
        int bottom = top + mCircleIndicator.getMeasuredHeight();
        mCircleIndicator.layout(left, top, right, bottom);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsAutoPlay) {
            final int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
        Log.e(TAG, "[onDetachedFromWindow]");
    }

    private void startAutoPlay() {
        mHandler.removeCallbacks(mBannerRunnable);
        mHandler.postDelayed(mBannerRunnable, mBannerDuration);
    }

    private void stopAutoPlay() {
        mHandler.removeCallbacks(mBannerRunnable);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        private int oldPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "[onPageSelected] position=" + position + ',' + oldPosition);
            mCircleIndicator.onPageSelected(position, oldPosition);
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private Runnable mBannerRunnable = new Runnable() {
        @Override
        public void run() {
            final int current = mLoopViewPager.getCurrentItem();
            mLoopViewPager.setCurrentItem(current + 1);
            mHandler.postDelayed(this, mBannerDuration);
        }
    };

//    private static float dipSize(Context context, float size) {
//        Resources r;
//
//        if (context == null)
//            r = Resources.getSystem();
//        else
//            r = context.getResources();
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());
//    }
}
