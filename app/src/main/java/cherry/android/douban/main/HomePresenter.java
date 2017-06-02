package cherry.android.douban.main;

import android.support.annotation.NonNull;

import java.util.List;

import cherry.android.douban.model.Movie;
import cherry.android.douban.model.TheaterMovie;
import cherry.android.douban.network.Network;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HomePresenter implements HomeContract.Presenter {

    HomeContract.View mView;

    public HomePresenter(@NonNull HomeContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void loadMovieInTheater() {
        Network.instance().getMovieApi().movieInTheaters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TheaterMovie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie) throws Exception {
                        return theaterMovie.getMovies();
                    }
                })
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Movie> movies) throws Exception {
                        mView.showTheaterMovie(movies);
                    }
                });
    }

    @Override
    public void loadComingSoon() {
        Network.instance().getMovieApi().comingSoon(20, 40)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TheaterMovie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie) throws Exception {
                        return theaterMovie.getMovies();
                    }
                })
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Movie> movies) throws Exception {
                        mView.showComingSoon(movies);
                    }
                });
    }
}
