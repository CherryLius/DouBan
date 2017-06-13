package cherry.android.douban.detail.header;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cherry.android.douban.R;
import cherry.android.douban.base.AbstractHeader;
import cherry.android.douban.model.Movie;
import cherry.android.douban.util.Utils;
import cherry.android.router.api.Router;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MovieDetailHeader extends AbstractHeader<Movie> {
    @BindView(R.id.iv_image)
    ImageView imageView;
    @BindView(R.id.tv_alias_name)
    TextView aliasView;
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
    @BindView(R.id.layout_ticket_buy)
    View ticketBuyView;

    private Movie movie;

    public MovieDetailHeader(ViewGroup parent) {
        super(parent, R.layout.layout_movie_detail_header);
        ButterKnife.bind(this, mItemView);
    }

    public ImageView getMoviePosterView() {
        return imageView;
    }

    public View getTicketBuyView() {
        return ticketBuyView;
    }

    @Override
    public void updateHeader(Movie movie) {
        this.movie = movie;
        Glide.with(mContext).load(movie.getImages().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(imageView);
        nameView.setText(movie.getTitle());
        if (movie.getAka().size() == 0) {
            aliasView.setVisibility(View.GONE);
        } else {
            aliasView.setText(Utils.list2String(movie.getAka()));
        }
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append(movie.getYear())
                .append('/')
                .append(Utils.list2String(movie.getGenres()))
                .append('\n')
                .append("原名：")
                .append(movie.getOriginalTitle())
                .append('\n')
                .append("上映时间：")
                .append(movie.getYear())
                .append('\n')
                .append("地区：")
                .append(Utils.list2String(movie.getCountries()));
        detailView.setText(detailBuilder.toString());
        Movie.Rating movieRating = movie.getRating();
        if (movieRating.getAverage() == 0) {
            ratingView.setVisibility(View.GONE);
            ratingBar.setRating(0);
            collectView.setText(R.string.rating_count_not_enough);
        } else {
            float rating = (float) (movieRating.getAverage() / movieRating.getMax() * ratingBar.getNumStars());
            ratingBar.setRating(rating);
            ratingView.setText(movieRating.getAverage() + "");
            collectView.setText(mContext.getString(R.string.label_collect_count, movie.getCollectCount()));
        }
    }

    @OnClick({R.id.layout_ticket_buy, R.id.iv_image})
    void onClick(View view) {
        if (movie == null)
            return;
        switch (view.getId()) {
            case R.id.layout_ticket_buy:
                Router.build("movie://activity/web?ticket_url=" + movie.getScheduleUrl()).open(mContext);
                break;
            case R.id.iv_image:
                Router.build("movie://activity/web?ticket_url=" + movie.getMobileUrl()).open(mContext);
                break;
        }
    }
}
