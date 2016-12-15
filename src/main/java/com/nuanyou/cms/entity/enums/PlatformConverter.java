package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PlatformConverter implements AttributeConverter<Platform, Integer> {


    @Override
    public Integer convertToDatabaseColumn(Platform attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public Platform convertToEntityAttribute(Integer dbData) {
        return Platform.toEnum(dbData);
    }

}