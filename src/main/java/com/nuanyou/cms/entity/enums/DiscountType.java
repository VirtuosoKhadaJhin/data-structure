package com.nuanyou.cms.entity.enums;

/**
 * 优惠类型
 */
public enum DiscountType {

    Item(1, "商品"),

    Discount(2, "优惠券"),

    Voucher(3, "代金券");

    public final int value;

    public final String name;

    DiscountType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public DiscountType[] getValues() {
        return DiscountType.values();
    }

    public static DiscountType toEnum(int value) {
        for (DiscountType type : DiscountType.values())
            if (value == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }


}