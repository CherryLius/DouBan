package cherry.android.douban.model;

/**
 * Created by LHEE on 2017/6/3.
 */

public class MovieAvatars {
    /**
     * small : http://img3.doubanio.com/img/celebrity/small/55167.jpg
     * large : http://img3.doubanio.com/img/celebrity/large/55167.jpg
     * medium : http://img3.doubanio.com/img/celebrity/medium/55167.jpg
     */

    /**
     * small : http://img7.doubanio.com/view/movie_poster_cover/ipst/public/p2460006593.webp
     * large : http://img7.doubanio.com/view/movie_poster_cover/lpst/public/p2460006593.webp
     * medium : http://img7.doubanio.com/view/movie_poster_cover/spst/public/p2460006593.webp
     */

    private String small;
    private String large;
    private String medium;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
