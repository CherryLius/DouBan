package cherry.android.douban;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cherry.android.douban.widget.FixedSwipeRefreshLayout;
import cherry.android.douban.widget.JDRefreshHeader;
import cherry.android.ptr.NestedPullRefreshLayout;
import cherry.android.ptr.OnRefreshListener;
import cherry.android.recycler.ItemViewDelegate;
import cherry.android.recycler.RecyclerAdapter;
import cherry.android.recycler.ViewChooser;
import cherry.android.recycler.ViewHolder;

public class NestedScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    NestedPullRefreshLayout pullRefreshLayout;
    FixedSwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView1;
    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scrolling);
        pullRefreshLayout = (NestedPullRefreshLayout) findViewById(R.id.layout_pull_to_refresh);
        pullRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefreshLayout.refreshComplete();
                    }
                }, 3000);
            }
        });
        pullRefreshLayout.setRefreshHeader(new JDRefreshHeader(this));
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter = new RecyclerAdapter();
        adapter.addDelegate(String.class)
                .bindDelegate(new DefaultItemViewDelegate(), new DefaultItemViewDelegate1())
                .to(new ViewChooser() {
                    @Override
                    public Class<? extends ItemViewDelegate> choose(Object o, int position) {
                        return position % 2 == 0 ? DefaultItemViewDelegate.class : DefaultItemViewDelegate1.class;
                    }
                });
        adapter.addDelegate(Integer.class, new ItemViewDelegate<Integer, ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
                View itemView = inflater.inflate(R.layout.item_app_simple, parent, false);
                return new ViewHolder(itemView);
            }

            @Override
            public void convert(ViewHolder holder, Integer integer, int position) {
                TextView tv = holder.findView(R.id.text);
                tv.setText("Integer item=" + integer);
            }
        });
        Random random = new Random();
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (random.nextInt(2) == 0) {
                items.add(i);
            } else {
                items.add("item=" + i);
            }
        }
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);


        swipeRefreshLayout = (FixedSwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new FixedSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_0);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(adapter);

        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                //Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_OFF_TIMEOUT, 1000);
                swipeRefreshLayout.setVisibility(View.GONE);
                pullRefreshLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn2:
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                pullRefreshLayout.setVisibility(View.GONE);
                break;
        }
    }

    static class DefaultItemViewDelegate implements ItemViewDelegate<String, ViewHolder> {

        @NonNull
        @Override
        public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View itemView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void convert(ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text2);
            textView.setText("string item=" + s);
            textView = holder.findView(android.R.id.text1);
            textView.setText("Title:");
        }
    }

    static class DefaultItemViewDelegate1 implements ItemViewDelegate<String, ViewHolder> {

        @NonNull
        @Override
        public ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.item_app_simple_2, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void convert(ViewHolder holder, String s, int position) {
            TextView tv = holder.findView(R.id.tv);
            tv.setText(s);
        }
    }
}
