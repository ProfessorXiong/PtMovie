package com.xiong.ptmovieday1.module.movie.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 * 影院列表实体类
 */
public class MovieListEntity extends BaseEntity{

    private List<MovieEntity> data;

    public List<MovieEntity> getData() {
        return data;
    }

    public void setData(List<MovieEntity> data) {
        this.data = data;
    }
}

