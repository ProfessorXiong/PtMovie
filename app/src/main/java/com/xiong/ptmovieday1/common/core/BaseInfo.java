package com.xiong.ptmovieday1.common.core;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class BaseInfo {
    //电影列表的url
    public static String movieListUrl = "http://api.putao.so/sbiz/movie/list?citycode=";
    //影院列表的url
    public static String cinemaListUrl = "http://api.putao.so/sbiz/movie/cinema/list?citycode=";
    //区县列表的url
    public static String countyListUrl = "http://api.putao.so/sbiz/movie/county?citycode=";
    //电影详情的url
    public static String movieDetailurl = "http://api.putao.so/sbiz/movie/detail?movieid=";
    //电影排期的url
    public static String moviePlaydateUrl = "http://api.putao.so/sbiz/movie/playdate";
    //电影场次的url
    public static String moviePlaytime = "http://api.putao.so/sbiz/movie/showtime";
}
