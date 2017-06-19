package com.nuanyou.cms.model.contract.enums;

import javax.persistence.AttributeConverter;

/**
 * 模版状态
 */
public enum TemplateStatus {

    Draft(1, "草稿"),

    Validating(2, "审核中"),

    Release(3, "已发布"),

    Discard(4, "已废弃");

    public final int value;

    public final String name;

    TemplateStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public TemplateStatus[] getValues() {
        return TemplateStatus.values();
    }

    public static TemplateStatus toEnum(int value) {
        for (TemplateStatus type : TemplateStatus.values())
            if (value == type.value)
                return type;
        return null;
    }


    @javax.persistence.Converter
    public static class Converter implements AttributeConverter<TemplateStatus, Integer> {

        @Override
        public Integer convertToDatabaseColumn(TemplateStatus attribute) {
            if (attribute == null)
                return null;
            return attribute.value;
        }

        @Override
        public TemplateStatus convertToEntityAttribute(Integer dbData) {
            if (dbData == null)
                return null;
            return TemplateStatus.toEnum(dbData);
        }

    }
}