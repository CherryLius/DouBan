package cherry.android.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
    private TextView mNumberIndicator;

    private CircleIndicator mCircleIndicator;
    private int mBannerDuration = 2000;

    private int mTitleHeight = 80;
    private int mTitlePadding = 10;
    @DrawableRes
    private int mTitleBackground = R.drawable.drawable_white_44;
    private int mNumberIndicatorSize = 80;
    @DrawableRes
    private int mNumberIndicatorBackground = R.drawable.ic_default_number_indicator;
    private int mPagerScrollDuration = 800;
    @ColorInt
    private int mTextColor = Color.WHITE;
    private int mTextSize = 14;

    private int mIndicatorGap = 5;
    private int mIndicatorPadding = 10;
    private int mIndicatorWidth = 10;
    private int mIndicatorHeight = 10;
    @DrawableRes
    private int mIndicatorDrawableRes = R.drawable.ic_default_indicator;

    private boolean mIsAutoPlay;
    private Handler mHandler = new Handler();

    @IndicatorStyle
    private int mIndicatorStyle = CIRCLE_INDICATOR;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setChildrenDrawingOrderEnabled(true);
        initAttrs(context, attrs);
        init();
        setIndicatorStyle(mIndicatorStyle);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        if (ta != null) {
            mIndicatorGap = ta.getDimensionPixelSize(R.styleable.Banner_indicatorGap, mIndicatorGap);
            mIndicatorPadding = ta.getDimensionPixelSize(R.styleable.Banner_indicatorPadding, mIndicatorPadding);
            mIndicatorWidth = ta.getDimensionPixelSize(R.styleable.Banner_indicatorWidth, mIndicatorWidth);
            mIndicatorHeight = ta.getDimensionPixelSize(R.styleable.Banner_indicatorHeight, mIndicatorHeight);
            mIndicatorDrawableRes = ta.getResourceId(R.styleable.Banner_indicatorDrawable, mIndicatorDrawableRes);

            mNumberIndicatorSize = ta.getDimensionPixelSize(R.styleable.Banner_numIndicatorSize, mNumberIndicatorSize);
            mNumberIndicatorBackground = ta.getResourceId(R.styleable.Banner_numIndicatorBackground, mNumberIndicatorBackground);

            mTitleHeight = ta.getDimensionPixelSize(R.styleable.Banner_titleHeight, mTitleHeight);
            mTitlePadding = ta.getDimensionPixelSize(R.styleable.Banner_titlePadding, mTitlePadding);
            mTitleBackground = ta.getResourceId(R.styleable.Banner_titleBackground, mTitleBackground);

            mTextSize = ta.getDimensionPixelSize(R.styleable.Banner_textSize, mTextSize);
            mTextColor = ta.getColor(R.styleable.Banner_textColor, mTextColor);
            mPagerScrollDuration = ta.getInt(R.styleable.Banner_pagerScrollDuration, mPagerScrollDuration);

            mIndicatorStyle = ta.getInt(R.styleable.Banner_indicatorStyle, mIndicatorStyle);
            ta.recycle();
        }
    }

    private void init() {
        mTitles = new ArrayList<>();
        mLoopViewPager = new LoopViewPager(getContext(), mPagerScrollDuration);
        mLoopViewPager.setBoundaryCaching(true);
        mLoopViewPager.addOnPageChangeListener(mPageChangeListener);
        addView(mLoopViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setAutoPlay(true);
    }

    public void setAutoPlay(boolean isAutoPlay) {
        this.mIsAutoPlay = isAutoPlay;
    }

    public void setAdapter(@NonNull PagerAdapter adapter) {
        mLoopViewPager.setAdapter(adapter);
    }

    public <T> void setTitles(@NonNull List<T> source, Function<T, String> func) {
        mTitles.clear();
        for (int i = 0; i < source.size(); i++) {
            mTitles.add(func.apply(source.get(i)));
        }
    }

    public void setIndicatorStyle(@IndicatorStyle int style) {
        this.mIndicatorStyle = style;
    }

    public void apply() {
        createTitles();
        createIndicator();
        mLoopViewPager.setCurrentItem(0);
        onBannerPageSelected(0, -1);
        Log.d(TAG, "apply");
        if (mIsAutoPlay)
            startAutoPlay();
    }

    private void createTitles() {
        if (mTitles.size() != 0 && mTitleLayout == null) {
            mTitleLayout = new LinearLayout(getContext());
            mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
            mTitleLayout.setBackgroundResource(mTitleBackground);
            mTitleLayout.setPadding(mTitlePadding, 0, mTitlePadding, 0);
            addView(mTitleLayout, new LayoutParams(LayoutParams.MATCH_PARENT, mTitleHeight));
            mTitleView = new AppCompatTextView(getContext());
            mTitleView.setTextColor(mTextColor);
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mTitleView.setGravity(Gravity.CENTER_VERTICAL);
            mTitleView.setMaxLines(1);
            mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.gravity = Gravity.CENTER_VERTICAL;
            mTitleLayout.addView(mTitleView, lp);
        }
    }

    private void createIndicator() {
        if (mIndicatorStyle == NUMBER_INDICATOR
                || mIndicatorStyle == TITLE_NUMBER_INDICATOR) {
            mNumberIndicator = new AppCompatTextView(getContext());
            mNumberIndicator.setTextColor(mTextColor);
            mNumberIndicator.setGravity(Gravity.CENTER);
            if (mIndicatorStyle == NUMBER_INDICATOR) {
                mNumberIndicator.setBackgroundResource(mNumberIndicatorBackground);
                addView(mNumberIndicator, new LayoutParams(mNumberIndicatorSize, mNumberIndicatorSize));
            } else {
                mTitleLayout.addView(mNumberIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            }

            return;
        }
        if (mCircleIndicator == null) {
            mCircleIndicator = new CircleIndicator(getContext());
            mCircleIndicator.setGap(mIndicatorGap);
            mCircleIndicator.setIndicatorPadding(mIndicatorPadding);
            mCircleIndicator.setIndicatorWidth(mIndicatorWidth);
            mCircleIndicator.setIndicatorHeight(mIndicatorHeight);
            mCircleIndicator.setIndicatorDrawableRes(mIndicatorDrawableRes);
        }
        final int count = mLoopViewPager.getAdapter().getCount();
        mCircleIndicator.setCount(count);
        if (mIndicatorStyle == CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR) {
            addView(mCircleIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        } else if (mIndicatorStyle == TITLE_CIRCLE_INDICATOR_INSIDE) {
            if (mTitleLayout == null)
                throw new IllegalStateException("cannot get a title container, please check titles");
            mTitleLayout.addView(mCircleIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
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

        layoutIndicator();
    }

    private void layoutIndicator() {
        if (mIndicatorStyle == NONE_INDICATOR)
            return;
        final int paddingLeft = getPaddingLeft();
        //final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int parentWidth = getMeasuredWidth();
        final int parentHeight = getMeasuredHeight();
        if (mIndicatorStyle == TITLE_CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_NUMBER_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR_INSIDE) {
            if (mTitleLayout != null) {
                int left = paddingLeft;
                int top = parentHeight - mTitleLayout.getMeasuredHeight() - paddingBottom;
                int right = parentWidth - paddingRight + paddingLeft;
                int bottom = top + mTitleLayout.getMeasuredHeight();
                mTitleLayout.layout(left, top, right, bottom);
            }
        }
        if (mIndicatorStyle == CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR) {
            int left = (parentWidth - mCircleIndicator.getMeasuredWidth()) / 2;
            int top = parentHeight - mCircleIndicator.getMeasuredHeight() - paddingBottom;
            int right = left + mCircleIndicator.getMeasuredWidth();
            int bottom = top + mCircleIndicator.getMeasuredHeight();
            if (mIndicatorStyle == TITLE_CIRCLE_INDICATOR
                    && mTitleLayout != null) {
                top -= mTitleLayout.getMeasuredHeight();
                bottom = top + mCircleIndicator.getMeasuredHeight();
            }
            mCircleIndicator.layout(left, top, right, bottom);
        }

        if (mIndicatorStyle == NUMBER_INDICATOR) {
            int left = parentWidth - 10 - mNumberIndicator.getMeasuredWidth();
            int top = parentHeight - mNumberIndicator.getMeasuredHeight() - 10;
            int right = left + mNumberIndicator.getMeasuredWidth();
            int bottom = top + mNumberIndicator.getMeasuredHeight();
            mNumberIndicator.layout(left, top, right, bottom);
        }
    }

//    @Override
//    protected int getChildDrawingOrder(int childCount, int i) {
//        if (mCircleIndicator == null)
//            return super.getChildDrawingOrder(childCount, i);
//        final int indicatorIndex = indexOfChild(mCircleIndicator);
//        if (indicatorIndex == -1)
//            return super.getChildDrawingOrder(childCount, i);
//        if (i == indicatorIndex) {
//            return childCount - 1;
//        } else if (i == childCount - 1) {
//            return indicatorIndex;
//        }
//        return super.getChildDrawingOrder(childCount, i);
//    }

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

    private void onBannerPageSelected(int position, int oldPosition) {
        if (mCircleIndicator != null)
            mCircleIndicator.onPageSelected(position, oldPosition);
        if (mTitles.size() == 0) return;
        if (mTitleView != null)
            mTitleView.setText(mTitles.get(position));
        if (mNumberIndicator != null) {
            int number = position + 1;
            mNumberIndicator.setText(number + "/" + mLoopViewPager.getAdapter().getCount());
        }
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
            onBannerPageSelected(position, oldPosition);
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
}
