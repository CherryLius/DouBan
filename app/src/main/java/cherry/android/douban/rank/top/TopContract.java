package cherry.android.douban.rank.top;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.Movie;

/**
 * Created by LHEE on 2017/6/19.
 */

public interface TopContract {
    interface View extends BaseView<Presenter> {
        void showTopMovies(List<Movie> movies);
        void showNoMoreMovie();
    }

    interface Presenter extends BasePresenter {
        void loadTop(int start, int count);
        void refreshMovies();
        List<Movie> getCurrentMovies();
    }
}
