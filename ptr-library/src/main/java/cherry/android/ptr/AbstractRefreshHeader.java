package cherry.android.ptr;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/6/23.
 */

public abstract class AbstractRefreshHeader implements IRefreshHeader {
    protected Context mContext;
    protected View mHeaderView;

    public AbstractRefreshHeader(@NonNull Context context,
                                 @NonNull ViewGroup parent,
                                 @LayoutRes int layoutId) {
        mContext = context;
        mHeaderView = LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    @NonNull
    @Override
    public View getView() {
        return mHeaderView;
    }

    @Override
    public int getRefreshThreshold() {
        return mHeaderView.getMeasuredHeight();
    }
}
