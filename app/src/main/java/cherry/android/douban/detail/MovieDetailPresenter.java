package cherry.android.douban.detail;

import android.text.TextUtils;

import java.io.IOException;

import cherry.android.douban.base.AbstractPresenterImpl;
import cherry.android.douban.model.Movie;
import cherry.android.douban.network.Network;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/6/5.
 */

public class MovieDetailPresenter extends AbstractPresenterImpl<MovieDetailContract.View, MovieDetailContract.Presenter>
        implements MovieDetailContract.Presenter {
    private static final String TAG = "MovieDetailPresenter";

    MovieDetailPresenter(MovieDetailContract.View view) {
        super(view);
    }

    @android.support.annotation.NonNull
    @Override
    protected MovieDetailContract.Presenter getPresenter() {
        return this;
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

    @Override
    public void loadMoviePhotos(String id) {
        if (TextUtils.isEmpty(id))
            return;
        Network.instance().getMovieApi().moviePhotoInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Logger.i("Test", responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Network.instance().getMovieApi().movieReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Logger.i("Test", "reviews=" + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
