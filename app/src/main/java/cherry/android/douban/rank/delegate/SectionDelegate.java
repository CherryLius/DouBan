package cherry.android.douban.rank.delegate;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cherry.android.douban.R;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/19.
 */

public class SectionDelegate implements ItemViewDelegate<String, ViewHolder> {

    @NonNull
    @Override
    public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_rank_title, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        TextView textView = holder.findView(R.id.tv_title);
        textView.setText(s);
        TextView moreView = holder.findView(R.id.tv_more);
        if (s == null || !s.contains("250")) {
            moreView.setVisibility(View.GONE);
        }
    }
}
