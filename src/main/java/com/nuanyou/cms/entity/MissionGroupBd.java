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
    
    @Column(name = "gourpid")
    private Long gourpId;
    
    @Column(name = "bdid")
    private Long bdId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getGourpId() {
        return gourpId;
    }
    
    public void setGourpId(Long gourpId) {
        this.gourpId = gourpId;
    }
    
    public Long getBdId() {
        return bdId;
    }
    
    public void setBdId(Long bdId) {
        this.bdId = bdId;
    }
}
