package cherry.android.douban.detail;

import android.text.TextUtils;

import cherry.android.douban.model.Movie;
import cherry.android.douban.network.Network;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/5.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    MovieDetailContract.View mView;

    MovieDetailPresenter(MovieDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadMovieDetail(String id) {
        if (TextUtils.isEmpty(id))
            return;
        Network.instance().getMovieApi().movieInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(@NonNull Movie movie) throws Exception {
                        mView.showMovie(movie);
                    }
                });
    }
}
