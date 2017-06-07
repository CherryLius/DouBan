package cherry.android.douban.detail.delegate;

import android.view.View;
import android.widget.TextView;

import cherry.android.douban.recycler.ItemViewDelegate;
import cherry.android.douban.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/7.
 */

public class SimpleDelegate implements ItemViewDelegate<String, ViewHolder> {

    @Override
    public int getViewLayoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public boolean isMatchViewType(String s, int position) {
        return position != 0 && position != 1;
    }

    @Override
    public ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        TextView textView = holder.findView(android.R.id.text1);
        textView.setText(s);
    }
}
