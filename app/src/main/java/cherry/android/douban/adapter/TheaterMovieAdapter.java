package cherry.android.douban.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.listener.OnItemClickListener;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MoviePerson;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TheaterMovieAdapter extends RecyclerView.Adapter<TheaterMovieAdapter.TheaterHolder> {

    public static final int TYPE_IN_THEATER = 1001;
    public static final int TYPE_COMING_SOON = 1002;

    private List<Movie> mMovies;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private int mType;

    @IntDef({TYPE_IN_THEATER, TYPE_COMING_SOON})
    public @interface TheaterType {
    }

    public TheaterMovieAdapter(Context context, @TheaterType int type) {
        mContext = context;
        mType = type;
    }

    @Override
    public TheaterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater_movie, parent, false);
        return new TheaterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheaterHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
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
        holder.watchingView.setText(mContext.getString(resId, movie.getCollect_count()));
        if (mItemClickListener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public List<Movie> getCurrentMovies() {
        return mMovies;
    }

    public void showMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mItemClickListener = l;
    }

    private String list2String(List<MoviePerson> persons) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < persons.size(); i++) {
            builder.append(persons.get(i).getName());
            if (i < persons.size() - 1)
                builder.append('/');

        }
        return builder.toString();
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
