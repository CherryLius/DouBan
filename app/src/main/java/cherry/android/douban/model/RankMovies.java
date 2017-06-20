//package cherry.android.douban.model;
//
//import android.support.annotation.IntDef;
//import android.support.annotation.NonNull;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.util.List;
//
///**
// * Created by LHEE on 2017/6/18.
// */
//
//public class RankMovies {
//
//    public static final int TYPE_TITLE = -1;
//    public static final int TYPE_TOP_250 = 0;
//    public static final int TYPE_WEEKLY = 1;
//    public static final int TYPE_NEW_MOVIES = 2;
//    public static final int TYPE_NORTH_AMERICA = 3;
//
//    @IntDef({TYPE_TITLE, TYPE_TOP_250, TYPE_WEEKLY, TYPE_NEW_MOVIES, TYPE_NORTH_AMERICA})
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface RankType {
//    }
//
//    private Movie movie;
//    private String title;
//    private int type;
//
//    public RankMovies(@NonNull String title, @RankType int type) {
//        this.title = title;
//        this.type = type;
//    }
//
//    public RankMovies(Movie movie, @RankType int type) {
//        this.movie = movie;
//        this.type = type;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public Movie getMovie() {
//        return movie;
//    }
//}
