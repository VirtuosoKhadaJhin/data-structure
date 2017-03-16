package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CommentOrderDao;
import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CommentOrderService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return commentOrderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Order order = entity.getOrder();
                if (order != null) {
                    String ordersn = order.getOrdersn();
                    if (StringUtils.isNotBlank(ordersn))
                        predicate.add(cb.equal(root.get("order").get("ordersn").as(String.class), ordersn));
                }
                Merchant merchant = entity.getMerchant();
                if (merchant != null) {
                    Long id = merchant.getId();
                    if (id != null)
                        predicate.add(cb.equal(root.get("merchant").get("id").as(Long.class), id));
                    String name = merchant.getName();
                    if (name != null)
                        predicate.add(cb.like(root.get("merchant").get("name").as(String.class), "%" + name + "%"));
                }
                if (time != null) {
                    Date date = time.getBegin();
                    if (date != null)
                        predicate.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), date));
                    date = time.getEnd();
                    if (date != null)
                        predicate.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), date));
                }
                if ("high".equals(scoreStr))
                    predicate.add(cb.greaterThanOrEqualTo(root.get("score").as(Double.class), 4));
                else if ("low".equals(scoreStr))
                    predicate.add(cb.lessThan(root.get("score").as(Double.class), 4));

                return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
            }
        }, new PageRequest(index - 1, PageUtil.pageSize));
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
