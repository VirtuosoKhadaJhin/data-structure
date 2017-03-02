package com.nuanyou.cms.entity.order;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.*;
import com.nuanyou.cms.util.PriceUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "view_export_order")
public class ViewOrderExport {
    private Long id;
    private String ordersn;
    private String ordercode;
    private OrderType ordertype;
    private String transactionid;
    private OrderPayType paytype;
    private Merchant merchant;
    private Short total;
    private BigDecimal price;
    private BigDecimal kpprice;
    private NewOrderStatus orderstatus;
    private RefundStatus refundstatus;
    private String statusname;
    private Date statustime;
    private Date paytime;
    private Date confirmtime;
    private Date usetime;
    private Date commenttime;
    private BigDecimal oprice;
    private BigDecimal okpprice;
    private Platform platform;
    private Os os;
    private String sceneid;
    private BigDecimal merchantsubsidy;
    private Byte paystatus;
    private Date refundtime;
    private Date refundaudittime;
    private String refundreason;
    private String refundremark;
    private Byte refundsource;
    private Date createtime;
    private Byte iscode;
    private BigDecimal couponprice;
    private BigDecimal couponlocalprice;
    private BigDecimal rate;
    private BigDecimal merchantprice;
    private BigDecimal mchrmbprice;
    private String message;
    private Integer flightid;
    private Integer templateid;
    private String tempsceneid;
    private Byte source;
    private Integer appointstatus;
    private Byte isdelete;
    private Long discoutcardid;
    private BigDecimal voucherprice;
    private BigDecimal itemprice;
    private BigDecimal itemlocalprice;
    private BigDecimal payable;
    private BigDecimal youfurmbreduce;//优付优惠人民币金额
    private BigDecimal youfulocalereduce;//优付优惠金额
    private BigDecimal mchrmbreduce;//商户优惠人民币金额
    private BigDecimal mchlocalereduce;//商户优惠金额
   // private Coupon coupon;
    private Long userId;

    private String address;
    private String postalcode;
    private String username;
    private String province;
    private String district;
    private String city;
    private String tel;

    private String nickname;

    private BigDecimal postage;
    private BigDecimal postagermb;


    private List<OrderItem> orderItems;
    //private String items;

    //private String coupontitle;

    //private String itemname;

    //private BigDecimal itemunitprice;

    private String coupontitle;


    private Integer buyTimes;





    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }





    @Column(name = "ordersn", nullable = false, length = 40)
    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }


    @Column(name = "ordercode", nullable = true, length = 20)
    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }


    @Column(name = "ordertype", nullable = true)
    @Convert(converter = OrderTypeConverter.class)
    public OrderType getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(OrderType ordertype) {
        this.ordertype = ordertype;
    }





    @Column(name = "transactionid", nullable = true, length = 40)
    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    @Convert(converter = OrderPayTypeConverter.class)
    @Column(name = "paytype")
    public OrderPayType getPaytype() {
        return paytype;
    }
    public void setPaytype(OrderPayType paytype) {
        this.paytype = paytype;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mchid", nullable = true)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }




    @Column(name = "userid")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


//    @Transient
//    public String getItems() {
//        return items;
//    }
//
//
//    public void setItems(String items) {
//        this.items = items;
//    }



    @Column(name = "total", nullable = true)
    public Short getTotal() {
        return total;
    }

    public void setTotal(Short total) {
        this.total = total;
    }


    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Column(name = "kpprice", nullable = true, precision = 5)
    public BigDecimal getKpprice() {
        return kpprice;
    }
    public void setKpprice(BigDecimal kpprice) {
        this.kpprice = kpprice;
    }


    @Column(name = "orderstatus", nullable = true)
    @Convert(converter = NewOrderStatusConverter.class)
    public NewOrderStatus getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(NewOrderStatus orderstatus) {
        this.orderstatus = orderstatus;
    }

    @Column(name = "newrefundstatus", nullable = true)
    @Convert(converter=RefundStatusConverter.class  )
    public RefundStatus getRefundstatus() {
        return refundstatus;
    }

    public void setRefundstatus(RefundStatus refundstatus) {
        this.refundstatus = refundstatus;
    }

    @Column(name = "orderstatusname", nullable = true, length = 20)
    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }


    @Column(name = "statustime", nullable = true)
    public Date getStatustime() {
        return statustime;
    }

    public void setStatustime(Date statustime) {
        this.statustime = statustime;
    }


    @Column(name = "paytime", nullable = true)
    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }


    @Column(name = "confirmtime", nullable = true)
    public Date getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(Date confirmtime) {
        this.confirmtime = confirmtime;
    }


    @Column(name = "usetime", nullable = true)
    public Date getUsetime() {
        return usetime;
    }

    public void setUsetime(Date usetime) {
        this.usetime = usetime;
    }


    @Column(name = "commenttime", nullable = true)
    public Date getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(Date commenttime) {
        this.commenttime = commenttime;
    }


    @Column(name = "oprice", nullable = true, precision = 2)
    public BigDecimal getOprice() {
        return oprice;
    }

    public void setOprice(BigDecimal oprice) {
        this.oprice = oprice;
    }


    @Column(name = "okpprice", nullable = true, precision = 5)
    public BigDecimal getOkpprice() {
        return okpprice;
    }

    public void setOkpprice(BigDecimal okpprice) {
        this.okpprice = okpprice;
    }


    @Column(name = "platform", nullable = true)
    @Convert(converter=PlatformConverter.class)
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }


    @Column(name = "OS", nullable = true)
    @Convert(converter = OsConverter.class)
    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }





    @Column(name = "sceneid", nullable = true, length = 255)
    public String getSceneid() {
        return sceneid;
    }

    public void setSceneid(String sceneid) {
        this.sceneid = sceneid;
    }


    @Column(name = "merchantsubsidy", nullable = true, precision = 5)
    public BigDecimal getMerchantsubsidy() {
        return merchantsubsidy;
    }

    public void setMerchantsubsidy(BigDecimal merchantsubsidy) {
        this.merchantsubsidy = merchantsubsidy;
    }


    @Column(name = "paystatus", nullable = true)
    public Byte getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(Byte paystatus) {
        this.paystatus = paystatus;
    }


    public BigDecimal getCouponlocalprice() {
        return couponlocalprice;
    }

    public void setCouponlocalprice(BigDecimal couponlocalprice) {
        this.couponlocalprice = couponlocalprice;
    }

    @Column(name = "refundtime", nullable = true)
    public Date getRefundtime() {
        return refundtime;
    }

    public void setRefundtime(Date refundtime) {
        this.refundtime = refundtime;
    }


    @Column(name = "refundaudittime", nullable = true)
    public Date getRefundaudittime() {
        return refundaudittime;
    }

    public void setRefundaudittime(Date refundaudittime) {
        this.refundaudittime = refundaudittime;
    }


    @Column(name = "refundreason", nullable = true, length = 500)
    public String getRefundreason() {
        return refundreason;
    }

    public void setRefundreason(String refundreason) {
        this.refundreason = refundreason;
    }


    @Column(name = "refundremark", nullable = true, length = 500)
    public String getRefundremark() {
        return refundremark;
    }

    public void setRefundremark(String refundremark) {
        this.refundremark = refundremark;
    }


    @Column(name = "refundsource", nullable = true)
    public Byte getRefundsource() {
        return refundsource;
    }

    public void setRefundsource(Byte refundsource) {
        this.refundsource = refundsource;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    @Column(name = "iscode", nullable = true)
    public Byte getIscode() {
        return iscode;
    }

    public void setIscode(Byte iscode) {
        this.iscode = iscode;
    }





    @Column(name = "couponprice", nullable = true, precision = 2)
    public BigDecimal getCouponprice() {
        return couponprice;
    }

    public void setCouponprice(BigDecimal couponprice) {
        this.couponprice = couponprice;
    }


    @Column(name = "rate", nullable = true, precision = 4)
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


    @Column(name = "merchantprice", nullable = true, precision = 5)
    public BigDecimal getMerchantprice() {
        return merchantprice;
    }

    public void setMerchantprice(BigDecimal merchantprice) {
        this.merchantprice = merchantprice;
    }


    @Column(name = "mchrmbprice", nullable = true, precision = 2)
    public BigDecimal getMchrmbprice() {
        return mchrmbprice;
    }

    public void setMchrmbprice(BigDecimal mchrmbprice) {
        this.mchrmbprice = mchrmbprice;
    }


    @Column(name = "message", nullable = true, length = 255)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Column(name = "flightid", nullable = true)
    public Integer getFlightid() {
        return flightid;
    }

    public void setFlightid(Integer flightid) {
        this.flightid = flightid;
    }


    @Column(name = "templateid", nullable = true)
    public Integer getTemplateid() {
        return templateid;
    }

    public void setTemplateid(Integer templateid) {
        this.templateid = templateid;
    }


    @Column(name = "tempsceneid", nullable = true, length = 255)
    public String getTempsceneid() {
        return tempsceneid;
    }

    public void setTempsceneid(String tempsceneid) {
        this.tempsceneid = tempsceneid;
    }


    @Column(name = "source", nullable = true)
    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }


    @Column(name = "appointstatus", nullable = true)
    public Integer getAppointstatus() {
        return appointstatus;
    }

    public void setAppointstatus(Integer appointstatus) {
        this.appointstatus = appointstatus;
    }


    @Column(name = "isdelete", nullable = true)
    public Byte getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Byte isdelete) {
        this.isdelete = isdelete;
    }


    @Column(name = "discoutcardid", nullable = true)
    public Long getDiscoutcardid() {
        return discoutcardid;
    }

    public void setDiscoutcardid(Long discoutcardid) {
        this.discoutcardid = discoutcardid;
    }


    @Column(name = "voucherprice", nullable = true, precision = 2)
    public BigDecimal getVoucherprice() {
        return voucherprice;
    }

    public void setVoucherprice(BigDecimal voucherprice) {
        this.voucherprice = voucherprice;
    }


    @Column(name = "itemprice", nullable = true, precision = 2)
    public BigDecimal getItemprice() {
        return itemprice;
    }

    public void setItemprice(BigDecimal itemprice) {
        this.itemprice = itemprice;
    }


    @Column(name = "itemlocalprice", nullable = true, precision = 2)
    public BigDecimal getItemlocalprice() {
        return itemlocalprice;
    }

    public void setItemlocalprice(BigDecimal itemlocalprice) {
        this.itemlocalprice = itemlocalprice;
    }


    @Column(name = "payable", nullable = true, precision = 2)
    public BigDecimal getPayable() {
        return payable;
    }

    public void setPayable(BigDecimal payable) {
        this.payable = payable;
    }


    @Column(name = "youfurmbreduce", nullable = true, precision = 2)
    public BigDecimal getYoufurmbreduce() {
        return youfurmbreduce;
    }

    public void setYoufurmbreduce(BigDecimal youfurmbreduce) {
        this.youfurmbreduce = youfurmbreduce;
    }


    @Column(name = "mchrmbreduce", nullable = true, precision = 2)
    public BigDecimal getMchrmbreduce() {
        return mchrmbreduce;
    }

    public void setMchrmbreduce(BigDecimal mchrmbreduce) {
        this.mchrmbreduce = mchrmbreduce;
    }






    @Column(name = "youfulocalereduce")
    public BigDecimal getYoufulocalereduce() {
        return youfulocalereduce;
    }

    public void setYoufulocalereduce(BigDecimal youfulocalereduce) {
        this.youfulocalereduce = youfulocalereduce;
    }
    @Column(name = "mchlocalereduce")
    public BigDecimal getMchlocalereduce() {
        return mchlocalereduce;
    }

    public void setMchlocalereduce(BigDecimal mchlocalereduce) {
        this.mchlocalereduce = mchlocalereduce;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "postalcode")
    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    @Column(name = "district")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Column(name = "tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCoupontitle() {
        return coupontitle;
    }

    public void setCoupontitle(String coupontitle) {
        this.coupontitle = coupontitle;
    }

    @Transient
    public String getKppriceF() {

        return PriceUtil.getFormatPrice(kpprice);
    }

    @Transient
    public String getOkppriceF() {
        return PriceUtil.getFormatPrice(okpprice);
    }


    @Transient
    public Double getPayableF() {
        return payable==null?this.getPrice().doubleValue():payable.doubleValue();
    }

    @Transient
    public String getOpriceF(){
        return oprice == null ? "" : oprice.stripTrailingZeros().toPlainString();

    }



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public BigDecimal getPostagermb() {
        return postagermb;
    }

    public void setPostagermb(BigDecimal postagermb) {
        this.postagermb = postagermb;
    }

    @Transient
    public List<OrderItem> getOrderItems() {
        return orderItems;
   }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }



    @Transient
    public String getCouponInfo(){
        if(coupontitle==null){
            return null;
        }

        return coupontitle+"/"+couponprice+"/"+couponlocalprice;
    }
    @Transient
    public String getFullOrderItems(){
        StringBuffer sb=new StringBuffer();
        for (OrderItem orderItem : orderItems) {
            if(!sb.toString().isEmpty()){
                sb.append("\n\r");
            }
            sb.append(orderItem.getName())
                    .append("--")
                    .append(orderItem.getSpec()==null?"":orderItem.getSpec()+"*")
                    .append(orderItem.getNum())
                    .append("--")
                    .append(orderItem.getPrice())
                    .append("￥")
            ;
        }
        return sb.toString();
    }

    @Transient
    public Integer getBuyTimes(){return buyTimes;}
    public void setBuyTimes(Integer buyTimes){this.buyTimes=buyTimes;}



}
