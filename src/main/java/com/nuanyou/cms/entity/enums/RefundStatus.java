package com.nuanyou.cms.entity.enums;

/**
 * by Felix
 */
public enum RefundStatus {

    Unknown(0,"未退款"),
    RefundInProgress(201, "已申请退款"),
    Failure(202, "退款已拒绝"),
    Success(203, "退款已处理");




    public final Integer value;

    public final String name;

    RefundStatus(Integer value, String name) {
        this.value = value;this.name=name;
    }


    public static RefundStatus toEnum(Integer value) {
        if(value==null){
            return null;
        }
        RefundStatus[] values = RefundStatus.values();
        for (RefundStatus type : values)
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
