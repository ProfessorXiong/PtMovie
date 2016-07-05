package com.xiong.ptmovieday1.module.movie.resp;

/**
 * Created by Administrator on 2016/6/22 0022.
 * 区县的实体类
 */
public class CountyEntity{


    //城市编号
    String countycode;
    //城市名称
    String countyname;

    public CountyEntity(){

    }

    public String getCountycode() {
        return countycode;
    }

    public void setCountycode(String countycode) {
        this.countycode = countycode;
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }
}
