package com.nuanyou.cms.model.mission;

import java.util.List;

/**
 * Created by young on 2017/9/12.
 */
public class TaskTypeChangeVo {

    private List<Long> taskIds;

    private String taskType;

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
