package com.nuanyou.cms.entity.enums;

/**
 * 二维码类型
 */
public enum CardType {

    /**
     * 礼品券
     */
    Gift(1, "礼品券"), //
    /**
     * 折扣券
     */
    Discount(2, "折扣券"), //
    /**
     * 代金券
     */
    Voucher(3, "代金券");//

    public final Byte value;

    public final String name;

    CardType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public CardType[] getValues() {
        return CardType.values();
    }

    public static CardType toEnum(byte value) {
        CardType[] values = CardType.values();
        for (CardType type : values)
            if ((value & type.value) == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public static CardType toEnum(Integer value) {
        if (value == null)
            return null;
        return toEnum(value.byteValue());
    }

}