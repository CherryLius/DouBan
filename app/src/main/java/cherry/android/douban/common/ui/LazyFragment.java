package cherry.android.douban.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cherry.android.douban.base.BaseFragment;

/**
 * Created by Administrator on 2017/6/13.
 */

public abstract class LazyFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
