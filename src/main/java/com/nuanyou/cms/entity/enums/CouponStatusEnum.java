package com.nuanyou.cms.entity.enums;

/**
 * Created by yangkai on 2016/8/22.
 */
public enum CouponStatusEnum {
    UNUSE( 1, "未使用"), USED( 2, "已使用"), FREEZE( 3, "已冻结"), EXPIRE( 4, "已过期");

    private Integer value;

    private String name;

    CouponStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByValue(Integer status) {
        String name = null;
        for (CouponStatusEnum couponStatusEnum : CouponStatusEnum.values()) {
            if (status == couponStatusEnum.getValue()) {
                name = couponStatusEnum.getName();
                break;
            }
        }
        return name;
    }
}
