package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.RankDao;
import com.nuanyou.cms.entity.Rank;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.RankService;
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
 * Created by Felix on 2016/9/30.
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RankDao rankDao;
    @Autowired
    private UserService userService;

    @Override
    public Page<Rank> findByCondition(Integer index, final Rank entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        final List<Long> countryIds = userService.findUserCountryId();
        return rankDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("city").get("country").get("id").in(countryIds));
                }
                if (entity.getCountry() != null && entity.getCountry().getId() != null)
                    predicate.add(cb.equal(root.get("city").get("country").get("id"), entity.getCountry().getId()));

                if (entity.getCity() != null && entity.getCity().getId() != null)
                    predicate.add(cb.equal(root.get("city").get("id"), entity.getCity().getId()));
    
                if (entity.getObjtype() != null)
                    predicate.add(cb.equal(root.get("objtype"), entity.getObjtype()));
                
                if (entity.getDisplay()!=null)
                    predicate.add(cb.equal(root.get("display"),entity.getDisplay()));
                
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public Rank saveNotNull(Rank entity) {
        if (entity.getId() == null) {
            return rankDao.save(entity);
        }
        Rank oldEntity = rankDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return rankDao.save(oldEntity);
    }
}
