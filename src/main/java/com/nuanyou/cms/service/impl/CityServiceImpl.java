package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CityDao;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.service.CityService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;
    @Autowired
    private UserService userService;

    @Override
    public Page<City> findByCondition(final City entity, Pageable pageable) {
         final List<Long> countryIds = userService.findUserCountryId();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("country").get("id").in(countryIds));
                }
                if (entity.getId() != null) {
                    predicate.add(cb.equal(root.get("id"), entity.getId()));
                }
                if (StringUtils.isNotEmpty(entity.getName())) {
                    predicate.add(cb.like(root.get("name"), "%"+entity.getName()+"%"));
                }
                if (entity.getDisplay() != null) {
                    predicate.add(cb.equal(root.get("display"), entity.getDisplay()));
                }else {
                    predicate.add(cb.equal(root.get("display"), true));
                }
                Predicate[] arrays = new Predicate[predicate.size()];

                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return cityDao.findAll(specification,pageable);
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
            return cityDao.findAllCities();
        }
        return cityDao.findIdNameList(countryId);
    }

    @Override
    public List<City> findAllCities() {
        return cityDao.findAllCities();
    }

}