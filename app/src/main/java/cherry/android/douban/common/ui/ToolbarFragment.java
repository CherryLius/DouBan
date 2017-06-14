package cherry.android.douban.common.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cherry.android.douban.base.BaseFragment;

/**
 * Created by Administrator on 2017/6/14.
 */

public abstract class ToolbarFragment extends BaseFragment {

    private AppCompatActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getToolbar() == null)
            return;
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
            mActivity.setSupportActionBar(getToolbar());
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

    protected abstract Toolbar getToolbar();

}
