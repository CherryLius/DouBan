package cherry.android.douban.network.api;

import cherry.android.douban.model.Movie;
import cherry.android.douban.model.MovieCelebrity;
import cherry.android.douban.model.NorthAmericaMovie;
import cherry.android.douban.model.TheaterMovie;
import cherry.android.douban.model.NewWeeklyMovie;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/2.
 */

public interface MovieApi {
    //https://developers.douban.com/wiki/?title=movie_v2#works
    /*******        榜单        ********/
    /**
     * 正在热映
     *
     * @return
     */
    @GET("/v2/movie/in_theaters")
    Observable<TheaterMovie> movieInTheaters(@Query("start") int start,
                                             @Query("count") int count);

    /**
     * @param city city name or ID. example: 中文，如: “北京” 或者数字ID: 108288
     * @return
     */
    @GET("/v2/movie/in_theaters")
    Observable<TheaterMovie> movieInTheaters(@Query("city") String city);

    /**
     * 即将上映
     *
     * @param start
     * @param count
     * @return
     */
    @GET("/v2/movie/coming_soon")
    Observable<TheaterMovie> comingSoon(@Query("start") int start,
                                        @Query("count") int count);

    /**
     * top250
     *
     * @param start
     * @param count
     * @return
     */
    @GET("/v2/movie/top250")
    Observable<TheaterMovie> top250(@Query("start") int start,
                                    @Query("count") int count);

    /**
     * 口碑榜
     *
     * @return
     */
    @GET("/v2/movie/weekly")
    Observable<NewWeeklyMovie> weekly();

    /**
     * 新片榜
     *
     * @return
     */
    @GET("/v2/movie/new_movies")
    Observable<NewWeeklyMovie> newMovies();

    /**
     * 北美票房榜
     *
     * @return
     */
    @GET("/v2/movie/us_box")
    Observable<NorthAmericaMovie> northAmericaMovie();

    /*******        搜索        ********/
    @GET("/v2/movie/search")
    Observable<ResponseBody> searchMovie(@Query("q") String query,
                                         @Query("tag") String tag);

    @GET("/v2/movie/search")
    Observable<ResponseBody> searchMovie(@Query("q") String query,
                                         @Query("tag") String tag,
                                         @Query("start") int start,
                                         @Query("count") int count);
    /*******        电影条目        ********/
    /**
     * 电影条目信息 /v2/movie/subject/:id
     *
     * @return
     */
    @GET("/v2/movie/subject/{id}")
    Observable<Movie> movieInfo(@Path("id") String id);

    @GET("/v2/movie/subject/{id}")
    Observable<Movie> movieInfo(@Path("id") String id,
                                @Query("apikey") String apiKey);

    /**
     * 电影条目剧照 /v2/movie/subject/:id/photos
     *
     * @param id
     * @return
     */
    @GET("/v2/movie/subject/{id}/photos")
    Observable<ResponseBody> moviePhotoInfo(@Path("id") String id);

    @GET("/v2/movie/subject/{id}/photos")
    Observable<ResponseBody> moviePhotoInfo(@Path("id") String id,
                                            @Query("apikey") String apiKey);

    @GET("/v2/movie/subject/{id}/photos")
    Observable<ResponseBody> moviePhotoInfo(@Path("id") String id,
                                            @Query("start") int start,
                                            @Query("count") int count);

    /**
     * 电影条目长评 /v2/movie/subject/:id/reviews
     *
     * @param id
     * @return
     */
    @GET("/v2/movie/subject/{id}/reviews")
    Observable<ResponseBody> movieReviews(@Path("id") String id);

    @GET("/v2/movie/subject/{id}/reviews")
    Observable<ResponseBody> movieReviews(@Path("id") String id,
                                          @Query("start") int start,
                                          @Query("count") int count);

    /**
     * 电影条目短评 /v2/movie/subject/:id/comments
     *
     * @param id
     * @return
     */
    @GET("/v2/movie/subject/{id}/comments")
    Observable<ResponseBody> movieComments(@Path("id") String id);

    @GET("/v2/movie/subject/{id}/comments")
    Observable<ResponseBody> movieComments(@Path("id") String id,
                                           @Query("start") int start,
                                           @Query("count") int count);
    /*******        影人条目        ********/
    /**
     * 影人条目信息
     *
     * @return
     */
    @GET("/v2/movie/celebrity/{id}")
    Observable<MovieCelebrity> celebrityInfo(@Path("id") String id);

    /**
     * 影人剧照
     *
     * @return
     */
    @GET("/v2/movie/celebrity/{id}/photos")
    Observable<ResponseBody> celebrityPhotoInfo(@Path("id") String id);

    @GET("/v2/movie/celebrity/{id}/photos")
    Observable<ResponseBody> celebrityPhotoInfo(@Path("id") String id,
                                                @Query("start") int start,
                                                @Query("count") int count);

    /**
     * 影人作品 /v2/movie/celebrity/:id/works
     *
     * @return
     */
    @GET("/v2/movie/celebrity/{id}/works")
    Observable<ResponseBody> celebrityWorksInfo(@Path("id") String id);

    @GET("/v2/movie/celebrity/{id}/works")
    Observable<ResponseBody> celebrityWorksInfo(@Path("id") String id,
                                                @Query("start") int start,
                                                @Query("count") int count);
}
