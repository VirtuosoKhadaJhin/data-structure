package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class SupportTypeConverter implements AttributeConverter<List<SupportType>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<SupportType> attribute) {
        if (attribute == null || attribute.isEmpty())
            return 0;
        Integer flag = 0;
        for (SupportType e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<SupportType> convertToEntityAttribute(Integer dbData) {
        if (dbData == null||dbData==0)
            return null;
        return SupportType.toEnums(dbData);
    }

}