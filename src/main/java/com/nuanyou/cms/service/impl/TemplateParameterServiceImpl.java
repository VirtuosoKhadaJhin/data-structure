package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.TemplateParameterDao;
import com.nuanyou.cms.entity.TemplateParameter;
import com.nuanyou.cms.service.TemplateParameterService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class TemplateParameterServiceImpl implements TemplateParameterService {
    @Autowired
    private TemplateParameterDao templateParameterDao;

    @Override
    public Page<TemplateParameter> findByCondition(TemplateParameter templateParameter, Pageable pageable) {
        ExampleMatcher e = ExampleMatcher.matching();
        ExampleMatcher.GenericPropertyMatcher g = ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.ENDING);
        if (templateParameter.getTemplateid() != null) {
            e = e.withMatcher("templateid", g.exact());
        }    if (templateParameter.getId() != null) {
            e = e.withMatcher("id", g.exact());
        }
        return templateParameterDao.findAll(Example.of(templateParameter, e), pageable);
    }


    @Override
    public TemplateParameter saveNotNull(TemplateParameter entity) {
        if (entity.getId() == null) {
            return templateParameterDao.save(entity);
        }
        TemplateParameter oldEntity = templateParameterDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return templateParameterDao.save(oldEntity);
    }
}
