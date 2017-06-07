package cherry.android.douban.detail.header;

import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.AbstractHeader;
import cherry.android.douban.model.Movie;
import cherry.android.douban.widget.ExpandableTextView;

/**
 * Created by Administrator on 2017/6/7.
 */

public class SummaryDetailHeader extends AbstractHeader<Movie> {
    @BindView(R.id.tv_summary)
    ExpandableTextView textView;

    public SummaryDetailHeader(ViewGroup parent) {
        super(parent, R.layout.layout_movie_detail_summary_detail);
        ButterKnife.bind(this, mItemView);
    }

    @Override
    public void updateHeader(Movie movie) {
        textView.setText(movie.getSummary());
    }
}
