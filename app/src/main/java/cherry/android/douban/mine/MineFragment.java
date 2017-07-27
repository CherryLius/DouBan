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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cherry.android.douban.R;
import cherry.android.douban.common.ui.ToolbarFragment;
import cherry.android.douban.mine.holder.HeaderHolder;
import cherry.android.douban.mine.holder.SimpleItemHolder;
import cherry.android.douban.route.MovieRouter;
import cherry.android.douban.util.CompatUtils;
import cherry.android.douban.widget.CustomGridItem;
import cherry.android.douban.widget.CustomGridLayout;
import cherry.android.router.api.Router;

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
        SimpleItemHolder item = SimpleItemHolder.newItem(getContext())
                .setName(getString(R.string.label_e_wallet))
                .setNumber(getString(R.string.prefix_money, 0));

        infoLayout.addItem(item.getItemView());
        item = SimpleItemHolder.newItem(getContext())
                .setName(getString(R.string.label_coupon))
                .setNumber(getString(R.string.prefix_pieces, 0));

        infoLayout.addItem(item.getItemView());
        item = SimpleItemHolder.newItem(getContext())
                .setName(getString(R.string.label_accumulate))
                .setNumber(getString(R.string.prefix_score, 0));

        infoLayout.addItem(item.getItemView());
    }

    private void initMyOrderLayout() {
        HeaderHolder head = new HeaderHolder(getContext());
        myOrderLayout.setColumn(3);
        myOrderLayout.setHeader(head.getItemView());

        CustomGridItem item = new CustomGridItem(getContext(), getString(R.string.label_wait_to_pay), R.mipmap.ic_my_payment);
        myOrderLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_wait_to_take), R.mipmap.ic_my_ed);
        myOrderLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_wait_to_comment), R.mipmap.ic_my_evaluation);
        myOrderLayout.addItem(item);

    }

    private void initOtherOrderLayout() {
        HeaderHolder head = new HeaderHolder(getContext());
        head.setTitle(getString(R.string.label_other_order)).setMoreVisbility(false);
        otherOrderLayout.setColumn(3);
        otherOrderLayout.setHeader(head.getItemView());

        CustomGridItem item = new CustomGridItem(getContext(), getString(R.string.label_order_movie), R.mipmap.ic_my_order_movie);
        otherOrderLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_order_play), R.mipmap.ic_my_order_play);
        otherOrderLayout.addItem(item);
    }

    private void initTicketLayout() {
        HeaderHolder head = new HeaderHolder(getContext());
        head.setTitle(getString(R.string.label_ticket_wallet));
        ticketLayout.setColumn(3);
        ticketLayout.setHeader(head.getItemView());

        CustomGridItem item = new CustomGridItem(getContext(), getString(R.string.label_never_used), R.mipmap.ic_my_order_ticket);
        ticketLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_roll_in), R.mipmap.ic_my_into);
        ticketLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.label_roll_out), R.mipmap.ic_my_roll_out);
        ticketLayout.addItem(item);
    }

    private void initOtherLayout() {
        otherLayout.setColumn(3);
        CustomGridItem item = new CustomGridItem(getContext(), getString(R.string.address_manage), R.mipmap.ic_my_location);
        otherLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.vip_center), R.mipmap.ic_my_vip);
        otherLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.frequently_actor), R.mipmap.ic_my_linkman);
        otherLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.subscribe), R.mipmap.ic_my_subscribe);
        otherLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.collection), R.mipmap.ic_my_clllection);
        otherLayout.addItem(item);
        item = new CustomGridItem(getContext(), getString(R.string.online_server), R.mipmap.ic_subnav_service_icon);
        otherLayout.addItem(item);
    }

    @OnClick({R.id.iv_search})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
//                Router.build("movie://activity/search").open();
                MovieRouter.get().getRouteService().startSearchActivity();
                break;
        }
    }
}
