package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class RefundStatusConverter implements AttributeConverter<RefundStatus, Integer> {


    @Override
    public Integer convertToDatabaseColumn(RefundStatus attribute) {
        if(attribute==null){
            return null;
        }
        return attribute.value;
    }

    @Override
    public RefundStatus convertToEntityAttribute(Integer dbData) {
        return RefundStatus.toEnum(dbData);
    }
}
