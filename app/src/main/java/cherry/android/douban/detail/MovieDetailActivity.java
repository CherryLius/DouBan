package cherry.android.douban.detail;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.model.Movie;
import cherry.android.douban.recycler.CommonAdapter;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.douban.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.douban.util.Utils;
import cherry.android.router.annotations.Route;

/**
 * Created by LHEE on 2017/6/4.
 */
@Route("movie://activity/movie/detail")
public class MovieDetailActivity extends BaseActivity implements MovieDetailContract.View {
    //    @BindView(R.id.app_bar_layout)
//    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private String mMovieId;

    private MovieDetailContract.Presenter mPresenter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private DetailHeader mDetailHeader;
    private SummaryHeader mSummaryHeader;
    private float mDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        new MovieDetailPresenter(this);
        initToolbar();
        init();
        registerListener();
        mPresenter.loadMovieDetail(mMovieId);
    }

    void init() {
        mMovieId = getIntent().getStringExtra("id");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item === " + i);
        }
        Adapter adapter = new Adapter(list);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_movie_detail_header, recyclerView, false);
        mDetailHeader = new DetailHeader(view);
        mHeaderAndFooterWrapper.addHeaderView(view);

        view = LayoutInflater.from(this).inflate(R.layout.layout_movie_detail_summary_detail,recyclerView,false);
        mSummaryHeader = new SummaryHeader(view);
        mHeaderAndFooterWrapper.addHeaderView(view);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
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
                if (mDistance <= mDetailHeader.imageView.getHeight()) {
                    float amount = mDistance / mDetailHeader.imageView.getHeight();
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
        mDetailHeader.updateDetail(this, movie);
        mSummaryHeader.updateSummary(movie);
    }

    static class DetailHeader {
        View itemView;

        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView nameView;
        @BindView(R.id.tv_detail)
        TextView detailView;
        @BindView(R.id.tv_rating_average)
        TextView ratingView;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.tv_collect_count)
        TextView collectView;


        public DetailHeader(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void updateDetail(Context context, Movie movie) {
            Glide.with(context).load(movie.getImages().getLarge())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                    .into(imageView);
            nameView.setText(movie.getTitle());
            StringBuilder detailBuilder = new StringBuilder();
            detailBuilder.append(movie.getYear())
                    .append('/')
                    .append(Utils.list2String(movie.getGenres()))
                    .append('\n')
                    .append("原名：")
                    .append(movie.getOriginalTitle())
                    .append('\n')
                    .append("上映时间：")
                    .append('\n')
                    .append("片长：");
            detailView.setText(detailBuilder.toString());
            Movie.Rating movieRating = movie.getRating();
            float rating = (float) (movieRating.getAverage() / movieRating.getMax() * ratingBar.getNumStars());
            ratingBar.setRating(rating);
            ratingView.setText(movieRating.getAverage() + "");
            collectView.setText(context.getString(R.string.label_collect_count, movie.getCollectCount()));
        }
    }

    static class SummaryHeader {
        @BindView(R.id.tv_summary)
        TextView textView;

        SummaryHeader(View view) {
            ButterKnife.bind(this,view);
        }

        public void updateSummary(Movie movie) {
            textView.setText(movie.getSummary());
        }
    }

    static class Adapter extends CommonAdapter<String, ViewHolder> {

        public Adapter(List<String> data) {
            super(data, android.R.layout.simple_list_item_1);
        }

        @Override
        public void convert(ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text1);
            textView.setText(s);
        }

        @Override
        protected ViewHolder createViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }
    }
}
