package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RefundSourceConverter implements AttributeConverter<RefundSource, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RefundSource attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public RefundSource convertToEntityAttribute(Integer dbData) {
        return RefundSource.toEnum(dbData);
    }

}