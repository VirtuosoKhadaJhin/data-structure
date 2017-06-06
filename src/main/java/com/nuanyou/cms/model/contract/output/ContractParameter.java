package com.nuanyou.cms.model.contract.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/12.
 */
public class ContractParameter implements Comparable<ContractParameter> {

    private Long id;

    private String name;

    private String key;

    private String defaultValue;

    private Integer type;

    private Integer sort = 0;

    private List<CodeAndName> source;

    private boolean editable;

    private boolean nullable;

    private String regex;

    private String hint;

    private String remark;

    private boolean multiValuable;

    private boolean common;

    private boolean custom;

    @ApiModelProperty(value = "参数名称")
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "参数键名")
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @ApiModelProperty(value = "默认值")
    @JsonProperty("defaultvalue")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @ApiModelProperty(value = "类型：1、文本，2、整型 ，3、浮点型，4、日期，5、备选数据")
    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ApiModelProperty(value = "排序值")
    @JsonProperty("sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        if (sort == null)
            sort = 0;
        this.sort = sort;
    }
//    @ApiModelProperty(value = "备选数据源")
//    @JsonProperty("source")
//    public String  getSource() {
//        return source;
//    }
//
//    public void setSource(String  source) {
//        this.source = source;
//    }


    @ApiModelProperty(value = "备选数据源")
    @JsonProperty("source")
    public List<CodeAndName>  getSource() {
        return source;
    }

    public void setSource(List<CodeAndName>  source) {
        this.source = source;
    }


    @ApiModelProperty(value = "是否可编辑")
    @JsonProperty("editable")
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    @ApiModelProperty(value = "是否可为空")
    @JsonProperty("nullable")
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    @ApiModelProperty(value = "校验正则表达式")
    @JsonProperty("regex")
    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
    @ApiModelProperty(value = "格式校验提示")
    @JsonProperty("hint")
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
    @ApiModelProperty(value = "属性说明")
    @JsonProperty("remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @ApiModelProperty(value = "是否多值")
    @JsonProperty("multivaluable")
    public boolean isMultiValuable() {
        return multiValuable;
    }

    public void setMultiValuable(boolean multiValuable) {
        this.multiValuable = multiValuable;
    }
    @ApiModelProperty(value = "是否是公共属性")
    @JsonProperty("common")
    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }
    @ApiModelProperty(value = "是否是自定义属性")
    @JsonProperty("custom")
    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(ContractParameter that) {
        return this.sort.compareTo(that.sort);
    }

    public static class CodeAndName{
        public String code;
        public String name;
    }

}