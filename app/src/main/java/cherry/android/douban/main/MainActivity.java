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

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.home.HomeFragment;
import cherry.android.douban.mine.MineFragment;
import cherry.android.douban.rank.RankFragment;
import cherry.android.router.annotations.Route;
import cherry.android.toast.Toaster;

@Route("/activity/main")
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private List<Fragment> mFragmentList;
    private long mLastPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView.setSelectedItemId(R.id.menu_hot);
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
        mFragmentList.add(new MineFragment());
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

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toaster.warning(this, "再按一次退出应用").show();
            mLastPressedTime = System.currentTimeMillis();
        }
    }
}
