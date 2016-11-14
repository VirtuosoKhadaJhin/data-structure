package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态
 */
public enum OrderStatus {

    /**
     * 已支付
     */
    Paid(1, "已支付"), //
    /**
     * 已消费
     */
    Consumed(2, "已消费"), //
    /**
     * 已评价
     */
    Commented(4, "已评价"), //
    /**
     * 已关闭
     */
    Closed(8, "已关闭"), //
    /**
     * 已删除
     */
    Deleted(16, "已删除"), //
    /**
     * 已申请退款
     */
    ApplyRefund(32, "已申请退款");//

    public final Byte value;

    public final String name;

    OrderStatus(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public OrderStatus[] getValues() {
        return OrderStatus.values();
    }

    public static List<OrderStatus> toEnums(byte value) {
        List<OrderStatus> result = new ArrayList<>();
        OrderStatus[] values = OrderStatus.values();
        for (OrderStatus type : values) {
            if (type.value == 0)
                continue;
            if ((value & type.value) == type.value)
                result.add(type);
        }
        return result;
    }

    public static List<OrderStatus> toEnums(Integer value) {
        return toEnums(value.byteValue());
    }

}