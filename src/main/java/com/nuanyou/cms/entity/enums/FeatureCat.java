package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;

/**
 * 优惠类型
 */
public enum FeatureCat {

    All(0, "全部"),

    Food(1, "美食"),

    Shopping(2, "购物"),

    Play(3, "娱乐");

    public final int value;

    public final String name;

    FeatureCat(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public FeatureCat[] getValues() {
        return FeatureCat.values();
    }

    public static FeatureCat toEnum(int value) {
        for (FeatureCat type : FeatureCat.values())
            if (value == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }


    @javax.persistence.Converter
    public static class Converter implements AttributeConverter<FeatureCat, Integer> {

        @Override
        public Integer convertToDatabaseColumn(FeatureCat attribute) {
            if (attribute == null)
                return null;
            return attribute.value;
        }

        @Override
        public FeatureCat convertToEntityAttribute(Integer dbData) {
            return FeatureCat.toEnum(dbData);
        }

    }
}