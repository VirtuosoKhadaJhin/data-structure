package com.nuanyou.cms.model.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Byron on 2017/6/6.
 */
public enum LangsCountry {
    EN_UK ( 1, "en-US", "英语" ),
    ZH_CN ( 2, "zh-CN", "中文" ),
    KO_KR ( 3, "ko-KR", "韩语"),
    JA_JP ( 4, "ja-JP", "日本" ),
    DE_DE ( 5, "de-DE", "德语" ),
    TH ( 6, "th", "泰国" ),
    En_GB(7, "en-GB", "英国英语" )
    ;

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

    public static Boolean verifyIsLocalLanguage(String langsDictionary, Integer key) {
        return langsDictionary.equals ( LangsCountry.ZH_CN.getValue () )
                || langsDictionary.equals ( LangsCountry.EN_UK.getValue () )
                || langsDictionary.equals ( LangsCountry.En_GB.getValue () )
                || langsDictionary.equals ( LangsCountry.toEnum ( key ).getValue () );
    }

    public static List<LangsCountry> localValues(Integer key) {//
        List<LangsCountry> langsCountries = Lists.newArrayList ();
        for (LangsCountry langsCountry : LangsCountry.values ()) {
            if (key == langsCountry.getKey () || langsCountry.getKey () == 1 || langsCountry.getKey () == 2 || langsCountry.getKey () == 7) {
                langsCountries.add ( langsCountry );
            }
        }
        return langsCountries;
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
