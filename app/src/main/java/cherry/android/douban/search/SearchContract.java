package cherry.android.douban.search;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.Movie;

/**
 * Created by ROOT on 2017/7/20.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showHotSearch(String[] hots);

        void showQuery(List<Movie> movies);
    }

    interface Presenter extends BasePresenter {
        void search(String keyWords);
    }
}
