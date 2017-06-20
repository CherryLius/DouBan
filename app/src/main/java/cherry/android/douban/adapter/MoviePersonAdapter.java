package cherry.android.douban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.model.MoviePerson;
import cherry.android.recycler.CommonAdapter;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MoviePersonAdapter extends CommonAdapter<MoviePerson, MoviePersonAdapter.MoviePersonHolder> {

    private Context mContext;

    public MoviePersonAdapter(Context context, List<MoviePerson> data) {
        super(data, R.layout.item_movie_person);
        mContext = context;
    }

    @Override
    protected void convert(MoviePersonHolder holder, MoviePerson moviePerson, int position) {
        String imageUrl = moviePerson.getAvatars() != null ? moviePerson.getAvatars().getLarge() : "";
        Glide.with(mContext).load(imageUrl)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_movie_default))
                .into(holder.imageView);
        holder.nameView.setText(moviePerson.getName());
        holder.castView.setText(moviePerson.getId());
    }

    @Override
    protected MoviePersonHolder createDefaultViewHolder(View itemView) {
        return new MoviePersonHolder(itemView);
    }

    static class MoviePersonHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView nameView;
        @BindView(R.id.tv_cast)
        TextView castView;

        public MoviePersonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
