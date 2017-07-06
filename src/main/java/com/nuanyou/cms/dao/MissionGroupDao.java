package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.mission.MissionGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MissionGroupDao extends JpaRepository<MissionGroup, Long>, JpaSpecificationExecutor {

    @Query(value = "SELECT t from MissionGroup t where t.delFlag=0")
    List<MissionGroup> findAllGroup();

    @Query(value = "SELECT t from MissionGroup t where t.leader.id=?1 and t.delFlag=0")
    MissionGroup findByLeaderId(Long leaderid);

    @Query(value = "SELECT t from MissionGroup t where t.viceLeader.id=?1 and t.delFlag=0")
    MissionGroup findByViceLeaderId(Long viceLeaderId);

    @Query(value = "SELECT t from MissionGroup t where t.id=?1 and t.delFlag=0")
    MissionGroup findByGroupId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE MissionGroup set isPublic=?2 where id=?1")
    void updatePublicByGroupId(Long groupId, byte isPublic);

    @Modifying
    @Transactional
    @Query("UPDATE MissionGroup set delFlag=1, leader=null, viceLeader=null where id=?1")
    void deleteGroup(Long groupId);

    @Modifying
    @Transactional
    @Query("UPDATE MissionGroup set leaderId=?1 where id=?2")
    void updateLeaderByGroupId(BdUser leader, Long groupId);

    @Query("SELECT t from MissionGroup t where t.city.id=?1 and t.delFlag=0")
    List<MissionGroup> findGroupsByCityId(Long city);

    @Query("SELECT t from MissionGroup t where t.country.id=?1 and t.delFlag=0")
    List<MissionGroup> findGroupsByCountryId(Long country);

    @Query("SELECT t from MissionGroup t where t.name=?1 and t.delFlag=0")
    List<MissionGroup> findByName(String name);

    @Query(value = "select t from MissionGroup t where t.name=?2 and t.id != ?1 and t.delFlag=0")
    List<MissionGroup> findByNameNonGroup(Long groupId, String name);
}
