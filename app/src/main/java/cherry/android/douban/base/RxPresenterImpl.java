package cherry.android.douban.base;

import android.support.annotation.NonNull;

import cherry.android.douban.rx.IRxLifecycleBinding;

/**
 * Created by Administrator on 2017/6/15.
 */

public abstract class RxPresenterImpl<V extends BaseView, P extends BasePresenter, E>
        extends AbstractPresenterImpl<V, P> {

    protected IRxLifecycleBinding<E> mRxLifecycle;

    public RxPresenterImpl(@NonNull V view, @NonNull IRxLifecycleBinding<E> lifecycle) {
        super(view);
        mRxLifecycle = lifecycle;
    }

}