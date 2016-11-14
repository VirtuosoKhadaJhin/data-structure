package com.nuanyou.cms.entity.enums;


/**
 * 渠道类型
 */
public enum ChannelType {

    /**
     * 链接
     */
    Link(1, "链接"), //
    /**
     * 关注
     */
    Attention(2, "关注");//

    public final Byte value;

    public final String name;

    ChannelType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public ChannelType[] getValues() {
        return ChannelType.values();
    }

    public static ChannelType toEnum(byte value) {
        ChannelType[] values = ChannelType.values();
        for (ChannelType type : values)
            if ((value & type.value) == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public static ChannelType toEnum(Integer value) {
        if (value == null)
            return null;
        return toEnum(value.byteValue());
    }

}