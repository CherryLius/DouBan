package cherry.android.douban.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by LHEE on 2017/6/3.
 */

public class CenterDrawableTextView extends AppCompatTextView {
    public CenterDrawableTextView(Context context) {
        super(context);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] compoundDrawables = getCompoundDrawables();
        Drawable leftDrawable = compoundDrawables[0];
        if (leftDrawable != null) {
            int drawableWidth = leftDrawable.getIntrinsicWidth();
            int drawablePadding = getCompoundDrawablePadding();
            int textWith = (int) getPaint().measureText(getHint().toString());
            int translateX = (getWidth() - (drawableWidth + drawablePadding + textWith)) / 2;
            canvas.save();
            canvas.translate(translateX, 0);
        }
        super.onDraw(canvas);
    }
}
