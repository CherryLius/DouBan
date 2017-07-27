package cherry.android.douban.celebrity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.celebrity.delegate.WorksItemDelegate;
import cherry.android.douban.celebrity.header.CelebrityHeader;
import cherry.android.douban.celebrity.header.CelebritySummaryHeader;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.route.MovieRouter;
import cherry.android.douban.util.CompatUtils;
import cherry.android.douban.util.PaletteHelper;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.ViewHolder;
import cherry.android.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.router.annotations.Extra;
import cherry.android.router.annotations.Route;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/7.
 */
@Route("/activity/celebrity/detail")
public class CelebrityActivity extends BaseActivity implements CelebrityContract.View, RecyclerAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_tool_title)
    TextView titleView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Extra(name = "id", nonNull = true)
    String mCelebrityId;
    @Extra(name = "name")
    String mCelebrityName;
    @Extra(name = "imageUrl", nonNull = true)
    String mImageUrl;
    private float mDistance;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private RecyclerAdapter mAdapter;
    private CelebrityHeader mCelebrityHeader;
    private CelebritySummaryHeader mSummaryHeader;

    private CelebrityContract.Presenter mPresenter;
    private int mToolbarBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CelebrityPresenter(this, this);
        mPresenter.loadCelebrityInfo(mCelebrityId);
    }

    void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter();
        mAdapter.addDelegate(String.class, new SimpleItemDelegate());
        mAdapter.addDelegate(MovieCelebrity.Works.class, new WorksItemDelegate());
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        initHeader();
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
        mAdapter.setOnItemClickListener(this);
    }

    void initHeader() {
        mCelebrityHeader = new CelebrityHeader(recyclerView);
        mHeaderAndFooterWrapper.addHeaderView(mCelebrityHeader.getItemView());
        mSummaryHeader = new CelebritySummaryHeader(recyclerView);
        mHeaderAndFooterWrapper.addHeaderView(mSummaryHeader.getItemView());
    }

    void initToolbar() {
        titleView.setText(R.string.label_celebrity);
        Drawable drawable = CompatUtils.getDrawable(this, R.drawable.ic_arrow_back_black_24dp, Color.WHITE);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        Router.bind(this);
        initToolbar();
        initView();
        initToolbarBackground();
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
            if (mDistance <= mCelebrityHeader.getImageView().getHeight()) {
                float amount = mDistance / mCelebrityHeader.getImageView().getHeight();
                float delta = (float) Math.sin(Math.PI / 2 * amount);
                int colorVal = mToolbarBackground;
                int color = Color.argb((int) (255 * delta), Color.red(colorVal), Color.green(colorVal), Color.blue(colorVal));
                toolbar.setBackgroundColor(color);
                titleView.setText(R.string.label_celebrity);
            } else {
                toolbar.setBackgroundColor(mToolbarBackground);
                titleView.setText(mCelebrityName);
            }
        }
    };

    void initToolbarBackground() {
        PaletteHelper.palette(this, mImageUrl, new PaletteHelper.PaletteCallback() {
            @Override
            public void onGenerated(int color) {
                mToolbarBackground = color;
                mCelebrityHeader.getImageView().setBackgroundColor(mToolbarBackground);
            }
        });
    }

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
    public void setPresenter(@NonNull CelebrityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showCelebrityInfo(MovieCelebrity celebrity) {
        mCelebrityHeader.updateHeader(celebrity);
        mSummaryHeader.updateHeader(celebrity);
    }

    @Override
    public void showWorks(List<?> works) {
        mAdapter.setItems(works);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        int realPosition = position - mHeaderAndFooterWrapper.getHeaderCount();
        List<Object> list = mPresenter.getCurrentData();
        Object object = list.get(realPosition);
        if (object instanceof MovieCelebrity.Works) {
            MovieCelebrity.Works work = (MovieCelebrity.Works) object;
            final Movie movie = work.getSubject();
//            String url = "movie://activity/movie/detail?id=" + movie.getId()
//                    + "&name=" + movie.getTitle()
//                    + "&imageUrl=" + movie.getImages().getLarge();
//            Router.build(url).open(this);
            MovieRouter.get()
                    .getRouteService()
                    .startMovieDetailActivity(this, movie.getId(), movie.getTitle(), movie.getImages().getLarge());
        }
    }

    private class SimpleItemDelegate implements ItemViewDelegate<String, ViewHolder> {

        @NonNull
        @Override
        public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.layout_simple_text_view, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void convert(@NonNull ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(R.id.tv_simple);
            textView.setText(s);
        }
    }
}
