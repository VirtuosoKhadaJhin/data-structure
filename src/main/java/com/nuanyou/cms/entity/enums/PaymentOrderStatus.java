package com.nuanyou.cms.entity.enums;

/**
 * Created by Byron on 2017/6/8.
 */
public enum PaymentOrderStatus {

    UNPAY ( 0, "未支付" ),
    PAY_WAITING ( 1, "等待" ),
    PAY_SUCCES ( 2, "支付成功" ),
    PAY_REFUND ( 3, "退款" ),//全额退款
    FAILD_PAY ( 4, "支付失败" );

    private Integer key;

    private String name;

    PaymentOrderStatus(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static PaymentOrderStatus toEnum(byte key) {
        PaymentOrderStatus[] values = PaymentOrderStatus.values ();
        for (PaymentOrderStatus status : values)
            if (key == status.getKey ()) {
                return status;
            }
        throw new IllegalArgumentException ( "Cannot create evalue from value: " + key + "!" );
    }

    public static PaymentOrderStatus toEnums(String name) {
        for (PaymentOrderStatus type : PaymentOrderStatus.values ()) {
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
