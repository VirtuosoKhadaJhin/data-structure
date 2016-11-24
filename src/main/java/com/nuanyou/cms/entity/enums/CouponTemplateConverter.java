package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CouponTemplateConverter implements AttributeConverter<CouponTemplateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CouponTemplateType attribute) {
        if (attribute == null)
            return null;
        return attribute.value;
    }

    @Override
    public CouponTemplateType convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        return CouponTemplateType.toEnum(dbData);
    }

}