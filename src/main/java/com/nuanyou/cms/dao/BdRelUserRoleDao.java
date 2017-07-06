package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdRelUserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BdRelUserRoleDao extends JpaRepository<BdRelUserRole, Long>, JpaSpecificationExecutor {

    BdRelUserRole findByUserId(Long id);

    @Query("select t from BdRelUserRole t where t.user.id in (?1)")
    List<BdRelUserRole> findByUserIds(Collection<Long> userIds);
}
