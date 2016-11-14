package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.FakeOrderDao;
import com.nuanyou.cms.entity.FakeOrder;
import com.nuanyou.cms.service.FakeOrderService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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
 * Created by Felix on 2016/9/8.
 */
@Service
public class FakeOrderServiceImpl implements FakeOrderService
{
    @Autowired
    private FakeOrderDao fakeOrderDao;



    @Override
    public FakeOrder saveNotNull(FakeOrder entity) {
        if (entity.getId() == null) {
            return fakeOrderDao.save(entity);
        }
        FakeOrder oldEntity = fakeOrderDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return fakeOrderDao.save(oldEntity);
    }

    @Override
    public Page<FakeOrder> findByCondition(final FakeOrder fakeOrder, Pageable pageable) {
        return fakeOrderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (fakeOrder.getFakeUser()!=null&&StringUtils.isNotBlank(fakeOrder.getFakeUser().getNickname())){
                    Predicate p = cb.like(root.get("fakeUser").get("nickname"),"%"+fakeOrder.getFakeUser().getNickname()+"%");
                    cb.or(p,p);
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }
}
