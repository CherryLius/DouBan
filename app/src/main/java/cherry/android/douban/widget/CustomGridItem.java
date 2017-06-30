package cherry.android.douban.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;

/**
 * Created by Administrator on 2017/6/30.
 */

public class CustomGridItem extends FrameLayout {

    @BindView(R.id.iv_grid)
    ImageView imageView;
    @BindView(R.id.tv_grid)
    TextView textView;

    public CustomGridItem(@NonNull Context context, CharSequence text, int drawableRes) {
        this(context, null);
        imageView.setImageResource(drawableRes);
        textView.setText(text);
    }

    public CustomGridItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGridItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_default_grid_item, this, true);
        ButterKnife.bind(this, view);
    }
}
