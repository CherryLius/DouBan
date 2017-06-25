package cherry.android.ptr;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static cherry.android.ptr.PullToRefreshLayout.STATE_COMPLETE;
import static cherry.android.ptr.PullToRefreshLayout.STATE_IDLE;
import static cherry.android.ptr.PullToRefreshLayout.STATE_PULL_TO_REFRESH;
import static cherry.android.ptr.PullToRefreshLayout.STATE_REFRESHING;
import static cherry.android.ptr.PullToRefreshLayout.STATE_RELEASE_TO_REFRESH;

/**
 * Created by Administrator on 2017/6/23.
 */

public abstract class AbstractRefreshHeader implements IRefreshHeader, OnStateChangedListener {
    protected Context mContext;
    protected View mHeaderView;

    public AbstractRefreshHeader(@NonNull Context context,
                                 @NonNull ViewGroup parent,
                                 @LayoutRes int layoutId) {
        mContext = context;
        mHeaderView = LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    @NonNull
    @Override
    public View getView() {
        return mHeaderView;
    }

    @Override
    public int getRefreshThreshold() {
        return mHeaderView.getMeasuredHeight();
    }

    @Override
    public void onStateChanged(@PullToRefreshLayout.State int state) {
        switch (state) {
            case STATE_IDLE:
                onIdle();
                break;
            case STATE_PULL_TO_REFRESH:
                onPullToRefresh();
                break;
            case STATE_RELEASE_TO_REFRESH:
                onReleaseToRefresh();
                break;
            case STATE_REFRESHING:
                onRefreshing();
                break;
            case STATE_COMPLETE:
                onComplete();
                break;
        }
    }

    public abstract void onIdle();

    protected abstract void onPullToRefresh();

    protected abstract void onReleaseToRefresh();

    protected abstract void onRefreshing();

    protected abstract void onComplete();
}
