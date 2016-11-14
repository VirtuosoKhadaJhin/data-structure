package com.nuanyou.cms.entity.enums;

import java.util.ArrayList;
import java.util.List;

/**
 
 */
public enum ItemSupportType {

    ZhiYouGou(1, "直邮购");//

    public final Byte value;

    public final String name;

    ItemSupportType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public ItemSupportType[] getValues() {
        return ItemSupportType.values();
    }

    public static List<ItemSupportType> toEnums(byte value) {
        List<ItemSupportType> result = new ArrayList<>();
        ItemSupportType[] values = ItemSupportType.values();
        for (ItemSupportType type : values)
            if ((value & type.value) == type.value)
                result.add(type);
        return result;
    }

    public static List<ItemSupportType> toEnums(Integer value) {
        if (value == null)
            return null;
        return toEnums(value.byteValue());
    }

}