package cherry.android.douban.route;

import android.content.Context;
import android.support.annotation.NonNull;

import cherry.android.douban.detail.MovieDetailActivity;
import cherry.android.douban.web.WebActivity;
import cherry.android.router.annotations.ClassName;
import cherry.android.router.annotations.Query;
import cherry.android.router.annotations.URL;

/**
 * Created by ROOT on 2017/7/27.
 */

public interface RouteService {
    @URL("/activity/search")
    void startSearchActivity();

    @URL("/activity/web")
    void startWebActivity(@Query("ticket_url") String ticketUrl);

    @ClassName(WebActivity.class)
    void startWebActivity(@NonNull Context context,
                          @Query("ticket_url") String ticketUrl);

    @URL("/activity/movie/top")
    void startTopMovieActivity(Context context);

    @URL("/activity/movie/detail")
    void startMovieDetailActivity(@Query("id") String id,
                                  @Query("name") String name,
                                  @Query("imageUrl") String image);

    @ClassName(MovieDetailActivity.class)
    void startMovieDetailActivity(@NonNull Context context,
                                  @Query("id") String id,
                                  @Query("name") String name,
                                  @Query("imageUrl") String image);

//    @URL("/activity/celebrity/detail")
//    void startCelebrityActivity(@Query("id") String id,
//                                @Query("name") String name,
//                                @Query("imageUrl") String image);

    @URL("/activity/celebrity/detail")
    void startCelebrityActivity(@NonNull Context context,
                                @Query("id") String id,
                                @Query("name") String name,
                                @Query("imageUrl") String image);
}
