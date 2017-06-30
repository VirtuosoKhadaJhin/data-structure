package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MissionGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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
}
