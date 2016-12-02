package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class VerifyTypeConverter implements AttributeConverter<VerifyType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(VerifyType attribute) {
        if (attribute == null)
            return null;
        return attribute.value;
    }

    @Override
    public VerifyType convertToEntityAttribute(Integer dbData) {
        return VerifyType.toEnum(dbData);
    }

}