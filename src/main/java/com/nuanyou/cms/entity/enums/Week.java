package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 周
 */
public enum Week {

    /**
     * 每天
     */
    All(0, "每天"), //
    /**
     * 周一
     */
    Monday(1, "周一"), //
    /**
     * 周二
     */
    Tuesday(2, "周二"), //
    /**
     * 周三
     */
    Wednesday(4, "周三"), //
    /**
     * 周四
     */
    Thursday(8, "周四"), //
    /**
     * 周五
     */
    Friday(16, "周五"), //
    /**
     * 周六
     */
    Saturday(32, "周六"), //
    /**
     * 周日
     */
    Sunday(64, "周日");//

    public final Byte value;

    public final String name;

    Week(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Week[] getValues() {
        return Week.values();
    }

    public static List<Week> toEnums(byte value) {
        List<Week> result = new ArrayList<>();
        if (value == 0) {
            result.add(Week.All);
            return result;
        }
        Week[] values = Week.values();
        for (Week type : values) {
            if (type.value == 0)
                continue;
            if ((value & type.value) == type.value)
                result.add(type);
        }
        return result;
    }

    public static List<Week> toEnums(Integer value) {
        return toEnums(value.byteValue());
    }

}