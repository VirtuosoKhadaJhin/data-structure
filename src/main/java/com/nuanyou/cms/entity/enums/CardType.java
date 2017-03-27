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

    public final int value;

    public final String name;

    CardType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public CardType[] getValues() {
        return CardType.values();
    }

    public static CardType toEnum(Integer value) {
        if (value == null)
            return null;
        CardType[] values = CardType.values();
        for (CardType type : values)
            if (value.equals(type.value))
                return type;
        return null;
    }

}