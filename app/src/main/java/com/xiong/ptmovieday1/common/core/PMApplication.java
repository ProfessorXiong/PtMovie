package com.xiong.ptmovieday1.common.core;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class PMApplication extends Application {
    private static PMApplication instance;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        queue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(20*1024*1024){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
            @Override
            public Bitmap getBitmap(String s) {
                return cache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                cache.put(s,bitmap);
            }
        });
        //百度地图功能初始化
        SDKInitializer.initialize(this);
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

    /**
     * 获取imageloader的实例
     * @return
     */
    public ImageLoader getImageLoader(){
        return imageLoader;
    }

}
