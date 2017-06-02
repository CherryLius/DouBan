package cherry.android.douban.main;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.Movie;

/**
 * Created by Administrator on 2017/6/2.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void showTheaterMovie(List<Movie> movies);

        void showComingSoon(List<Movie> movies);
    }

    interface Presenter extends BasePresenter {
        void loadMovieInTheater();

        void loadComingSoon();
    }
}
