package cherry.android.douban.rank;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.NorthAmericaMovie;
import cherry.android.douban.model.RankMovies;
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

    private List<RankMovies> mList;

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
        zipObservable().compose(RxHelper.<List<RankMovies>>mainIO())
                .compose(mRxLifecycle.<List<RankMovies>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Observer<List<RankMovies>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<RankMovies> rankMovies) {
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
    public List<RankMovies> getCurrentData() {
        return mList;
    }

    private Observable<List<RankMovies>> zipObservable() {
        MovieApi api = Network.get().getMovieApi();
//        Observable<List<RankMovies>> observable = Observable.zip(api.top250(0, 5),
//                api.northAmericaMovie(),
//                api.weekly(),
//                api.newMovies(),
//                new Function4<TheaterMovie, NorthAmericaMovie, NewWeeklyMovie, NewWeeklyMovie, List<RankMovies>>() {
//                    @Override
//                    public List<RankMovies> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie,
//                                                  @io.reactivex.annotations.NonNull NorthAmericaMovie northAmericaMovie,
//                                                  @io.reactivex.annotations.NonNull NewWeeklyMovie newWeeklyMovie,
//                                                  @io.reactivex.annotations.NonNull NewWeeklyMovie newWeeklyMovie2) throws Exception {
//                        List<RankMovies> list = new ArrayList<>();
//                        list.add(new RankMovies("Top250", RankMovies.TYPE_TITLE));
//                        list.add(new RankMovies(theaterMovie.getMovies(), RankMovies.TYPE_TOP_250));
//                        list.add(new RankMovies("口碑榜", RankMovies.TYPE_TITLE));
//                        list.add(new RankMovies(newWeeklyMovie.getMovies(), RankMovies.TYPE_WEEKLY));
//                        list.add(new RankMovies("北美票房榜", RankMovies.TYPE_TITLE));
//                        list.add(new RankMovies(northAmericaMovie.getMovies(), RankMovies.TYPE_NORTH_AMERICA));
//                        list.add(new RankMovies("新片榜", RankMovies.TYPE_TITLE));
//                        list.add(new RankMovies(newWeeklyMovie2.getMovies(), RankMovies.TYPE_NEW_MOVIES));
//                        return list;
//                    }
//                });
        Observable<List<RankMovies>> observable = Observable.zip(api.top250(0, 6),
                api.northAmericaMovie(), new BiFunction<TheaterMovie, NorthAmericaMovie, List<RankMovies>>() {
                    @Override
                    public List<RankMovies> apply(@io.reactivex.annotations.NonNull TheaterMovie theaterMovie,
                                                  @io.reactivex.annotations.NonNull NorthAmericaMovie northAmericaMovie) throws Exception {
                        List<RankMovies> list = new ArrayList<>();
                        list.add(new RankMovies("Top250", RankMovies.TYPE_TITLE));
                        list.addAll(movieToRankMovie(theaterMovie.getMovies(), RankMovies.TYPE_TOP_250));
                        list.add(new RankMovies("北美票房榜", RankMovies.TYPE_TITLE));
                        for (NorthAmericaMovie.Subjects box : northAmericaMovie.getSubjects()) {
                            list.add(new RankMovies(box.getMovie(), RankMovies.TYPE_NORTH_AMERICA));
                        }
                        Logger.i("Test", "biFunc=" + list.size());
                        return list;
                    }
                });
        return observable;
    }

    private static List<RankMovies> movieToRankMovie(List<Movie> movies, int type) {
        List<RankMovies> list = new ArrayList<>(movies.size());
        for (int i = 0; i < movies.size(); i++) {
            list.add(new RankMovies(movies.get(i), type));
        }
        return list;
    }
}
