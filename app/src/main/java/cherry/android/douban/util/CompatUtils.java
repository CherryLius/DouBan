package cherry.android.douban.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

/**
 * Created by Administrator on 2017/6/12.
 */

public final class CompatUtils {
    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        Drawable drawable = AppCompatResources.getDrawable(context, id);
        return drawable;
    }

    public static Drawable getDrawable(Context context, @DrawableRes int id, @ColorInt int tint) {
        Drawable drawable = getDrawable(context, id);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, tint);
        return drawable;
    }

    /**
     * VectorDrawableCompat.create
     * more bugs.
     *
     * 1.gradle add
     * defaultConfig {
     *      vectorDrawables.useSupportLibrary = true
     * }
     * 2.Activity Add static block
     * static {
     *     AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
     * }
     * 3.ImageView or ImageButton can use Vector with app:srcCompat="@drawable/your_vector"
     * 4.TextView or Drawable should wrap with a drawable xml.
     * exp:
     * <pre>
     * &lt;selector xmlns:android=&quot;...&quot;&gt;
     *     &lt;item android:state_checked=&quot;true&quot;
     *           android:drawable=&quot;@drawable/vector_checked_icon&quot; /&gt;
     *     &lt;item android:drawable=&quot;@drawable/vector_icon&quot; /&gt;
     * &lt;/selector&gt;
     *
     * &lt;TextView
     *         ...
     *         android:drawableLeft=&quot;@drawable/vector_state_list_icon&quot; /&gt;
     * </pre>
     * @param context
     * @param id
     * @return
     */

    public static Drawable getVectorDrawable(Context context, @DrawableRes int id) {
        return VectorDrawableCompat.create(context.getResources(), id, context.getTheme());
    }

    public static Drawable getVectorDrawable(Context context, @DrawableRes int id, @ColorInt int tint) {
        Drawable drawable = getVectorDrawable(context, id);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, tint);
        return drawable;
    }
}
