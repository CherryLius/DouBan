package cherry.android.douban.detail;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

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
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MoviePerson;
import cherry.android.douban.recycler.BaseAdapter;
import cherry.android.douban.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.router.annotations.Route;

/**
 * Created by LHEE on 2017/6/4.
 */
@Route("movie://activity/movie/detail")
public class MovieDetailActivity extends BaseActivity implements MovieDetailContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private String mMovieId;

    private MovieDetailContract.Presenter mPresenter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private MovieDetailHeader mDetailHeader;
    private SummaryDetailHeader mSummaryHeader;
    private float mDistance;

    private MoviePersonDelegate mMoviePersonDelegate;
    private MovieAdvanceDelegate mMovieAdvanceDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        new MovieDetailPresenter(this);
        initToolbar();
        initView();
        registerListener();
        mPresenter.loadMovieDetail(mMovieId);
    }

    void initToolbar() {
        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, getTheme());
        DrawableCompat.setTint(drawable, Color.WHITE);
        toolbar.setNavigationIcon(drawable);
        //toolbar.inflateMenu(R.menu.menu_movie_detail);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initView() {
        mMovieId = getIntent().getStringExtra("id");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item === " + i);
        }
        //Adapter adapter = new Adapter(list);
        BaseAdapter adapter = new BaseAdapter(list);
        adapter.addDelegate(mMoviePersonDelegate = new MoviePersonDelegate());
        adapter.addDelegate(mMovieAdvanceDelegate = new MovieAdvanceDelegate());
        adapter.addDelegate(new SimpleDelegate());
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        initHeader();
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    void initHeader() {
        mDetailHeader = new MovieDetailHeader(recyclerView);
        mSummaryHeader = new SummaryDetailHeader(recyclerView);
        mHeaderAndFooterWrapper.addHeaderView(mDetailHeader.getItemView());
        mHeaderAndFooterWrapper.addHeaderView(mSummaryHeader.getItemView());
    }

    void registerListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    int colorVal = ContextCompat.getColor(MovieDetailActivity.this, R.color.colorPrimary);
                    int color = Color.argb((int) (255 * delta), Color.red(colorVal), Color.green(colorVal), Color.blue(colorVal));
                    toolbar.setBackgroundColor(color);
                } else {
                    toolbar.setBackgroundResource(R.color.colorPrimary);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
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
        mPresenter.loadMoviePhotos(mMovieId);
    }
}
