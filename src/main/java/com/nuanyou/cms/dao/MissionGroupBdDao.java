package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.mission.MissionGroupBd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MissionGroupBdDao extends JpaRepository<MissionGroupBd, Long>, JpaSpecificationExecutor {


    List<MissionGroupBd> findByGroupId(Long groupid);

    List<MissionGroupBd> findByBdId(Long bdid);

    @Query("select t from MissionGroupBd t where t.groupId!=?1")
    List<MissionGroupBd> findByNonGroupId(Long groupId);

    @Query("select t from MissionGroupBd t where t.groupId in (?1)")
    List<MissionGroupBd> findByGroupIds(List<Long> groupIds);

    @Transactional
    @Modifying
    @Query("delete from MissionGroupBd t where t.groupId = ?1")
    void deleteByGroupId(Long id);

    @Transactional
    @Modifying
    @Query("delete from MissionGroupBd t where t.bdId in (?1)")
    void deleteOldLeaders(List<Long> oldLeaders);

    @Query("select t from MissionGroupBd t where t.groupId=?1 and t.bdId=?2")
    List<MissionGroupBd> findByGroupIdAndBdId(Long id, Long oldLeader);
}
