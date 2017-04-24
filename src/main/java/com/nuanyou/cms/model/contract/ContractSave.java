package com.nuanyou.cms.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangkai on 2016/9/10.
 */
public class ContractSave {

    private Long id;

    private String pdfUrl;

    private String htmlContent;

    public ContractSave() {
    }

    public ContractSave(Long id, String pdfUrl) {
        this.id = id;
        this.pdfUrl = pdfUrl;
    }

    @ApiModelProperty(value = "合同id")
    @JsonProperty("contractid")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "初始版pdf合同地址")
    @JsonProperty("pdfurl")
    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    @ApiModelProperty(value = "网页版合同html内容")
    @JsonProperty("htmlcontent")
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
