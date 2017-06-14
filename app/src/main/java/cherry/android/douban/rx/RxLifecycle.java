package cherry.android.douban.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by Administrator on 2017/6/14.
 */

public final class RxLifecycle {
    public static <T, E> ObservableTransformer<T, T> bindUntilEvent(@NonNull final Observable<E> lifecycle,
                                                                    @NonNull final E event) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.takeUntil(takeUntilEvent(lifecycle, event));
            }
        };
    }

    private static <E> Observable<E> takeUntilEvent(@NonNull Observable<E> lifecycle,
                                                    @NonNull final E event) {
        return lifecycle.filter(new Predicate<E>() {
            @Override
            public boolean test(@NonNull E e) throws Exception {
                return event.equals(e);
            }
        });
    }
}
