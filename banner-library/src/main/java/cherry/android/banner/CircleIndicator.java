package cherry.android.banner;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/4.
 */

public class CircleIndicator extends ViewGroup {

    private static final String TAG = "CircleIndicator";

    private SparseArray<View> mIndicators;
    private int mIndicatorGap = 5;
    private int mIndicatorPadding = 10;
    private int mIndicatorWidth = 10;
    private int mIndicatorHeight = 10;
    @DrawableRes
    private int mIndicatorDrawableRes = R.drawable.ic_default_indicator;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mIndicators = new SparseArray<>();
    }

    public void setGap(int gap) {
        mIndicatorGap = gap;
    }

    public void setIndicatorPadding(int padding) {
        mIndicatorPadding = padding;
    }

    public void setIndicatorWidth(int width) {
        mIndicatorWidth = width;
    }

    public void setIndicatorHeight(int height) {
        mIndicatorHeight = height;
    }

    public void setIndicatorDrawableRes(@DrawableRes int resId) {
        mIndicatorDrawableRes = resId;
    }

    public void setCount(int count) {
        final int oldSize = mIndicators.size();
        if (count == oldSize) return;
        final int delta = count - oldSize;
        fillIndicators(delta);
    }

    private void fillIndicators(int delta) {
        for (int i = 0; i < Math.abs(delta); i++) {
            if (delta > 0) {
                View view = new View(getContext());
                view.setBackgroundResource(mIndicatorDrawableRes);
                mIndicators.put(mIndicators.size(), view);
                addView(view, new LayoutParams(mIndicatorWidth, mIndicatorHeight));
            } else {
                View view = mIndicators.get(i);
                mIndicators.remove(i);
                removeView(view);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        int indicatorWidth = mIndicatorWidth * childCount + mIndicatorGap * (childCount - 1) + 2 * mIndicatorPadding;
        int indicatorHeight = mIndicatorHeight + 2 * mIndicatorPadding;
        int width = Math.max(indicatorWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth());
        int height = Math.max(indicatorHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight());
        setMeasuredDimension(measureDimen(width, widthMeasureSpec),
                measureDimen(height, heightMeasureSpec));
    }

    private static int measureDimen(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentWidth = getMeasuredWidth();
        final int parentHeight = getMeasuredHeight();
        final int indicatorCount = mIndicators.size();
        int totalGap = mIndicatorGap * (indicatorCount - 1);
        int totalWidth = mIndicatorWidth * indicatorCount + totalGap + 2 * mIndicatorPadding;
        int left = (parentWidth - totalWidth) / 2 + mIndicatorPadding;
        int top = (parentHeight - mIndicatorHeight) / 2;
        Log.d(TAG, "size=" + indicatorCount);
        Log.d(TAG, "left=" + left + ",top=" + top);
        Log.e(TAG, "[parentWidth]=" + parentWidth + ",[parentHeight]=" + parentHeight);

        for (int i = 0; i < indicatorCount; i++) {
            final View child = mIndicators.get(i);
            int realLeft = left + ((mIndicatorGap + mIndicatorWidth) * i);
            Log.e(TAG, "realLeft=" + realLeft);
            child.layout(realLeft, top, realLeft + mIndicatorWidth, top + mIndicatorHeight);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicators.clear();
    }

    public void onPageSelected(int position, int oldPosition) {
        Log.d(TAG, "position=" + position + ", oldPosition=" + oldPosition + ",mIndicators =" + mIndicators.size());
        if (mIndicators.size() == 0)
            return;
        if (oldPosition >= 0 && oldPosition != position) {
            mIndicators.get(oldPosition).setSelected(false);
        }
        mIndicators.get(position).setSelected(true);
    }
}
