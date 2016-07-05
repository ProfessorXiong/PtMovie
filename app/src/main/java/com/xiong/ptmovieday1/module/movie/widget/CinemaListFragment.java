package com.xiong.ptmovieday1.module.movie.widget;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.BaseFragment;
import com.xiong.ptmovieday1.common.core.BaseInfo;
import com.xiong.ptmovieday1.common.core.LocationMgr;
import com.xiong.ptmovieday1.common.core.PMApplication;
import com.xiong.ptmovieday1.common.util.SharePreUtils;
import com.xiong.ptmovieday1.module.movie.resp.CinemaEntity;
import com.xiong.ptmovieday1.module.movie.resp.CinemaListEntity;
import com.xiong.ptmovieday1.module.movie.resp.CountyEntity;
import com.xiong.ptmovieday1.module.movie.resp.CountyListEntity;
import com.xiong.ptmovieday1.module.movie.ui.ChangciActivty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 * 显示影院列表的fragment
 */
public class CinemaListFragment extends BaseFragment {
    private CinemaListEntity cinemaListEntity;  //影院列表的实体
    private CountyListEntity countyListEntity;  //区县列表的实体
    private ListView list;  //用来显示电影院条目的listView
    private ToggleButton tbZone, tbSort;         //区县选择和排序按钮
    private PopupWindow popWindowCounty, popWindowSort;//区县和排序弹出窗口
    private ArrayAdapter<String> countyListAdapter; //区县列表的adapter
    private List<String> countyNames;               //存放所有区县名称
    private List<CinemaEntity> cinemas;             //存放所有的影院
    private CinemaListAdapter clAdapter;            //影院列表的adapter
    private Long movieid;                         //指定电影的id
    private String moviename;                       //指定电影的名称

    private String myLatitudeStr;
    private String myLongitudeStr;
    private TextView tv_currentAddress;         //当前位置
    DecimalFormat df = new DecimalFormat("0.##");

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }


    public void setMovieid(Long movieid) {
        this.movieid = movieid;
    }

    /**
     * 按价格对cinemas进行排序
     * @param cinemas
     */
    private void sortCinemasByPrice(List<CinemaEntity> cinemas) {
        Collections.sort(cinemas, new Comparator<CinemaEntity>() {
            @Override
            public int compare(CinemaEntity lhs, CinemaEntity rhs) {
                return lhs.getPricerange().compareTo(rhs.getPricerange());
            }
        });
    }

    /**
     * 按距离对cinemas进行排序
     * @param cinemas
     */
    private void sortCinemasByDistance(List<CinemaEntity> cinemas){
        Collections.sort(cinemas, new Comparator<CinemaEntity>() {
            @Override
            public int compare(CinemaEntity lhs, CinemaEntity rhs) {
                if(lhs.getDistance() != rhs.getDistance()){
                    return lhs.getDistance() > rhs.getDistance() ? 1 : -1;
                }else{
                    return 0;
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cinemaListEntity = new CinemaListEntity();
        countyListEntity = new CountyListEntity();
        cinemas = new ArrayList<CinemaEntity>();
        countyNames = new ArrayList<String>();
    }

    /**
     * 当城市改变的时候进行的操作,对列表进行刷新
     */
    public void notifyCityChanged(){
        initData();
    }

    public void setCurrentAddress(String address){
        tv_currentAddress.setText(address);
    }
    @Override
    public void initData() {
        /**
         * 获取当前经纬度信息
         */
        myLatitudeStr = LocationMgr.getInstance().getLatitude();
        myLongitudeStr = LocationMgr.getInstance().getLongitude();
        /**
         * 请求获取影院列表,根据是否有movieid对影院进行筛选
         */
        //获取到城市编码
        String citycode = "";
        try {
            citycode = URLEncoder.encode(SharePreUtils.readString(SharePreUtils.CITY,"北京"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = null;
        if(movieid == null){
            url = BaseInfo.cinemaListUrl + citycode;
        }else{
            url = BaseInfo.cinemaListUrl + citycode + "&movieid=" + movieid;
        }
        final StringRequest cinemeListRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                cinemaListEntity = JSONObject.parseObject(s, CinemaListEntity.class);
                //将影院实体全部添加到列表中去
                if(cinemaListEntity.getData() != null){
                    cinemas = cinemaListEntity.getData();
                    //计算距离
                    if(!myLatitudeStr.equals("") && !myLongitudeStr.equals("")){
                        for(CinemaEntity entity:cinemas){
                            double myLatitude = Double.parseDouble(myLatitudeStr);
                            double myLongitude = Double.parseDouble(myLongitudeStr);
                            LatLng myLatLng = new LatLng(myLatitude,myLongitude);
                            double cinemaLatitude = Double.parseDouble(entity.getLatitude());
                            double cinemaLongitude = Double.parseDouble(entity.getLongitude());
                            LatLng cinemaLatLng = new LatLng(cinemaLatitude,cinemaLongitude);
                            //如果坐标系不一致就转换一下
                            if(entity.getCs().equals("2")){
                                CoordinateConverter cc = new CoordinateConverter();
                                cc.from(CoordinateConverter.CoordType.COMMON);
                                cc.coord(cinemaLatLng);
                                cinemaLatLng = cc.convert();
                            }
                            //单位为米
                            double distance = DistanceUtil.getDistance(myLatLng, cinemaLatLng);
                            entity.setDistance(distance);
                        }
                    }
                    sortCinemasByPrice(cinemas);
                }
                clAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("获取影院列表出错 : " + volleyError.getMessage());
            }
        });
        /**
         * 请求获取城镇列表
         */
        final StringRequest countyListRequest = new StringRequest(BaseInfo.countyListUrl + citycode, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                countyListEntity = JSONObject.parseObject(s, CountyListEntity.class);
                countyNames.clear();
                countyNames.add("全部");
                if(countyListEntity.getData() != null){
                    for (CountyEntity entity : countyListEntity.getData()) {
                        countyNames.add(entity.getCountyname());
                    }
                }
                countyListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("获取城镇列表出错 : " + volleyError.getMessage());
            }
        });

        PMApplication.getInstance().getRequestQueue().add(cinemeListRequest);
        PMApplication.getInstance().getRequestQueue().add(countyListRequest);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema_list, null);
        tv_currentAddress = (TextView) view.findViewById(R.id.tv_current_address);
        tbZone = (ToggleButton) view.findViewById(R.id.cinemalist_toggle_zone);
        tbZone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!popWindowCounty.isShowing())
                        popWindowCounty.showAsDropDown(tbZone);
                }

            }
        });
        tbSort = (ToggleButton) view.findViewById(R.id.cinemalist_toggle_sort);
        tbSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!popWindowSort.isShowing())
                        popWindowSort.showAsDropDown(tbSort);
                }
            }
        });
        list = (ListView) view.findViewById(R.id.cinemalist_lv_listview);
        clAdapter = new CinemaListAdapter();
        list.setAdapter(clAdapter);
        /**
         * 设置监听事件
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChangciActivty.class);
                intent.putExtra("cinemaname", cinemas.get(position).getCinemaname());
                intent.putExtra("cinemaid", cinemas.get(position).getId());
                intent.putExtra("address", cinemas.get(position).getAddress());
                //放置一个最低价
                intent.putExtra("lowestprice",cinemas.get(position).getPricerange());
                //还需要设置电影名称和id
                if(movieid!=null){
                    intent.putExtra("movieid",movieid);
                }
                if(moviename != null){
                    intent.putExtra("moviename",moviename);
                }
                startActivity(intent);

            }
        });
        //初始化区县的弹出窗口
        initPopWindow1();
        //初始化排序弹出窗口
        initPopWindow2();
        initData();
        return view;
    }

    /**
     * 初始化区县列表弹出窗口
     */
    private void initPopWindow1() {
        ListView list = new ListView(getActivity());

        countyListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countyNames);
        list.setAdapter(countyListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countyName = ((TextView) view).getText().toString();
                if ("全部".equals(countyName)) {
                    cinemas = cinemaListEntity.getData();
                } else {
                    cinemas = new ArrayList<CinemaEntity>();
                    for (CinemaEntity entity : cinemaListEntity.getData()) {
                        if (entity.getCountyname().equals(countyName)) {
                            cinemas.add(entity);
                        }
                    }
                }
                //对结果进行一次排序
                sortCinemasByPrice(cinemas);
                clAdapter.notifyDataSetChanged();
                popWindowCounty.dismiss();
            }
        });
        popWindowCounty = new PopupWindow(list, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindowCounty.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popWindowCounty.setOutsideTouchable(true);
        popWindowCounty.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tbZone.setChecked(false);
            }
        });
    }

    /**
     * 初始化城市列表弹出窗口
     */
    private void initPopWindow2() {
        ListView list = new ListView(getActivity());
        List<String> sorts = new ArrayList<String>();
        sorts.add("按距离排序");
        sorts.add("按价格排序");
        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sorts));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    sortCinemasByDistance(cinemas);
                }else{
                    sortCinemasByPrice(cinemas);
                }
                clAdapter.notifyDataSetChanged();
                popWindowSort.dismiss();
            }
        });
        popWindowSort = new PopupWindow(list, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindowSort.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popWindowSort.setOutsideTouchable(true);
        popWindowSort.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tbSort.setChecked(false);
            }
        });
    }

    private class CinemaListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return cinemas.size();
        }
        @Override
        public Object getItem(int position) {
            return cinemas.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.view_cinemalist_item, null);
                ViewHolder holder = new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.cinemaname.setText(cinemas.get(position).getCinemaname());
            holder.address.setText(cinemas.get(position).getAddress());
            holder.pricerange.setText(cinemas.get(position).getPricerange().substring(0, 2) + "元起");
            holder.countdes.setText(cinemas.get(position).getCountdes());
            if(cinemas.get(position).getCpcount() > 1){
                holder.cpcount.setVisibility(View.VISIBLE);
                holder.cpcount.setText(cinemas.get(position).getCpcount() + "家比价");
            }
            if(cinemas.get(position).getDistance() != 0){
                holder.distance.setVisibility(View.VISIBLE);
                holder.distance.setText(df.format(cinemas.get(position).getDistance()/1000) + "km");
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView cinemaname;
        TextView address;
        TextView pricerange;
        TextView countdes;
        TextView cpcount;
        TextView distance;
        ViewHolder(View host) {
            host.setTag(this);
            this.cinemaname = (TextView) host.findViewById(R.id.cimema_item_tv_cinemaname);
            this.address = (TextView) host.findViewById(R.id.cinema_item_tv_address);
            this.pricerange = (TextView) host.findViewById(R.id.cinema_item_tv_pricerange);
            this.countdes = (TextView) host.findViewById(R.id.cimema_item_tv_countdes);
            this.cpcount = (TextView) host.findViewById(R.id.cinema_item_tv_cplist);
            this.distance = (TextView) host.findViewById(R.id.cinema_item_tv_distance);
        }
    }
}
