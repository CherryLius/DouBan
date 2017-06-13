package cherry.android.douban.sticker;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by Administrator on 2017/6/13.
 */

public final class StickyHeaderHelper {

    public static <V extends ViewGroup> void onScroll(float scrollX, float scrollY, V scrollView,
                                                      IStickyHeader stickyHeader) {
        if (stickyHeader == null)
            return;
        if (scrollView == null)
            return;
        final ViewParent parent = scrollView.getParent();
        if (!(parent instanceof ViewGroup))
            return;

        final View view = stickyHeader.getStickyRelatedView();
        final float threshold = stickyHeader.thresholdValue();
        final View stickyView = stickyHeader.getStickyView();
        final ViewGroup viewGroup = (ViewGroup) parent;
        int index = viewGroup.indexOfChild(stickyView);
        if (scrollY + threshold > view.getTop()) {
            if (index == -1) {
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
                marginLayoutParams.topMargin = (int) threshold;
                viewGroup.addView(stickyView, marginLayoutParams);
            }
        } else {
            if (index != -1)
                viewGroup.removeView(stickyView);
        }
    }
}