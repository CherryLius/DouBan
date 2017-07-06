package cherry.android.douban.home;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Random;

import cherry.android.douban.base.RxPresenterImpl;
import cherry.android.douban.model.GankData;
import cherry.android.douban.network.Network;
import cherry.android.douban.rx.FragmentEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxHelper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/7/6.
 */

public class HomePresenter extends RxPresenterImpl
        <HomeContract.View, HomeContract.Presenter, FragmentEvent> implements HomeContract.Presenter {

    public HomePresenter(@NonNull HomeContract.View view, @NonNull IRxLifecycleBinding<FragmentEvent> lifecycle) {
        super(view, lifecycle);
    }

    @NonNull
    @Override
    protected HomeContract.Presenter getPresenter() {
        return this;
    }

    @Override
    public void loadBanner() {
        Random random = new Random();
        Network.get().getGankIOApi().gank(6, 1 + random.nextInt(5)).compose(RxHelper.<GankData>mainIO())
                .compose(mRxLifecycle.<GankData>bindUntilEvent(FragmentEvent.DESTROY))
                .map(new Function<GankData, List<GankData.Result>>() {
                    @Override
                    public List<GankData.Result> apply(@io.reactivex.annotations.NonNull GankData gankData) throws Exception {
                        return gankData.getResults();
                    }
                })
                .subscribe(new Observer<List<GankData.Result>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<GankData.Result> results) {
                        mView.showBanner(results);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
