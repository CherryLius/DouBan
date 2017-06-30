package cherry.android.douban.mine;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.common.ui.ToolbarFragment;
import cherry.android.douban.util.CompatUtils;
import cherry.android.douban.widget.CustomGridItem;
import cherry.android.douban.widget.CustomGridLayout;

public class MineFragment extends ToolbarFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_settings)
    ImageView settingsView;
    @BindView(R.id.iv_search)
    ImageView searchView;
    @BindView(R.id.iv_satisfied)
    ImageView satisfiedView;

    @BindView(R.id.grid_info)
    CustomGridLayout infoLayout;
    @BindView(R.id.grid_my_order)
    CustomGridLayout myOrderLayout;
    @BindView(R.id.grid_other_order)
    CustomGridLayout otherOrderLayout;
    @BindView(R.id.grid_ticket)
    CustomGridLayout ticketLayout;
    @BindView(R.id.grid_other)
    CustomGridLayout otherLayout;

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
        initInfoLayout();
        initMyOrderLayout();
        initOtherOrderLayout();
        initTicketLayout();
        initOtherLayout();
    }

    void initToolbar(Context context) {
        Drawable drawable = CompatUtils.getDrawable(context, R.drawable.ic_settings_black_24dp, Color.WHITE);
        settingsView.setImageDrawable(drawable);
        drawable = CompatUtils.getDrawable(context, R.drawable.ic_search_black_24dp, Color.WHITE);
        searchView.setImageDrawable(drawable);
        drawable = CompatUtils.getDrawable(context, R.drawable.ic_sentiment_very_satisfied_black_24dp, Color.WHITE);
        satisfiedView.setImageDrawable(drawable);
    }

    void initInfoLayout() {
        infoLayout.setColumn(3);

        SimpleItem item = SimpleItem.newItem(getContext())
                .setName(getString(R.string.label_e_wallet))
                .setNumber(getString(R.string.prefix_money, 0));

        infoLayout.addItem(item.itemView);
        item = SimpleItem.newItem(getContext())
                .setName(getString(R.string.label_coupon))
                .setNumber(getString(R.string.prefix_pieces, 0));

        infoLayout.addItem(item.itemView);
        item = SimpleItem.newItem(getContext())
                .setName(getString(R.string.label_accumulate))
                .setNumber(getString(R.string.prefix_score, 0));

        infoLayout.addItem(item.itemView);
    }

    private void initMyOrderLayout() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mine_grid_header, null);
        myOrderLayout.setColumn(3);
        myOrderLayout.setHeader(headerView);

        CustomGridItem item = new CustomGridItem(getContext(), getString(R.string.label_wait_to_pay), R.mipmap.ic_my_payment);
        myOrderLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_wait_to_take), R.mipmap.ic_my_ed);
        myOrderLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_wait_to_comment), R.mipmap.ic_my_evaluation);
        myOrderLayout.addItem(item);

    }

    private void initOtherOrderLayout() {
    }

    private void initTicketLayout() {
    }

    private void initOtherLayout() {
    }

    static class SimpleItem {

        View itemView;
        @BindView(R.id.tv_number)
        TextView numView;
        @BindView(R.id.tv_name)
        TextView nameView;

        private SimpleItem(Context context) {
            itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_simple_info, null, false);
            ButterKnife.bind(this, itemView);
        }

        public SimpleItem setNumber(CharSequence charSequence) {
            numView.setText(charSequence);
            return this;
        }

        public SimpleItem setName(CharSequence charSequence) {
            nameView.setText(charSequence);
            return this;
        }

        private static SimpleItem newItem(Context context) {
            return new SimpleItem(context);
        }
    }
}
