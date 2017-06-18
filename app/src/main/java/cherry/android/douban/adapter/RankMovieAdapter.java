package cherry.android.douban.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cherry.android.douban.R;
import cherry.android.douban.model.RankMovies;
import cherry.android.douban.recycler.BaseAdapter;
import cherry.android.douban.recycler.ItemViewDelegate;
import cherry.android.douban.recycler.ViewHolder;
import cherry.android.douban.util.Logger;

/**
 * Created by LHEE on 2017/6/18.
 */

public class RankMovieAdapter extends BaseAdapter<RankMovies, ViewHolder> {

    public RankMovieAdapter() {
        addDelegate(new ItemViewDelegate<RankMovies, ViewHolder>() {
            @Override
            public int getViewLayoutId() {
                return R.layout.item_rank_title;
            }

            @Override
            public boolean isMatchViewType(RankMovies rankMovies, int position) {
                return rankMovies.getType() == RankMovies.TYPE_TITLE;
            }

            @Override
            public ViewHolder createViewHolder(View itemView) {
                return new ViewHolder(itemView);
            }

            @Override
            public void convert(ViewHolder holder, RankMovies rankMovies, int position) {
                TextView textView = holder.findView(R.id.tv_title);
                textView.setText(rankMovies.getTitle());
                TextView moreView = holder.findView(R.id.tv_more);
                if (rankMovies.getTitle() == null || !rankMovies.getTitle().contains("250")) {
                    moreView.setVisibility(View.GONE);
                }
            }
        });
        addDelegate(new ItemViewDelegate<RankMovies, ViewHolder>() {
            @Override
            public int getViewLayoutId() {
                return R.layout.item_rank_movie;
            }

            @Override
            public boolean isMatchViewType(RankMovies rankMovies, int position) {
                return rankMovies.getType() != RankMovies.TYPE_TITLE;
            }

            @Override
            public ViewHolder createViewHolder(View itemView) {
                return new ViewHolder(itemView);
            }

            @Override
            public void convert(ViewHolder holder, RankMovies rankMovies, int position) {
                ImageView imageView = holder.findView(R.id.iv_image);
                TextView nameView = holder.findView(R.id.tv_name);
                Logger.i("Test", "position=" + position + ",movie=" + rankMovies.getMovie());
                if (rankMovies.getMovie() != null) {
                    nameView.setText(rankMovies.getMovie().getTitle());
                    Glide.with(holder.itemView.getContext())
                            .load(rankMovies.getMovie().getImages().getLarge())
                            .into(imageView);
                }
            }
        });
    }
}
