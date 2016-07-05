package com.xiong.ptmovieday1.module.movie.resp;

/**
 * Created by Administrator on 2016/6/22 0022.
 * 包含某个电影详情的实体类
 */
public class MovieDetailEntity extends BaseEntity{

    private MovieDetailBody data;

    public MovieDetailBody getData() {
        return data;
    }

    public void setData(MovieDetailBody data) {
        this.data = data;
    }

    public class MovieDetailBody{
        /** 葡萄生成的影片id  Y    */ private Long id;
        /** 影片id  Y  　 　 */ private String movieid;
        /** 影片名称  Y  　 　 */ private String moviename;
        /** 影片英文名称  N  　 　 */ private String englishname;
        /** 语言  N  多个数据，以逗号分割 中文 */ private String language;
        /** 影片类型  N  多个数据，以逗号分割 　 */ private String type;
        /** 出产地区  N  　 　 */ private String state;
        /** 导演  N  　 　 */ private String director;
        /** 主演  N  　 　 */ private String actors;
        /** 片长  N  　 　 */ private String length;
        /** 一句话影评  N  　 　 */ private String highlight;
        /** 电影的首映日期  N  　11位的long数据如：1426176000000  */ private Long releasedate;
        /** 影片logo  Y  　 　 */ private String logo;
        /** 电影详情介绍  Y  　 　 */ private String content;
        /** 影片评分  N  　　注意：不是IMDB评分 　 */ private String generalmark;
        /** 剧照URL数组  N  多个URL数据就以逗号分割 　 */ private String still;
        /** 影片版本  N  　 IMAX3D */ private String gcedition;
        private String videourl;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMovieid() {
            return movieid;
        }

        public void setMovieid(String movieid) {
            this.movieid = movieid;
        }

        public String getMoviename() {
            return moviename;
        }

        public void setMoviename(String moviename) {
            this.moviename = moviename;
        }

        public String getEnglishname() {
            return englishname;
        }

        public void setEnglishname(String englishname) {
            this.englishname = englishname;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getActors() {
            return actors;
        }

        public void setActors(String actors) {
            this.actors = actors;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getHighlight() {
            return highlight;
        }

        public void setHighlight(String highlight) {
            this.highlight = highlight;
        }

        public Long getReleasedate() {
            return releasedate;
        }

        public void setReleasedate(Long releasedate) {
            this.releasedate = releasedate;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGeneralmark() {
            return generalmark;
        }

        public void setGeneralmark(String generalmark) {
            this.generalmark = generalmark;
        }

        public String getStill() {
            return still;
        }

        public void setStill(String still) {
            this.still = still;
        }

        public String getGcedition() {
            return gcedition;
        }

        public void setGcedition(String gcedition) {
            this.gcedition = gcedition;
        }

        public String getVideourl() {
            return videourl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }
    }
}
