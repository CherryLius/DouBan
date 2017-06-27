package cherry.android.ptr;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/6/27.
 */

interface IPullToRefresh {

    void setRefreshHeader(@NonNull IRefreshHeader refreshHeader);

    void setState(@Common.State int state);

    void refreshComplete();

    void setOnChildScrollUpCallback(OnChildScrollUpCallback callback);

    void setOnRefreshListener(OnRefreshListener listener);
}
