package cherry.android.douban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.model.MovieAvatars;
import cherry.android.douban.recycler.CommonAdapter;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MovieAdvanceAdapter extends CommonAdapter<MovieAvatars, MovieAdvanceAdapter.AdvanceHolder> {

    private Context mContext;

    public MovieAdvanceAdapter(Context context) {
        super(R.layout.item_movie_advance);
        mContext = context;
    }

    @Override
    protected void convert(AdvanceHolder holder, MovieAvatars movieAvatars, int position) {
        Glide.with(mContext).load(movieAvatars.getLarge())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(holder.imageView);
    }

    @Override
    protected AdvanceHolder createDefaultViewHolder(View itemView) {
        return new AdvanceHolder(itemView);
    }

    static class AdvanceHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public AdvanceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
