package cherry.android.douban.base;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/6/2.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(@NonNull T presenter);
}
