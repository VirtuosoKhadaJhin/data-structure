package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CulturalRecommendationsGroupDao;
import com.nuanyou.cms.entity.CulturalRecommendationsGroup;
import com.nuanyou.cms.entity.Rank;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CulturalRecommendationsGroupService;
import com.nuanyou.cms.service.UserService;
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
 * Created by Alan.ye on 2016/9/8.
 */
@Service
public class CulturalRecommendationsGroupServiceImpl implements CulturalRecommendationsGroupService {

    @Autowired
    private CulturalRecommendationsGroupDao dao;

    @Autowired
    private UserService userService;

    @Override
    public CulturalRecommendationsGroup saveNotNull(CulturalRecommendationsGroup entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        CulturalRecommendationsGroup oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

    public void delete(Long id) {
        CulturalRecommendationsGroup entity = dao.findOne(id);
        if (entity != null) {
            entity.setDisplay(false);
            entity.setDeleted(true);
            dao.save(entity);
        }
    }

    @Override
    public List<CulturalRecommendationsGroup> getIdNameList() {
        return dao.getIdNameList();
    }

    @Override
    public Page<CulturalRecommendationsGroup> findByCondition(final CulturalRecommendationsGroup entity,Pageable pageable) {

        final List<Long> countryIds = userService.findUserCountryId();
        return dao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("countryId").in(countryIds));
                }
                if (entity.getCountryId() != null)
                    predicate.add(cb.equal(root.get("countryId"), entity.getCountryId()));

                if (entity.getCity() != null && entity.getCity().getId() != null)
                    predicate.add(cb.equal(root.get("city").get("id"), entity.getCity().getId()));

                if (entity.getTitle() != null)
                    predicate.add(cb.like(root.get("title"), "%" + entity.getTitle() + "%"));

                if (entity.getDisplay()!=null)
                    predicate.add(cb.equal(root.get("display"),entity.getDisplay()));

                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }
}