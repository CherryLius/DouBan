package cherry.android.douban.detail.header;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.AbstractHeader;
import cherry.android.douban.model.Movie;
import cherry.android.douban.util.Utils;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MovieDetailHeader extends AbstractHeader<Movie> {
    @BindView(R.id.iv_image)
    ImageView imageView;
    @BindView(R.id.tv_name)
    TextView nameView;
    @BindView(R.id.tv_detail)
    TextView detailView;
    @BindView(R.id.tv_rating_average)
    TextView ratingView;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_collect_count)
    TextView collectView;

    public MovieDetailHeader(ViewGroup parent) {
        super(parent, R.layout.layout_movie_detail_header);
        ButterKnife.bind(this, mItemView);
    }

    public ImageView getMoviePosterView() {
        return imageView;
    }

    @Override
    public void updateHeader(Movie movie) {
        Glide.with(mContext).load(movie.getImages().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(imageView);
        nameView.setText(movie.getTitle());
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append(movie.getYear())
                .append('/')
                .append(Utils.list2String(movie.getGenres()))
                .append('\n')
                .append("原名：")
                .append(movie.getOriginalTitle())
                .append('\n')
                .append("上映时间：")
                .append('\n')
                .append("片长：");
        detailView.setText(detailBuilder.toString());
        Movie.Rating movieRating = movie.getRating();
        float rating = (float) (movieRating.getAverage() / movieRating.getMax() * ratingBar.getNumStars());
        ratingBar.setRating(rating);
        ratingView.setText(movieRating.getAverage() + "");
        collectView.setText(mContext.getString(R.string.label_collect_count, movie.getCollectCount()));
    }
}
