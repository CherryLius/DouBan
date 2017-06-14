package cherry.android.douban.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.common.ui.ToolbarFragment;

/**
 * Created by Administrator on 2017/6/13.
 */

public class RankFragment extends ToolbarFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_rank;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
