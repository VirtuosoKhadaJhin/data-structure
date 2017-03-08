package com.nuanyou.cms.entity.enums;

/**
 * Created by yangkai on 2017/2/15.
 */
public enum PushDetailTypeEnum {
    USERDEFINED(1, "自定义"), NEARBYMERCHANT(2, "周边商家"), SHOWORDER(3, "查看订单"), SHOWCOUPON(4, "查看优惠券");

    private int type;

    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    PushDetailTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getNameByType(Integer type) {
        if (type != null) {
            for (PushDetailTypeEnum pushDetailTypeEnum : PushDetailTypeEnum.values()) {
                if (type == pushDetailTypeEnum.getType()) {
                    return pushDetailTypeEnum.getName();
                }
            }
        }
        return null;
    }
}
