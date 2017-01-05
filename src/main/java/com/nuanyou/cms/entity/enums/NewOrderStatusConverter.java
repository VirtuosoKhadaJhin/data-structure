package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class NewOrderStatusConverter implements AttributeConverter<NewOrderStatus, Integer> {


    @Override
    public Integer convertToDatabaseColumn(NewOrderStatus attribute) {
        if(attribute==null){
            return null;
        }
        return attribute.value;
    }

    @Override
    public NewOrderStatus convertToEntityAttribute(Integer dbData) {
        return NewOrderStatus.toEnum(dbData);
    }
}
