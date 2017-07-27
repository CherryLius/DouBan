package cherry.android.douban.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cherry.android.douban.R;
import cherry.android.douban.common.ui.ToolbarFragment;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.NorthAmericaMovie;
import cherry.android.douban.rank.delegate.NorthHeaderDelegate;
import cherry.android.douban.rank.delegate.NorthRankDelegate;
import cherry.android.douban.rank.delegate.RankDelegate;
import cherry.android.douban.rank.delegate.SectionDelegate;
import cherry.android.douban.route.MovieRouter;
import cherry.android.douban.route.RouteService;
import cherry.android.douban.widget.JDRefreshHeader;
import cherry.android.ptr.NestedPullRefreshLayout;
import cherry.android.ptr.OnRefreshListener;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/13.
 */

public class RankFragment extends ToolbarFragment implements RankContract.View, RecyclerAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.layout_pull_to_refresh)
    NestedPullRefreshLayout pullToRefreshLayout;
    private RecyclerAdapter mAdapter;

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
    }

    void initView() {
        pullToRefreshLayout.setRefreshHeader(new JDRefreshHeader(getContext()));
        pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadMovies();
            }
        });
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = mPresenter.getCurrentData().get(position);
                if (item instanceof String
                        || item instanceof NorthAmericaMovie.Subjects
                        || item instanceof NorthAmericaMovie)
                    return layoutManager.getSpanCount();
                return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setVerticalScrollBarEnabled(true);
        mAdapter = new RecyclerAdapter();
        mAdapter.addDelegate(String.class, new SectionDelegate());
        mAdapter.addDelegate(Movie.class, new RankDelegate());
        mAdapter.addDelegate(NorthAmericaMovie.class, new NorthHeaderDelegate());
        mAdapter.addDelegate(NorthAmericaMovie.Subjects.class, new NorthRankDelegate());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        pullToRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.autoRefresh();
            }
        }, 100);
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
    public void showMovies(List movies) {
        pullToRefreshLayout.refreshComplete();
        mAdapter.setItems(movies);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        List<Object> movies = mPresenter.getCurrentData();
        Object item = movies.get(position);
        if (item instanceof String
                && item.toString().contains("250")) {
//            Router.build("movie://activity/movie/top").open(getActivity());
            MovieRouter.get()
                    .getRouteService()
                    .startTopMovieActivity(getActivity());
            return;
        }
        Movie movie = null;
        if (item instanceof Movie) {
            movie = (Movie) item;
        } else if (item instanceof NorthAmericaMovie.Subjects) {
            NorthAmericaMovie.Subjects subject = (NorthAmericaMovie.Subjects) item;
            movie = subject.getMovie();
        }
        if (movie != null) {
            if (movie == null) return;
//            String url = "movie://activity/movie/detail?id=" + movie.getId()
//                    + "&name=" + movie.getTitle()
//                    + "&imageUrl=" + movie.getImages().getLarge();
//            Router.build(url).open(getActivity());
            MovieRouter.get()
                    .getRouteService()
                    .startMovieDetailActivity(getActivity(),
                            movie.getId(),
                            movie.getTitle(),
                            movie.getImages().getLarge());
        }
    }

    @OnClick(R.id.tv_search)
    void onClick() {
//        Router.build("movie://activity/search").open();
        MovieRouter.get().getRouteService().startSearchActivity();
    }
}
