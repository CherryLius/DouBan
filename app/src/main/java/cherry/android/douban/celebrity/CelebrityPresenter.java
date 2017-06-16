package cherry.android.douban.celebrity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CelebrityPresenter extends RxPresenterImpl<CelebrityContract.View, CelebrityContract.Presenter, ActivityEvent>
        implements CelebrityContract.Presenter {

    public CelebrityPresenter(@NonNull CelebrityContract.View view,
                              @NonNull IRxLifecycleBinding<ActivityEvent> lifecycle) {
        super(view, lifecycle);
    }

    @Override
    public void loadCelebrityInfo(@NonNull String id) {
        if (TextUtils.isEmpty(id))
            return;
        Network.get().getMovieApi().celebrityInfo(id)
                .compose(RxHelper.<MovieCelebrity>mainIO())
                .compose(mRxLifecycle.<MovieCelebrity>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<MovieCelebrity>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull MovieCelebrity movieCelebrity) {
                        mView.showCelebrityInfo(movieCelebrity);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Logger.e("Test", "onError", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @NonNull
    @Override
    protected CelebrityContract.Presenter getPresenter() {
        return this;
    }
}
