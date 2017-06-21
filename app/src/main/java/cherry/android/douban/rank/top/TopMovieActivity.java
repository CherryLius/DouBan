package cherry.android.douban.rank.top;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.model.Movie;
import cherry.android.douban.rank.delegate.RankDelegate;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.wrapper.LoadMoreWrapper;
import cherry.android.router.annotations.Route;
import cherry.android.router.api.Router;

/**
 * Created by LHEE on 2017/6/19.
 */
@Route("movie://activity/movie/top")
public class TopMovieActivity extends BaseActivity implements TopContract.View, RecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerAdapter mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private TopContract.Presenter mPresenter;
    private int mStart;

    @Override
    protected int getViewLayoutId() {
        return R.layout.layout_swipe_recycler;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        new TopPresenter(this, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new RecyclerAdapter();
        mAdapter.addDelegate(Movie.class, new RankDelegate());
        mAdapter.setOnItemClickListener(this);
        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter, R.layout.layout_load_more);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.SimpleLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mStart += 36;
                mPresenter.loadTop(mStart, 36);
            }
        });
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mLoadMoreWrapper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                mPresenter.refreshMovies();
            }
        });
        mPresenter.loadTop(mStart, 36);
    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unregisterListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(@NonNull TopContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTopMovies(List<Movie> movies) {
        mAdapter.setItems(movies);
        swipeRefreshLayout.setRefreshing(false);
        if (mStart == 0 && mLoadMoreWrapper.getState() != LoadMoreWrapper.STATE_NO_MORE) {
            mLoadMoreWrapper.setState(LoadMoreWrapper.STATE_LOADING_MORE);
        }
    }

    @Override
    public void showNoMoreMovie() {
        mLoadMoreWrapper.setState(LoadMoreWrapper.STATE_NO_MORE);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        List<Movie> movies = mPresenter.getCurrentMovies();
        Movie movie = movies.get(position);
        if (movie == null) return;
        String url = "movie://activity/movie/detail?id=" + movie.getId()
                + "&name=" + movie.getTitle()
                + "&imageUrl=" + movie.getImages().getLarge();
        Router.build(url).open(this);
    }
}
