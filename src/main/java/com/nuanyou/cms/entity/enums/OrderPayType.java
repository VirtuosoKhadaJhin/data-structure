package com.nuanyou.cms.entity.enums;

/**
 * Created by yangkai on 2016/9/19.
 */
public enum OrderPayType {
    WXPAY( 1,"微信"), ALIPAY( 2,"支付宝"), COD(3,"到付"), APPLEPAY(4,"APPLEPAY"), UNION( 5,"联盟支付"), JDPAY(6,"京东支付");

    public final Integer value;

    public final String name;

    OrderPayType(Integer value,String name) {
        this.value = value;this.name=name;
    }


    public static OrderPayType toEnum(Integer value) {
        if(value==null){
            return null;
        }
        OrderPayType[] values = OrderPayType.values();
        for (OrderPayType type : values)
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
