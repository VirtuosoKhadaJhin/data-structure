package com.nuanyou.cms.model;

/**
 * Created by mylon on 2017/6/14.
 */
public class LangsCountryVo {

    private Integer langsCountryKey;

    private String langsValue;

    private String langsDesc;

    public LangsCountryVo(Integer langsCountryKey, String langsValue, String langsDesc) {
        this.langsCountryKey = langsCountryKey;
        this.langsValue = langsValue;
        this.langsDesc = langsDesc;
    }

    public Integer getLangsCountryKey() {
        return langsCountryKey;
    }

    public void setLangsCountryKey(Integer langsCountryKey) {
        this.langsCountryKey = langsCountryKey;
    }

    public String getLangsValue() {
        return langsValue;
    }

    public void setLangsValue(String langsValue) {
        this.langsValue = langsValue;
    }

    public String getLangsDesc() {
        return langsDesc;
    }

    public void setLangsDesc(String langsDesc) {
        this.langsDesc = langsDesc;
    }
}
