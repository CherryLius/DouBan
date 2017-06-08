package cherry.android.douban.celebrity.header;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.AbstractHeader;
import cherry.android.douban.model.MovieCelebrity;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CelebrityHeader extends AbstractHeader<MovieCelebrity> {
    @BindView(R.id.iv_image)
    ImageView imageView;
    @BindView(R.id.tv_name)
    TextView nameView;
    @BindView(R.id.tv_detail)
    TextView detailView;
    @BindView(R.id.tv_collect_count)
    TextView collectView;

    public CelebrityHeader(ViewGroup parent) {
        super(parent, R.layout.layout_celebrity_header);
        ButterKnife.bind(this, mItemView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void updateHeader(MovieCelebrity data) {
        Glide.with(mContext).load(data.getAvatars().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(imageView);
        nameView.setText(data.getName());
        detailView.setText(data.getNameEn());
        collectView.setText(mContext.getString(R.string.label_collect_count, 25));
    }
}
