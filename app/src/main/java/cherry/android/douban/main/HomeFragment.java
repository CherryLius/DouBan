package cherry.android.douban.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.adapter.TheaterMovieAdapter;
import cherry.android.douban.base.BaseFragment;
import cherry.android.douban.model.Movie;
import cherry.android.douban.recycler.DividerItemDecoration;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_0)
    RecyclerView hotRecyclerView;
    @BindView(R.id.recycler_1)
    RecyclerView comingRecyclerView;
    AppCompatActivity mActivity;

    HomeContract.Presenter mPresenter;
    TheaterMovieAdapter mTheaterAdapter;
    TheaterMovieAdapter mComingSoonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        new HomePresenter(this);
        mTheaterAdapter = new TheaterMovieAdapter();
        mComingSoonAdapter = new TheaterMovieAdapter();
        initRecyclerView(hotRecyclerView, mTheaterAdapter);
        initRecyclerView(comingRecyclerView, mComingSoonAdapter);
        mPresenter.loadMovieInTheater();
        mPresenter.loadComingSoon();
    }

    void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (toolbar == null)
            return;
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
            mActivity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity != null) {
            mActivity.setSupportActionBar(null);
            mActivity = null;
        }
    }

    @Override
    public void setPresenter(@NonNull HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTheaterMovie(List<Movie> movies) {
        if (movies != null) {
            mTheaterAdapter.showMovies(movies);
        }
    }

    @Override
    public void showComingSoon(List<Movie> movies) {
        if (movies != null) {
            mComingSoonAdapter.showMovies(movies);
        }
    }
}
