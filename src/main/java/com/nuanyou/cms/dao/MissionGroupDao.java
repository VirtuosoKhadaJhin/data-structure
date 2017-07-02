package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.MissionGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sharp on 2017/6/28 - 15:26
 */
public interface MissionGroupDao extends JpaRepository<MissionGroup, Long>, JpaSpecificationExecutor {

    /**
     * 查找所有可见的group
     *
     * @return
     */
    @Query(value = "SELECT t from MissionGroup t where delflag=0")
    List<MissionGroup> findAllGroup();
    /**
     * 通过组长查找组
     *
     * @param leaderid
     * @return
     */
    List<MissionGroup> findByLeaderId(Long leaderid);

    /**
     * 通过组id查找组
     * @param id
     * @return
     */
    List<MissionGroup> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE MissionGroup set isPublic=?2 where id=?1")
    void updatePublicByGroupId(Long groupId, byte isPublic);

    @Modifying
    @Transactional
    @Query("UPDATE MissionGroup set delFlag=1 where id=?1")
    void deleteGroup(Long groupId);

    @Modifying
    @Transactional
    @Query("UPDATE MissionGroup set leaderId=?1 where id=?2")
    void updateLeaderByGroupId(BdUser leader, Long groupId);

}
