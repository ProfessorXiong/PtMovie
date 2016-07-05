package com.xiong.ptmovieday1.common.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.LocationMgr;
import com.xiong.ptmovieday1.common.util.SharePreUtils;
import com.xiong.ptmovieday1.module.movie.widget.CinemaListFragment;
import com.xiong.ptmovieday1.module.movie.widget.MovieListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    //切换电影列表和影院列表的viewpager
    ViewPager viewPager;
    //放置movielistFragment和cinemalistFragment
    List<Fragment> fragments;
    //显示所在城市的textview
    private TextView tv_city;
    //用于切换电影列表和影院列表的tv
    private TextView tv_movielist,tv_cinemalist;
    //百度地图定位相关类
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        fragments = new ArrayList<Fragment>();
        fragments.add(new MovieListFragment());
        fragments.add(new CinemaListFragment());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    clearSelect((ViewGroup) tv_movielist.getParent());
                    tv_movielist.setSelected(true);
                }else{
                    clearSelect((ViewGroup) tv_cinemalist.getParent());
                    tv_cinemalist.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        //百度地图定位
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );            //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    /**
     * 百度地图的定位之前初始化参数的方法
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=2000;      //定位的时间间隔
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 将控件与activity绑定
     */
    private void bindViews(){
        tv_movielist = (TextView) findViewById(R.id.activity_main_btn_movielist);
        tv_cinemalist = (TextView) findViewById(R.id.activity_main_btn_cinemalist);
        tv_movielist.setOnClickListener(this);
        tv_cinemalist.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        tv_city = (TextView) findViewById(R.id.activity_main_tv_city);
        tv_city.setText(SharePreUtils.readString(SharePreUtils.CITY,"北京"));

        //让tv_movielist选中
        tv_movielist.setSelected(true);
    }

    /**
     * 清除testview选择状态
     * @param viewGroup
     */
    private void clearSelect(ViewGroup viewGroup){
        if(viewGroup.getChildCount() != 0){
            for(int i=0;i<viewGroup.getChildCount();i++){
                viewGroup.getChildAt(i).setSelected(false);
            }
        }
    }

    /**
     * 当tv_movielist和tv_cinemalist被点击后调用
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_btn_movielist:
                clearSelect((ViewGroup) v.getParent());
                v.setSelected(true);
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.activity_main_btn_cinemalist:
                clearSelect((ViewGroup) v.getParent());
                v.setSelected(true);
                viewPager.setCurrentItem(1,true);
                break;
            default:
            break;
        }
    }

    /**
     * Viewpager的FragmentAdapter
     */
    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 这个方法用来创建fragment对象,一般只会被调用一次
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        /**
         * 这个方法用来初始化对象,当getItemPosition返回的值为UNCHANGED时不会执行,当为NONE时会执行
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(position == 0){
                MovieListFragment fragment = (MovieListFragment) super.instantiateItem(container,position);
                return fragment;
            }else{
                CinemaListFragment fragment = (CinemaListFragment) super.instantiateItem(container,position);
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        /**
         * 这个方法一定要被重写,ViewPager依此来判断是否需要调用instantiateItem方法
         * @param object
         * @return
         */
        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        /**
         * 获取当前页的标题,这个标题会被viewpagerindicator用到
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
                if(position == 0){
                    return "电影列表";
                }else{
                    return "影院列表";
                }
        }
    }

    /**
     * 百度地图定位功能的监听器
     */
    private class MyLocationListener implements BDLocationListener {
        //定位1次之后不再定位了
        int count = 1;
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            if(count-- > 0){
                ((CinemaListFragment)fragments.get(1)).setCurrentAddress(bdLocation.getLocationDescribe());
                //获取到定位的城市名称
                String cityname = bdLocation.getAddress().city;
                if(cityname.contains("市")){
                    cityname = cityname.substring(0,cityname.indexOf("市"));
                }
                final String city = cityname;
                //如果定位的城市和当前的城市不是同一个城市就把城市信息保存到sp中
                if(!LocationMgr.getInstance().getCity().equals(city)){
                    //通知用户是否需要切换到城市
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("定位到...");
                    builder.setMessage(city + ",当前城市为" + SharePreUtils.readString(SharePreUtils.CITY,"北京") + "是否切换?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //保存定位的城市
                            LocationMgr.getInstance().setCity(city);
                            //设置显示的城市
                            tv_city.setText(city);
                            //让电影列表和影院列表重新加载
                            ((MovieListFragment)fragments.get(0)).notifyCityChanged();
                            ((CinemaListFragment)fragments.get(1)).notifyCityChanged();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
                //保存经纬度信息
                LocationMgr.getInstance().setLatitude(bdLocation.getLatitude() + "");
                LocationMgr.getInstance().setLongitude(bdLocation.getLongitude() + "");
                //保存完经纬度之后通知影院列表更新
                ((CinemaListFragment)fragments.get(1)).notifyCityChanged();
            }else{
                mLocationClient.stop();
            }
        }
    }
}
