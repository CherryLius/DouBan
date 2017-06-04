package cherry.android.douban.model;

/**
 * Created by LHEE on 2017/6/3.
 */

public class MoviePerson {
    /**
     * alt : https://movie.douban.com/celebrity/1023041/
     * avatars : {"small":"http://img3.doubanio.com/img/celebrity/small/55167.jpg","large":"http://img3.doubanio.com/img/celebrity/large/55167.jpg","medium":"http://img3.doubanio.com/img/celebrity/medium/55167.jpg"}
     * name : 派蒂·杰金斯
     * id : 1023041
     */

    /**
     * alt : https://movie.douban.com/celebrity/1044996/
     * avatars : {"small":"http://img7.doubanio.com/img/celebrity/small/8710.jpg","large":"http://img7.doubanio.com/img/celebrity/large/8710.jpg","medium":"http://img7.doubanio.com/img/celebrity/medium/8710.jpg"}
     * name : 盖尔·加朵
     * id : 1044996
     */

    private String alt;
    private MovieAvatars avatars;
    private String name;
    private String id;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public MovieAvatars getAvatars() {
        return avatars;
    }

    public void setAvatars(MovieAvatars avatars) {
        this.avatars = avatars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
