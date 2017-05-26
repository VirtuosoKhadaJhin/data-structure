package com.nuanyou.cms.model.contract.request;

/**
 * Created by young on 2017/5/24.
 */
public class TemplateParameterRequests {

    private String[] name;

    private String[] key;

    private String[] defaultValue;

    private Integer[] type;

    private Integer[] sort;

    private boolean[] editable;

    private boolean[] nullable;

    private String[] hint;

    private String[] remark;

    private boolean[] multiValuable;

    private String[] regex;


    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    public String[] getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String[] defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer[] getType() {
        return type;
    }

    public void setType(Integer[] type) {
        this.type = type;
    }

    public Integer[] getSort() {
        return sort;
    }

    public void setSort(Integer[] sort) {
        this.sort = sort;
    }

    public boolean[] getEditable() {
        return editable;
    }

    public void setEditable(boolean[] editable) {
        this.editable = editable;
    }

    public boolean[] getNullable() {
        return nullable;
    }

    public void setNullable(boolean[] nullable) {
        this.nullable = nullable;
    }

    public String[] getHint() {
        return hint;
    }

    public void setHint(String[] hint) {
        this.hint = hint;
    }

    public String[] getRemark() {
        return remark;
    }

    public void setRemark(String[] remark) {
        this.remark = remark;
    }

    public boolean[] getMultiValuable() {
        return multiValuable;
    }

    public void setMultiValuable(boolean[] multiValuable) {
        this.multiValuable = multiValuable;
    }

    public String[] getRegex() {
        return regex;
    }

    public void setRegex(String[] regex) {
        this.regex = regex;
    }
}
