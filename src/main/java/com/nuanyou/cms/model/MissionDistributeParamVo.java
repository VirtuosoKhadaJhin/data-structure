package com.nuanyou.cms.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Byron on 2017/6/29.
 */
public class MissionDistributeParamVo {

    private Long bdId;

    private Date distrDt;

    private List<Long> taskIds;

    public Long getBdId() {
        return bdId;
    }

    public void setBdId(Long bdId) {
        this.bdId = bdId;
    }

    public Date getDistrDt() {
        return distrDt;
    }

    public void setDistrDt(Date distrDt) {
        this.distrDt = distrDt;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }
}
