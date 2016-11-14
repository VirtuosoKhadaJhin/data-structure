package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CodeTypeConverter implements AttributeConverter<CodeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CodeType attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public CodeType convertToEntityAttribute(Integer dbData) {
        return CodeType.toEnum(dbData);
    }

}