package com.nuanyou.cms.model.contract.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/10.
 */
public class ContractTemplate {
    private Long id;

    private String title;

    private Integer type;

    private List<ContractParameter> parameters;

    public ContractTemplate(Long id, String title, Integer type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public ContractTemplate() {
    }

    @ApiModelProperty(value = "模板id")
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "模板标题")
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "模版类型: 1.主合同 2.附加合同")
    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ApiModelProperty(value = "模板参数集合")
    @JsonProperty("parameters")
    public List<ContractParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ContractParameter> parameters) {
        this.parameters = parameters;
    }
}
