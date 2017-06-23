package cherry.android.ptr;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Administrator on 2017/6/23.
 */

public interface IRefreshHeader {
    @NonNull
    View getView();

    int getRefreshThreshold();

    IRefreshListener getRefreshingListener();

    void onPositionChanged(float percent, @PullToRefreshLayout.State int state);
}
