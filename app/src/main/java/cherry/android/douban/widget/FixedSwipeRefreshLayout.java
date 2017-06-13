package cherry.android.douban.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/6/13.
 */

public class FixedSwipeRefreshLayout extends SwipeRefreshLayout {

    private ChildScrollUpCallback mCallback;

    public FixedSwipeRefreshLayout(Context context) {
        super(context);
    }

    public FixedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCallback != null)
            return mCallback.canChildScrollUp();
        return super.canChildScrollUp();
    }

    public void setCanChildScrollUpCallback(ChildScrollUpCallback callback) {
        mCallback = callback;
    }

    public interface ChildScrollUpCallback {
        boolean canChildScrollUp();
    }
}
