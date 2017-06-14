package cherry.android.douban.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Administrator on 2017/6/2.
 */

public abstract class BaseActivity extends AppCompatActivity implements IRxLifecycleBinding<ActivityEvent> {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        super.onCreate(savedInstanceState);
        setContentView(getViewLayoutId());
        onViewInflated();
        registerListener();
    }

    @Override
    protected void onStart() {
        lifecycleSubject.onNext(ActivityEvent.START);
        super.onStart();
    }

    @Override
    protected void onResume() {
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        unregisterListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull ActivityEvent event) {
        return bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull Observable<ActivityEvent> lifecycle,
                                                          @NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycle, event);
    }

    @LayoutRes
    protected abstract int getViewLayoutId();

    protected abstract void onViewInflated();

    protected abstract void registerListener();

    protected abstract void unregisterListener();
}
