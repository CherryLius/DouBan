package cherry.android.douban.base;

import android.view.View;

/**
 * Created by Administrator on 2017/6/7.
 */

public interface BaseHeader<T> {
    View getItemView();

    void updateHeader(T data);
}
