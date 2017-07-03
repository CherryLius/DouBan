package cherry.android.douban.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import cherry.android.douban.R;
import cherry.android.douban.widget.CustomGridItem;
import cherry.android.douban.widget.CustomGridLayout;

import static cherry.android.douban.App.getContext;

/**
 * Created by Administrator on 2017/6/30.
 */

public class CustomGridActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_grid);
    }
}
