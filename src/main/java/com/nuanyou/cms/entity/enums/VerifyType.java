package com.nuanyou.cms.entity.enums;


public enum VerifyType {

    User(1, "用户"),
    Merchant(2, "商家");

    public final int value;

    public final String name;

    VerifyType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static VerifyType toEnum(int value) {
        VerifyType[] values = VerifyType.values();
        for (VerifyType type : values)
            if (type.value == value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

}