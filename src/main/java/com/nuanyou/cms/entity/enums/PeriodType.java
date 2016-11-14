package com.nuanyou.cms.entity.enums;

/**
 * 周期类型
 */
public enum PeriodType {

    /**
     * 无
     */
    None(0, "无"), //
    /**
     * 实时
     */
    RealTime(1, "实时"), //
    /**
     * 周
     */
    Week(2, "周"), //
    /**
     * 月
     */
    Month(4, "月"), //
    /**
     * 季
     */
    Quarter(8, "季");//

    public final Byte value;

    public final String name;

    PeriodType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public PeriodType[] getValues() {
        return PeriodType.values();
    }

    public static PeriodType toEnum(byte value) {
        PeriodType[] values = PeriodType.values();
        for (PeriodType type : values) {
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

    public static PeriodType toEnum(Integer value) {
        if (value == null)
            return null;
        return toEnum(value.byteValue());
    }

}