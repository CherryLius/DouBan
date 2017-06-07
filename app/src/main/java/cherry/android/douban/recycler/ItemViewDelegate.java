package cherry.android.douban.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/6/6.
 */

public interface ItemViewDelegate<T, VH extends RecyclerView.ViewHolder> {
    int getViewLayoutId();

    boolean isMatchViewType(T t, int position);

    VH createViewHolder(View itemView);

    void convert(VH holder, T t, int position);
}
