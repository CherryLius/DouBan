package cherry.android.douban.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class CustomGridLayout extends ViewGroup {

    private static final int DEFAULT_COLUMN = 4;

    private View mHeaderView;
    private int mColumn;

    public CustomGridLayout(Context context) {
        this(context, null);
    }

    public CustomGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mColumn = DEFAULT_COLUMN;
    }

    public void setColumn(@IntRange(from = 1) int column) {
        if (column <= 0)
            throw new IllegalArgumentException("Grid's column should be greater than 0");
        mColumn = column;
    }

    public void addItem(View item) {
        if (item != null) {
            addView(item);
        }
    }

    public void setHeader(@NonNull View view) {
        if (mHeaderView != null) {
            removeView(mHeaderView);
        }
        mHeaderView = view;
        addView(mHeaderView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("Test", "onFinishInflate");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("Test", "onMeasure");
        final int childCount = getChildCount();
        int maxChildWidth = 0;
        int maxChildHeight = 0;
        int validChildCount = childCount;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                validChildCount--;
                continue;
            }
            final LayoutParams lp = child.getLayoutParams();
            if (lp instanceof MarginLayoutParams)
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            else
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if (child == mHeaderView)
                continue;
            int extraW = 0, extraH = 0;
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams margins = (MarginLayoutParams) lp;
                extraW = margins.leftMargin + margins.rightMargin;
                extraH = margins.topMargin + margins.bottomMargin;
            }
            maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth() + extraW);
            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight() + extraH);
        }
        boolean hasHeader = mHeaderView != null && mHeaderView.getVisibility() != GONE;
        if (mHeaderView != null)
            Log.d("Test", "header::w=" + mHeaderView.getMeasuredWidth() + ", " + mHeaderView.getMeasuredHeight());
        validChildCount = !hasHeader ? validChildCount : validChildCount - 1;
        int rows = getRow(validChildCount);
        int calH = maxChildHeight * rows + (hasHeader ? mHeaderView.getMeasuredHeight() : 0);
        int width = Math.max(maxChildWidth * mColumn + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth());
        int height = Math.max(calH + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight());
        setMeasuredDimension(measureDimen(width, widthMeasureSpec), measureDimen(height, heightMeasureSpec));
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
        final int childCount = getChildCount();
        int validCount = childCount;
        boolean hasHeader = mHeaderView != null && mHeaderView.getVisibility() != GONE;
        if (hasHeader) {
            Log.d("Test", "onLayout :: l=" + l + ",t=" + t + ",r=" + r + ",=" + mHeaderView.getMeasuredHeight());
            Log.d("Test", "Top=" + getPaddingTop());
            mHeaderView.layout(getPaddingLeft(), getPaddingTop(),
                    getPaddingLeft() + getMeasuredWidth() - getPaddingRight(),
                    getPaddingLeft() + mHeaderView.getMeasuredHeight());
            validCount--;
        }
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE)
                continue;
            validCount--;
        }
        int goneCount = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE
                    || child == mHeaderView) {
                goneCount++;
                continue;
            }
            layoutChildren(child, i - goneCount, validCount);// not delete gone view
        }
    }

    private void layoutChildren(final View child, int position, int validCount) {
        boolean hasHeader = mHeaderView != null && mHeaderView.getVisibility() != GONE;
        final int parentPaddingTop = getPaddingTop() + (hasHeader ? mHeaderView.getMeasuredHeight() : 0);
        final int parentPaddingLeft = getPaddingLeft();
        final int parentPaddingBottom = getPaddingBottom();
        final int parentPaddingRight = getPaddingRight();

        final int row = position / mColumn;
        final int column = position % mColumn;
        int rows = getRow(validCount);

        final int cellWidth = (getMeasuredWidth() - parentPaddingLeft - parentPaddingRight) / mColumn;
        final int cellHeight = (getMeasuredHeight() - parentPaddingTop - parentPaddingBottom) / rows;
        final int childWidth = child.getMeasuredWidth();
        final int childHeight = child.getMeasuredHeight();

        int left, top;

        if (column == 0) {
            left = parentPaddingLeft;
        } else {
            left = parentPaddingLeft + column * cellWidth;
        }

        if (row == 0) {
            top = parentPaddingTop;
        } else {
            top = parentPaddingTop + row * cellHeight;
        }

        //居中
        left += (cellWidth - childWidth) / 2;
        top += (cellHeight - childHeight) / 2;
        int right = left + childWidth;
        int bottom = top + childHeight;
        child.layout(left, top, right, bottom);
    }

    private int getRow(int position) {
        return position / mColumn + (position % mColumn == 0 ? 0 : 1);
    }
}
