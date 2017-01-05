package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.LandMarkDao;
import com.nuanyou.cms.entity.Landmark;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.LandMarkService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<Landmark> findByCondition(final Landmark entity, Pageable pageable) {
        return landMarkDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(entity.getName())) {
                    Predicate p = cb.like(root.get("name"), "%" + entity.getName() + "%");
                    predicate.add(p);
                }
                if (entity.getDistrict() != null && entity.getDistrict().getId() != null) {
                    Predicate p = cb.equal(root.get("district").get("id"), entity.getDistrict().getId());
                    predicate.add(p);
                }
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
