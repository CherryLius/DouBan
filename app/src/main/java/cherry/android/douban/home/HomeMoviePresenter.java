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
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HomeMoviePresenter extends RxPresenterImpl<HomeMovieContract.View, HomeMovieContract.Presenter, FragmentEvent>
        implements HomeMovieContract.Presenter {
    private static final String TAG = "HomeMoviePresenter";
    private List<Movie> mMovieList;

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
    public void loadMovies(boolean isComingSoon, final int start, final int count) {
        Observable<TheaterMovie> observable;
        if (isComingSoon) {
            observable = Network.get().getMovieApi().comingSoon(start, count);
        } else {
            observable = Network.get().getMovieApi().movieInTheaters(start, count);
        }
        observable.compose(RxHelper.<TheaterMovie>mainIO())
                .compose(mRxLifecycle.<TheaterMovie>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(observableMovie(start, count))
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Movie> movies) {
                        if (mMovieList == null || mMovieList.size() == 0) {
                            mMovieList = movies;
                        } else {
                            mMovieList.addAll(movies);
                        }
                        mView.showMovies(mMovieList);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Logger.e(TAG, "onError", e);
                        mView.showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void refreshMovies(boolean isComingSoon) {
        Observable<TheaterMovie> observable;
        int count = mMovieList == null ? 0 : mMovieList.size();
        if (isComingSoon) {
            observable = Network.get().getMovieApi().comingSoon(0, count);
        } else {
            observable = Network.get().getMovieApi().movieInTheaters(0, count);
        }
        observable.compose(RxHelper.<TheaterMovie>mainIO())
                .compose(mRxLifecycle.<TheaterMovie>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(observableMovie(0, count))
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Movie> movies) {
                        if (mMovieList == null || mMovieList.size() == 0) {
                            mMovieList = movies;
                        } else {
                            mMovieList.clear();
                            mMovieList.addAll(movies);
                        }
                        mView.showMovies(mMovieList);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        mView.showError();
                        Logger.e(TAG, "onError", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private ObservableTransformer<TheaterMovie, List<Movie>> observableMovie(final int start, final int count) {
        return new ObservableTransformer<TheaterMovie, List<Movie>>() {
            @Override
            public ObservableSource<List<Movie>> apply(@io.reactivex.annotations.NonNull Observable<TheaterMovie> upstream) {
                return upstream.map(new Function<TheaterMovie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie) throws Exception {
                        if (theaterMovie.getTotal() <= (start + count))
                            mView.showNoMoreMovie();
                        return theaterMovie.getMovies();
                    }
                });
            }
        };
    }
}
