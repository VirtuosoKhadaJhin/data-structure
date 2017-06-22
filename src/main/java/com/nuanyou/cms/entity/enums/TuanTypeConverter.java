package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class TuanTypeConverter implements AttributeConverter<List<TuanType>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<TuanType> attribute) {
        if (attribute == null || attribute.isEmpty())
            return 0;
        Integer flag = 0;
        for (TuanType e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<TuanType> convertToEntityAttribute(Integer dbData) {
        if (dbData == null||dbData==0)
            return null;
        return TuanType.toEnums(dbData);
    }

}