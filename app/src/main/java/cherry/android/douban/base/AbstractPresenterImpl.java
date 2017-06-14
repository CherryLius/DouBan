package cherry.android.douban.base;

import android.support.annotation.NonNull;

import cherry.android.douban.rx.IRxLifecycleBinding;

/**
 * Created by Administrator on 2017/6/7.
 */

public abstract class AbstractPresenterImpl<V extends BaseView, P extends BasePresenter, T> {
    protected V mView;
    protected IRxLifecycleBinding<T> mRxLifecycle;

    public AbstractPresenterImpl(@NonNull V view) {
        mView = view;
        mView.setPresenter(getPresenter());
    }

    public AbstractPresenterImpl(@NonNull V view, @NonNull IRxLifecycleBinding<T> lifecycle) {
        this(view);
        mRxLifecycle = lifecycle;
    }

    @NonNull
    protected abstract P getPresenter();
}
