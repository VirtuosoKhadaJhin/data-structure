package com.nuanyou.cms.entity.enums;

/**
 *
 */
public enum OrderType {
    Huigou(1, "惠购订单"),
    Youfu(2, "优付订单"),
    WaiMai(3, "外卖订单"),
    TuanGou(4, "特品推荐订单"),
    Pos(5, "POS收款"),
    Lianmeng(6, "联盟订单"),
    Shunfenggou(7, "顺风购订单"),
    Daijinquan(8, "代金券订单"),
    Zhiyougou(9, "直邮购");

    public final Byte value;

    public final String name;

    OrderType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public OrderType[] getValues() {
        return OrderType.values();
    }

    public static OrderType toEnum(Integer value) {
        OrderType[] values = OrderType.values();
        for (OrderType type : values) {
            if (type.value== value.byteValue())
               return type;
        }
        return null;
    }

}