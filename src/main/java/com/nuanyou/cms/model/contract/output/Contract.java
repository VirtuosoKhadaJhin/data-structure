package com.nuanyou.cms.model.contract.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Map;

/**
 * Created by Alan.ye on 2017/4/20.
 */
public class Contract {

    private Long id;
    private Long parentId;
    private Long templateId;
    private String templateTitle;
    private Date startTime;
    private Integer type;
    private Date endTime;
    private Date submitTime;
    private Date rejectTime;
    private Date approveTime;
    private String status;
    private Boolean read;
    private String pdfUrl;
    private String htmlContent;
    private Map<String, String> parameters;
    private Date updateTime;
    private Date createTime;
    private Long userId;
    private String username;
    private Long mchId;
    private Long countryId;
    private String countryName;
    private String mchName;//企业名称
    private String relatedMchName;
    private String signImgUrl;
    private String businessLicense;
    private String paperContract;
    private String remark;
    private String contractNo;
    private Long approverId;
    private String approverName;



    @ApiModelProperty(value = "合同id")
    @JsonProperty("contractid")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "商户名称")
    @JsonProperty("merchantname")
    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    @ApiModelProperty(value = "商户ID")
    @JsonProperty("merchantid")
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    @ApiModelProperty(value = "国家ID")
    @JsonProperty("countryId")
    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @ApiModelProperty(value = "BDid")
    @JsonProperty("userid")
    @JsonIgnore
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @ApiModelProperty(value = "用户名")
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ApiModelProperty(value = "模版ID")
    @JsonProperty("templateid")

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @ApiModelProperty(value = "合同详情pdf地址")
    @JsonProperty("pdfurl")
    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    @ApiModelProperty(value = "合同类型名称")
    @JsonProperty("templatetitle")
    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    @ApiModelProperty(value = "网页版合同")
    @JsonProperty("htmlcontent")
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @ApiModelProperty(value = "参数列表")
    @JsonProperty("parameters")
    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @ApiModelProperty(value = "状态")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ApiModelProperty(value = "备注")
    @JsonProperty("remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ApiModelProperty(value = "主合同ID")
    @JsonProperty("parentid")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @ApiModelProperty(value = "是否已读")
    @JsonProperty("read")
    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @ApiModelProperty(value = "纸质合同URL")
    @JsonProperty("papercontract")
    public String getPaperContract() {
        return paperContract;
    }

    public void setPaperContract(String paperContract) {
        this.paperContract = paperContract;
    }

    @ApiModelProperty(value = "营业执照URL")
    @JsonProperty("businesslicense")
    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    @ApiModelProperty(value = "签名URL")
    @JsonProperty("signature")
    public String getSignImgUrl() {
        return signImgUrl;
    }

    public void setSignImgUrl(String signImgUrl) {
        this.signImgUrl = signImgUrl;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后更新时间")
    @JsonProperty("updatetime")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "合同开始时间")
    @JsonProperty("starttime")
    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "合同截止时间")
    @JsonProperty("endtime")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "审核通过时间")
    @JsonProperty("approvetime")
    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("createtime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "提交审核时间")
    @JsonProperty("submittime")
    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    @ApiModelProperty(value = "合同类型")
    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ApiModelProperty(value = "拒接时间")
    @JsonProperty("rejecttime")
    public Date getRejectTime() {
        return rejectTime;
    }

    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    @ApiModelProperty(value = "合同编号")
    @JsonProperty("contractno")
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @ApiModelProperty(value = "审核人id")
    @JsonProperty("approverid")
    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }


    @ApiModelProperty(value = "商户本地名称")
    @JsonProperty("relatedMchName")
    public String getRelatedMchName() {
        return relatedMchName;
    }

    public void setRelatedMchName(String relatedMchName) {
        this.relatedMchName = relatedMchName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}