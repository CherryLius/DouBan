package cherry.android.douban.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by Administrator on 2017/6/16.
 */

public final class PalettenHelper {

    public static void get(Bitmap bitmap, final Palette.PaletteAsyncListener listener) {
        Palette.from(bitmap).generate(listener);
    }

    public static void paletten(Context context, String url, final Palette.PaletteAsyncListener listener) {
        if (listener == null) return;
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                get(resource, listener);
            }
        };
        Glide.with(context).asBitmap().load(url).into(target);
    }
}
