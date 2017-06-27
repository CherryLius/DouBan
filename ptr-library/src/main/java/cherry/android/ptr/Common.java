package cherry.android.ptr;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/6/27.
 */

public interface Common {
    int STATE_IDLE = 0;
    int STATE_PULL_TO_REFRESH = 1;
    int STATE_RELEASE_TO_REFRESH = 2;
    int STATE_REFRESHING = 3;
    int STATE_COMPLETE = 4;

    @IntDef({STATE_IDLE,
            STATE_PULL_TO_REFRESH,
            STATE_RELEASE_TO_REFRESH,
            STATE_REFRESHING,
            STATE_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }
}
