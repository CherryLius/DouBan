package cherry.android.douban.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import cherry.android.douban.R;
import cherry.android.douban.adapter.TheaterMovieAdapter;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.model.Movie;
import cherry.android.douban.util.Utils;
import cherry.android.recycler.DividerItemDecoration;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.wrapper.LoadMoreWrapper;
import cherry.android.router.annotations.Route;

/**
 * Created by ROOT on 2017/7/20.
 */
@Route("movie://activity/search")
public class SearchActivity extends BaseActivity implements SearchContract.View {

    @BindView(R.id.et_search)
    EditText searchView;
    @BindView(R.id.tv_cancel)
    TextView cancelView;
    @BindView(R.id.flex_hot_search)
    FlexboxLayout hotLayout;
    @BindView(R.id.flex_search_history)
    FlexboxLayout historyLayout;
    @BindView(R.id.view_stub)
    ViewStub viewStub;
    @BindView(R.id.layout_history)
    View historyView;
    RecyclerView recyclerView;
    RecyclerAdapter searchAdapter;


    SearchContract.Presenter mPresenter;

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onViewInflated() {
        ButterKnife.bind(this);
        new SearchPresenter(this, this);
        searchView.requestFocus();
    }

    @Override
    protected void registerListener() {

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
        for (int i = 0; i < hots.length; i++) {
            String title = hots[i];
            TextView textView = new TextView(this);
            textView.setBackgroundResource(R.drawable.ic_round_rect_stroke_bg);
            textView.setText(title);
            textView.setTextSize(12);
            textView.setPadding(15, 15, 15, 15);
            hotLayout.addView(textView);
            ViewGroup.LayoutParams lp = textView.getLayoutParams();
            if (lp instanceof FlexboxLayout.LayoutParams) {
                FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) lp;
                params.setMargins(10, 0, 10, 15);
            }
        }
    }

    @Override
    public void showQuery(List<Movie> movies) {
        initRecyclerView();
        searchAdapter.setItems(movies);
    }

    void initRecyclerView() {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) viewStub.inflate();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            searchAdapter = new TheaterMovieAdapter(this, TheaterMovieAdapter.TYPE_IN_THEATER);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(searchAdapter);
        }
    }

    @OnEditorAction(R.id.et_search)
    boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (searchView.getEditableText() != null) {
                String keyWords = searchView.getEditableText().toString();
                mPresenter.search(keyWords);
                hideSoftKeyBoard();
                searchView.clearFocus();
                return true;
            }
        }
        return false;
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = Utils.getSystemService(this, INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }
}
