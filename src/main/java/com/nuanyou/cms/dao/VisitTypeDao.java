package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by young on 2017/9/12.
 */
public interface VisitTypeDao extends JpaRepository<VisitType, Long>, JpaSpecificationExecutor {
}
