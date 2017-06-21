package com.nuanyou.cms.entity.enums;

/**
 * Created by Byron on 2017/6/8.
 */
public enum PaymentResultStatus {

    PAY_SUCCES(0, "支付成功"),
    PAY_WAITING_PWD(1, "等待密码"),
    PAY_REPEAT(2, "重复订单"),
    PAY_FAILD(3, "失败");

    private Integer key;

    private String name;

    PaymentResultStatus(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static PaymentResultStatus toEnum(int key) {
        PaymentResultStatus[] values = PaymentResultStatus.values();
        for (PaymentResultStatus status : values)
            if (key == status.getKey()) {
                return status;
            }
        throw new IllegalArgumentException("Cannot create evalue from value: " + key + "!");
    }

    public static PaymentResultStatus toEnums(String name) {
        for (PaymentResultStatus type : PaymentResultStatus.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot create evalue from value: " + name + "!");
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
