package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class OrderPayTypeConverter implements AttributeConverter<OrderPayType, Integer> {


    @Override
    public Integer convertToDatabaseColumn(OrderPayType attribute) {
        if(attribute==null){
            return null;
        }
        return attribute.value;
    }

    @Override
    public OrderPayType convertToEntityAttribute(Integer dbData) {
        return OrderPayType.toEnum(dbData);
    }
}
