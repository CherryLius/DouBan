package cherry.android.douban.detail.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cherry.android.douban.R;
import cherry.android.douban.adapter.MovieAdvanceAdapter;
import cherry.android.douban.model.MovieAvatars;
import cherry.android.douban.recycler.DividerItemDecoration;
import cherry.android.douban.recycler.ItemViewDelegate;
import cherry.android.douban.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MovieAdvanceDelegate implements ItemViewDelegate<Object, ViewHolder> {

    private MovieAdvanceAdapter mAdapter;

    @Override
    public int getViewLayoutId() {
        return R.layout.layout_movie_person;
    }

    @Override
    public boolean isMatchViewType(Object o, int position) {
        return position == 1;
    }

    @Override
    public ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, Object o, int position) {
        TextView cateView = holder.findView(R.id.tv_cate);
        cateView.setText(R.string.label_advance);
        RecyclerView recyclerView = holder.findView(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        decoration.setGap(20);
        decoration.useSpace(true);
        recyclerView.addItemDecoration(decoration);
        mAdapter = new MovieAdvanceAdapter(recyclerView.getContext());
        recyclerView.setAdapter(mAdapter);
    }

    public void update(List<MovieAvatars> data) {
        if (mAdapter != null)
            mAdapter.showData(data);
    }
}
