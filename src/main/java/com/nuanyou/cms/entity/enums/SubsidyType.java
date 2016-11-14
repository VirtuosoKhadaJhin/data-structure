package com.nuanyou.cms.entity.enums;

/**
 * 用户补贴方式
 */
public enum SubsidyType {

    /**
     * 无优惠
     */
    None(0, "无优惠"), //
    /**
     * 折扣
     */
    Discount(1, "折扣"), //
    /**
     * 固定金额
     */
    Quota(2, "固定金额");

    public final Byte value;

    public final String name;

    SubsidyType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public SubsidyType[] getValues() {
        return SubsidyType.values();
    }

    public static SubsidyType toEnum(byte value) {
        SubsidyType[] values = SubsidyType.values();
        for (SubsidyType type : values) {
            if (type.value == 0) {
                if (value == 0)
                    return type;
                else
                    continue;
            }
            if ((value & type.value) == type.value)
                return type;
        }
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public static SubsidyType toEnum(Integer value) {
        if (value == null)
            return null;
        return toEnum(value.byteValue());
    }

}