package cherry.android.douban.model;

import java.util.List;

/**
 * Created by LHEE on 2017/6/18.
 */

public interface IMovieProvider {
    List<Movie> getMovies();

    void setMovies(List<Movie> movies);
}
