package cherry.android.douban.detail.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cherry.android.douban.R;
import cherry.android.douban.adapter.MovieAdvanceAdapter;
import cherry.android.douban.model.MovieAvatars;
import cherry.android.recycler.DividerItemDecoration;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewHolder;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MovieAdvanceDelegate implements ItemViewDelegate<String, ViewHolder> {

    private MovieAdvanceAdapter mAdapter;

    @NonNull
    @Override
    public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_movie_person, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
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
            mAdapter.setItems(data);
    }
}
