package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MissionGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/28 - 15:26
 */
public interface MissionGroupDao extends JpaRepository<MissionGroup, Long>, JpaSpecificationExecutor {
}
