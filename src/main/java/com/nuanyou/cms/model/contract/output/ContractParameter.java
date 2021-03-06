package com.nuanyou.cms.model.contract.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Alan.ye on 2016/9/12.
 */
public class ContractParameter implements Comparable<ContractParameter> {

    private Long id;

    private MLString name;

    private String key;

    private String defaultValue;

    private Integer type;

    private Integer sort = 0;

    private String source;

    private boolean editable;

    private boolean nullable;

    private String regex;

    private MLString hint;

    private MLString remark;

    private boolean multiValuable;

    private boolean common;

    private boolean custom;

    private String note;

    private Long referenceId;
    @ApiModelProperty(value = "参数名称")
    @JsonProperty("name")
    public MLString getName() {
        return name;
    }

    public void setName(MLString name) {
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


    @ApiModelProperty(value = "备选数据源")
    @JsonProperty("source")
    public String  getSource() {
        return source;
    }

    public void setSource(String  source) {
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
    public MLString getHint() {
        return hint;
    }

    public void setHint(MLString hint) {
        this.hint = hint;
    }
    @ApiModelProperty(value = "属性说明")
    @JsonProperty("remark")
    public MLString getRemark() {
        return remark;
    }

    public void setRemark(MLString remark) {
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


    @ApiModelProperty(value = "引用id")
    @JsonProperty("referenceid")
    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int compareTo(ContractParameter that) {
        return this.sort.compareTo(that.sort);
    }

    public static class CodeAndName{
        public String code;
        public String name;
    }

    public static class MLString{
        private  String key;
        private  String content;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}