package cherry.android.ptr;

/**
 * Created by Administrator on 2017/6/23.
 */

public interface IRefreshListener {
    void onIdle();

    void onPullToRefresh();

    void onReleaseToRefresh();

    void onRefreshing();

    void onComplete();
}
