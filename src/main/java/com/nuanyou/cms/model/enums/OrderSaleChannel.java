package com.nuanyou.cms.model.enums;

import com.nuanyou.cms.entity.enums.AppointType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mylon.sun on 2017/11/15.
 */
public enum OrderSaleChannel {

    dianping(1, "大众点评"),
    ctrip(2, "携程"),
    mafengwo(3, "蚂蜂窝"),
    qyer(4, "穷游"),
    fliggy(5, "飞猪");

    public final Byte value;

    public final String name;

    OrderSaleChannel(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public AppointType[] getValues() {
        return AppointType.values();
    }

    public static List<OrderSaleChannel> toEnums(byte value) {
        List<OrderSaleChannel> result = new ArrayList<>();
        OrderSaleChannel[] values = OrderSaleChannel.values();
        for (OrderSaleChannel type : values) {
            if (type.value == 0)
                continue;
            if ((value & type.value) == type.value)
                result.add(type);
        }
        return result;
    }

    public static List<OrderSaleChannel> toEnums(Integer value) {
        return toEnums(value.byteValue());
    }
}
