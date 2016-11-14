package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CommentOrderDao;
import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CommentOrderService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class CommentOrderServiceImpl implements CommentOrderService {

    @Autowired
    private CommentOrderDao commentOrderDao;

    @Override
    public Page<CommentOrder> findByCondition(Integer index, final CommentOrder entity, final TimeCondition time, final String scoreStr) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        return commentOrderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Order order = entity.getOrder();
                if (order != null) {
                    String ordersn = order.getOrdersn();
                    if (StringUtils.isNotBlank(ordersn)) {
                        Predicate p = cb.equal(root.get("order").get("ordersn").as(String.class), ordersn);
                        predicate.add(p);
                    }
                }
                if (time != null) {
                    if (time.getBegin() != null) {
                        Predicate p = cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), time.getBegin());
                        predicate.add(p);
                    }
                    if (time.getEnd() != null) {
                        Predicate p = cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), time.getEnd());
                        predicate.add(p);
                    }
                }
                if ("high".equals(scoreStr)) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("score").as(Double.class), 4);
                    predicate.add(p);
                } else if ("low".equals(scoreStr)) {
                    Predicate p = cb.lessThan(root.get("score").as(Double.class), 4);
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public CommentOrder saveNotNull(CommentOrder entity) {
        if (entity.getId() == null) {
            return commentOrderDao.save(entity);
        }
        CommentOrder oldEntity = commentOrderDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return commentOrderDao.save(oldEntity);
    }
}
