package cherry.android.douban.detail.header;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import cherry.android.douban.R;
import cherry.android.douban.model.Movie;
import cherry.android.douban.route.MovieRouter;
import cherry.android.douban.sticker.IStickyHeader;
import cherry.android.douban.util.Utils;

/**
 * Created by Administrator on 2017/6/13.
 */

public class TicketBuySticky implements IStickyHeader, View.OnClickListener {

    private View mStickyView;
    private View mRelatedView;
    private int mToolbarHeight;
    private Movie mMovie;

    public TicketBuySticky(Context context, View relatedView) {
        mStickyView = LayoutInflater.from(context).inflate(R.layout.layout_movie_detail_ticket_buy, null);
        mStickyView.setBackgroundColor(Color.WHITE);
        mRelatedView = relatedView;
        mToolbarHeight = Utils.getActionBarSize(context);
        mStickyView.setOnClickListener(this);
    }

    @NonNull
    @Override
    public View getStickyView() {
        return mStickyView;
    }

    @Override
    public float thresholdValue() {
        return mToolbarHeight;
    }

    @NonNull
    @Override
    public View getStickyRelatedView() {
        return mRelatedView;
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
    }

    @Override
    public void onClick(View v) {
        if (mMovie == null)
            return;
//        Router.build("movie://activity/web?ticket_url=" + mMovie.getScheduleUrl()).open();
        MovieRouter.get().getRouteService().startWebActivity(mMovie.getScheduleUrl());
    }
}
