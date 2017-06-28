package cherry.android.douban.celebrity.delegate;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.util.Utils;
import cherry.android.recycler.ItemViewDelegate;

/**
 * Created by LHEE on 2017/6/28.
 */

public class WorksItemDelegate implements ItemViewDelegate<MovieCelebrity.Works, WorksItemDelegate.WorksHolder> {

    @NonNull
    @Override
    public WorksHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_works, parent, false);
        return new WorksHolder(itemView);
    }

    @Override
    public void convert(@NonNull WorksHolder holder, MovieCelebrity.Works works, int position) {
        Movie movie = works.getSubject();
        Glide.with(holder.itemView)
                .load(movie.getImages().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(holder.imageView);
        holder.roleView.setText(Utils.list2String(works.getRoles()));
        holder.nameView.setText(movie.getTitle());
        Movie.Rating movieRating = movie.getRating();
        if (movieRating.getAverage() > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            float rating = (float) (movieRating.getAverage() / movieRating.getMax() * holder.ratingBar.getNumStars());
            holder.ratingBar.setRating(rating);
            holder.averageView.setText(movie.getRating().getAverage() + "");
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.averageView.setText(R.string.no_rating);
        }
        holder.dateView.setText(movie.getYear());
    }

    static class WorksHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView nameView;
        @BindView(R.id.tv_role)
        TextView roleView;
        @BindView(R.id.tv_date)
        TextView dateView;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.tv_rating_average)
        TextView averageView;

        public WorksHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
