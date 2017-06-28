package cherry.android.douban.debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.recycler.CommonAdapter;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.ViewChooser;
import cherry.android.recycler.ViewHolder;
import cherry.android.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.toast.Toaster;

public class HeaderFooterActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
//    @BindView(R.id.layout_pull_to_refresh)
//    PullToRefreshLayout mRefreshLayout;

    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_header_footer);
        ButterKnife.bind(this);

//        mRefreshLayout.setRefreshHeader(new JDRefreshHeader(this));
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toaster.iSuccess(HeaderFooterActivity.this, "刷新成功").show();
//                        mRefreshLayout.refreshComplete();
//                    }
//                }, 3000);
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item === " + i);
        }
//        Adapter adapter = new Adapter(list);
        RecyclerAdapter adapter = new RecyclerAdapter(list);
        adapter.addDelegate(String.class).bindDelegate(new RecyclerDelegate(), new SimpleDelegate())
                .to(new ViewChooser() {
                    @Override
                    public Class<? extends ItemViewDelegate> choose(Object o, int position) {
                        return position == 5 ? RecyclerDelegate.class : SimpleDelegate.class;
                    }
                });
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, RecyclerView.ViewHolder holder, int position) {
                if (position % 5 == 0) {
                    Toaster.iError(HeaderFooterActivity.this, "Header Footer Activity " + position).show();
                    startActivity(new Intent(HeaderFooterActivity.this, NestedScrollingActivity.class));
                    return;
                }
                if (position % 5 == 1) {
                    Toaster.iInfo(HeaderFooterActivity.this, "Header Footer Activity " + position).show();
                    startActivity(new Intent(HeaderFooterActivity.this, ListViewActivity.class));
                    return;
                }
                if (position % 5 == 2) {
                    Toaster.iSuccess(HeaderFooterActivity.this, "Header Footer Activity " + position).show();
                    return;
                }
                if (position % 5 == 3) {
                    Toaster.iWarning(HeaderFooterActivity.this, "Header Footer Activity " + position).show();
                    return;
                }
                if (position % 5 == 4) {
                    Toaster.normal(HeaderFooterActivity.this, "Header Footer Activity " + position).show();
                    return;
                }
            }
        });

        mWrapper = new HeaderAndFooterWrapper(adapter);
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_movie_detail_header, recyclerView, false);
        TextView textView = new TextView(this);
        textView.setText("It is A Header");
        mWrapper.addHeaderView(textView);
        mWrapper.addHeaderView(headerView);

        recyclerView.setAdapter(mWrapper);
    }

    static class Adapter extends CommonAdapter<String, ViewHolder> {

        public Adapter(List<String> data) {
            super(data, android.R.layout.simple_list_item_1);
        }

        @Override
        public void convert(ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text1);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    120));
            textView.setText(s);
        }

        @Override
        protected ViewHolder createDefaultViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }
    }

    static class SimpleDelegate implements ItemViewDelegate<String, ViewHolder> {

        @NonNull
        @Override
        public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void convert(ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text1);
            textView.setText(s);
        }
    }

    static class RecyclerDelegate implements ItemViewDelegate<String, RecyclerDelegate.DelegateHolder> {

        @NonNull
        @Override
        public DelegateHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.layout_recycler, parent, false);
            return new DelegateHolder(itemView);
        }

        @Override
        public void convert(RecyclerDelegate.DelegateHolder holder, String s, int position) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                list.add("item === " + i);
            }
            holder.recyclerView.setAdapter(new Adapter(list));
        }

        static class DelegateHolder extends ViewHolder {
            @BindView(R.id.recycler)
            RecyclerView recyclerView;

            public DelegateHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                ViewGroup.LayoutParams params = this.recyclerView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                this.recyclerView.setLayoutParams(params);
                this.recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }
}
