package cherry.android.douban.rank.delegate;

import android.content.Context;
import android.support.annotation.NonNull;
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
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MoviePerson;
import cherry.android.douban.model.MovieWrapper;
import cherry.android.douban.util.Utils;
import cherry.android.recycler.ItemViewDelegate;

/**
 * Created by Administrator on 2017/6/20.
 */

public class NorthRankDelegate implements ItemViewDelegate<MovieWrapper, NorthRankDelegate.NorthHolder> {

    @NonNull
    @Override
    public NorthHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_north_rank, parent, false);
        return new NorthHolder(itemView);
    }

    @Override
    public void convert(@NonNull NorthHolder holder, MovieWrapper movieWrapper, int position) {
        final Context context = holder.itemView.getContext();
        final Movie movie = movieWrapper.getMovie();
        holder.number.setText(position + "");
        Glide.with(context).load(movie.getImages().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(holder.imageView);
        holder.name.setText(movie.getTitle());
        Movie.Rating movieRating = movie.getRating();
        if (movieRating.getAverage() > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            float rating = (float) (movieRating.getAverage() / movieRating.getMax() * holder.ratingBar.getNumStars());
            holder.ratingBar.setRating(rating);
            holder.average.setText(movie.getRating().getAverage() + "");
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.average.setText(R.string.no_rating);
        }
        holder.director.setText(context.getString(R.string.prefix_director,
                list2String(movie.getDirectors())));
        holder.cast.setText(context.getString(R.string.prefix_cast,
                list2String(movie.getCasts())));
    }

    private String list2String(List<MoviePerson> persons) {
        return Utils.list2String(persons, new Utils.IPicker<MoviePerson>() {
            @Override
            public String pick(MoviePerson moviePerson) {
                return moviePerson.getName();
            }
        });
    }

    static class NorthHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_number)
        TextView number;
        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.tv_rating_average)
        TextView average;
        @BindView(R.id.tv_director)
        TextView director;
        @BindView(R.id.tv_cast)
        TextView cast;

        public NorthHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
