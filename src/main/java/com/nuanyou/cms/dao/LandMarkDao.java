package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/14.
 */
public interface LandMarkDao extends JpaRepository<Landmark, Long>, JpaSpecificationExecutor {
}
