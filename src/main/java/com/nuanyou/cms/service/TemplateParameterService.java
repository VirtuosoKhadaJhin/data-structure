package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.TemplateParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Felix on 2016/9/7.
 */
public interface TemplateParameterService {

    Page<TemplateParameter> findByCondition(TemplateParameter district, Pageable pageable);

    TemplateParameter saveNotNull(TemplateParameter entity);

}