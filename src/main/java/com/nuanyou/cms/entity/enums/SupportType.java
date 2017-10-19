package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付类型
 */
public enum SupportType {

    /**
     * 惠购
     */
    HuiGou(1, "中文点单"), //
    /**
     * 优付
     */
    YouFu(2, "优付"), //
    /**
     * 团购
     */
    TuanGou(4, "特品推荐"),//
    /**
     * 直邮购
     */
    ZhiYouGou(8, "专柜直购");//

    public final Byte value;

    public final String name;

    SupportType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public SupportType[] getValues() {
        return SupportType.values();
    }

    public static List<SupportType> toEnums(byte value) {
        List<SupportType> result = new ArrayList<>();
        SupportType[] values = SupportType.values();
        for (SupportType type : values)
            if ((value & type.value) == type.value)
                result.add(type);
        return result;
    }

    public static List<SupportType> toEnums(Integer value) {
        if (value == null)
            return null;
        return toEnums(value.byteValue());
    }

}