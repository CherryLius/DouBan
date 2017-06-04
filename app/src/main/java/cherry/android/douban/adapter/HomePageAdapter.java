package cherry.android.douban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cherry.android.douban.home.HomeMovieFragment;

/**
 * Created by LHEE on 2017/6/3.
 */

public class HomePageAdapter extends FragmentStatePagerAdapter {

    private List<HomeMovieFragment> mList;

    public HomePageAdapter(FragmentManager fm, List<HomeMovieFragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTab();
    }
}
