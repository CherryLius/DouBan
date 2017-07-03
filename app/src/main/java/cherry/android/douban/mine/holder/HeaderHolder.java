package cherry.android.douban.mine.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;

/**
 * Created by LHEE on 2017/6/30.
 */

public class HeaderHolder {
    private View itemView;
    @BindView(R.id.tv_title)
    TextView titleView;
    @BindView(R.id.tv_more)
    TextView moreView;

    public HeaderHolder(Context context) {
        itemView = LayoutInflater.from(context).inflate(R.layout.layout_mine_grid_header, null);
        ButterKnife.bind(this, itemView);
    }

    public View getItemView() {
        return itemView;
    }

    public HeaderHolder setTitle(CharSequence charSequence) {
        titleView.setText(charSequence);
        return this;
    }

    public HeaderHolder setMoreVisbility(boolean visible) {
        moreView.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

}
