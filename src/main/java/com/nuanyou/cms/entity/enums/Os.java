package com.nuanyou.cms.entity.enums;

/**
 *
 */
public enum Os {
    Huigou(1, "IOS"),
    Youfu(2, "IPAD"),
    WaiMai(3, "Android"),
    Other(9,"其他");

    public final Byte value;

    public final String name;

    Os(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Os[] getValues() {
        return Os.values();
    }

    public static Os toEnum(Integer value) {
        Os[] values = Os.values();
        for (Os type : values) {
            if (type.value== value.byteValue())
               return type;
        }
        return null;
    }

}