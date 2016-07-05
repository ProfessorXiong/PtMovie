package com.xiong.ptmovieday1.module.movie.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/6/25 0025.
 * 场次列表实体类
 */
public class ChangciListEntity extends BaseEntity{
    private List<ChangciEntity> data;

    public List<ChangciEntity> getData() {
        return data;
    }

    public void setData(List<ChangciEntity> data) {
        this.data = data;
    }
}
