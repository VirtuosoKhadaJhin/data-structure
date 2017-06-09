package com.nuanyou.cms.model.enums;

/**
 * Created by Byron on 2017/6/6.
 */
public enum LangsCountry {
    EN_UK ( 1, "en-US", "英语" ),
    ZH_CN ( 2, "zh-CN", "中文" ),
    KO_KR ( 3, "ko-KR", "韩语"),
    JA_JP ( 4, "ja-JP", "日本" ),
    DE_DE ( 5, "de-DE", "德语" ),
    TH ( 6, "th", "泰国" );

    private Integer key;
    private String value;
    private String desc;

    LangsCountry(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    LangsCountry(Integer key, String value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    public static LangsCountry toEnum(String value) {
        for (LangsCountry langsCountry : LangsCountry.values ()) {
            if (value.equals ( langsCountry.getValue () )) {
                return langsCountry;
            }
        }
        throw new IllegalArgumentException ( "Cannot create evalue from value: " + value + "!" );
    }

    public static LangsCountry toEnum(Integer key) {
        if (key == null)
            return null;
        for (LangsCountry langsCountry : LangsCountry.values ()) {
            if (key == langsCountry.getKey ()) {
                return langsCountry;
            }
        }
        throw new IllegalArgumentException ( "Cannot create evalue from key: " + key + "!" );
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
