package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.FeatureDao;
import com.nuanyou.cms.entity.Feature;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.FeatureService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureDao featureDao;

    @Override
    public Page<Feature> findByCondition(Integer index, final Feature entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        return featureDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (entity.getCountry() != null && entity.getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("country").get("id"), entity.getCountry().getId());
                    predicate.add(p);
                }
                if (entity.getCity() != null && entity.getCity().getId() != null) {
                    Predicate p = cb.equal(root.get("city").get("id"), entity.getCity().getId());
                    predicate.add(p);
                }

                if (entity.getCat() != null) {
                    Predicate p = cb.equal(root.get("cat"), entity.getCat());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public Feature saveNotNull(Feature entity) {
        if (entity.getId() == null) {
            return featureDao.save(entity);
        }
        Feature oldEntity = featureDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        if (oldEntity.getCity().getId() == null) {
            oldEntity.setCity(null);
        }
        if (oldEntity.getCountry().getId() == null) {
            oldEntity.setCountry(null);
        }
        return featureDao.save(oldEntity);
    }


}
