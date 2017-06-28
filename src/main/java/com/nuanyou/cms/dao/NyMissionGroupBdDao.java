package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MissionGroupBd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/28 - 15:27
 */
public interface NyMissionGroupBdDao extends JpaRepository<MissionGroupBd, Long>, JpaSpecificationExecutor {
}
