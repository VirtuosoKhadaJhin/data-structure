package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public enum UserRange {

    None(0, "无限制"),
    HuiGou(1, "惠购订单"),
    YouFu(2, "优付订单"),
    WaiMai(4, "外卖"),
    TuanGou(8, "团购"),
    POS(16, "POS收款"),
    Voucher(32, "代金券");

    public final Byte value;

    public final String name;

    UserRange(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public UserRange[] getValues() {
        return UserRange.values();
    }

    public static List<UserRange> toEnums(byte value) {
        List<UserRange> result = new ArrayList<>();
        if (value == 0) {
            result.add(None);
            return result;
        }
        UserRange[] values = UserRange.values();
        for (UserRange type : values) {
            if (type.value == 0) {
                continue;
            }
            if ((value & type.value) == type.value)
                result.add(type);
        }
        return result;
    }

    public static List<UserRange> toEnums(Integer value) {
        if (value == null)
            return null;
        return toEnums(value.byteValue());
    }

    public static Integer toValue(List<UserRange> attribute) {
        if (attribute == null || attribute.isEmpty())
            return null;
        Integer flag = 0;
        for (UserRange e : attribute) {
            flag += e.value;
        }
        return flag;
    }

}