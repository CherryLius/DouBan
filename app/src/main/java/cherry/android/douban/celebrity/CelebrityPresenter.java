package cherry.android.douban.celebrity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CelebrityPresenter extends RxPresenterImpl<CelebrityContract.View, CelebrityContract.Presenter, ActivityEvent>
        implements CelebrityContract.Presenter {

    private List<Object> mList = new ArrayList<>();

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
                .map(new Function<MovieCelebrity, List<MovieCelebrity.Works>>() {
                    @Override
                    public List<MovieCelebrity.Works> apply(@io.reactivex.annotations.NonNull MovieCelebrity movieCelebrity) throws Exception {
                        mView.showCelebrityInfo(movieCelebrity);
                        return movieCelebrity.getWorks();
                    }
                })
                .subscribe(new Observer<List<MovieCelebrity.Works>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<MovieCelebrity.Works> works) {
                        if (works != null && works.size() > 0) {
                            mList.clear();
                            mList.add("电影作品(" + works.size() + ")");
                            mList.addAll(works);
                            mView.showWorks(mList);
                        }
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

    @Override
    public List getCurrentData() {
        return mList;
    }

    @NonNull
    @Override
    protected CelebrityContract.Presenter getPresenter() {
        return this;
    }
}
