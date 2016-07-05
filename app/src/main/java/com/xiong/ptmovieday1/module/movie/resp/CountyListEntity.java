package com.xiong.ptmovieday1.module.movie.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/6/22 0022.
 * 区县列表实体类
 */
public class CountyListEntity extends BaseEntity{

    List<CountyEntity> data;

    public List<CountyEntity> getData() {
        return data;
    }

    public void setData(List<CountyEntity> data) {
        this.data = data;
    }
}
