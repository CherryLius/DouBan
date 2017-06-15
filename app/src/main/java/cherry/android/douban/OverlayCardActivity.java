package cherry.android.douban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.recycler.CommonAdapter;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.douban.recycler.layout.OverlayCardLayoutManager;
import cherry.android.douban.util.Logger;

public class OverlayCardActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private CommonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_footer);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new OverlayCardLayoutManager());
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        recyclerView.setAdapter(mAdapter = new CommonAdapter<Integer, ViewHolder>(list, R.layout.item_overlay_card) {
            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {
                TextView textView = holder.findView(R.id.tv);
                textView.setText("" + integer);
            }

            @Override
            protected ViewHolder createDefaultViewHolder(View itemView) {
                return new ViewHolder(itemView);
            }
        });
        mAdapter.notifyDataSetChanged();
    }
}
