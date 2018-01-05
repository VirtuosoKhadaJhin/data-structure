package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.enums.*;
import com.nuanyou.cms.entity.user.PasUserProfile;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by young on 2017/12/18.
 */
public class OrderModel extends BasicModel {

    private Long id;
    @ApiModelProperty("订单编号")
    private String ordersn;
    @ApiModelProperty("状态")
    private String ststus;
    @ApiModelProperty("流水号")
    private String transactionid;
    @ApiModelProperty("总价（RMB）")
    private BigDecimal price;
    @ApiModelProperty("总价（本地）")
    private BigDecimal kpprice;
    @ApiModelProperty("使用时间")
    private Date usetime;
    @ApiModelProperty("下单时间")
    private Date createtime;
    @ApiModelProperty("订单类型")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getStstus() {
        return ststus;
    }

    public void setStstus(String ststus) {
        this.ststus = ststus;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getKpprice() {
        return kpprice;
    }

    public void setKpprice(BigDecimal kpprice) {
        this.kpprice = kpprice;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getUsetime() {
        return usetime;
    }

    public void setUsetime(Date usetime) {
        this.usetime = usetime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
