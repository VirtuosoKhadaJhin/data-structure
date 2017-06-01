package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.CmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/7.
 */
public interface CmsUserDao extends JpaRepository<CmsUser, Long>, JpaSpecificationExecutor {


    CmsUser findByEmail(String email);
}
