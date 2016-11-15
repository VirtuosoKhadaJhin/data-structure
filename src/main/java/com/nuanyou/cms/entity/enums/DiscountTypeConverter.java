package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DiscountTypeConverter implements AttributeConverter<DiscountType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DiscountType attribute) {
        if (attribute == null)
            return null;
        return attribute.value;
    }

    @Override
    public DiscountType convertToEntityAttribute(Integer dbData) {
        return DiscountType.toEnum(dbData);
    }

}