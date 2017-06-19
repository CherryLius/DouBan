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
import cherry.android.douban.R;
import cherry.android.douban.common.ui.ToolbarFragment;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.RankMovies;
import cherry.android.douban.rank.delegate.RankDelegate;
import cherry.android.douban.rank.delegate.SectionDelegate;
import cherry.android.recycler.BaseAdapter;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewChooser;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/13.
 */

public class RankFragment extends ToolbarFragment implements RankContract.View, BaseAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private BaseAdapter mAdapter;

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
        mAdapter = new BaseAdapter();
        mAdapter.addDelegate(RankMovies.class).bindDelegate(new RankDelegate(), new SectionDelegate())
                .to(new ViewChooser<RankMovies>() {
                    @Override
                    public Class<? extends ItemViewDelegate<RankMovies, ? extends RecyclerView.ViewHolder>> choose(RankMovies rankMovies, int position) {
                        return rankMovies.getType() == RankMovies.TYPE_TITLE ? SectionDelegate.class : RankDelegate.class;
                    }
                });
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
        mAdapter.setItems(movies);
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
