package com.nuanyou.cms.entity.enums;

/**
 * by Felix
 */
public enum RefundStatus {

    Unknown(0, "未退款"),
    RefundInProgress(201, "待处理"),
    Failure(202, "退款失败"),
    Success(203, "退款成功");


    public final Integer value;

    public final String name;

    RefundStatus(Integer value, String name) {
        this.value = value;
        this.name = name;
    }


    public static RefundStatus toEnum(Integer value) {
        if (value == null) {
            return null;
        }
        RefundStatus[] values = RefundStatus.values();
        for (RefundStatus type : values)
            if (type.value.equals(value)) {
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
