package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class UserRangeConverter implements AttributeConverter<List<UserRange>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<UserRange> attribute) {
        if (attribute == null || attribute.isEmpty())
            return null;
        Integer flag = 0;
        for (UserRange e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<UserRange> convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        return UserRange.toEnums(dbData);
    }

}