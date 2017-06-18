package cherry.android.douban.rank;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.RankMovies;

/**
 * Created by LHEE on 2017/6/18.
 */

public interface RankContract {
    interface View extends BaseView<Presenter> {
        void showMovies(List<RankMovies> movies);
    }

    interface Presenter extends BasePresenter {
        void loadMovies();

        List<RankMovies> getCurrentData();
    }
}
