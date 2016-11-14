package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class WeekConverter implements AttributeConverter<List<Week>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<Week> attribute) {
        if (attribute == null || attribute.isEmpty())
            return null;
        Integer flag = 0;
        for (Week e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<Week> convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        return Week.toEnums(dbData);
    }

}