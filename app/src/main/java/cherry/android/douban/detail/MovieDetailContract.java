package cherry.android.douban.detail;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.Movie;

/**
 * Created by Administrator on 2017/6/5.
 */

public interface MovieDetailContract {

    interface View extends BaseView<Presenter> {
        void showMovie(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void loadMovieDetail(String id);

        void loadMoviePhotos(String id);
    }
}
