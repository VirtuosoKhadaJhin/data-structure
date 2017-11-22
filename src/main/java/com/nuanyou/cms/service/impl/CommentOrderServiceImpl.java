package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CommentOrderDao;
import com.nuanyou.cms.dao.CommentReplyDao;
import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.entity.CommentReply;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.service.CommentOrderService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private CommentReplyDao commentReplyDao;
    @Autowired
    private UserService userService;

    @Override
    public Page<CommentOrder> findByCondition(final CommentOrder entity, final TimeCondition time, final String scoreStr, Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        return commentOrderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                predicate.add(cb.lessThanOrEqualTo(root.get("deleted"), Boolean.FALSE));
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("order").get("countryid").in(countryIds));
                }
                Order order = entity.getOrder();
                if (order != null) {
                    String ordersn = order.getOrdersn();
                    if (StringUtils.isNotBlank(ordersn))
                        predicate.add(cb.like(root.get("order").get("ordersn").as(String.class), "%" + ordersn + "%"));
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
                if (entity.getDisplay() != null) {
                    predicate.add(cb.equal(root.get("display").as(Boolean.class), entity.getDisplay()));
                }
                if ("high".equals(scoreStr)) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("score").as(Double.class), 4));
                } else if ("low".equals(scoreStr)) {
                    predicate.add(cb.lessThan(root.get("score").as(Double.class), 4));
                }
                return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
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

    @Override
    public void delete(Long id) {
        CommentOrder entity = commentOrderDao.findOne(id);
        if (entity != null) {
            entity.setDeleted(true);
            entity.setDisplay(false);
            commentOrderDao.save(entity);
        }
    }

    @Override
    public void reply(CommentReply entity) {
        Long commentId = entity.getCommentId();
        if (commentId == null)
            throw new APIException(ResultCodes.MissingParameter, "评论ID不能为空");

        CommentOrder commentOrder = commentOrderDao.findOne(commentId);
        if (commentOrder == null)
            throw new APIException(ResultCodes.Fail, "找不到该评论ID＝" + commentId);

        commentReplyDao.save(entity);

        commentOrder.setReplyTime(new Date());
        commentOrderDao.save(commentOrder);
    }

    @Override
    public List<CommentReply> findReply(Long id) {
        List<CommentReply> list = commentReplyDao.findAll(Example.of(new CommentReply(id)), new Sort(Sort.Direction.DESC, "createTime"));
        return list;
    }

    @Override
    public void showOrHideComment(Long id, Boolean isShow) {
        commentOrderDao.showOrHideComment(id, isShow);
    }


    @Override
    public List<CommentOrder> findByCondition(final CommentOrder entity, final TimeCondition time,final String scoreStr) {
        final List<Long> countryIds = userService.findUserCountryId();
        return commentOrderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                predicate.add(cb.lessThanOrEqualTo(root.get("deleted"), Boolean.FALSE));
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("order").get("countryid").in(countryIds));
                }
                Order order = entity.getOrder();
                if (order != null) {
                    String ordersn = order.getOrdersn();
                    if (StringUtils.isNotBlank(ordersn))
                        predicate.add(cb.like(root.get("order").get("ordersn").as(String.class), "%" + ordersn + "%"));
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
                if (entity.getDisplay() != null) {
                    predicate.add(cb.equal(root.get("display").as(Boolean.class), entity.getDisplay()));
                }
                if ("high".equals(scoreStr)) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("score").as(Double.class), 4));
                } else if ("low".equals(scoreStr)) {
                    predicate.add(cb.lessThan(root.get("score").as(Double.class), 4));
                }
                return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
            }
        });
    }

}