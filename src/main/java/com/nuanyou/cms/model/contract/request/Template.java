package com.nuanyou.cms.model.contract.request;

import java.util.List;

/**
 * Created by Felix on 2017/6/2.
 */
public class Template {
    private List<TemplateParameterRequest> list;
    private String title;
    private Integer templateType;
    private Long countryId;
    private String type;
    private String shortCode;
    private Long id;
    private List<Long> selectedParamIds;
    private List<Long> paramIds;

    public List<TemplateParameterRequest> getList() {
        return list;
    }

    public void setList(List<TemplateParameterRequest> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getSelectedParamIds() {
        return selectedParamIds;
    }

    public void setSelectedParamIds(List<Long> selectedParamIds) {
        this.selectedParamIds = selectedParamIds;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public List<Long> getParamIds() {
        return paramIds;
    }

    public void setParamIds(List<Long> paramIds) {
        this.paramIds = paramIds;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
