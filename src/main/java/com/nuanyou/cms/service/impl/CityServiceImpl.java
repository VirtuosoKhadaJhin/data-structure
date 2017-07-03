package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CityDao;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.service.CityService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Override
    public Page<City> findByCondition(City entity, Pageable pageable) {
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

    @Override
    public List<City> findCityByCountryId(Long countryId) {
        if (countryId == null) {
            return cityDao.findAll();
        }
        return cityDao.findIdNameList(countryId);
    }

    @Override
    public List<City> findAllCities() {
        return cityDao.findAll();
    }

}