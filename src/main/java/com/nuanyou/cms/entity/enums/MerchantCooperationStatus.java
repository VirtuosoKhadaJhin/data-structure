package com.nuanyou.cms.entity.enums;

/**
 * Created by Byron on 2017/7/4.
 */
public enum MerchantCooperationStatus {

    NON(0, "未签约"),
    COMPETITION_COMMON(1, "竞对共有"),
    EXCLUSIVE(2, "独家经营"),
    CANCEL(3, "取消合作"),
    COLSE(4, "关门"),
    REMOVAL(5, "迁址");

    private Integer key;

    private String name;

    MerchantCooperationStatus(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static MerchantCooperationStatus toEnum(int key) {
        MerchantCooperationStatus[] values = MerchantCooperationStatus.values();
        for (MerchantCooperationStatus status : values)
            if (key == status.getKey()) {
                return status;
            }
        throw new IllegalArgumentException("Cannot create evalue from value: " + key + "!");
    }

    public static MerchantCooperationStatus toEnums(String value) {
        for (MerchantCooperationStatus type : MerchantCooperationStatus.values()) {
            if (value.equals(type.getName())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
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
