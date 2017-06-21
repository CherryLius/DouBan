package cherry.android.douban.home;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.Movie;

/**
 * Created by Administrator on 2017/6/2.
 */

public interface HomeMovieContract {

    interface View extends BaseView<Presenter> {

        void showMovies(List<Movie> movies);

        void showNoMoreMovie();

        void showError();
    }

    interface Presenter extends BasePresenter {
        void loadMovies(boolean isComingSoon, int start, int count);

        void refreshMovies(boolean isComingSoon);
    }
}
