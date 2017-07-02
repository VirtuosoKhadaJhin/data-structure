package com.nuanyou.cms.model;

import java.util.List;

/**
 * Created by sharp on 2017/6/30 - 13:34
 */
public class MissionGroupRequestVo {
    
    private Long groupId;
    
    private List<Long> userIds;
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public List<Long> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
