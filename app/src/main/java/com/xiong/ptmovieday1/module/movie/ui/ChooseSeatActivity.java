package com.xiong.ptmovieday1.module.movie.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.BaseActivity;
import com.xiong.ptmovieday1.common.core.BaseInfo;
import com.xiong.ptmovieday1.common.core.PMApplication;
import com.xiong.ptmovieday1.common.widget.GeneralHeader;
import com.xiong.ptmovieday1.module.movie.resp.ChooseSeatData;
import com.xiong.ptmovieday1.module.movie.resp.ChooseSeatEntity;
import com.xiong.ptmovieday1.module.movie.widget.ChooseSeatView;

/**
 * 选择座位的activity
 */
public class ChooseSeatActivity extends BaseActivity {
    //标准头部
    private GeneralHeader header;
    //选座的控件
    private ChooseSeatView csView;
    //下单按钮
    private Button btn_order;
    //放置已经选择的座位信息
    private LinearLayout linear_seatinfo;
    private TextView tv_my_price,tv_cinemaname,tv_price,tv_datafrom;
    private int ticketPrice;
    private String cpname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seat);
        bindViews();
        getData();
    }

    private void bindViews(){
        header = (GeneralHeader) findViewById(R.id.header);
        tv_cinemaname = (TextView) findViewById(R.id.tv_cinemaname);
        tv_price = (TextView) findViewById(R.id.tv_price);
        csView = (ChooseSeatView) findViewById(R.id.choose_seat_view);
        csView.setOnSeatChangeLister(new ChooseSeatView.OnSeatChangeLister() {
            @Override
            public void onSeatChoosen(String seatname, int hasChoosenCount) {
                if(hasChoosenCount != 0)
                    btn_order.setEnabled(true);
                btn_order.setText("已选" + hasChoosenCount);
                TextView textView = new TextView(ChooseSeatActivity.this);
                textView.setText(seatname);
                linear_seatinfo.addView(textView);
                tv_my_price.setText(ticketPrice * hasChoosenCount/100 + "元");
            }

            @Override
            public void onSeatUnChoosen(String seatname, int hasChoosenCount) {
                if(hasChoosenCount == 0){
                    btn_order.setText("请先选座");
                    btn_order.setEnabled(false);
                }else{
                    btn_order.setText("已选" + hasChoosenCount);
                }
                for(int i=0;i<linear_seatinfo.getChildCount();i++){
                    TextView text = (TextView) linear_seatinfo.getChildAt(i);
                    if(text.getText().toString().equals(seatname))
                        linear_seatinfo.removeView(text);
                }
                tv_my_price.setText(ticketPrice * hasChoosenCount/100 + "元");
            }
        });
        linear_seatinfo = (LinearLayout) findViewById(R.id.linear_seat_info);
        tv_my_price = (TextView) findViewById(R.id.tv_my_price);
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                //获取到座位信息
                for(int i=0;i<linear_seatinfo.getChildCount();i++){
                    TextView tv = (TextView) linear_seatinfo.getChildAt(i);
                    String content = tv.getText().toString();
                    sb.append(content);
                    if(i != linear_seatinfo.getChildCount() -1)
                        sb.append(",");
                }
                //跳转到下单页面
                jumpToOrderActivity(sb.toString());
            }
        });
        tv_datafrom = (TextView) findViewById(R.id.tv_datafrom);
    }

    /**
     * 跳转到下单的页面
     */
    private void jumpToOrderActivity(String seatinfo) {
        System.out.println(seatinfo);
    }

    private void getData(){
        long mpid = getIntent().getLongExtra("mpid",0);
        long cpid = getIntent().getLongExtra("cpid",0);
        cpname = getIntent().getStringExtra("cpname");
        if(cpname != null && !TextUtils.isEmpty(cpname))
            tv_datafrom.setText("数据来自 " + cpname);
        else{
            tv_datafrom.setText("数据来自 " + "葡萄生活");
        }

        if(mpid == 0 || cpid ==0)
            return;

        String url = BaseInfo.chooseSeateUrl + "?mpid=" + mpid + "&cpid=" + cpid;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ChooseSeatEntity chooseSeatEntity = JSONObject.parseObject(s, ChooseSeatEntity.class);
                ticketPrice = chooseSeatEntity.getData().getPrice();
                tv_price.setText(ticketPrice / 100 + "元/张");
                tv_cinemaname.setText(chooseSeatEntity.getData().getCinemaname());
                header.setTitleText(chooseSeatEntity.getData().getMoviename());
                ChooseSeatData data = chooseSeatEntity.getData();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                csView.setSeatRowLists(data.getSeatRowList());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("获取座位信息失败");
            }
        });
        PMApplication.getInstance().getRequestQueue().add(request);
    }
}
