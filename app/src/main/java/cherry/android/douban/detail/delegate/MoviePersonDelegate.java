package cherry.android.douban.detail.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import cherry.android.douban.R;
import cherry.android.douban.adapter.MoviePersonAdapter;
import cherry.android.douban.model.MoviePerson;
import cherry.android.douban.recycler.BaseAdapter;
import cherry.android.douban.recycler.DividerItemDecoration;
import cherry.android.douban.recycler.ItemViewDelegate;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MoviePersonDelegate implements ItemViewDelegate<Object, ViewHolder>, BaseAdapter.OnItemClickListener {

    private List<MoviePerson> moviePersonList;
    private MoviePersonAdapter mAdapter;

    @Override
    public int getViewLayoutId() {
        return R.layout.layout_movie_person;
    }

    @Override
    public boolean isMatchViewType(Object o, int position) {
        return position == 0;
    }

    @Override
    public ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, Object o, int position) {
        RecyclerView recyclerView = holder.findView(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        decoration.setGap(20);
        decoration.useSpace(true);
        recyclerView.addItemDecoration(decoration);
        mAdapter = new MoviePersonAdapter(recyclerView.getContext(), moviePersonList);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    public void update(List<MoviePerson> data) {
        moviePersonList = data;
        if (mAdapter != null)
            mAdapter.showData(data);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        MoviePerson moviePerson = moviePersonList.get(position);
        String query = "id=" + moviePerson.getId() + "&name=" + moviePerson.getName();
        Router.build("movie://activity/celebrity/detail?" + query).open(itemView.getContext());
    }
}
