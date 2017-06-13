package cherry.android.douban.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cherry.android.douban.base.BaseFragment;

/**
 * Created by Administrator on 2017/6/13.
 */

public abstract class LazyFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getUserVisibleHint())
            onLazyLoad();
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared())
            onLazyLoad();
    }

    public abstract void onLazyLoad();

    public abstract boolean isPrepared();
}
