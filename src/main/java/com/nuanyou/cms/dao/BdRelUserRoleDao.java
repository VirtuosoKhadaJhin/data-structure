package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdRelUserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/26 - 15:46
 */
public interface BdRelUserRoleDao extends JpaRepository<BdRelUserRole, Long>, JpaSpecificationExecutor {
    BdRelUserRole findByUserId(Long id);
}
