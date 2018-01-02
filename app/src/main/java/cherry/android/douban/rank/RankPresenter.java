package cherry.android.douban.rank;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.NorthAmericaMovie;
import cherry.android.douban.model.TheaterMovie;
import cherry.android.douban.network.Network;
import cherry.android.douban.network.api.MovieApi;
import cherry.android.douban.rx.FragmentEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * Created by LHEE on 2017/6/18.
 */

public class RankPresenter extends RxPresenterImpl<RankContract.View,
        RankContract.Presenter,
        FragmentEvent> implements RankContract.Presenter {

    private List<Object> mList;

    public RankPresenter(@NonNull RankContract.View view, @NonNull IRxLifecycleBinding<FragmentEvent> lifecycle) {
        super(view, lifecycle);
    }

    @NonNull
    @Override
    protected RankContract.Presenter getPresenter() {
        return this;
    }

    @Override
    public void loadMovies() {
        zipObservable()
                .compose(RxHelper.<List<Object>>delay(2000))
                .compose(RxHelper.<List<Object>>mainIO())
                .compose(mRxLifecycle.<List<Object>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Observer<List<Object>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Object> rankMovies) {
                        mList = rankMovies;
                        mView.showMovies(rankMovies);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Logger.e("Test", "Error", e);
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

    private Observable<List<Object>> zipObservable() {
        MovieApi api = Network.get().getMovieApi();
        Observable<List<Object>> observable = Observable.zip(api.top250(0, 6),
                api.northAmericaMovie(), new BiFunction<TheaterMovie, NorthAmericaMovie, List<Object>>() {
                    @Override
                    public List<Object> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie,
                                              @io.reactivex.annotations.NonNull NorthAmericaMovie northAmericaMovie) throws Exception {
                        List<Object> list = new ArrayList<>();
                        list.add("Top250");
                        list.addAll(theaterMovie.getMovies());
                        list.add("北美票房榜");
                        list.add(northAmericaMovie);
                        list.addAll(northAmericaMovie.getSubjects());
                        return list;
                    }
                });
        return observable;
    }
}
