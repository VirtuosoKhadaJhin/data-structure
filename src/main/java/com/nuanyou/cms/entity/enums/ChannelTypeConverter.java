package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class ChannelTypeConverter implements AttributeConverter<ChannelType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ChannelType attribute) {
        if (attribute == null)
            return null;
        return attribute.value.intValue();
    }

    @Override
    public ChannelType convertToEntityAttribute(Integer dbData) {
        return ChannelType.toEnum(dbData);
    }

}