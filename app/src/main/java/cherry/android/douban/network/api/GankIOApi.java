package cherry.android.douban.network.api;

import cherry.android.douban.model.GankData;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/7/6.
 */

public interface GankIOApi {

    @GET("data/福利/{count}/{page}")
    Observable<GankData> gank(@Path("count") int count,
                              @Path("page") int page);
}
