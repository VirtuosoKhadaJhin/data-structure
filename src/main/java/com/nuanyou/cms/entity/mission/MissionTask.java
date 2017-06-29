package com.nuanyou.cms.entity.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MissionGroup;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Byron on 2017/6/27.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_mission_task")
public class MissionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mchid")
    private Merchant merchant;

    @Column(name = "mchname")
    private String mchName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private MissionGroup group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bdid")
    private BdUser bdId;

    @Column(name = "status")
    private int status;

    @Column(name = "version")
    private String version;

    @Column(name = "orderno")
    private String orderno;

    @Column(name = "remark")
    private String remark;

    @Column(name = "auditor")
    private Long auditor;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "auditdt", nullable = true)
    private Date auditDt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "finishdt", nullable = true)
    private Date finishDt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "createdt", nullable = true)
    private Date createDt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "updatedt", nullable = true)
    private Date updateDt;

    @Column(name = "delflag")
    private Boolean delFlag = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public MissionGroup getGroup() {
        return group;
    }

    public void setGroup(MissionGroup group) {
        this.group = group;
    }

    public BdUser getBdId() {
        return bdId;
    }

    public void setBdId(BdUser bdId) {
        this.bdId = bdId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public Date getAuditDt() {
        return auditDt;
    }

    public void setAuditDt(Date auditDt) {
        this.auditDt = auditDt;
    }

    public Date getFinishDt() {
        return finishDt;
    }

    public void setFinishDt(Date finishDt) {
        this.finishDt = finishDt;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }
}
