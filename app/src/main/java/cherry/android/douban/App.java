package cherry.android.douban;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/6/2.
 */

public class App extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
