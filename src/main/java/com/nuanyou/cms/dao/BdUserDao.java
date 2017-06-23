package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/22 - 18:29
 */
public interface BdUserDao extends JpaRepository<BdUser, Long>, JpaSpecificationExecutor {
}
