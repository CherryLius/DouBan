package cherry.android.douban.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MoviePerson;
import cherry.android.douban.recycler.CommonAdapter;
import cherry.android.douban.util.Utils;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TheaterMovieAdapter extends CommonAdapter<Movie, TheaterMovieAdapter.TheaterHolder> {

    public static final int TYPE_IN_THEATER = 1001;
    public static final int TYPE_COMING_SOON = 1002;

    private Context mContext;
    private int mType;

    @IntDef({TYPE_IN_THEATER, TYPE_COMING_SOON})
    public @interface TheaterType {
    }

    public TheaterMovieAdapter(Context context, @TheaterMovieAdapter.TheaterType int type) {
        super(R.layout.item_theater_movie);
        mContext = context;
        mType = type;
    }

    @Override
    public void convert(TheaterHolder holder, Movie movie, int position) {
        Glide.with(holder.itemView)
                .load(movie.getImages().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(holder.imageView);
        holder.textView.setText(movie.getTitle());
        Movie.Rating movieRating = movie.getRating();
        if (movieRating.getAverage() > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            float rating = (float) (movieRating.getAverage() / movieRating.getMax() * holder.ratingBar.getNumStars());
            holder.ratingBar.setRating(rating);
            holder.ratingView.setText(movie.getRating().getAverage() + "");
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.ratingView.setText(R.string.no_rating);
        }
        holder.directorView.setText(mContext.getString(R.string.prefix_director,
                list2String(movie.getDirectors())));
        holder.castView.setText(mContext.getString(R.string.prefix_cast,
                list2String(movie.getCasts())));
        int resId = mType == TYPE_IN_THEATER ? R.string.suffix_watching : R.string.suffix_want_watching;
        holder.watchingView.setText(mContext.getString(resId, movie.getCollectCount()));
    }

    @Override
    protected TheaterHolder createViewHolder(View itemView) {
        return new TheaterHolder(itemView);
    }

    private String list2String(List<MoviePerson> persons) {
        return Utils.list2String(persons, new Utils.IPicker<MoviePerson>() {
            @Override
            public String pick(MoviePerson moviePerson) {
                return moviePerson.getName();
            }
        });
    }

    public List<Movie> getCurrentMovies() {
        return mDataList;
    }

    static class TheaterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView textView;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.tv_rating_average)
        TextView ratingView;
        @BindView(R.id.tv_director)
        TextView directorView;
        @BindView(R.id.tv_cast)
        TextView castView;
        @BindView(R.id.tv_watching)
        TextView watchingView;

        public TheaterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
