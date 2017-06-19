package cherry.android.douban.detail.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cherry.android.douban.R;
import cherry.android.douban.adapter.MoviePersonAdapter;
import cherry.android.douban.model.MoviePerson;
import cherry.android.recycler.BaseAdapter;
import cherry.android.recycler.DividerItemDecoration;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewHolder;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MoviePersonDelegate implements ItemViewDelegate<String, ViewHolder>, BaseAdapter.OnItemClickListener {

    private List<MoviePerson> moviePersonList;
    private MoviePersonAdapter mAdapter;

    @NonNull
    @Override
    public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_movie_person, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
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
            mAdapter.setItems(data);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        MoviePerson moviePerson = moviePersonList.get(position);
        String query = "id=" + moviePerson.getId() + "&name=" + moviePerson.getName()
                + "&imageUrl=" + moviePerson.getAvatars().getLarge();
        Router.build("movie://activity/celebrity/detail?" + query).open(itemView.getContext());
    }
}
