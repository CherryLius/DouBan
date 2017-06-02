package cherry.android.douban.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.model.Movie;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TheaterMovieAdapter extends RecyclerView.Adapter<TheaterMovieAdapter.TheaterHolder> {

    private List<Movie> mMovies;

    @Override
    public TheaterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater_movie, parent, false);
        return new TheaterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheaterHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Glide.with(holder.itemView).load(movie.getImages().getLarge()).into(holder.imageView);
        holder.textView.setText(movie.getTitle());
        Movie.Rating movieRating = movie.getRating();
        if (movieRating.getAverage() > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            float rating = (float) (movieRating.getAverage() / movieRating.getMax() * holder.ratingBar.getNumStars());
            holder.ratingBar.setRating(rating);
            holder.ratingTextView.setText(movie.getRating().getAverage() + "");
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.ratingTextView.setText(R.string.no_rating);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public void showMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    static class TheaterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView textView;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.tv_rating_average)
        TextView ratingTextView;

        public TheaterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
