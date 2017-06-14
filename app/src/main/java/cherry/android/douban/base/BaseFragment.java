package cherry.android.douban.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cherry.android.douban.rx.FragmentEvent;
import cherry.android.douban.rx.IRxLifecycleBinding;
import cherry.android.douban.rx.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Administrator on 2017/6/2.
 */

public abstract class BaseFragment extends Fragment implements IRxLifecycleBinding<FragmentEvent> {

    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        return inflater.inflate(getViewLayoutId(), container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleSubject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleSubject.onNext(FragmentEvent.DETACH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull FragmentEvent event) {
        return bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull Observable<FragmentEvent> lifecycle,
                                                          @NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycle, event);
    }

    @LayoutRes
    protected abstract int getViewLayoutId();
}
