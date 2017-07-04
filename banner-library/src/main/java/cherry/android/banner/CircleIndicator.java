package cherry.android.banner;

import android.content.Context;
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
    private int mIndicatorGap = 15;
    private int mIndicatorPadding = 10;
    private int mIndicatorWidth = 12;
    private int mIndicatorHeight = 12;

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
                view.setBackgroundResource(R.drawable.ic_default_indicator);
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentWidth = getMeasuredWidth();
        final int parentHeight = getMeasuredHeight();
        final int indicatorCount = mIndicators.size();
        int totalGap = mIndicatorGap * (indicatorCount - 1);
        int totalWidth = mIndicatorWidth * indicatorCount + totalGap + 2 * mIndicatorPadding;
        int totalHeight = mIndicatorHeight + 2 * mIndicatorPadding;
        int left = (parentWidth - totalWidth) / 2 + mIndicatorPadding;
        int top = parentHeight - totalHeight;
        Log.d(TAG, "size=" + indicatorCount);
        Log.d(TAG, "left=" + left + ",top=" + top);

        for (int j = 0; j < indicatorCount; j++) {
            final View child = mIndicators.get(j);
            int realLeft = left + ((mIndicatorGap + mIndicatorWidth) * j);
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
