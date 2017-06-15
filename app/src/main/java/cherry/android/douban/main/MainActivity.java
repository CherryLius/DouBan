package cherry.android.douban.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.home.HomeFragment;
import cherry.android.douban.rank.RankFragment;
import cherry.android.douban.rx.ActivityEvent;
import cherry.android.douban.util.Logger;
import cherry.android.router.annotations.Route;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@Route("movie://activity/main")
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView.setSelectedItemId(R.id.menu_hot);
        Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        Logger.i("Test", " along=" + aLong);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.e("Test", "onPause");
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        initFragmentList();
    }

    @Override
    protected void registerListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void unregisterListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
    }

    private void initFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new RankFragment());
        mFragmentList.add(new EmptyFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hot:
                showFragment(0);
                return true;
            case R.id.menu_rank:
                showFragment(1);
                return true;
            case R.id.menu_mine:
                showFragment(2);
                return true;
        }
        return false;
    }

    private void showFragment(int position) {
        Fragment fragment = mFragmentList.get(position);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            ft.add(R.id.fragment_container, fragment);
        }

        for (int i = 0; i < mFragmentList.size(); i++) {
            if (i == position) continue;
            ft.hide(mFragmentList.get(i));
        }

        ft.show(fragment).commit();
    }

}
