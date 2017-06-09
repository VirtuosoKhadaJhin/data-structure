package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.user.ParamsDataMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2017/6/9.
 */
public interface ParamsDataMappingDao extends JpaRepository<ParamsDataMapping, Long>, JpaSpecificationExecutor {




}
