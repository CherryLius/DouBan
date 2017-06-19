package cherry.android.douban.rank;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;

/**
 * Created by LHEE on 2017/6/18.
 */

public interface RankContract {
    interface View extends BaseView<Presenter> {
        void showMovies(List movies);
    }

    interface Presenter extends BasePresenter {
        void loadMovies();

        List getCurrentData();
    }
}
