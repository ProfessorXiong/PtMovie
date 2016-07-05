package com.xiong.ptmovieday1.module.movie.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.PMApplication;

/**
 * 这是浏览海报的Activity
 */
public class HaibaoBrowseActivity extends AppCompatActivity {
    private String [] haibaos;          //海报的地址
    private int index;                  //从第几个图片进来的索引

    private ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haibao_browse);
        getData();
        pager = (ViewPager) findViewById(R.id.activity_haibao_viewpager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new MyPagerAdapter());
        if(index != -1){
            pager.setCurrentItem(index);
        }
    }

    /**
     * 从intent里获取传递过来的数据
     */
    private void getData() {
        Bundle b = getIntent().getExtras();
        haibaos = b.getStringArray("haibaos");
        index = b.getInt("index",-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return haibaos.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(HaibaoBrowseActivity.this);
            //获取图片
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.loading, R.drawable.load_failed);
            PMApplication.getInstance().getImageLoader().get(haibaos[position],listener);
            //添加到容器中
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //把view给移除
            container.removeView((View)object);
        }
    }
}
