package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.LandMarkDao;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.Landmark;
import com.nuanyou.cms.service.LandMarkService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by Felix on 2016/9/14.
 */
@Service
public class LandMarkServiceImpl implements LandMarkService {

    @Autowired
    private LandMarkDao landMarkDao;
    @Autowired
    private UserService userService;

    @Override
    public Page<Landmark> findByCondition(final Landmark entity, Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        return landMarkDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();

                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("district").get("country").get("id").in(countryIds));
                }

                if (entity.getDisplay() != null)
                    predicate.add(cb.equal(root.get("display"), entity.getDisplay()));

                if (StringUtils.isNotBlank(entity.getName()))
                    predicate.add(cb.like(root.get("name"), "%" + entity.getName() + "%"));

                District district = entity.getDistrict();
                if (district != null && district.getId() != null)
                    predicate.add(cb.equal(root.get("district").get("id"), district.getId()));

                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public Landmark saveNotNull(Landmark entity) {
        if (entity.getId() == null) {
            return landMarkDao.save(entity);
        }
        Landmark oldEntity = landMarkDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return landMarkDao.save(oldEntity);
    }
}