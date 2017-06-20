package cherry.android.douban.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MovieWrapper {
    public static final int TYPE_TOP_250 = 0;
    public static final int TYPE_NORTH_AMERICA = 3;

    @IntDef({TYPE_TOP_250, TYPE_NORTH_AMERICA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MovieType {
    }

    @MovieType
    private int type;
    private Movie movie;

    public MovieWrapper(Movie movie, @MovieType int type) {
        this.movie = movie;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Movie getMovie() {
        return movie;
    }

    public static List<MovieWrapper> wrap(List<Movie> movies, @MovieType int type) {
        List<MovieWrapper> wrappers = new ArrayList<>(movies.size());
        for (int i = 0; i < movies.size(); i++) {
            wrappers.add(new MovieWrapper(movies.get(i), type));
        }
        return wrappers;
    }
}
