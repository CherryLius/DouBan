package cherry.android.douban.search;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cherry.android.douban.R;
import cherry.android.douban.adapter.QueryAdapter;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.model.Movie;
import cherry.android.douban.route.MovieRouter;
import cherry.android.douban.util.Utils;
import cherry.android.douban.widget.ClearTextView;
import cherry.android.recycler.DividerItemDecoration;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.router.annotations.Route;

/**
 * Created by ROOT on 2017/7/20.
 */
@Route("/activity/search")
public class SearchActivity extends BaseActivity implements SearchContract.View, RecyclerAdapter.OnItemClickListener {

    @BindView(R.id.et_search)
    ClearTextView searchView;
    @BindView(R.id.tv_cancel)
    TextView cancelView;
    @BindView(R.id.flex_hot_search)
    FlexboxLayout hotLayout;
    @BindView(R.id.flex_search_history)
    FlexboxLayout historyLayout;
    @BindView(R.id.view_stub)
    ViewStubCompat viewStub;
    @BindView(R.id.layout_search_history)
    View historyView;
    RecyclerView recyclerView;
    QueryAdapter mAdapter;

    SearchContract.Presenter mPresenter;

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        new SearchPresenter(this, this);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        searchView.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyBoard();
            }
        }, 200);
    }

    @Override
    protected void registerListener() {
        searchView.setOnTextClearListener(new ClearTextView.OnTextClearListener() {
            @Override
            public void onClear(View v) {
                if (recyclerView != null)
                    recyclerView.setVisibility(View.GONE);
                historyView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void unregisterListener() {

    }

    @Override
    public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showHotSearch(String[] hots) {
        for (String title : hots) {
            final TextView textView = new TextView(this);
            textView.setBackgroundResource(R.drawable.ic_round_rect_stroke_bg);
            textView.setText(title);
            textView.setTextSize(12);
            textView.setPadding(30, 15, 30, 15);
            textView.setGravity(Gravity.CENTER);
            hotLayout.addView(textView);
            ViewGroup.LayoutParams lp = textView.getLayoutParams();
            if (lp instanceof FlexboxLayout.LayoutParams) {
                FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) lp;
                params.setMargins(10, 0, 10, 15);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keyWords = textView.getText().toString();
                    searchView.setText(keyWords);
                    searchView.setSelection(keyWords.length());
                    search(keyWords);
                }
            });
        }
    }

    @Override
    public void showQuery(List<Movie> movies) {
        initRecyclerView();
        mAdapter.setItems(movies);
        recyclerView.setVisibility(View.VISIBLE);
        historyView.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    void initRecyclerView() {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) viewStub.inflate();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new QueryAdapter();
            mAdapter.setOnItemClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(mAdapter);
        }
    }

    @OnEditorAction(R.id.et_search)
    boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (searchView.getEditableText() != null) {
                String keyWords = searchView.getEditableText().toString();
                search(keyWords);
                return true;
            }
        }
        return false;
    }

    private void search(String keyWords) {
        mPresenter.search(keyWords);
        hideSoftKeyBoard();
    }

    private void showSoftKeyBoard() {
        InputMethodManager imm = Utils.getSystemService(this, INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, 0);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = Utils.getSystemService(this, INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
        List<Movie> movies = mAdapter.getCurrentItems();
        Movie movie = movies.get(position);
//        String url = "movie://activity/movie/detail?id=" + movie.getId()
//                + "&name=" + movie.getTitle()
//                + "&imageUrl=" + movie.getImages().getLarge();
//        Router.build(url).open(this);
        MovieRouter.get()
                .getRouteService()
                .startMovieDetailActivity(this,
                        movie.getId(),
                        movie.getTitle(),
                        movie.getImages().getLarge());
    }

    @OnClick(R.id.tv_cancel)
    void onClick() {
        finish();
    }
}
