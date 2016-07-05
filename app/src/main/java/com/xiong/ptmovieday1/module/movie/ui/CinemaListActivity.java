package com.xiong.ptmovieday1.module.movie.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.module.movie.widget.CinemaListFragment;

/**
 * 这是显示播放指定电影的影院列表的activity
 */
public class CinemaListActivity extends FragmentActivity {
    private String moviename;
    private Long movieid;   // 电影的id
    private ImageView image_back;       //返回按钮
    private TextView text_moviename;    //电影标题的textView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_list);
        getDataFromIntent();
        bindView();
        addFragment();
    }

    /**
     * 把Fragment添加进来
     */
    private void addFragment() {
        CinemaListFragment fragment = new CinemaListFragment();
            fragment.setMovieid(movieid);
            fragment.setMoviename(moviename);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.activity_cinemalist_linear_content,fragment);
            transaction.commit();
    }

    /**
     * 绑定控件
     */
    private void bindView() {
        image_back = (ImageView) findViewById(R.id.activity_cinemalist_image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        text_moviename = (TextView) findViewById(R.id.activity_cinemalist_tv_moviename);
        text_moviename.setText(moviename);
    }

    /**
     * 从上下文获取intent
     */
    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        moviename = bundle.getString("moviename");
        movieid = bundle.getLong("movieid");
    }
}
