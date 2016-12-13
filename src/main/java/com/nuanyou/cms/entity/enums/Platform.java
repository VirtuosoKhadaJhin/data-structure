package com.nuanyou.cms.entity.enums;

/**
 *
 */
public enum Platform {
    Huigou(1, "微信"),
    Youfu(2, "APP"),
    WaiMai(3, "WAP"),
    TuanGou(4, "POS");

    public final Byte value;

    public final String name;

    Platform(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Platform[] getValues() {
        return Platform.values();
    }

    public static Platform toEnum(Integer value) {
        if(value==null){
            return null;
        }
        Platform[] values = Platform.values();
        for (Platform type : values) {
            if (type.value== value.byteValue())
               return type;
        }
        return null;
    }

}