package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.RateDao;
import com.nuanyou.cms.entity.Rate;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.RateService;
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
 * Created by Felix on 2016/9/27.
 */
@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private RateDao rateDao;

    @Autowired
    private UserService userService;

    @Override
    public Page<Rate> findByCondition(Integer index, final Rate entity) {


        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        final List<Long> countryIds = userService.findUserCountryId();
        return rateDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                Predicate[] pre = new Predicate[predicate.size()];
                Predicate krw = cb.equal(root.get("code"), "KRW");
                Predicate jpy = cb.equal(root.get("code"), "JPY");
                Predicate eur = cb.equal(root.get("code"), "EUR");
                Predicate thb = cb.equal(root.get("code"), "THB");
                Predicate or = cb.or(krw, jpy, eur, thb);
                predicate.add(or);
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("country").get("id").in(countryIds));
                }
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public Rate saveNotNull(Rate entity) {
        if (entity.getId() == null) {
            return rateDao.save(entity);
        }
        Rate oldEntity = rateDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return rateDao.save(oldEntity);
    }
}
