package cherry.android.douban.rank.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.model.Movie;
import cherry.android.douban.model.RankMovies;
import cherry.android.recycler.ItemViewDelegate;

/**
 * Created by Administrator on 2017/6/19.
 */

public class RankDelegate implements ItemViewDelegate<Movie, RankDelegate.RankHolder> {

    @NonNull
    @Override
    public RankHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_rank_movie, parent, false);
        return new RankHolder(itemView);
    }

    @Override
    public void convert(RankHolder holder, Movie movie, int position) {
        holder.nameView.setText(movie.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(movie.getImages().getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(holder.imageView);
    }

    static class RankHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView nameView;

        public RankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
