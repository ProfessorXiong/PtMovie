package com.xiong.ptmovieday1.module.movie.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.BaseFragment;
import com.xiong.ptmovieday1.common.core.BaseInfo;
import com.xiong.ptmovieday1.common.core.PMApplication;
import com.xiong.ptmovieday1.common.util.SharePreUtils;
import com.xiong.ptmovieday1.module.movie.resp.MovieEntity;
import com.xiong.ptmovieday1.module.movie.resp.MovieListEntity;
import com.xiong.ptmovieday1.module.movie.ui.CinemaListActivity;
import com.xiong.ptmovieday1.module.movie.ui.MovieDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 * 电影列表的fragment
 */
public class MovieListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private GridView gridView;
    private MovieListEntity movieListEntity;
    private MovieListAdapter movieAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieListEntity = new MovieListEntity();
    }

    /**
     * 当城市改变的时候进行的操作,对列表进行刷新
     */
    public void notifyCityChanged(){
        initData();
    }

    /**
     * 从网络上获取数据
     */
    @Override
    public void initData() {
        //城市名的urlencode
        String citycode = "";
        try {
            citycode = URLEncoder.encode(SharePreUtils.readString(SharePreUtils.CITY,"北京"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = null;
        StringRequest request = new StringRequest(BaseInfo.movieListUrl + citycode, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                /**
                 * 将获取到的数据填充到列表实体里面去
                 */
                movieListEntity = JSONObject.parseObject(s, MovieListEntity.class);
                movieAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("获取电影列表出错 : " + volleyError.getMessage());
            }
        });
        PMApplication.getInstance().getRequestQueue().add(request);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        movieListEntity = (MovieListEntity) getArguments().getParcelable("movielist");
        View view = inflater.inflate(R.layout.fragment_movie_list,null,false);
            //获取gridView
            gridView = (GridView) view.findViewById(R.id.movielist_fragment_gridview);
            movieAdapter = new MovieListAdapter();
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(this);
            initData();
        return view;
    }

    /**
     * 电影条目的点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long movieid = movieListEntity.getData().get(position).getMovieid();
        String moviename = movieListEntity.getData().get(position).getMoviename();
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra("movieid",movieid);
            intent.putExtra("moviename",moviename);
        startActivity(intent);
    }

    private class MovieListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            List<MovieEntity> data = movieListEntity.getData();
            if(data == null){
                return 0;
            }else{
                return data.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return movieListEntity.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(getActivity(),R.layout.view_movielist_item,null);
                ViewHolder holder = new ViewHolder(convertView);
            }
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.moviename.setText(movieListEntity.getData().get(position).getMoviename());
            holder.gcedition.setText(movieListEntity.getData().get(position).getGcedition());
            holder.generalmark.setText(movieListEntity.getData().get(position).getGeneralmark());
            //根据影片上映时间判断是购票还是预定
            long releasedate = movieListEntity.getData().get(position).getReleasedate();
            if(System.currentTimeMillis() >= releasedate){
                holder.buyticket.setText("购票");
            }else{
                holder.buyticket.setText("预定");
            }
            //设置监听事件
            holder.buyticket.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CinemaListActivity.class);
                    intent.putExtra("moviename",movieListEntity.getData().get(position).getMoviename());
                    intent.putExtra("movieid",movieListEntity.getData().get(position).getMovieid());
                    startActivity(intent);
                }
            });
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.image,R.drawable.loading,R.drawable.load_failed);
            PMApplication.getInstance().getImageLoader().get(movieListEntity.getData().get(position).getLogo(),listener);
            return convertView;
        }
    }

    class ViewHolder{
        ImageView image;
        TextView moviename;
        TextView gcedition;
        TextView buyticket;
        TextView generalmark;
        ViewHolder(View host){
            host.setTag(this);
            this.image = (ImageView) host.findViewById(R.id.movieitem_image_logo);
            this.moviename = (TextView) host.findViewById(R.id.movieitem_tv_moviename);
            this.gcedition = (TextView) host.findViewById(R.id.movieitem_tv_gcedition);
            this.generalmark = (TextView) host.findViewById(R.id.movieitem_image_generalmark);
            this.buyticket = (TextView) host.findViewById(R.id.movieitem_tv_buyticket);
        }
    }
}
