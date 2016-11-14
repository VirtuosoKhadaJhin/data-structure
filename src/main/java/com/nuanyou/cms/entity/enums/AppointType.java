package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public enum AppointType {

    /**
     * 无需预约
     */
    NonReserve(0, "无需预约"), //
    /**
     * 团购需预约
     */
    TuanGouReserve(1, "团购需预约"), //
    /**
     * 惠购需预约
     */
    HuiGouReserve(2, "惠购需预约");//

    public final Byte value;

    public final String name;

    AppointType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public AppointType[] getValues() {
        return AppointType.values();
    }

    public static List<AppointType> toEnums(byte value) {
        List<AppointType> result = new ArrayList<>();
        if (value == 0) {
            result.add(AppointType.NonReserve);
            return result;
        }
        AppointType[] values = AppointType.values();
        for (AppointType type : values) {
            if (type.value == 0)
                continue;
            if ((value & type.value) == type.value)
                result.add(type);
        }
        return result;
    }

    public static List<AppointType> toEnums(Integer value) {
        return toEnums(value.byteValue());
    }

}