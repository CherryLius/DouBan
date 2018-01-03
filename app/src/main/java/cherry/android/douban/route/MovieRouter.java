package cherry.android.douban.route;

import java.util.concurrent.locks.ReentrantLock;

import cherry.android.router.api.Router;

/**
 * Created by ROOT on 2017/7/27.
 */

public class MovieRouter {
    private RouteService routeService;
    private static MovieRouter _instance;

    private static ReentrantLock mLock = new ReentrantLock(true);

    public static MovieRouter get() {
        if (_instance != null) {
            return _instance;
        }
        mLock.lock();
        if (_instance == null) {
            _instance = new MovieRouter();
        }
        mLock.unlock();
        return _instance;
    }

    private MovieRouter() {
    }

    public RouteService getRouteService() {
        if (routeService == null) {
            routeService = Router.create(RouteService.class);
        }
        return routeService;
    }
}
