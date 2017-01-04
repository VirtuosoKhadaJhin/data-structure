package com.nuanyou.cms.entity.enums;

/**
 * by Felix
 */
public enum NewOrderStatus {
    //状态码 0、已下单 1、已支付 2、已消费 3、已评价 4、已关闭 104、自动核销 105、商户核销 203、退款成功

    /**
     * 已支付
     */
    Placed(0, "已下单"),
    /**
     * 已消费
     */
    Payed(1, "已支付"),
    /**
     * 已评价
     */
    Consumed(2, "已消费"),
    /**
     * 已关闭
     */
    Commented(3, "已评价"),
    /**
     * 已删除
     */
    Closed(4, "已关闭"),
    /**
     * 已申请退款
     */
    AutoVerification(104, "自动核销"),

    /**
     * 已申请退款
     */
    MerchantVerfication(105, "商户核销"),
    /**
     * 已申请退款
     */
    RefundSuccess(203, "退款成功");




    public final Integer value;

    public final String name;

    NewOrderStatus(Integer value, String name) {
        this.value = value;this.name=name;
    }


    public static NewOrderStatus toEnum(Integer value) {
        if(value==null){
            return null;
        }
        NewOrderStatus[] values = NewOrderStatus.values();
        for (NewOrderStatus type : values)
            if (type.value.equals(value)){
                return type;
            }
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
