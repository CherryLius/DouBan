package cherry.android.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/3.
 */

public class Banner extends ViewGroup {

    private LoopViewPager mLoopViewPager;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
