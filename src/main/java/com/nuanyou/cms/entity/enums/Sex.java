package com.nuanyou.cms.entity.enums;


public enum Sex {
    None(0, "无"),
    Male(1, "男"),
    Female(2, "女");

    public final Byte value;

    public final String name;

    Sex(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Sex[] getValues() {
        return Sex.values();
    }

    public static Sex toEnum(byte value) {
        Sex[] values = Sex.values();
        for (Sex type : values)
            if (type.value.equals(value))
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public static Sex toEnum(Integer value) {
        if (value == null)
            return null;
        return toEnum(value.byteValue());
    }

}