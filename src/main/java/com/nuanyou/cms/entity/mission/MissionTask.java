package com.nuanyou.cms.entity.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
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

    @Column(name = "mchid")
    private Long mchId;

    @Column(name = "mchname")
    private String mchName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private MissionGroup group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bdid")
    private BdUser bdId;

    @Column(name = "status")
    private MissionTaskStatus status;

    @Column(name = "remark")
    private String remark;

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

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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

    public MissionTaskStatus getStatus() {
        return status;
    }

    public void setStatus(MissionTaskStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
