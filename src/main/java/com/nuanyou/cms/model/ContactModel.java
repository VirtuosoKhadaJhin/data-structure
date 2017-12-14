package com.nuanyou.cms.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by young on 2017/9/27.
 */
public class ContactModel extends BasicModel{
    @ApiModelProperty("标识")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("职务标识")
    private Long jobId;
    @ApiModelProperty("职务")
    private String jobName;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("电话")
    private String telephone;
    @ApiModelProperty("邮箱")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
