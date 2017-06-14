package cherry.android.douban.rx;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * Created by Administrator on 2017/6/14.
 */

public interface IRxLifecycleBinding<E> {
    <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull Observable<E> lifecycle,
                                                   @NonNull E event);

    <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull E event);
}
