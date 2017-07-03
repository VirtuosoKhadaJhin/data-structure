package com.nuanyou.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 战队 与 bd关系表
 * Created by sharp on 2017/6/28 - 15:02
 */
@Entity
@Table(name = "ny_mission_group_bd", catalog = "nuanyou20") //catalog配置访问的数据库
public class MissionGroupBd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "groupid")
    private Long groupId;
    
    @Column(name = "bdid")
    private Long bdId;
    
    public MissionGroupBd(Long groupId, Long bdId) {
        this.groupId = groupId;
        this.bdId = bdId;
    }
    
    public MissionGroupBd() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public Long getBdId() {
        return bdId;
    }
    
    public void setBdId(Long bdId) {
        this.bdId = bdId;
    }
}
