package cherry.android.douban.base;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/6/7.
 */

public abstract class AbstractPresenterImpl<V extends BaseView, P extends BasePresenter> {
    protected V mView;

    public AbstractPresenterImpl(@NonNull V view) {
        mView = view;
        mView.setPresenter(getPresenter());
    }

    @NonNull
    protected abstract P getPresenter();
}
