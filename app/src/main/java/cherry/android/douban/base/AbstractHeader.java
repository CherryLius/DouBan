package cherry.android.douban.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/6/7.
 */

public abstract class AbstractHeader<T> implements BaseHeader<T> {
    protected Context mContext;
    protected View mItemView;

    public AbstractHeader(ViewGroup parent, @LayoutRes int layoutId) {
        mContext = parent.getContext();
        mItemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    @Override
    public View getItemView() {
        return mItemView;
    }
}
