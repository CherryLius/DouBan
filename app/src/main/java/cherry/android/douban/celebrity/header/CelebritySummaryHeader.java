package cherry.android.douban.celebrity.header;

import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.AbstractHeader;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.widget.ExpandableTextView;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CelebritySummaryHeader extends AbstractHeader<MovieCelebrity> {
    @BindView(R.id.tv_summary)
    ExpandableTextView summaryView;

    public CelebritySummaryHeader(ViewGroup parent) {
        super(parent, R.layout.layout_celebrity_summary);
        ButterKnife.bind(this, mItemView);
    }

    @Override
    public void updateHeader(MovieCelebrity data) {
        summaryView.setText("暂无简介");
    }
}
