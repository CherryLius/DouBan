package cherry.android.douban.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.adapter.TheaterMovieAdapter;
import cherry.android.douban.common.Constants;
import cherry.android.douban.common.ui.LazyFragment;
import cherry.android.douban.model.Movie;
import cherry.android.douban.recycler.BaseAdapter;
import cherry.android.douban.recycler.DividerItemDecoration;
import cherry.android.router.api.Router;

/**
 * Created by LHEE on 2017/6/3.
 */

public class HomeMovieFragment extends LazyFragment implements HomeMovieContract.View, BaseAdapter.OnItemClickListener {
    private static final String EXTRA_TAB = "extra_tab";
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private String mTab;
    private boolean mFirstVisible = true;
    private HomeMovieContract.Presenter mPresenter;
    private TheaterMovieAdapter mMovieAdapter;

    public HomeMovieFragment() {
        mPresenter = new HomeMoviePresenter(this);
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
        mTab = getArguments().getString(EXTRA_TAB, "");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_swipe_recycler, container, false);
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
        recyclerView.setAdapter(mMovieAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                loadMovies();
            }
        });
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
        if (!isComingSoon()) {
            mPresenter.loadMovieInTheater();
        } else {
            mPresenter.loadComingSoon();
        }
    }

    @Override
    public void setPresenter(@NonNull HomeMovieContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTheaterMovie(List<Movie> movies) {
        mMovieAdapter.showData(movies);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showComingSoon(List<Movie> movies) {
        mMovieAdapter.showData(movies);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        List<Movie> movies = mMovieAdapter.getCurrentMovies();
        Movie movie = movies.get(position);
        String url = "movie://activity/movie/detail?id=" + movie.getId()
                + "&name=" + movie.getTitle();
        Router.build(url).open(getActivity());
    }
}
