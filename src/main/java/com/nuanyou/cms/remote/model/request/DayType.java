package com.nuanyou.cms.remote.model.request;

/**
 * Created by Edward on 2017/3/21.
 */
public enum DayType {
    Day(1, "日结"),
    Month(2, "月结"),
    HalfMonth(3, "半月结");

    public final Byte value;

    public final String name;

    DayType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public DayType[] getValues() {
        return DayType.values();
    }

    public static DayType toEnum(Integer value) {
        DayType[] values = DayType.values();
        for (DayType type : values) {
            if (type.value== value.byteValue())
                return type;
        }
        return null;
    }
}
