package com.xiong.ptmovieday1.module.movie.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.BaseActivity;
import com.xiong.ptmovieday1.common.core.BaseInfo;
import com.xiong.ptmovieday1.common.core.PMApplication;
import com.xiong.ptmovieday1.common.widget.MyScrollView;
import com.xiong.ptmovieday1.module.movie.resp.MovieDetailEntity;
import com.xiong.ptmovieday1.module.movie.util.BitmapUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 显示电影详情的activity
 */
public class MovieDetailActivity extends BaseActivity {
    private TextView tv_moviename,  //电影名称
            tv_type,        //电影类型
            tv_state,       //首映地点
            tv_length,      //电影时长
            tv_releasedate, //首映日期
            tv_highlight,   //一句话评论
            tv_director,      //导演
            tv_actors,      //演员
            tv_content;     //剧情介绍
    private RatingBar ratingBar_generalmark;    //评分
    private ImageView image_logo, image_bg,image_back;               //影片logo,和头部的背景,返回按钮
    private RecyclerView recyclerView;    //剧照
    private Button btn_buy;                     //购买按钮
    private MyScrollView scroll;      //显示内容的滚动视图
    private ViewGroup  headLayout;  //头部的layout
    private long movieid;   //电影的id,通过id获取电影的详情
    private String moviename;//电影的名称
    private MovieDetailEntity movieDetailEntity;//获取到的电影详情实体类
    private StringRequest request;
    private String [] haibaos;                    //所有海报的url地址
    private ImageButton imgbtn_prevue;//用来播放预告片的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_movie_detail);
        //取得数据
        getDataFromIntent();
        //绑定界面
        bindView();
    }

    /**
     * 绑定控件
     */
    private void bindView() {
        tv_moviename = (TextView) findViewById(R.id.moviedetail_tv_moviename);
        tv_type = (TextView) findViewById(R.id.moviedetail_tv_type);
        tv_state = (TextView) findViewById(R.id.moviedetail_tv_state);
        tv_length = (TextView) findViewById(R.id.moviedetail_tv_length);
        tv_releasedate = (TextView) findViewById(R.id.moviedetail_tv_releasedate);
        tv_highlight = (TextView) findViewById(R.id.moviedetail_tv_highlight);
        tv_director = (TextView) findViewById(R.id.moviedetail_tv_director);
        tv_actors = (TextView) findViewById(R.id.moviedetail_tv_actors);
        tv_content = (TextView) findViewById(R.id.moviedetail_tv_content);
        ratingBar_generalmark = (RatingBar) findViewById(R.id.moviedetail_rating_generalmark);
        image_logo = (ImageView) findViewById(R.id.moviedetail_image_logo);
        image_bg = (ImageView) findViewById(R.id.moviedetail_image_bg);
        image_back = (ImageView) findViewById(R.id.cinema_image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailActivity.this.onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.moviedetail_recylerview);
//        设置recyclerView的layoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        btn_buy = (Button) findViewById(R.id.moviedetail_btn_buytickets);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this,CinemaListActivity.class);
                intent.putExtra("movieid",movieid);
                intent.putExtra("moviename",moviename);
                MovieDetailActivity.this.startActivity(intent);
            }
        });
        headLayout = (ViewGroup) findViewById(R.id.moviedetail_layout_head);
        final ColorDrawable headBg = new ColorDrawable(Color.RED);
        headBg.setAlpha(0);
        headLayout.setBackground(headBg);
        scroll = (MyScrollView) findViewById(R.id.moviedetail_scrollview);
        scroll.setMyScrollViewListener(new MyScrollView.MyScrollViewListener() {
            final int MAX_HEIGHT = 200;
            @Override
            public void onScroll(int yPos) {
                if(yPos <= MAX_HEIGHT && yPos > 0){
                    headBg.setAlpha(yPos * 255 / MAX_HEIGHT);
                }
            }
        });
        imgbtn_prevue = (ImageButton) findViewById(R.id.img_btn_prevue);
        imgbtn_prevue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videourl = movieDetailEntity.getData().getVideourl();
                if(videourl != null && URLUtil.isHttpUrl(videourl)){
                    Intent intent = new Intent(MovieDetailActivity.this,VideoPlayActivity.class);
                    intent.putExtra("videourl",videourl);
                    intent.putExtra("moviename",movieDetailEntity.getData().getMoviename());
                    startActivity(intent);
                }else{
                    Toast.makeText(MovieDetailActivity.this, "sorry,没有预告", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取初始数据的方法
     */
    private void getDataFromIntent(){
        //取得movieid;
        movieid = getIntent().getLongExtra("movieid",-1);
        moviename = getIntent().getStringExtra("moviename");
        request = new StringRequest(BaseInfo.movieDetailurl + movieid, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                movieDetailEntity = JSONObject.parseObject(s, MovieDetailEntity.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("获取电影详情出错 : " + volleyError.getMessage());
            }
        });
        //将请求添加到队列中去
        PMApplication.getInstance().getRequestQueue().add(request);
    }

    /**
     * 将数据与控件绑定
     */
    private void setData() {
        //设置各个空间的内容以及scrollview的内容
        tv_moviename.setText(movieDetailEntity.getData().getMoviename());
        tv_type.setText(movieDetailEntity.getData().getType());
        tv_state.setText(movieDetailEntity.getData().getState());
        tv_length.setText(movieDetailEntity.getData().getLength() + "分钟");
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        tv_releasedate.setText(sdf.format(new Date(movieDetailEntity.getData().getReleasedate())));
        tv_highlight.setText(movieDetailEntity.getData().getHighlight());
        tv_director.setText(movieDetailEntity.getData().getDirector());
        tv_actors.setText(movieDetailEntity.getData().getActors());
        tv_content.setText(movieDetailEntity.getData().getContent());
        float score = Float.parseFloat(movieDetailEntity.getData().getGeneralmark());
        ratingBar_generalmark.setRating(score/2);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(image_logo,R.drawable.loading,R.drawable.load_failed);
        ImageRequest imageRequest = new ImageRequest(movieDetailEntity.getData().getLogo(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                image_bg.setImageBitmap(BitmapUtil.blurBitmap(bitmap,getApplicationContext()));
            }
        }, 0, 0, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("获取海报图片失败");
            }
        });
        PMApplication.getInstance().getRequestQueue().add(imageRequest);
        PMApplication.getInstance().getImageLoader().get(movieDetailEntity.getData().getLogo(),listener);
        if(movieDetailEntity.getData().getStill()!= null){
            haibaos = movieDetailEntity.getData().getStill().split(",");
            /**
             * 设置recyclerView的内容
             */
            recyclerView.setAdapter(new RecyclerView.Adapter() {
                //创建holder
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                    return new MyViewHolder(View.inflate(MovieDetailActivity.this,R.layout.view_image,null));
                }
                //绑定数据
                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
                    ImageLoader.ImageListener listener1 = ImageLoader.getImageListener(((MyViewHolder)viewHolder).image,R.drawable.putao_haibao,R.drawable.putao_haibao);

                    ((MyViewHolder)viewHolder).image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MovieDetailActivity.this,HaibaoBrowseActivity.class);
                            intent.putExtra("haibaos",haibaos);
                            intent.putExtra("index",i);
                            MovieDetailActivity.this.startActivity(intent);
                        }
                    });
                    PMApplication.getInstance().getImageLoader().get(haibaos[i],listener1);
                }

                @Override
                public int getItemCount() {
                    if(haibaos != null)
                        return haibaos.length;
                    else
                        return 0;
                }

                class MyViewHolder extends RecyclerView.ViewHolder{
                    ImageView image;
                    public MyViewHolder(View itemView) {
                        super(itemView);
                        image = (ImageView) itemView;
                        image.setClickable(true);
                    }
                }
            });
        }else{
            recyclerView.setVisibility(View.GONE);
        }

    }

}
