package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MissionGroupBd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by sharp on 2017/6/28 - 15:27
 */
public interface MissionGroupBdDao extends JpaRepository<MissionGroupBd, Long>, JpaSpecificationExecutor {


    List<MissionGroupBd> findByGroupId(Long groupid);

    List<MissionGroupBd> findByBdId(Long bdid);

    @Query("select t from MissionGroupBd t where t.groupId!=?1")
    List<MissionGroupBd> findByNonGroupId(Long groupId);
}
