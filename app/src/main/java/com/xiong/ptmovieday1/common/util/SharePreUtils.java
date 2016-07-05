package com.xiong.ptmovieday1.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xiong.ptmovieday1.common.core.PMApplication;

/**
 * Created by Administrator on 2016/7/1 0001.
 * 用来访问sharePreference的工具类
 */
public class SharePreUtils {
    private static final String SP_NAME = "PutaoMovie_sp";
    public static final String CITY = "cityname";
    /**
     * 从sp中读数据
     * @param key
     * @param defValue
     * @return
     */
    public static String readString(String key,String defValue){
        SharedPreferences sharedPreferences = PMApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defValue);
    }

    /**
     * 像sp中写数据
     * @param key
     * @param value
     */
    public static void writeString(String key,String value){
        SharedPreferences sharedPreferences = PMApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,value).commit();
    }
}
