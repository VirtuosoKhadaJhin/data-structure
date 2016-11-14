package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 攻略类型
 */
public enum GuidelineType {

    /**
     * 应用内
     */
    App(1, "应用内"), //
    /**
     * 推送
     */
    Push(2, "推送");//

    public final Byte value;

    public final String name;

    GuidelineType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public GuidelineType[] getValues() {
        return GuidelineType.values();
    }

    public static List<GuidelineType> toEnums(byte value) {
        List<GuidelineType> result = new ArrayList<>();
        GuidelineType[] values = GuidelineType.values();
        for (GuidelineType type : values) {
            if ((value & type.value) == type.value)
                result.add(type);
        }
        return result;
    }

    public static List<GuidelineType> toEnums(Integer value) {
        return toEnums(value.byteValue());
    }

}