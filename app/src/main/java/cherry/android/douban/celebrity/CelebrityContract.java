package cherry.android.douban.celebrity;

import android.support.annotation.NonNull;

import java.util.List;

import cherry.android.douban.base.BasePresenter;
import cherry.android.douban.base.BaseView;
import cherry.android.douban.model.MovieCelebrity;

/**
 * Created by Administrator on 2017/6/7.
 */

public interface CelebrityContract {

    interface View extends BaseView<Presenter> {
        void showCelebrityInfo(MovieCelebrity celebrity);
        void showWorks(List<?> works);
    }

    interface Presenter extends BasePresenter {
        void loadCelebrityInfo(@NonNull String id);
        List getCurrentData();
    }
}
