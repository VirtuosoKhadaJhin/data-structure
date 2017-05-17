package com.nuanyou.cms.model.contract.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by yangkai on 2016/9/10.
 */
public class ContractTemplate {
    private Long id;

    private String title;

    private List<ContractTemplateParameter> parameters;

    public ContractTemplate(Long id, String title) {
        this.id = id;
        this.title = title;
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

    @JsonProperty("name")
    public String getName() {
        return title;
    }

    @ApiModelProperty(value = "模板参数集合")
    @JsonProperty("parameters")
    public List<ContractTemplateParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ContractTemplateParameter> parameters) {
        this.parameters = parameters;
    }
}
