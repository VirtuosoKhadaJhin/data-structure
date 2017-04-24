package com.nuanyou.cms.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangkai on 2016/9/12.
 */
public class ContractTemplateParameter implements Comparable {
    private String name;

    private String key;

    private String defaultValue;

    private Byte type;

    private Integer sort = 0;

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

    @ApiModelProperty(value = "类型：1、文本，2：数字")
    @JsonProperty("type")
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @ApiModelProperty(value = "排序值")
    @JsonProperty("sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        if (sort == null) {
            sort = 0;
        }
        this.sort = sort;
    }


    @Override
    public int compareTo(Object o) {

        ContractTemplateParameter contractTemplateParameter = (ContractTemplateParameter) o;

        int sort = contractTemplateParameter.getSort();


        return this.sort.compareTo(sort);
    }
}
