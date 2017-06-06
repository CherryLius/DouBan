package cherry.android.douban;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.recycler.CommonAdapter;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.douban.recycler.wrapper.HeaderAndFooterWrapper;

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
        Adapter adapter = new Adapter(list);

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
            textView.setText(s);
        }

        @Override
        protected ViewHolder createViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }
    }
}
