package cherry.android.douban.detail;

import android.text.TextUtils;

import cherry.android.douban.model.Movie;
import cherry.android.douban.network.Network;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/5.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private static final String TAG = "MovieDetailPresenter";

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
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Movie movie) {
                        mView.showMovie(movie);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.e(TAG, "onError", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
