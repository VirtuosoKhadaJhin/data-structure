package com.nuanyou.cms.entity.enums;

/**
 * 来源
 */
public enum RefundSource {

    Client(1, "客户端"),
    CMS(2, "CMS"),
    Merchant(3, "商户通");

    public final Integer value;

    public final String name;

    RefundSource(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public RefundSource[] getValues() {
        return RefundSource.values();
    }

    public static RefundSource toEnum(Integer value) {
        RefundSource[] values = RefundSource.values();
        for (RefundSource type : values)
            if (value == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }


}