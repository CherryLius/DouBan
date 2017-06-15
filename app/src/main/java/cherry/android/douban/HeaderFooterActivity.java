package cherry.android.douban;

import android.os.Bundle;
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
import cherry.android.douban.recycler.BaseAdapter;
import cherry.android.douban.recycler.CommonAdapter;
import cherry.android.douban.recycler.ItemViewDelegate;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.douban.recycler.wrapper.HeaderAndFooterWrapper;
import cherry.android.douban.widget.ExpandableTextView;

public class HeaderFooterActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_footer);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("item === " + i);
        }
//        Adapter adapter = new Adapter(list);
        BaseAdapter adapter = new BaseAdapter(list);
        adapter.addDelegate(new RecyclerDelegate());
        adapter.addDelegate(new SimpleDelegate());

        mWrapper = new HeaderAndFooterWrapper(adapter);
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_movie_detail_header, recyclerView, false);
        TextView textView = new TextView(this);
        textView.setText("It is A Header");
        mWrapper.addHeaderView(textView);
        mWrapper.addHeaderView(headerView);
        mWrapper.addHeaderView(new ExpandableTextView(this));
        recyclerView.setAdapter(mWrapper);
    }

    static class Adapter<VH extends ViewHolder> extends CommonAdapter<String, VH> {

        public Adapter(List<String> data) {
            super(data, android.R.layout.simple_list_item_1);
        }

        @Override
        public void convert(VH holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text1);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    120));
            textView.setText(s);
        }

        @Override
        protected VH createDefaultViewHolder(View itemView) {
            return (VH) new ViewHolder(itemView);
        }
    }

    static class SimpleDelegate implements ItemViewDelegate<String, ViewHolder> {

        @Override
        public int getViewLayoutId() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public boolean isMatchViewType(String s, int position) {
            return position != 5;
        }

        @Override
        public ViewHolder createViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        @Override
        public void convert(ViewHolder holder, String s, int position) {
            TextView textView = holder.findView(android.R.id.text1);
            textView.setText(s);
        }
    }

    static class RecyclerDelegate implements ItemViewDelegate<String, RecyclerDelegate.DelegateHolder> {

        @Override
        public int getViewLayoutId() {
            return R.layout.layout_recycler;
        }

        @Override
        public boolean isMatchViewType(String s, int position) {
            return position == 5;
        }

        @Override
        public DelegateHolder createViewHolder(View itemView) {
            return new DelegateHolder(itemView);
        }

        @Override
        public void convert(DelegateHolder holder, String s, int position) {
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
                ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                recyclerView.setLayoutParams(params);
                recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }
}
