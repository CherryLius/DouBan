package cherry.android.douban.detail;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cherry.android.douban.R;
import cherry.android.douban.base.BaseActivity;
import cherry.android.douban.util.Logger;
import cherry.android.router.annotations.Route;

/**
 * Created by LHEE on 2017/6/4.
 */
@Route("movie://activity/movie/detail")
public class MovieDetailActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, getTheme());
        DrawableCompat.setTint(drawable, Color.WHITE);
        toolbar.setNavigationIcon(drawable);
        toolbar.inflateMenu(R.menu.menu_movie_detail);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
