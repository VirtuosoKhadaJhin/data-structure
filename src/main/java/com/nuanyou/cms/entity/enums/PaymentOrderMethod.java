package com.nuanyou.cms.entity.enums;

/**
 * Created by Byron on 2017/6/9.
 */
public enum PaymentOrderMethod {

    PAY_WX ( 0, "微信" ),
    PAY_ZFB(1, "支付宝"),
    PAY_APPLE(2, "苹果"),
    PAY_JD(3, "京东"),
    PAY_KRP(4, "KRP"),
    PAY_JUMP(5, "重定向"),
    UNKNOWN(null, "未知");

    private Integer key;

    private String name;

    PaymentOrderMethod(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static PaymentOrderMethod toEnum(byte key) {
        PaymentOrderMethod[] values = PaymentOrderMethod.values ();
        for (PaymentOrderMethod status : values)
            if (key == status.getKey ()) {
                return status;
            }
        throw new IllegalArgumentException ( "Cannot create evalue from value: " + key + "!" );
    }

    public static PaymentOrderMethod toEnums(String name) {
        for (PaymentOrderMethod type : PaymentOrderMethod.values ()) {
            if (name.equals ( type.getName () )) {
                return type;
            }
        }
        throw new IllegalArgumentException ( "Cannot create evalue from value: " + name + "!" );
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
