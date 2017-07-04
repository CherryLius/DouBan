package cherry.android.douban.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cherry.android.banner.Banner;
import cherry.android.banner.Function;
import cherry.android.banner.LoopViewPager;
import cherry.android.douban.R;

/**
 * Created by Administrator on 2017/7/4.
 */

public class BannerActivity extends AppCompatActivity {

    //one piece, naruto, attack on titan, dragon ball, bleach
    private static final String[] URLS = {"http://imgsrc.baidu.com/forum/pic/item/6b254190f603738d4fcd7dadb31bb051f919ec5b.jpg",
            "http://www.bz55.com/uploads/allimg/150309/139-150309161302.jpg",
            "http://d.3987.com/sydz_130820/013.jpg",
            "http://pic35.nipic.com/20131108/8054625_084759432147_2.jpg",
            "http://g.hiphotos.baidu.com/zhidao/pic/item/38dbb6fd5266d0169f17ec4b972bd40735fa3542.jpg"};

    Banner banner;
    LoopViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_banner);
        banner = (Banner) findViewById(R.id.banner);
        viewPager = (LoopViewPager) findViewById(R.id.view_pager);
        banner.setAdapter(new MyPagerAdapter());
        viewPager.setBoundaryCaching(true);
        viewPager.setAdapter(new MyPagerAdapter());
        List<String> list = new ArrayList<>();
        list.add("12345");
        list.add("23456");
        list.add("23456");
        list.add("23456");
        list.add("23456");
        banner.setTitles(list, new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s;
            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return URLS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(R.mipmap.ic_launcher);
            container.addView(imageView);
            return imageView;
//            TextView textView = new TextView(container.getContext());
//            textView.setText("position=" + position);
//            container.addView(textView);
//            return textView;
        }
    }
}
