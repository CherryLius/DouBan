package cherry.android.douban.mine;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.common.ui.ToolbarFragment;
import cherry.android.douban.util.CompatUtils;

public class MineFragment extends ToolbarFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_settings)
    ImageView settingsView;
    @BindView(R.id.iv_search)
    ImageView searchView;
    @BindView(R.id.iv_satisfied)
    ImageView satisfiedView;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initToolbar(getContext());
    }

    void initToolbar(Context context) {
        Drawable drawable = CompatUtils.getDrawable(context, R.drawable.ic_settings_black_24dp, Color.WHITE);
        settingsView.setImageDrawable(drawable);
        drawable = CompatUtils.getDrawable(context, R.drawable.ic_search_black_24dp, Color.WHITE);
        searchView.setImageDrawable(drawable);
        drawable = CompatUtils.getDrawable(context, R.drawable.ic_sentiment_very_satisfied_black_24dp, Color.WHITE);
        satisfiedView.setImageDrawable(drawable);
    }
}
