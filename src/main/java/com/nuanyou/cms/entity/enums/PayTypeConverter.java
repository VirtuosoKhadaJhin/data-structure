package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class PayTypeConverter implements AttributeConverter<List<PayType>, Integer> {

    @Override
    public Integer convertToDatabaseColumn(List<PayType> attribute) {
        if (attribute == null || attribute.isEmpty())
            return 0;
        Integer flag = 0;
        for (PayType e : attribute) {
            flag += e.value;
        }
        return flag;
    }

    @Override
    public List<PayType> convertToEntityAttribute(Integer dbData) {
        if (dbData == null||dbData==0)
            return null;
        return PayType.toEnums(dbData);
    }

}