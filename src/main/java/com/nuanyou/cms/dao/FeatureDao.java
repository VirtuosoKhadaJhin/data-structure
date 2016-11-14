package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/29.
 */
public interface FeatureDao extends JpaRepository<Feature, Long>, JpaSpecificationExecutor {

}
