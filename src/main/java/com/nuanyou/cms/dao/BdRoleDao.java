package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/26 - 11:43
 */
public interface BdRoleDao extends JpaRepository<BdRole, Long>, JpaSpecificationExecutor {
}
