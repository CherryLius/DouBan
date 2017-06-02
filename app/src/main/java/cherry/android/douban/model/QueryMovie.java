package cherry.android.douban.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class QueryMovie {
    private int count;
    private int start;
    private int total;
    private String query;
    private String title;
    @SerializedName("subjects")
    private List<Movie> movies;
}
