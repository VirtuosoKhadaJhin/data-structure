package com.nuanyou.cms.model;

/**
 * Created by Felix on 2017/4/25.
 */
public class MerchantSettlementParamConfig {

    private Long templateId;
    private String artificialPaymentDays;
    private String artificialpoundage;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getArtificialPaymentDays() {
        return artificialPaymentDays;
    }

    public void setArtificialPaymentDays(String artificialPaymentDays) {
        this.artificialPaymentDays = artificialPaymentDays;
    }

    public String getArtificialpoundage() {
        return artificialpoundage;
    }

    public void setArtificialpoundage(String artificialpoundage) {
        this.artificialpoundage = artificialpoundage;
    }

    public MerchantSettlementParamConfig(Long templateId, String artificialpoundage, String artificialPaymentDays) {
        this.templateId = templateId;
        this.artificialPaymentDays = artificialPaymentDays;
        this.artificialpoundage = artificialpoundage;
    }
}
