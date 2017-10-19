package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购类型
 */
public enum TuanType {

    /**
     * 随时退
     */
    Anytime(1, "随时退"), //
    /**
     * 无条件退
     */
    Unconditional(2, "无条件退"), //
    /**
     * 免预约
     */
    NotAppointment(4, "免预约"),

    InAdvance(8, "提前一小时预定"),;//

    public final Byte value;

    public final String name;

    TuanType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public TuanType[] getValues() {
        return TuanType.values();
    }

    public static List<TuanType> toEnums(byte value) {
        List<TuanType> result = new ArrayList<>();
        TuanType[] values = TuanType.values();
        for (TuanType type : values)
            if ((value & type.value) == type.value)
                result.add(type);
        return result;
    }

    public static List<TuanType> toEnums(Integer value) {
        if (value == null)
            return null;
        return toEnums(value.byteValue());
    }

}