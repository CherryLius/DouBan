package cherry.android.douban.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.ptr.AbstractRefreshHeader;
import cherry.android.ptr.Common;
import cherry.android.ptr.OnStateChangedListener;

import static cherry.android.ptr.Common.STATE_COMPLETE;
import static cherry.android.ptr.Common.STATE_REFRESHING;

/**
 * Created by Administrator on 2017/6/23.
 */

public class JDRefreshHeader extends AbstractRefreshHeader {


    private SimpleDateFormat mDateFormat;
    private JDHolder mHolder;
    private AnimationDrawable mAnimation;

    public JDRefreshHeader(@NonNull Context context) {
        super(context, R.layout.layout_jd_refresh_header);
        mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        mHolder = new JDHolder(mHeaderView);
        mHolder.timeView.setText(context.getString(cherry.android.ptr.R.string.recent_refresh,
                formatDateTime(System.currentTimeMillis())));
    }

    private String formatDateTime(long time) {
        if (time == 0) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    @Override
    public OnStateChangedListener getStateChangedListener() {
        return this;
    }

    @Override
    public void onPositionChanged(float percent, @Common.State int state) {
        if (percent > 1) {
            percent = 1;
        }
        if (state != STATE_COMPLETE
                && state != STATE_REFRESHING) {
            mHolder.goodsView.setAlpha(percent);
            mHolder.goodsView.setScaleX(percent);
            mHolder.goodsView.setScaleY(percent);

            mHolder.peopleView.setAlpha(percent);
            mHolder.peopleView.setScaleX(percent);
            mHolder.peopleView.setScaleY(percent);
            mHolder.peopleView.setTranslationX(-100 * (1 - percent));
        }
    }


    @Override
    public void onIdle() {
        mHolder.peopleView.setImageResource(R.mipmap.ic_jd_people_0);
        mHolder.goodsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPullToRefresh() {
        mHolder.statusView.setText("下拉刷新");
    }

    @Override
    public void onReleaseToRefresh() {
        mHolder.statusView.setText("释放刷新");
    }

    @Override
    public void onRefreshing() {
        mHolder.statusView.setText("刷新中");
        mHolder.goodsView.setVisibility(View.GONE);
        mHolder.peopleView.setImageResource(R.drawable.anim_jd_runing_man);
        mAnimation = (AnimationDrawable) mHolder.peopleView.getDrawable();
        if (mAnimation != null) {
            mAnimation.start();
        }
    }

    @Override
    public void onComplete() {
        mHolder.statusView.setText("更新完成");
        mHolder.timeView.setText(mContext.getString(cherry.android.ptr.R.string.recent_refresh,
                formatDateTime(System.currentTimeMillis())));
        if (mAnimation != null && mAnimation.isRunning())
            mAnimation.stop();
    }

    static class JDHolder {

        @BindView(R.id.iv_jd_people)
        ImageView peopleView;
        @BindView(R.id.iv_jd_goods)
        ImageView goodsView;
        @BindView(R.id.tv_refresh_status)
        TextView statusView;
        @BindView(R.id.tv_refresh_time)
        TextView timeView;

        public JDHolder(View contentView) {
            ButterKnife.bind(this, contentView);
        }

    }
}
