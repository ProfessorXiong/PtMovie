package com.xiong.ptmovieday1.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class MyScrollView extends ScrollView {
    private MyScrollViewListener myScrollViewListener;

    public interface MyScrollViewListener{
        void onScroll(int yPos);
    }



    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(myScrollViewListener != null){
            myScrollViewListener.onScroll(t);
        }
    }

    //设置对外的监听器

    public void setMyScrollViewListener(MyScrollViewListener listener){
        myScrollViewListener = listener;
    }
}
