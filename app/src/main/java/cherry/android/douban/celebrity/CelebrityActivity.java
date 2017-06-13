package cherry.android.douban.celebrity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.celebrity.header.CelebrityHeader;
import cherry.android.douban.celebrity.header.CelebritySummaryHeader;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.recycler.CommonAdapter;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.douban.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.douban.util.CompatUtils;
import cherry.android.router.annotations.Route;
import cherry.android.router.annotations.RouteField;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/7.
 */
@Route("movie://activity/celebrity/detail")
public class CelebrityActivity extends BaseActivity implements CelebrityContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_tool_title)
    TextView titleView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @RouteField(name = "id", nonNull = true)
    String mCelebrityId;
    @RouteField(name = "name")
    String mCelebrityName;
    private float mDistance;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private CelebrityHeader mCelebrityHeader;
    private CelebritySummaryHeader mSummaryHeader;
    private CelebrityContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Router.bind(this);
        new CelebrityPresenter(this);
        initToolbar();
        initView();
        registerListener();
        mPresenter.loadCelebrityInfo(mCelebrityId);
    }

    void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item === " + i);
        }
        Adapter adapter = new Adapter(list);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        initHeader();
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
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
                if (mDistance <= mCelebrityHeader.getImageView().getHeight()) {
                    float amount = mDistance / mCelebrityHeader.getImageView().getHeight();
                    float delta = (float) Math.sin(Math.PI / 2 * amount);
                    int colorVal = ContextCompat.getColor(CelebrityActivity.this, R.color.colorPrimary);
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

    static class Adapter extends CommonAdapter<String, ViewHolder> {
        public Adapter(List<String> data) {
            super(data, android.R.layout.simple_list_item_1);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text1);
            textView.setText(s);
        }

        @Override
        protected ViewHolder createDefaultViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }
    }
}
