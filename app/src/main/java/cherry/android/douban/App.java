package cherry.android.douban;

import android.app.Application;
import android.content.Context;

import cherry.android.douban.util.Logger;
import cherry.android.router.api.Router;
import cherry.android.router.api.intercept.IInterceptor;
import cherry.android.router.api.request.Request;

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
        Router.openLog(true, true);
        Router.init(this);
        Router.addGlobalInterceptor(new IInterceptor() {
            @Override
            public boolean intercept(Request request) {
                Logger.i(TAG, "[GlobalIntercept] url=" + request.getUri()
                        + ",class=" + request.getDestination());
                return false;
            }
        });
    }

    public static Context getContext() {
        return sContext;
    }
}
