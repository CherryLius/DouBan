package cherry.android.douban.adapter;

import android.content.Context;
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
import cherry.android.douban.util.Utils;
import cherry.android.recycler.CommonAdapter;

/**
 * Created by LHEE on 2017/7/20.
 */

public class QueryAdapter extends CommonAdapter<Movie, QueryAdapter.QueryHolder> {

    public QueryAdapter() {
        super(R.layout.item_query_movie);
    }

    @Override
    protected void convert(QueryHolder holder, Movie movie, int position) {
        final Context context = holder.itemView.getContext();
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
        holder.directorView.setText(context.getString(R.string.prefix_director,
                list2String(movie.getDirectors())));
        holder.castView.setText(context.getString(R.string.prefix_cast,
                list2String(movie.getCasts())));
        StringBuilder builder = new StringBuilder();
        builder.append(movie.getYear())
                .append('/')
                .append(Utils.list2String(movie.getGenres()));
        holder.detailView.setText(builder.toString());
    }

//    @Override
//    protected QueryHolder createDefaultViewHolder(View itemView) {
//        return new QueryHolder(itemView);
//    }

    private String list2String(List<MoviePerson> persons) {
        return Utils.list2String(persons, new Utils.IPicker<MoviePerson>() {
            @Override
            public String pick(MoviePerson moviePerson) {
                return moviePerson.getName();
            }
        });
    }

    public List<Movie> getCurrentItems() {
        return (List<Movie>) mItems;
    }

    static class QueryHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.tv_detail)
        TextView detailView;

        public QueryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
