package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SubsidyTypeConverter implements AttributeConverter<SubsidyType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SubsidyType attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public SubsidyType convertToEntityAttribute(Integer dbData) {
        return SubsidyType.toEnum(dbData);
    }

}