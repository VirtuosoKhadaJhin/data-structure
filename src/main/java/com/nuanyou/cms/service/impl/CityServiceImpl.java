package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CityDao;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CityService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Override
    public Page<City> findByCondition(Integer index, City entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");
        ExampleMatcher e = ExampleMatcher.matching();
        ExampleMatcher.GenericPropertyMatcher g = ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.ENDING);
        if (entity.getId() != null) {
            e = e.withMatcher("id", g.exact());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            e = e.withMatcher("name", g.contains());
        } else {
            entity.setName(null);
        }

        if (entity.getDisplay() != null) {
            e = e.withMatcher("display", g.exact());
        } else {
            entity.setDisplay(null);
        }
        return cityDao.findAll(Example.of(entity, e), pageable);
    }

    @Override
    public City saveNotNull(City entity) {
        if (entity.getId() == null) {
            return cityDao.save(entity);
        }
        City oldEntity = cityDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return cityDao.save(oldEntity);
    }
}
