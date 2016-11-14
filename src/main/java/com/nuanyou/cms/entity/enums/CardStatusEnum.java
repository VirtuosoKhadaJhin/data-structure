package com.nuanyou.cms.entity.enums;

/**
 * Created by yangkai on 2016/8/22.
 */
public enum CardStatusEnum {
    UNUSE("未使用", (byte) 1), //
    USED("已使用", (byte) 2), //
    FREEZE("冻结", (byte) 3), //
    EXPIRE("过期", (byte) 4);

    public String name;

    public byte value;

    private CardStatusEnum(String name, byte value) {
        this.value = value;
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CardStatusEnum toEnum(byte value) {
        CardStatusEnum[] values = CardStatusEnum.values();
        for (CardStatusEnum type : values)
            if (type.value == value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

}