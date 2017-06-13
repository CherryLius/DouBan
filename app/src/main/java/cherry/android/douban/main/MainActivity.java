package cherry.android.douban.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import cherry.android.douban.rank.RankFragment;
import cherry.android.router.annotations.Route;

@Route("movie://activity/main")
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragmentList();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_hot);
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
