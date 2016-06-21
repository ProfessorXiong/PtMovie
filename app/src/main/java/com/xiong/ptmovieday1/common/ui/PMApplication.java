package com.xiong.ptmovieday1.common.ui;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class PMApplication extends Application {
    private static PMApplication instance;
    private RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        queue = Volley.newRequestQueue(this);
    }

    /**
     * 获取PMApplication的实例
     * @return
     */
    public static PMApplication getInstance(){
        return instance;
    }

    /**
     * 获取requestQueue的实例
     * @return
     */
    public RequestQueue getRequestQueue(){
        return queue;
    }
}
