package com.xiong.ptmovieday1.module.movie.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 * 电影列表实体类
 */
public class CinemaListEntity extends BaseEntity{

    private List<CinemaEntity> data;

    public List<CinemaEntity> getData() {
        return data;
    }

    public void setData(List<CinemaEntity> data) {
        this.data = data;
    }
}
