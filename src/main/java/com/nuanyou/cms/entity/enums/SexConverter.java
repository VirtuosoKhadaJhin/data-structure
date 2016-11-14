package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SexConverter implements AttributeConverter<Sex, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sex attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public Sex convertToEntityAttribute(Integer dbData) {
        return Sex.toEnum(dbData);
    }

}