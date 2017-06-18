package cherry.android.douban.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/16.
 */

public final class PaletteHelper {

    public static void palette(Context context, String url, final PaletteCallback callback) {
        if (callback == null) return;
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                Logger.i("Test", "resource=" + resource);
                get(resource, callback);
            }
        };
        Glide.with(context).asBitmap().load(url).into(target);
    }

    private static void get(Bitmap bitmap, final PaletteCallback callback) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int color = getSwatchColor(palette, 0);
                if (callback != null)
                    callback.onGenerated(color);
            }
        });
    }

    public static int getSwatchColor(Palette palette, int defaultColor) {
        Palette.Swatch swatch = palette.getVibrantSwatch();
        List<Palette.Swatch> swatches = palette.getSwatches();
        if (swatch == null) {
            if (swatches != null && swatches.size() > 0) {
                Random random = new Random();
                int index = random.nextInt(swatches.size());
                swatch = swatches.get(index);
            }
        }
        if (swatch != null) {
            return swatch.getRgb();
        } else {
            return defaultColor;
        }
    }

    public interface PaletteCallback {
        void onGenerated(int color);
    }
}
