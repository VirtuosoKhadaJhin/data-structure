package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class GuidelineTypeConverter implements AttributeConverter<List<GuidelineType>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<GuidelineType> attribute) {
        if (attribute == null || attribute.isEmpty())
            return null;
        Integer flag = 0;
        for (GuidelineType e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<GuidelineType> convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        return GuidelineType.toEnums(dbData);
    }

}