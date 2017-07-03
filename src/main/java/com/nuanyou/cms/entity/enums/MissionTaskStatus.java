package com.nuanyou.cms.entity.enums;

/**
 * Created by Byron on 2017/6/27.
 */
public enum MissionTaskStatus {

    UN_DISTRIBUTE(0, "未指派"),
    UN_FINISH(1, "已指派"),
    FINISHED(2, "已完成"),
    APPROVED(3, "审核通过"),
    NON_APPROVAL(4, "审核不通过");

    private int key;
    private String value;

    MissionTaskStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static MissionTaskStatus toEnum(int key) {
        MissionTaskStatus[] values = MissionTaskStatus.values();
        for (MissionTaskStatus status : values)
            if (key == status.getKey()) {
                return status;
            }
        throw new IllegalArgumentException("Cannot create evalue from value: " + key + "!");
    }

    public static MissionTaskStatus toEnums(String value) {
        for (MissionTaskStatus type : MissionTaskStatus.values()) {
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot create evalue from value: " + value + "!");
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
