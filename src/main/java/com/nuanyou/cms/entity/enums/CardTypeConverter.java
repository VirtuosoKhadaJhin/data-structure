package com.nuanyou.cms.entity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CardTypeConverter implements AttributeConverter<CardType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CardType attribute) {
        if (attribute == null)
            return null;
        return attribute.value;
    }

    @Override
    public CardType convertToEntityAttribute(Integer dbData) {
        return CardType.toEnum(dbData);
    }

}