package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class AppointTypeConverter implements AttributeConverter<List<AppointType>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<AppointType> attribute) {
        if (attribute == null || attribute.isEmpty())
            return null;
        Integer flag = 0;
        for (AppointType e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<AppointType> convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        return AppointType.toEnums(dbData);
    }

}