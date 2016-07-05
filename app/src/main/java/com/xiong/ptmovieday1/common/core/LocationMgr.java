package com.xiong.ptmovieday1.common.core;

import com.xiong.ptmovieday1.common.util.SharePreUtils;

/**
 * Created by Administrator on 2016/7/1 0001.
 * 用来获取定位信息的辅助类
 */
public class LocationMgr {
    private LocationMgr(){}
    private static LocationMgr instance = new LocationMgr();

    public static LocationMgr getInstance(){
        return instance;
    }

    private static final String CITY = "cityname";
    private static final String LATITUDE = "latitude";  //纬度
    private static final String LONGITUDE = "longitude";//经度
    /**
     * 保存城市信息
     * @param city
     */
    public void setCity(String city){
        SharePreUtils.writeString(CITY,city);
    }

    /**
     * 获取城市信息,默认为背景
     * @return
     */
    public String getCity(){
        return SharePreUtils.readString(CITY,"北京");
    }

    /**
     * 保存纬度信息
     */
    public void setLatitude(String latitude){
        SharePreUtils.writeString(LATITUDE,latitude);
    }

    public String getLatitude(){
        return SharePreUtils.readString(LATITUDE,"");
    }
    /**
     * 保存精度信息
     */
    public void setLongitude(String longitude){
        SharePreUtils.writeString(LONGITUDE,longitude);
    }

    public String getLongitude(){
        return SharePreUtils.readString(LONGITUDE,"");
    }
}
