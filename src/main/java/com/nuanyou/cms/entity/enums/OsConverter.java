package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OsConverter implements AttributeConverter<Os, Integer> {


    @Override
    public Integer convertToDatabaseColumn(Os attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public Os convertToEntityAttribute(Integer dbData) {
        return Os.toEnum(dbData);
    }

}