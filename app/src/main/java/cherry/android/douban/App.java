package cherry.android.douban;

import android.app.Application;
import android.content.Context;

import cherry.android.douban.util.Logger;
import cherry.android.router.api.RouteMeta;
import cherry.android.router.api.Router;
import cherry.android.router.api.intercept.IInterceptor;

/**
 * Created by Administrator on 2017/6/2.
 */

public class App extends Application {
    private static final String TAG = "App";
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Router.openDebug();
        Router.openLog(true, false);
        Router.init(this);
        Router.addGlobalInterceptor(new IInterceptor() {
            @Override
            public boolean intercept(RouteMeta routeMeta) {
                Logger.i(TAG, "[GlobalIntercept] url=" + routeMeta.getUri()
                        + ",class=" + routeMeta.getDestination());
                return false;
            }
        });
    }

    public static Context getContext() {
        return sContext;
    }
}
