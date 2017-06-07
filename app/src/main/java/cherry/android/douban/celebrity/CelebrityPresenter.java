package cherry.android.douban.celebrity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cherry.android.douban.base.AbstractPresenterImpl;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.network.Network;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CelebrityPresenter extends AbstractPresenterImpl<CelebrityContract.View, CelebrityContract.Presenter>
        implements CelebrityContract.Presenter {

    public CelebrityPresenter(@NonNull CelebrityContract.View view) {
        super(view);
    }

    @Override
    public void loadCelebrityInfo(@NonNull String id) {
        if (TextUtils.isEmpty(id))
            return;
        Network.instance().getMovieApi().celebrityInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
