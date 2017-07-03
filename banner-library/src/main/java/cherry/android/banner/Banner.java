package cherry.android.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class Banner extends ViewGroup {

    private LoopViewPager mLoopViewPager;
    private List<String> mTitles;
    private TextView mTitleView;
    private SparseArray<View> mIndicators;
    private int mIndicatorGap = 5;
    private int mIndicatorPadding = 5;
    private int mIndicatorWidth = 10;
    private int mIndicatorHeight = 10;

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
        mIndicators = new SparseArray<>();
        mLoopViewPager = new LoopViewPager(getContext());
        mTitleView = new AppCompatTextView(getContext());
        addView(mLoopViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTitleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setAdapter(@NonNull PagerAdapter adapter) {
        mLoopViewPager.setAdapter(adapter);
        final int count = adapter.getCount();
        final int oldSize = mIndicators.size();
        if (count == oldSize) return;
        final int delta = count - oldSize;
        filterIndicators(delta);
    }

    private void filterIndicators(int delta) {
        for (int i = 0; i < Math.abs(delta); i++) {
            if (delta > 0) {
                View view = new View(getContext());
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
        mLoopViewPager.layout(paddingLeft, paddingTop,
                parentWidth - paddingRight + paddingLeft,
                parentHeight - paddingBottom + paddingTop);
        int indicatorCount = mIndicators.size();
        int totalGap = mIndicatorGap * (indicatorCount - 1);
        int totalWidth = mIndicatorWidth * indicatorCount + totalGap + 2 * mIndicatorPadding;
        int totalHeight = mIndicatorHeight + 2 * mIndicatorPadding;
        int left = (parentWidth - totalWidth) / 2 + mIndicatorPadding;
        int top = (parentHeight - totalHeight) / 2 + mIndicatorPadding;
        for (int j = 0; j < mIndicators.size(); j++) {
            final View child = mIndicators.get(j);
            left += ((mIndicatorGap + mIndicatorWidth) * i);
            child.layout(left, top, left + mIndicatorWidth, top + mIndicatorHeight);
        }
    }
}
