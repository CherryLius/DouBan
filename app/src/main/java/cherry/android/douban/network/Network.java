package cherry.android.douban.network;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import cherry.android.douban.App;
import cherry.android.douban.network.api.GankIOApi;
import cherry.android.douban.network.api.MovieApi;
import cherry.android.douban.util.Utils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/2.
 */

public class Network {
    private MovieApi mMovieApi;
    private GankIOApi mGankIOApi;
    private static Network _instance;
    private static ReentrantLock mLock = new ReentrantLock(true);

    public static Network get() {
        if (_instance != null) {
            return _instance;
        }
        mLock.lock();
        if (_instance == null) {
            _instance = new Network();
        }
        mLock.unlock();
        return _instance;
    }

    public MovieApi getMovieApi() {
        if (mMovieApi == null) {
            File cacheFile = new File(App.getContext().getCacheDir(), "cache_dou_ban_movie");
            Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(getInterceptor())
                    .addNetworkInterceptor(getNetworkInterceptor())
                    .addInterceptor(loggingInterceptor())
                    .cache(cache)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(NetworkConstants.DOU_BAN_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mMovieApi = retrofit.create(MovieApi.class);
        }
        return mMovieApi;
    }

    public GankIOApi getGankIOApi() {
        if (mGankIOApi == null) {
            File cacheFile = new File(App.getContext().getCacheDir(), "cache_gank_io");
            Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(getInterceptor())
                    .addNetworkInterceptor(getNetworkInterceptor())
                    .addInterceptor(loggingInterceptor())
                    .cache(cache)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(NetworkConstants.GANK_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mGankIOApi = retrofit.create(GankIOApi.class);
        }
        return mGankIOApi;
    }

    private Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!Utils.isNetworkAvailable(App.getContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    private Interceptor getNetworkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (Utils.isNetworkAvailable(App.getContext())) {
                    int maxAge = 60;//1 min
                    response = response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    int maxStale = 7 * 24 * 60 * 60;//1 week
                    response = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
    }

    private Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
