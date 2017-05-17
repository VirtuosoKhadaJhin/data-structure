package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.TemplateParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface TemplateParameterDao extends JpaRepository<TemplateParameter, Long>, JpaSpecificationExecutor {

}
