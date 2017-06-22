package com.nuanyou.cms.model.contract.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 2017/5/23.
 */
public class TemplateRequest {

    private String title;

    private Integer type;

    private Long countryId;

    private String shortCode;

    private List<Long> paramterids = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getParamterids() {
        return paramterids;
    }

    public void setParamterids(List<Long> paramterids) {
        this.paramterids = paramterids;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
