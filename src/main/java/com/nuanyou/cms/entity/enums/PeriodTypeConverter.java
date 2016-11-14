package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PeriodTypeConverter implements AttributeConverter<PeriodType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PeriodType attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public PeriodType convertToEntityAttribute(Integer dbData) {
        return PeriodType.toEnum(dbData);
    }

}