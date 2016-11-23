package com.nuanyou.cms.entity.enums;

/**
 * 优惠券模板类型
 */
public enum CouponTemplateType {

    None(0, "无"), //

    Merchant(1, "指定商家"), //

    Currency(2, "通用");//

    public final int value;

    public final String name;

    CouponTemplateType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static CouponTemplateType toEnum(int value) {
        CouponTemplateType[] values = CouponTemplateType.values();
        for (CouponTemplateType type : values)
            if (value == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

}