package cherry.android.douban.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.adapter.RankMovieAdapter;
import cherry.android.douban.common.ui.ToolbarFragment;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.NewWeeklyMovie;
import cherry.android.douban.model.RankMovies;
import cherry.android.douban.model.SubjectProvider;
import cherry.android.douban.model.TheaterMovie;
import cherry.android.douban.network.Network;
import cherry.android.douban.network.api.MovieApi;
import cherry.android.douban.recycler.BaseAdapter;
import cherry.android.douban.rx.RxHelper;
import cherry.android.douban.util.Logger;
import cherry.android.router.api.Router;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/6/13.
 */

public class RankFragment extends ToolbarFragment implements RankContract.View, BaseAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private RankMovieAdapter mAdapter;

    private RankContract.Presenter mPresenter;

    public RankFragment() {
        new RankPresenter(this, this);
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_rank;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
        mPresenter.loadMovies();
    }

    void initView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isTitle = mPresenter.getCurrentData().get(position).getType()
                        == RankMovies.TYPE_TITLE;
                return isTitle ? layoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        mAdapter = new RankMovieAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setPresenter(@android.support.annotation.NonNull RankContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovies(List<RankMovies> movies) {
        mAdapter.showData(movies);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        List<RankMovies> movies = mPresenter.getCurrentData();
        RankMovies rankMovies = movies.get(position);
        if (rankMovies.getType() == RankMovies.TYPE_TITLE
                && rankMovies.getTitle().contains("250")) {
            return;
        }
        Movie movie = rankMovies.getMovie();
        if (movie == null) return;
        String url = "movie://activity/movie/detail?id=" + movie.getId()
                + "&name=" + movie.getTitle()
                + "&imageUrl=" + movie.getImages().getLarge();
        Router.build(url).open(getActivity());
    }
}
