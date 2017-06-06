package cherry.android.douban.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class Movie {
    /**
     * rating : {"max":10,"average":7.4,"stars":"40","min":0}
     * reviews_count : 1019
     * wish_count : 23277
     * douban_site :
     * year : 2017
     * images : {"small":"http://img7.doubanio.com/view/movie_poster_cover/ipst/public/p2460006593.jpg","large":"http://img7.doubanio.com/view/movie_poster_cover/lpst/public/p2460006593.jpg","medium":"http://img7.doubanio.com/view/movie_poster_cover/spst/public/p2460006593.jpg"}
     * alt : https://movie.douban.com/subject/1578714/
     * id : 1578714
     * mobile_url : https://movie.douban.com/subject/1578714/mobile
     * title : 神奇女侠
     * do_count : null
     * share_url : http://m.douban.com/movie/subject/1578714
     * seasons_count : null
     * schedule_url : https://movie.douban.com/subject/1578714/cinema/
     * episodes_count : null
     * countries : ["美国"]
     * genres : ["动作","奇幻","冒险"]
     * collect_count : 75023
     * casts : [{"alt":"https://movie.douban.com/celebrity/1044996/","avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/8710.jpg","large":"http://img7.doubanio.com/img/celebrity/large/8710.jpg","medium":"http://img7.doubanio.com/img/celebrity/medium/8710.jpg"},"name":"盖尔·加朵","id":"1044996"},{"alt":"https://movie.douban.com/celebrity/1053621/","avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/32637.jpg","large":"http://img3.doubanio.com/img/celebrity/large/32637.jpg","medium":"http://img3.doubanio.com/img/celebrity/medium/32637.jpg"},"name":"克里斯·派恩","id":"1053621"},{"alt":"https://movie.douban.com/celebrity/1009298/","avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1416109882.48.jpg","large":"http://img3.doubanio.com/img/celebrity/large/1416109882.48.jpg","medium":"http://img3.doubanio.com/img/celebrity/medium/1416109882.48.jpg"},"name":"康妮·尼尔森","id":"1009298"},{"alt":"https://movie.douban.com/celebrity/1002676/","avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/51737.jpg","large":"http://img3.doubanio.com/img/celebrity/large/51737.jpg","medium":"http://img3.doubanio.com/img/celebrity/medium/51737.jpg"},"name":"罗宾·怀特","id":"1002676"}]
     * current_season : null
     * original_title : Wonder Woman
     * summary : 亚马逊公主戴安娜·普林斯（盖尔·加朵 Gal Gadot 饰），经过在家乡天堂岛的训练，取得上帝赐予的武器与装备，化身神奇女侠，与空军上尉史蒂夫·特雷弗（克里斯·派恩 Chris Pine 饰）一同来到人类世界，一起捍卫和平、拯救世界，在一战期间上演了震撼人心的史诗传奇。
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1023041/","avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1496555593.75.jpg","large":"http://img7.doubanio.com/img/celebrity/large/1496555593.75.jpg","medium":"http://img7.doubanio.com/img/celebrity/medium/1496555593.75.jpg"},"name":"派蒂·杰金斯","id":"1023041"}]
     * comments_count : 39158
     * ratings_count : 71344
     * aka : ["神力女超人(台)"]
     */

    private Rating rating;
    @SerializedName("reviews_count")
    private int reviewsCount;
    @SerializedName("wish_count")
    private int wishCount;
    @SerializedName("douban_site")
    private String douBanSite;
    private String year;
    private MovieAvatars images;
    private String alt;
    private String id;
    @SerializedName("mobile_url")
    private String mobileUrl;
    private String title;
    @SerializedName("do_count")
    private Object doCount;
    @SerializedName("share_url")
    private String shareUrl;
    @SerializedName("seasons_count")
    private Object seasonsCount;
    @SerializedName("schedule_url")
    private String scheduleUrl;
    @SerializedName("episodes_count")
    private Object episodesCount;
    @SerializedName("collect_count")
    private int collectCount;
    @SerializedName("current_season")
    private Object currentSeason;
    @SerializedName("original_title")
    private String originalTitle;
    private String summary;
    private String subtype;
    @SerializedName("comments_count")
    private int commentsCount;
    @SerializedName("ratings_count")
    private int ratingsCount;
    private java.util.List<String> countries;
    private java.util.List<String> genres;
    @SerializedName("casts")
    private List<MoviePerson> casts;
    @SerializedName("directors")
    private java.util.List<MoviePerson> directors;
    private java.util.List<String> aka;

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollect_count(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public MovieAvatars getImages() {
        return images;
    }

    public void setImages(MovieAvatars images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<MoviePerson> getCasts() {
        return casts;
    }

    public void setCasts(List<MoviePerson> casts) {
        this.casts = casts;
    }

    public List<MoviePerson> getDirectors() {
        return directors;
    }

    public void setDirectors(List<MoviePerson> directors) {
        this.directors = directors;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public int getWishCount() {
        return wishCount;
    }

    public void setWishCount(int wishCount) {
        this.wishCount = wishCount;
    }

    public String getDouBanSite() {
        return douBanSite;
    }

    public void setDouBanSite(String douBanSite) {
        this.douBanSite = douBanSite;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public Object getDoCount() {
        return doCount;
    }

    public void setDoCount(Object doCount) {
        this.doCount = doCount;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Object getSeasonsCount() {
        return seasonsCount;
    }

    public void setSeasonsCount(Object seasonsCount) {
        this.seasonsCount = seasonsCount;
    }

    public String getScheduleUrl() {
        return scheduleUrl;
    }

    public void setScheduleUrl(String scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
    }

    public Object getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(Object episodesCount) {
        this.episodesCount = episodesCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public Object getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Object currentSeason) {
        this.currentSeason = currentSeason;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getAka() {
        return aka;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public static class Rating {
        /**
         * max : 10
         * average : 8.2
         * stars : 45
         * min : 0
         */

        private int max;
        private double average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "rating=" + rating +
                ", title='" + title + '\'' +
                ", collect_count=" + collectCount +
                ", original_title='" + originalTitle + '\'' +
                ", subtype='" + subtype + '\'' +
                ", year='" + year + '\'' +
                ", images=" + images +
                ", alt='" + alt + '\'' +
                ", id='" + id + '\'' +
                ", genres=" + genres +
                ", casts=" + casts +
                ", directors=" + directors +
                '}';
    }
}