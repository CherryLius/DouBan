package cherry.android.douban.search;

import android.support.annotation.NonNull;

import cherry.android.douban.App;
import cherry.android.douban.R;
import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.QueryMovie;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import io.reactivex.functions.Consumer;

/**
 * Created by ROOT on 2017/7/20.
 */

public class SearchPresenter extends RxPresenterImpl<SearchContract.View, SearchContract.Presenter, ActivityEvent>
        implements SearchContract.Presenter {

    public SearchPresenter(@NonNull SearchContract.View view, @NonNull IRxLifecycleBinding<ActivityEvent> lifecycle) {
        super(view, lifecycle);
        String[] hots = App.getContext().getResources().getStringArray(R.array.hot_array);
        mView.showHotSearch(hots);
    }

    @NonNull
    @Override
    protected SearchContract.Presenter getPresenter() {
        return this;
    }

    @Override
    public void search(String keyWords) {
        Network.get().getMovieApi().searchMovie(keyWords).compose(RxHelper.<QueryMovie>mainIO())
                .compose(mRxLifecycle.<QueryMovie>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<QueryMovie>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull QueryMovie queryMovie) throws Exception {
                        Logger.d("Test", queryMovie.getMovies().toString());
                        mView.showQuery(queryMovie.getMovies());
                    }
                });
    }
}
