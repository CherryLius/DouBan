package cherry.android.douban.detail.delegate;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/7.
 */

public class SimpleDelegate implements ItemViewDelegate<String, ViewHolder> {

    @NonNull
    @Override
    public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        TextView textView = holder.findView(android.R.id.text1);
        textView.setText(s);
    }
}
