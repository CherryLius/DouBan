package cherry.android.douban.sticker;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Administrator on 2017/6/13.
 */

public interface IStickyHeader {
    @NonNull
    View getStickyView();

    float thresholdValue();

    @NonNull
    View getStickyRelatedView();
}
