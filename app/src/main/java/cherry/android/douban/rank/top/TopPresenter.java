package cherry.android.douban.rank.top;

import android.support.annotation.NonNull;

import java.util.List;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.TheaterMovie;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by LHEE on 2017/6/19.
 */

public class TopPresenter extends RxPresenterImpl<TopContract.View,
        TopContract.Presenter, ActivityEvent> implements TopContract.Presenter {

    private List<Movie> mMovieList;

    public TopPresenter(@NonNull TopContract.View view, @NonNull IRxLifecycleBinding<ActivityEvent> lifecycle) {
        super(view, lifecycle);
    }

    @NonNull
    @Override
    protected TopContract.Presenter getPresenter() {
        return this;
    }

    @Override
    public void loadTop(int start, int count) {
        Network.get().getMovieApi().top250(start, count)
                .compose(RxHelper.<TheaterMovie>mainIO())
                .compose(mRxLifecycle.<TheaterMovie>bindUntilEvent(ActivityEvent.DESTROY))
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
                        mView.showTopMovies(mMovieList);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void refreshMovies() {
        int count = mMovieList == null ? 0 : mMovieList.size();
        Network.get().getMovieApi().top250(0, count)
                .compose(RxHelper.<TheaterMovie>mainIO())
                .compose(mRxLifecycle.<TheaterMovie>bindUntilEvent(ActivityEvent.DESTROY))
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
                        mView.showTopMovies(mMovieList);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<Movie> getCurrentMovies() {
        return mMovieList;
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
