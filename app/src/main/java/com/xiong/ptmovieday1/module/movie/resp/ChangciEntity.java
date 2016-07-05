package com.xiong.ptmovieday1.module.movie.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/6/25 0025.
 * 电影播放场次的实体类
 */
public class ChangciEntity {
    /** 场次id  Y    */ private Long mpid;
    /** 影片名称  Y    */ private String moviename;
    /** 影院名称  Y    */ private String cinemaname;
    /** 放映时间  Y  说明：11位long类型数据，如1426744200000  */ private long playtime;
    /** 服务费  Y    */ private Integer servicefee;
    /** 语言  Y  中文、英语……  */ private String language;
    /** 版本  Y  3D、4D、IMAX、2D、双机3D、IMAX3D  */ private String edition;
    /** 影厅名称  Y    */ private String roomname;
    /** 关闭购票时间  Y  说明：11位long类型数据，如1426744200000  */ private long closetime;
    /** 影院原价 Y 以分为单位 */private Integer originalprice;
    /**
     * 比价列表
     */
    private List<CpEntity>  cpList;

    public List<CpEntity> getCpList() {
        return cpList;
    }

    public void setCpList(List<CpEntity> cpList) {
        this.cpList = cpList;
    }

    public Long getMpid() {
        return mpid;
    }

    public void setMpid(Long mpid) {
        this.mpid = mpid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public Integer getServicefee() {
        return servicefee;
    }

    public void setServicefee(Integer servicefee) {
        this.servicefee = servicefee;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public long getClosetime() {
        return closetime;
    }

    public void setClosetime(long closetime) {
        this.closetime = closetime;
    }

    public Integer getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(Integer originalprice) {
        this.originalprice = originalprice;
    }
}
