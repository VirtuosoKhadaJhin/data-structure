package com.nuanyou.cms.entity.enums;

/**
 * 二维码类型
 */
public enum CodeType {

    /**
     * 临时
     */
    Transient(1, "临时"), //
    /**
     * 永久
     */
    Persistent(2, "永久");//

    public final Byte value;

    public final String name;

    CodeType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public CodeType[] getValues() {
        return CodeType.values();
    }

    public static CodeType toEnum(byte value) {
        CodeType[] values = CodeType.values();
        for (CodeType type : values)
            if ((value & type.value) == type.value)
                return type;
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public static CodeType toEnum(Integer value) {
        if (value == null)
            return null;
        return toEnum(value.byteValue());
    }

}