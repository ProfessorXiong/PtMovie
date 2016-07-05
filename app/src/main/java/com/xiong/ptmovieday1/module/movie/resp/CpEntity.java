package com.xiong.ptmovieday1.module.movie.resp;

/**
 * Created by Administrator on 2016/6/25 0025.
 * cpList内容提供商的价格信息集合
 */
public class CpEntity {
    /** 葡萄售价  Y  以分为单位。是葡萄针对某个cp提供的价格，也是最终价格，ptprice=cpprice-葡萄折扣  */ private Integer ptprice;
    /** cp提供的线上售价  Y  以分为单位  */ private Integer cpprice;
    /** 内容提供商id  Y  如代表格瓦拉，中影等。在此处确定cp  */ private Long cpid;
    /** 内容提供商名称  N    */ private String cpname;
    /** 内容提信供logo  N  图片URL地址  */ private String cplogo;
    /** 优惠券息提示  N    */ private String coupontip;
    /** 优惠券额度  N  分为单位  */ private Integer coupon;
    /** 内容提供商描述  Y  关于cp的一些介绍，简单描述  */ private String cpdesc;

    public Integer getPtprice() {
        return ptprice;
    }

    public void setPtprice(Integer ptprice) {
        this.ptprice = ptprice;
    }

    public Integer getCpprice() {
        return cpprice;
    }

    public void setCpprice(Integer cpprice) {
        this.cpprice = cpprice;
    }

    public Long getCpid() {
        return cpid;
    }

    public void setCpid(Long cpid) {
        this.cpid = cpid;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public String getCplogo() {
        return cplogo;
    }

    public void setCplogo(String cplogo) {
        this.cplogo = cplogo;
    }

    public String getCoupontip() {
        return coupontip;
    }

    public void setCoupontip(String coupontip) {
        this.coupontip = coupontip;
    }

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public String getCpdesc() {
        return cpdesc;
    }

    public void setCpdesc(String cpdesc) {
        this.cpdesc = cpdesc;
    }
}
