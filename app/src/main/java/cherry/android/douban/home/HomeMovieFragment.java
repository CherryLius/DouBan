package cherry.android.douban.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.adapter.TheaterMovieAdapter;
import cherry.android.douban.common.Constants;
import cherry.android.douban.common.ui.LazyFragment;
import cherry.android.douban.model.Movie;
import cherry.android.recycler.DividerItemDecoration;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.wrapper.LoadMoreWrapper;
import cherry.android.router.api.Router;
import cherry.android.toast.Toaster;

/**
 * Created by LHEE on 2017/6/3.
 */

public class HomeMovieFragment extends LazyFragment implements HomeMovieContract.View, RecyclerAdapter.OnItemClickListener {
    private static final String EXTRA_TAB = "extra_tab";
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private String mTab;
    private boolean mFirstVisible = true;
    private HomeMovieContract.Presenter mPresenter;
    private TheaterMovieAdapter mMovieAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private int mStart = 0;

    public HomeMovieFragment() {
        mPresenter = new HomeMoviePresenter(this, this);
    }

    public static HomeMovieFragment newInstance(String tab) {
        HomeMovieFragment fragment = new HomeMovieFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_TAB, tab);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTab = getArguments().getString(EXTRA_TAB, "");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mMovieAdapter = new TheaterMovieAdapter(getContext(),
                isComingSoon()
                        ? TheaterMovieAdapter.TYPE_COMING_SOON
                        : TheaterMovieAdapter.TYPE_IN_THEATER);
        mMovieAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mLoadMoreWrapper = new LoadMoreWrapper(mMovieAdapter, R.layout.layout_load_more);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.SimpleLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mStart += 20;
                loadMovies();
            }
        });
        recyclerView.setAdapter(mLoadMoreWrapper);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                mPresenter.refreshMovies(isComingSoon());
            }
        });
        refreshLayout.setRefreshing(true);
    }

    private boolean isComingSoon() {
        return Constants.TAB_COMING_SOON.endsWith(mTab);
    }

    @Override
    public void onLazyLoad() {
        if (mFirstVisible) {
            loadMovies();
        }
        mFirstVisible = false;
    }

    @Override
    public boolean isPrepared() {
        return !TextUtils.isEmpty(mTab) && getUserVisibleHint()
                && mPresenter != null;
    }

    void loadMovies() {
        mPresenter.loadMovies(isComingSoon(), mStart, 20);
    }

    @Override
    public void setPresenter(@NonNull HomeMovieContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMovieAdapter.setItems(movies);
        refreshLayout.setRefreshing(false);
        if (mStart == 0 && mLoadMoreWrapper.getState() != LoadMoreWrapper.STATE_NO_MORE) {
            mLoadMoreWrapper.setState(LoadMoreWrapper.STATE_LOADING_MORE);
        }
    }

    @Override
    public void showNoMoreMovie() {
        mLoadMoreWrapper.setState(LoadMoreWrapper.STATE_NO_MORE);
    }

    @Override
    public void showError() {
        refreshLayout.setRefreshing(false);
        Toaster.iError(getContext(), "加载出错了");
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        List<Movie> movies = mMovieAdapter.getCurrentMovies();
        Movie movie = movies.get(position);
        String url = "movie://activity/movie/detail?id=" + movie.getId()
                + "&name=" + movie.getTitle()
                + "&imageUrl=" + movie.getImages().getLarge();
        Router.build(url).open(getActivity());
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.layout_swipe_recycler;
    }
}
