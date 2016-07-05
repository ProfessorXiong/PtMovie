package com.xiong.ptmovieday1.module.movie.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/6/25 0025.
 * 电影排期列表
 */
public class PlaydateListEntity extends BaseEntity {

    private List<PlaydateEntity> data;

    public List<PlaydateEntity> getData() {
        return data;
    }

    public void setData(List<PlaydateEntity> data) {
        this.data = data;
    }
}
