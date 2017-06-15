package cherry.android.douban.home;

import android.support.annotation.NonNull;

import java.util.List;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.TheaterMovie;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.FragmentEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HomeMoviePresenter extends RxPresenterImpl<HomeMovieContract.View, HomeMovieContract.Presenter, FragmentEvent>
        implements HomeMovieContract.Presenter {
    private static final String TAG = "HomeMoviePresenter";

    public HomeMoviePresenter(@NonNull HomeMovieContract.View view,
                              @NonNull IRxLifecycleBinding<FragmentEvent> lifecycle) {
        super(view, lifecycle);
    }

    @NonNull
    @Override
    protected HomeMovieContract.Presenter getPresenter() {
        return this;
    }

    @Override
    public void loadMovieInTheater() {
        Network.instance().getMovieApi().movieInTheaters()
                .compose(RxHelper.<TheaterMovie>mainIO())
                .compose(mRxLifecycle.<TheaterMovie>bindUntilEvent(FragmentEvent.DESTROY))
                .map(new Function<TheaterMovie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie) throws Exception {
                        return theaterMovie.getMovies();
                    }
                })
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Movie> movies) {
                        mView.showTheaterMovie(movies);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Logger.e(TAG, "onError", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadComingSoon() {
        Network.instance().getMovieApi().comingSoon(0, 40)
                .compose(RxHelper.<TheaterMovie>mainIO())
                .compose(mRxLifecycle.<TheaterMovie>bindUntilEvent(FragmentEvent.DESTROY))
                .map(new Function<TheaterMovie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie) throws Exception {
                        return theaterMovie.getMovies();
                    }
                })
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Movie> movies) {
                        mView.showComingSoon(movies);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Logger.e(TAG, "onError", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
