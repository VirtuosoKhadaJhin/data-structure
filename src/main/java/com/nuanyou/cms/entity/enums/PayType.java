package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付类型
 */
public enum PayType {

    /**
     * 微信
     */
    WeChat(1, "微信"), //
    /**
     * 支付宝
     */
    Alipay(2, "支付宝"), //
    /**
     * 到付
     */
    COD(4, "到付"), //
    /**
     * ApplePay
     */
    ApplePay(8, "ApplePay"), //
    /**
     * 联盟支付
     */
    Union(16, "联盟支付"), //

    /**
     * 京东支付
     */
    JdPay(32, "京东支付");//

    public final Byte value;

    public final String name;

    PayType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public PayType[] getValues() {
        return PayType.values();
    }

    public static PayType toEnum(byte value) {
        PayType[] values = PayType.values();
        for (PayType type : values)
            if ((value & type.value) == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public static List<PayType> toEnums(byte value) {
        List<PayType> result = new ArrayList<>();
        PayType[] values = PayType.values();
        for (PayType type : values)
            if ((value & type.value) == type.value)
                result.add(type);
        return result;
    }

    public static List<PayType> toEnums(Integer value) {
        return toEnums(value.byteValue());
    }

}