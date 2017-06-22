package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class ItemSupportTypeConverter implements AttributeConverter<List<ItemSupportType>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<ItemSupportType> attribute) {
        if (attribute == null || attribute.isEmpty())
            return 0;
        Integer flag = 0;
        for (ItemSupportType e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<ItemSupportType> convertToEntityAttribute(Integer dbData) {
        if (dbData == null||dbData==0)
            return null;
        return ItemSupportType.toEnums(dbData);
    }

}