package cherry.android.douban.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LHEE on 2017/6/18.
 */

public class SubjectProvider implements IMovieProvider {
    @SerializedName("subjects")
    protected List<Movie> movies;

    @Override
    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
