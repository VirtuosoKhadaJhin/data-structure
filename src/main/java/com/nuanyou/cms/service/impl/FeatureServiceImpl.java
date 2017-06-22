package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.FeatureDao;
import com.nuanyou.cms.entity.Feature;
import com.nuanyou.cms.entity.enums.FeatureCat;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.FeatureService;
import com.nuanyou.cms.util.BeanUtils;
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
 * Created by Felix on 2016/9/7.
 */
@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureDao dao;

    @Override
    public Page<Feature> findByCondition(Integer index, final Feature entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, new Sort(Sort.Direction.ASC, "sort", "id"));
        return dao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();

                predicate.add(cb.equal(root.get("deleted"), false));

                if (entity.getCountry() != null && entity.getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("country").get("id"), entity.getCountry().getId());
                    predicate.add(p);
                }
                if (entity.getCity() != null && entity.getCity().getId() != null) {
                    Predicate p = cb.equal(root.get("city").get("id"), entity.getCity().getId());
                    predicate.add(p);
                }

                if (entity.getType() != null) {
                    predicate.add(cb.equal(root.get("type"), entity.getType()));
                }

                if (entity.getCat() != null && entity.getCat() != FeatureCat.All) {
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
            return dao.save(entity);
        }
        Feature oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        if (oldEntity.getCity().getId() == null) {
            oldEntity.setCity(null);
        }
        if (oldEntity.getCountry().getId() == null) {
            oldEntity.setCountry(null);
        }
        return dao.save(oldEntity);
    }

    @Override
    public void delete(Long id) {
        Feature entity = dao.findOne(id);
        if (entity != null) {
            entity.setDisplay(false);
            entity.setDeleted(true);
            dao.save(entity);
        }
    }

}
