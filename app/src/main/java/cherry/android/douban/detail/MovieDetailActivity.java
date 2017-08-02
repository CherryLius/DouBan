package cherry.android.douban.detail;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.detail.delegate.MovieAdvanceDelegate;
import cherry.android.douban.detail.delegate.MoviePersonDelegate;
import cherry.android.douban.detail.delegate.SimpleDelegate;
import cherry.android.douban.detail.header.MovieDetailHeader;
import cherry.android.douban.detail.header.SummaryDetailHeader;
import cherry.android.douban.detail.header.TicketBuySticky;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MoviePerson;
import cherry.android.douban.sticker.StickyHeaderHelper;
import cherry.android.douban.util.CompatUtils;
import cherry.android.douban.util.PaletteHelper;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.ViewChooser;
import cherry.android.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.router.annotations.Extra;
import cherry.android.router.annotations.Route;
import cherry.android.router.api.Router;

/**
 * Created by LHEE on 2017/6/4.
 */
@Route("/activity/movie/detail")
public class MovieDetailActivity extends BaseActivity implements MovieDetailContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_tool_title)
    TextView titleView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Extra(name = "id")
    String mMovieId;
    @Extra(name = "name", nonNull = true)
    String mMovieName;
    @Extra(name = "imageUrl", nonNull = true)
    String mImageUrl;
    int mToolbarBackground = 0;

    private MovieDetailContract.Presenter mPresenter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private MovieDetailHeader mDetailHeader;
    private SummaryDetailHeader mSummaryHeader;
    private float mDistance;

    private MoviePersonDelegate mMoviePersonDelegate;
    private MovieAdvanceDelegate mMovieAdvanceDelegate;
    private TicketBuySticky mStickyHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MovieDetailPresenter(this, this);
        mPresenter.loadMovieDetail(mMovieId);
    }

    void initToolbar() {
        Drawable drawable = CompatUtils.getDrawable(this, R.drawable.ic_arrow_back_black_24dp, Color.WHITE);
        toolbar.setNavigationIcon(drawable);
        //toolbar.inflateMenu(R.menu.menu_movie_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item === " + i);
        }
        //Adapter adapter = new Adapter(list);
        RecyclerAdapter adapter = new RecyclerAdapter(list);
        adapter.addDelegate(String.class)
                .bindDelegate(mMoviePersonDelegate = new MoviePersonDelegate(),
                        mMovieAdvanceDelegate = new MovieAdvanceDelegate(),
                        new SimpleDelegate())
                .to(new ViewChooser<String>() {
                    @Override
                    public Class<? extends ItemViewDelegate<String, ? extends RecyclerView.ViewHolder>> choose(String s, int position) {
                        if (position == 0) return MoviePersonDelegate.class;
                        if (position == 1) return MovieAdvanceDelegate.class;
                        return SimpleDelegate.class;
                    }
                });
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        initHeader();
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
        mStickyHeader = new TicketBuySticky(this, mDetailHeader.getTicketBuyView());
    }

    void initHeader() {
        mDetailHeader = new MovieDetailHeader(recyclerView);
        mSummaryHeader = new SummaryDetailHeader(recyclerView);
        mHeaderAndFooterWrapper.addHeaderView(mDetailHeader.getItemView());
        mHeaderAndFooterWrapper.addHeaderView(mSummaryHeader.getItemView());
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        Router.inject(this);
        initToolbar();
        initView();
        initToolbarBackground();
    }

    void initToolbarBackground() {
        PaletteHelper.palette(this, mImageUrl, new PaletteHelper.PaletteCallback() {
            @Override
            public void onGenerated(int color) {
                mToolbarBackground = color;
                mDetailHeader.getMoviePosterView().setBackgroundColor(mToolbarBackground);
            }
        });
    }

    @Override
    protected void registerListener() {
        recyclerView.addOnScrollListener(mRecyclerScrollListener);
    }

    private RecyclerView.OnScrollListener mRecyclerScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mDistance += dy;
            if (mDistance <= mDetailHeader.getMoviePosterView().getHeight()) {
                float amount = mDistance / mDetailHeader.getMoviePosterView().getHeight();
                float delta = (float) Math.sin(Math.PI / 2 * amount);
                int colorVal = mToolbarBackground;
                int color = Color.argb((int) (255 * delta), Color.red(colorVal), Color.green(colorVal), Color.blue(colorVal));
                toolbar.setBackgroundColor(color);
                titleView.setText(R.string.label_movie);
            } else {
                toolbar.setBackgroundColor(mToolbarBackground);
                titleView.setText(mMovieName);
            }
            StickyHeaderHelper.onScroll(dx, mDistance, recyclerView, mStickyHeader);
        }
    };

    @Override
    protected void unregisterListener() {
        recyclerView.removeOnScrollListener(mRecyclerScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(@NonNull MovieDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovie(Movie movie) {
        mDetailHeader.updateHeader(movie);
        mSummaryHeader.updateHeader(movie);
        List<MoviePerson> list = movie.getDirectors();
        list.addAll(movie.getCasts());
        mMoviePersonDelegate.update(list);
        mStickyHeader.setMovie(movie);
        mPresenter.loadMoviePhotos(mMovieId);
    }
}
