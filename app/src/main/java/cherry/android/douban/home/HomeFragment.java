package cherry.android.douban.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.adapter.HomePageAdapter;
import cherry.android.douban.base.BaseFragment;
import cherry.android.douban.common.Constants;
import cherry.android.router.annotations.Route;

/**
 * Created by Administrator on 2017/6/2.
 */
@Route("movie://fragment/home")
public class HomeFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    AppCompatActivity mActivity;

    private HomePageAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    void init() {
        List<HomeMovieFragment> list = new ArrayList<>(2);
        list.add(HomeMovieFragment.newInstance(Constants.TAB_IN_THEATER));
        list.add(HomeMovieFragment.newInstance(Constants.TAB_COMING_SOON));
        mPagerAdapter = new HomePageAdapter(this.getChildFragmentManager(), list);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (toolbar == null)
            return;
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
            mActivity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity != null) {
            mActivity.setSupportActionBar(null);
            mActivity = null;
        }
    }
}
