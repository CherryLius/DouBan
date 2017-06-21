package cherry.android.douban.rank.delegate;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cherry.android.douban.R;
import cherry.android.douban.model.NorthAmericaMovie;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/21.
 */

public class NorthHeaderDelegate implements ItemViewDelegate<NorthAmericaMovie, ViewHolder> {
    @NonNull
    @Override
    public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_north_rank_header, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(@NonNull ViewHolder holder, NorthAmericaMovie northAmericaMovie, int position) {
        TextView textView = holder.findView(R.id.tv_date);
        textView.setText(northAmericaMovie.getDate());
    }
}
