package cherry.android.douban.home;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.GankData;

/**
 * Created by Administrator on 2017/7/6.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void showBanner(List<GankData.Result> list);
    }

    interface Presenter extends BasePresenter {
        void loadBanner();
    }
}
