package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderTypeConverter implements AttributeConverter<OrderType, Integer> {


    @Override
    public Integer convertToDatabaseColumn(OrderType attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public OrderType convertToEntityAttribute(Integer dbData) {
        return OrderType.toEnum(dbData);
    }

}