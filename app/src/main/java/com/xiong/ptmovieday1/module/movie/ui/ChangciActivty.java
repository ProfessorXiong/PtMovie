package com.xiong.ptmovieday1.module.movie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.BaseInfo;
import com.xiong.ptmovieday1.common.core.PMApplication;
import com.xiong.ptmovieday1.common.util.SharePreUtils;
import com.xiong.ptmovieday1.module.movie.resp.ChangciEntity;
import com.xiong.ptmovieday1.module.movie.resp.ChangciListEntity;
import com.xiong.ptmovieday1.module.movie.resp.CpEntity;
import com.xiong.ptmovieday1.module.movie.resp.MovieEntity;
import com.xiong.ptmovieday1.module.movie.resp.MovieListEntity;
import com.xiong.ptmovieday1.module.movie.resp.PlaydateEntity;
import com.xiong.ptmovieday1.module.movie.resp.PlaydateListEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 显示电影场次的activity
 */
public class ChangciActivty extends AppCompatActivity {

    //显示排期的listView
    private ListView mainList;
    //排期列表的adapter
    private ChangciAdapter adapter;
    //listView的头部
    private View listHead;
    //影院的名称\地址\电影名称\评分\类型\长度
    private TextView cinemaname_tv, address_tv, moviename_tv, genralmark_tv, type_tv, length_tv;
    //gallery的背景图 和 返回按钮
    private ImageView img_galleryBg, img_back;
    //电影相关信息的布局,用来完成点击跳转到电影详情页
    private LinearLayout layout_movieinfo;
    //用来添加场次的父布局和排期的父布局,
    private LinearLayout linear_playdate;
    //用来放置海报的gallery
    private Gallery gallery;
    //放置场次信息的list
    private List<ChangciEntity> changciInfo;
    private long movieid;
    private String moviename;
    private long cinemaid;
    //影片名称\id\影院地址
    private String cinemaname, address;
    //最低价格
    private String lowestPrice;
    //从互联网获取到的电影列表实体
    private MovieListEntity movieListEntity;
    //Gallery的布局参数,用来设定里面的图片的大小
    private Gallery.LayoutParams galleryParams1;
    //电影排期button的布局参数
    private LinearLayout.LayoutParams btn_layout_param;
    private int nowFilmIndex;       //记录当前是第几个电影的索引值,该索引值与电影海报在gallery中的索引值一致

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainList = new ListView(this);
        setContentView(mainList);
        listHead = getLayoutInflater().inflate(R.layout.activity_changci_activty, null);    //获得list的头部
        changciInfo = new LinkedList<ChangciEntity>();
        //从intent获取传递过来的数据
        getDataFromIntent();
        //绑定视图控件
        bindViews();
        //从互联网获取初始数据
        getInitData();
        //gallery的布局参数,用来对里面的图片进行布局
        galleryParams1 = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.MATCH_PARENT);
        btn_layout_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_layout_param.setMargins(5,5,5,5);
    }

    /**
     * 从intent获取原始数据
     */
    private void getDataFromIntent() {
        Bundle b = getIntent().getExtras();
        moviename = b.getString("moviename");
        movieid = b.getLong("movieid", -1);
        cinemaname = b.getString("cinemaname");
        cinemaid = b.getLong("cinemaid");
        address = b.getString("address");
        lowestPrice = b.getString("lowestprice","");
    }

    /**
     * 绑定视图控件
     */
    private void bindViews() {
        cinemaname_tv = (TextView) listHead.findViewById(R.id.activity_changci_cinemaname);
        cinemaname_tv.setText(cinemaname);
        address_tv = (TextView) listHead.findViewById(R.id.activity_changci_address);
        address_tv.setText(address);
        moviename_tv = (TextView) listHead.findViewById(R.id.activity_changci_tv_moviename);
        moviename_tv.setText(moviename);
        genralmark_tv = (TextView) listHead.findViewById(R.id.activity_changci_tv_genralmark);
        type_tv = (TextView) listHead.findViewById(R.id.activity_changci_tv_type);
        length_tv = (TextView) listHead.findViewById(R.id.activity_changci_tv_length);
        layout_movieinfo = (LinearLayout) listHead.findViewById(R.id.activity_changci_layout_movieinfo);
        layout_movieinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangciActivty.this,MovieDetailActivity.class);
                intent.putExtra("movieid",movieid);
                intent.putExtra("moviename",moviename);
                startActivity(intent);
            }
        });
        img_galleryBg = (ImageView) listHead.findViewById(R.id.activity_changci_image_gallerybg);
        img_back = (ImageView) listHead.findViewById(R.id.activity_changci_image_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        linear_playdate = (LinearLayout) listHead.findViewById(R.id.activity_changci_linear_paiqicontent);
        gallery = (Gallery) listHead.findViewById(R.id.activity_changci_gallery);

        mainList.addHeaderView(listHead);
        adapter = new ChangciAdapter();
        mainList.setAdapter(adapter);
        //设置mainList的点击事件
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout cpcontent = (LinearLayout) view.findViewById(R.id.view_changci_layout_cpcontent);
                //表示有多个比价的商家,显示商家的列表
                if(cpcontent.getChildCount()!=0){
                    if(cpcontent.getVisibility() == View.GONE){
                        cpcontent.setVisibility(View.VISIBLE);
                    }else{
                        cpcontent.setVisibility(View.GONE);
                    }
                }else{
                    //没有商家列表,直接进入选座的界面
                }
            }
        });
    }

    /**
     * 从互联网获取初始数据
     */
    private void getInitData() {
        String citycode = "";
        try {
            citycode = URLEncoder.encode(SharePreUtils.readString(SharePreUtils.CITY,"北京"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(BaseInfo.movieListUrl + citycode + "&cinemaid=" + cinemaid, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                movieListEntity = JSONObject.parseObject(s, MovieListEntity.class);
                //设置gallery的内容
                setGalleryAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("从互联网获取电影列表失败 : " + volleyError.getMessage());
            }
        });
        PMApplication.getInstance().getRequestQueue().add(request);
    }

    /**
     * 获取到电影列表后填充gallery
     */
    private void setGalleryAdapter() {
        //所有的电影条目
        final List<MovieEntity> movieEntities = movieListEntity.getData();
        //gallery的适配器
        final BaseAdapter adapter = new BaseAdapter() {
            public int getCount() {
                return movieEntities.size();
            }

            public Object getItem(int position) {
                return movieEntities.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(ChangciActivty.this, R.layout.view_image_h210, null);
                    ChangciActivty.ViewHolder holder = new ViewHolder(convertView);
                }
                ChangciActivty.ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.image.setAdjustViewBounds(true);
                holder.image.setLayoutParams(galleryParams1);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.image, R.drawable.loading, R.drawable.load_failed);
                PMApplication.getInstance().getImageLoader().get(movieEntities.get(position).getLogo(), imageListener);
                return convertView;
            }
        };

        gallery.setAdapter(adapter);
        gallery.setUnselectedAlpha(0.5f);
        /**
         * 设置gallery的监听事件,该监听事件绑定到了后续的排期列表和场次列表
         */
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public synchronized void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //重置索引值,表示当前切换到了第几部电影
                nowFilmIndex = position;
                System.out.println("nowfileIndex : " + nowFilmIndex);
                //重置movieid和moviename;
                movieid = movieEntities.get(position).getMovieid();
                moviename = movieEntities.get(position).getMoviename();
                //设置电影的标题等等...
                moviename_tv.setText(movieEntities.get(position).getMoviename());
                type_tv.setText(movieEntities.get(position).getType());
                length_tv.setText(movieEntities.get(position).getLength());
                genralmark_tv.setText(movieEntities.get(position).getGeneralmark());
                /**
                 * 设置gallery的背景
                 */
                {

                }
                //获取电影的排期列表,再此之前先清空排期的layout和播放时间的layout
                while (linear_playdate.getChildCount() != 0) {
                    //清空排期列表
                    linear_playdate.removeAllViews();
                }

                String url = BaseInfo.moviePlaydateUrl + "?movieid=" + movieid + "&moviename=" + moviename +
                        "&cinemaid=" + cinemaid + "&cinemaname=" + cinemaname;
                StringRequest playdateRequest = new StringRequest(url, new Response.Listener<String>() {
                    final Long movieid_clone = movieid; //当前的电影名称和id
                    final String moviename_clone = moviename;
                    final int filmIndex = nowFilmIndex; //当前处在第几个电影的索引
                    @Override
                    public void onResponse(String s) {
                        //在这里获取到了电影的排期数据
                        PlaydateListEntity playdateListEntity = JSONObject.parseObject(s, PlaydateListEntity.class);
                        //如果索引值不匹配则不更新
//                        System.out.println("filmIndex : " + filmIndex);
                        if(filmIndex != nowFilmIndex){
                            return ;
                        }
                        //如果没有排期的话
                        if (playdateListEntity.getData() == null) {
                            return;
                        }
                        //第二次清空
                        while (linear_playdate.getChildCount() != 0) {
                            linear_playdate.removeAllViews();
                        }
                        for (final PlaydateEntity entity : playdateListEntity.getData()) {
                            //针对每一个排期添加一个button
                            Button btn = new Button(ChangciActivty.this);
                            btn.setText(entity.getPlaydate());
                            btn.setBackgroundResource(R.drawable.btn_bg);
                            btn.setLayoutParams(btn_layout_param);
                            //绑定按钮的 监听事件
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String playdate = entity.getPlaydate();
                                    String playtimeUrl = BaseInfo.moviePlaytime + "?movieid=" + movieid_clone + "&moviename=" + moviename_clone +
                                            "&cinemaid=" + cinemaid + "&cinemaname=" + cinemaname + "&playdate=" + playdate;
//                                    System.out.println(playtimeUrl);
                                    changciInfo.clear();
                                    adapter.notifyDataSetChanged();
                                    StringRequest playtimeRequest = new StringRequest(playtimeUrl, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            //若果索引值与当前索引值不匹配,一样不予更新
                                            if(filmIndex != nowFilmIndex){
                                                return;
                                            }
                                            System.out.println("执行了一次场次列表更新");
                                            ChangciListEntity changciListEntity = JSONObject.parseObject(s, ChangciListEntity.class);
                                            //清空场次信息
                                            while(changciInfo.size()!=0){
                                                changciInfo.clear();
                                            }
                                            adapter.notifyDataSetChanged();
                                            if(changciListEntity.getData()!=null){
                                                for(ChangciEntity entity:changciListEntity.getData()){
                                                    //如果场次的播放时间早于系统当前时间那么就不添加到changciinfo中间去
                                                    if(entity.getPlaytime() < System.currentTimeMillis()){
                                                        continue;
                                                    }
                                                    changciInfo.add(entity);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            System.out.println("获取电影播放场次信息失败 : " + volleyError.getMessage());
                                        }
                                    });
                                    PMApplication.getInstance().getRequestQueue().add(playtimeRequest);
                                }
                            });
                            linear_playdate.addView(btn);
                        }

                        //模拟一次点击事件
                        linear_playdate.getChildAt(0).callOnClick();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("获取上映日期消息失败 : " + volleyError.getMessage());
                    }
                });
                PMApplication.getInstance().getRequestQueue().add(playdateRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        gallery.setSelection(0);

        //如果设定了电影的名称了id那么就跳转到相应的电影上面去,如果没有就跳转到最低价的电影上去
        if (moviename != null && movieid != -1) {
            int movieindex = 0; //电影的索引
            //找到指定电影的索引
            for (int j = 0; j < movieListEntity.getData().size(); j++) {
                if (moviename.equals(movieListEntity.getData().get(j).getMoviename())) {
                    movieindex = j;
                    break;
                }
            }
            gallery.setSelection(movieindex);
        }
    }

    /**
     * 这是gallery的adapter的viewholder.里面放的是电影海报
     */
    private class ViewHolder {
        ImageView image;
        ViewHolder(View convertView) {
            convertView.setTag(this);
            image = (ImageView) convertView;
        }
    }

    /**
     * 场次列表的adapter
     */
    private class ChangciAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return changciInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return changciInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.view_changci,null);
                ViewHolder_Changci holder = new ViewHolder_Changci(convertView);
            }
            ViewHolder_Changci holder = (ViewHolder_Changci) convertView.getTag();
            //如果之前的cpcontent没有隐藏就将其隐藏起来
            if(holder.cpContent.getVisibility() == View.VISIBLE){
                holder.cpContent.setVisibility(View.GONE);
            }
            //移除holder之前的视图
            while(holder.cpContent.getChildCount()!=0){
                holder.cpContent.removeAllViews();
            }
            holder.playtime.setText(holder.format.format(new Date(changciInfo.get(position).getPlaytime())));
            holder.price.setText(changciInfo.get(position).getOriginalprice()/100 + "元起");
            holder.miaoshu.setText(holder.format.format(new Date(changciInfo.get(position).getClosetime())) + "散场" +"/"+ changciInfo.get(position).getLanguage() +
            "/"+ changciInfo.get(position).getEdition() +"/"+ changciInfo.get(position).getRoomname());
            List<CpEntity> cplist = changciInfo.get(position).getCpList();
            if(cplist !=null && cplist.size() >1){
                holder.bijia.setVisibility(View.VISIBLE);
                holder.bijia.setText(cplist.size() + "家比价");
                //如果有多家比价的话就添加两个视图到条目下面
                for(int i=0;i<cplist.size();i++){
                    View view = getLayoutInflater().inflate(R.layout.view_changci_bijia,null);
                    ((TextView)view.findViewById(R.id.view_changci_bijia_tv_price)).setText(cplist.get(i).getCpprice()/100 + "元");
                    ((TextView)view.findViewById(R.id.view_changci_bijia_tv_cpname)).setText(cplist.get(i).getCpname());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ChangciActivty.this, "不卖不卖,我们不卖", Toast.LENGTH_SHORT).show();
                        }
                    });
                    holder.cpContent.addView(view);

                }
            }
            return convertView;
        }
    }

    /**
     * 场次adapter的viewholder
     */
    private class ViewHolder_Changci{
        TextView playtime;
        TextView price;
        TextView miaoshu;
        TextView bijia;
        SimpleDateFormat format;
        //用来放置比价商家的Layout
        LinearLayout cpContent;
        ViewHolder_Changci(View view){
            view.setTag(this);
            format = new SimpleDateFormat("dd日HH:mm");
            playtime = (TextView) view.findViewById(R.id.view_changci_playtime);
            price = (TextView) view.findViewById(R.id.view_changci_price);
            miaoshu = (TextView) view.findViewById(R.id.view_changci_miaoshu);
            bijia = (TextView) view.findViewById(R.id.view_changci_cplist);
            cpContent = (LinearLayout) view.findViewById(R.id.view_changci_layout_cpcontent);
        }
    }
}

//            gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //重置movieid和moviename;
//                movieid = movieEntities.get(position).getMovieid();
//                moviename = movieEntities.get(position).getMoviename();
//                //设置电影的标题等等...
//                moviename_tv.setText(movieEntities.get(position).getMoviename());
//                type_tv.setText(movieEntities.get(position).getType());
//                length_tv.setText(movieEntities.get(position).getLength());
//                genralmark_tv.setText(movieEntities.get(position).getGeneralmark());
//                //设置gallery背景,有必要这么复杂吗?
//                ImageRequest imageRequest = new ImageRequest(movieEntities.get(position).getLogo(), new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                        img_galleryBg.setImageBitmap(BitmapUtil.blurBitmap(bitmap,getApplicationContext()));
//                    }
//                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                    }
//                });
//                PMApplication.getInstance().getRequestQueue().add(imageRequest);
//                //获取电影的排期列表,再此之前先清空排期的layout和播放时间的layout
//                if(linear_playdate.getChildCount()!=0){
//                    //清空排期列表
//                    linear_playdate.removeAllViews();
//                }
//                if(changciContent.getChildCount() != 0){
//                    //清空场次列表
//                    changciContent.removeAllViews();
//                }
//                String url = BaseInfo.moviePlaydateUrl + "?movieid=" + movieid + "&moviename=" + moviename +
//                        "&cinemaid=" + cinemaid + "&cinemaname=" + cinemaname;
//                StringRequest paiqiRequest = new StringRequest(url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        //在这里获取到了电影的排期数据
//                        PlaydateListEntity playdateListEntity = JSONObject.parseObject(s, PlaydateListEntity.class);
//                        //如果有排期的话
//                        if(playdateListEntity.getData() == null){
//                            return;
//                        }
//                        //第二次清空
//                        if(linear_playdate.getChildCount()!=0){
//                            //清空排期列表
//                            linear_playdate.removeAllViews();
//                        }
//                        for(final PlaydateEntity entity:playdateListEntity.getData()){
//                            //针对每一个排期添加一个button
//                            Button btn = new Button(ChangciActivty.this);
//                            btn.setText(entity.getPlaydate());
//                            //设置按钮的监听事件,在这里获取到场次列表
//                            btn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    String url = BaseInfo.moviePlaytime + "?movieid=" + movieid + "&moviename=" + moviename +
//                                            "&cinemaid=" + cinemaid + "&cinemaname=" + cinemaname + "&playdate=" + entity.getPlaydate();
//                                    StringRequest changciRequest = new StringRequest(url, new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String s) {
//                                            //在这里填充场次列表
//                                            ChangciListEntity changciListEntity = JSONObject.parseObject(s, ChangciListEntity.class);
//                                            if(changciListEntity.getData() == null){
//                                                return;
//                                            }
//                                            //第二次清空
//                                            if(changciContent.getChildCount() != 0){
//                                                //清空场次列表
//                                                changciContent.removeAllViews();
//                                            }
//                                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");    //24小时制
//                                            for(ChangciEntity entity:changciListEntity.getData()){
//                                                final View view = View.inflate(ChangciActivty.this,R.layout.view_changci,null);
//                                                TextView tv_playtime = (TextView) view.findViewById(R.id.view_changci_playtime);    //播放时间
//                                                TextView tv_price = (TextView) view.findViewById(R.id.view_changci_price);          //价格
//                                                TextView tv_miaoshu = (TextView) view.findViewById(R.id.view_changci_miaoshu);      //描述
//                                                TextView tv_cplist = (TextView) view.findViewById(R.id.view_changci_cplist);        //比价情况
//                                                tv_playtime.setText(format.format(new Date(entity.getPlaytime())));
//                                                tv_price.setText(entity.getOriginalprice()/100 + "元起");
//                                                tv_miaoshu.setText(format.format(new Date(entity.getClosetime())) + "散场" +"/"+ entity.getLanguage() +
//                                                        "/"+ entity.getEdition() +"/"+ entity.getRoomname());
//
//                                                changciContent.addView(view);   //将view添加到changcicontent中
//
//                                                //获取到比价的商家
//                                                CpEntity[] cpList = entity.getCpList();
//                                                if(cpList !=null && cpList.length_tv >1){
//                                                    System.out.println(cpList.length_tv + "家提供价格");
//                                                    tv_cplist.setVisibility(View.VISIBLE);
//                                                    tv_cplist.setText(cpList.length_tv + "家比价");
//                                                    LinearLayout layout = new LinearLayout(ChangciActivty.this);
//                                                    layout.setOrientation(LinearLayout.VERTICAL);
//                                                    layout.setVisibility(View.GONE);   //隐藏这个layout
//                                                    for(int i=0;i<cpList.length_tv;i++){
//                                                        View v = View.inflate(ChangciActivty.this,R.layout.view_changci_bijia,null);
//                                                        //设置商家名称
//                                                        ((TextView)v.findViewById(R.id.view_changci_bijia_tv_cpname)).setText(cplist.get(i).getCpname());
//                                                        //设置价格
//                                                        ((TextView)v.findViewById(R.id.view_changci_bijia_tv_price)).setText(cplist.get(i).getPtprice()/100 + "");
//                                                        //
//                                                            //这里面设置v的点击事件
//                                                        //
//                                                        layout.addView(v);
//                                                    }
//                                                    view.setTag(layout);
//                                                    //让view的点击事件打开或隐藏商家
//                                                    view.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                            LinearLayout ll = (LinearLayout) v.getTag();
//                                                            if(ll.getVisibility() == View.GONE){
//                                                                ll.setVisibility(View.VISIBLE);
//                                                            }else{
//                                                                ll.setVisibility(View.GONE);
//                                                            }
//                                                        }
//                                                    });
//                                                    //把这个layout添加到布局中去
//                                                    changciContent.addView(layout);
//                                                }else{
//                                                    //将view本身设置为点击事件的响应者
//                                                    view.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                            //点击进入选座activity
//
//                                                        }
//                                                    });
//                                                }
//
//                                            }
//                                        }
//                                    }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError volleyError) {
//                                            System.out.println("获取电影场次信息失败 : " + volleyError.getMessage());
//                                        }
//                                    });
//                                    PMApplication.getInstance().getRequestQueue().add(changciRequest);
//                                }
//                            });
//                            linear_playdate.addView(btn);
//                        }
//                        //模拟点击了第一个按钮
//                        linear_playdate.getChildAt(0).callOnClick();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        System.out.println("获取电影排期出错" + volleyError.getMessage());
//                    }
//                });
//                PMApplication.getInstance().getRequestQueue().add(paiqiRequest);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
