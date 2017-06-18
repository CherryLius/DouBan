package cherry.android.douban.model;

/**
 * Created by Administrator on 2017/6/2.
 */

public class NewWeeklyMovie extends SubjectProvider{
    private String title;
//    @SerializedName("subjects")
//    private List<Movie> movies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public List<Movie> getMovies() {
//        return movies;
//    }
//
//    public void setMovies(List<Movie> movies) {
//        this.movies = movies;
//    }
}
