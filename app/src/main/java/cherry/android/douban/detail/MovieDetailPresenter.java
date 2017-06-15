package cherry.android.douban.detail;

import android.text.TextUtils;

import java.io.IOException;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.Movie;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/6/5.
 */

public class MovieDetailPresenter extends RxPresenterImpl<MovieDetailContract.View,
        MovieDetailContract.Presenter,
        ActivityEvent>
        implements MovieDetailContract.Presenter {
    private static final String TAG = "MovieDetailPresenter";

    public MovieDetailPresenter(@android.support.annotation.NonNull MovieDetailContract.View view,
                                @android.support.annotation.NonNull IRxLifecycleBinding<ActivityEvent> lifecycle) {
        super(view, lifecycle);
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
        Network.instance().getMovieApi().movieInfo(id, "07c78782db00a121175696889101e363")
                .compose(RxHelper.<Movie>mainIO())
                .compose(mRxLifecycle.<Movie>bindUntilEvent(ActivityEvent.DESTROY))
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
        Network.instance().getMovieApi().moviePhotoInfo(id, "07c78782db00a121175696889101e363")
                .compose(RxHelper.<ResponseBody>mainIO())
                .compose(mRxLifecycle.<ResponseBody>bindUntilEvent(ActivityEvent.DESTROY))
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
                .compose(RxHelper.<ResponseBody>mainIO())
                .compose(mRxLifecycle.<ResponseBody>bindUntilEvent(ActivityEvent.DESTROY))
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
