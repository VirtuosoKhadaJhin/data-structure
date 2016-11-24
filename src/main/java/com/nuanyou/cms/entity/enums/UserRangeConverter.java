package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class UserRangeConverter implements AttributeConverter<List<UserRange>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<UserRange> attribute) {
        return UserRange.toValue(attribute);
    }

    @Override
    public List<UserRange> convertToEntityAttribute(Integer dbData) {
        return UserRange.toEnums(dbData);
    }

}