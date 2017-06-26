package com.nuanyou.cms.model.contract.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/10.
 */
public class ContractTemplate {
    private Long id;

    private Long countryId;

    private String countryName;

    private String title;

    private Integer type;

    private Integer status;

    private Date statusChangeTime;

    private TemplateStatus templateStatus;

    private String shortCode;


    private List<ContractParameter> parameters;

    public ContractTemplate(Long countryId, String title, Integer type) {
        this.countryId = countryId;
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

    @ApiModelProperty(value = "国家")
    @JsonProperty("countryid")
    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ApiModelProperty(value = "模板参数集合")
    @JsonProperty("parameters")
    public List<ContractParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ContractParameter> parameters) {
        this.parameters = parameters;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public TemplateStatus getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(TemplateStatus templateStatus) {
        this.templateStatus = templateStatus;
    }


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "合同截止时间")
    @JsonProperty("statuschangetime")
    public Date getStatusChangeTime() {
        return statusChangeTime;
    }



    public void setStatusChangeTime(Date statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
