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

public class SimpleItemHolder {
    View itemView;
    @BindView(R.id.tv_number)
    TextView numView;
    @BindView(R.id.tv_name)
    TextView nameView;

    private SimpleItemHolder(Context context) {
        itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_simple_info, null, false);
        ButterKnife.bind(this, itemView);
    }

    public View getItemView() {
        return itemView;
    }

    public SimpleItemHolder setNumber(CharSequence charSequence) {
        numView.setText(charSequence);
        return this;
    }

    public SimpleItemHolder setName(CharSequence charSequence) {
        nameView.setText(charSequence);
        return this;
    }

    public static SimpleItemHolder newItem(Context context) {
        return new SimpleItemHolder(context);
    }
}
