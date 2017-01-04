package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.UserCardItem;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.enums.CardStatusEnum;
import com.nuanyou.cms.entity.enums.CouponStatusEnum;
import com.nuanyou.cms.entity.enums.NewOrderStatus;
import com.nuanyou.cms.entity.enums.RefundStatus;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.entity.order.OrderRefundLog;
import com.nuanyou.cms.entity.order.OrderVouchCard;
import com.nuanyou.cms.entity.order.ViewOrderExport;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.OrderRefundLogService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.DateUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nuanyou.cms.entity.order.Order.getRefundQualified;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ViewOrderExportDao viewOrderExportDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderRefundLogService orderRefundLogService;

    @Autowired
    private OrderVouchCardDao orderVouchCardDao;
    @Autowired
    private UserCardItemDao userCardItemDao;
    @Autowired
    private CouponDao couponDao;

    @Override
    public Page<Order> findByCondition(Integer index, final Order entity, final TimeCondition time, Pageable pageable) {
        return orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();

                if (entity.getId() != null) {
                    Predicate p = cb.equal(root.get("id"),entity.getId());
                    predicate.add(p);
                }
                if (time.getBegin() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), time.getBegin());
                    predicate.add(p);
                }
                if (time.getEnd() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("createtime").as(Date.class), time.getEnd());
                    predicate.add(p);
                }
                if(entity.getMerchant()!=null&&StringUtils.isNotBlank(entity.getMerchant().getKpname())){
                    Predicate p = cb.equal(root.get("merchant").get("kpname"), entity.getMerchant().getKpname());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getSceneid())) {
                    Predicate p = cb.equal(root.get("sceneid"), entity.getSceneid());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getOrdersn())) {
                    Predicate p = cb.like(root.get("ordersn"), "%"+entity.getOrdersn()+"%");
                    predicate.add(p);
                }
                if (entity.getOrdertype() != null) {
                    Predicate p = cb.equal(root.get("ordertype"), entity.getOrdertype());
                    cb.or(p,p);
                    predicate.add(p);
                }
                if (entity.getPaytype() != null) {
                    Predicate p = cb.equal(root.get("paytype"), entity.getPaytype());
                    predicate.add(p);
                }
                if (entity.getOrderstatus() != null) {
                    Predicate p = cb.equal(root.get("orderstatus"), entity.getOrderstatus());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("id"), entity.getMerchant().getId());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && entity.getMerchant().getDistrict() != null && entity.getMerchant().getDistrict().getCountry() != null
                        && entity.getMerchant().getDistrict().getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("district").get("country").get("id"), entity.getMerchant().getDistrict().getCountry().getId());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }


    @Override
    public Page<Order> findRefundByCondition(int index, final Order entity, final TimeCondition time) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "refundtime");
        return orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (entity.getRefundstatus() != null) {
                    //entity.setRefundstatus(RefundStatus.RefundInProgress);
                    Predicate pStatus = cb.equal(root.get("refundstatus"), entity.getRefundstatus());
                    predicate.add(pStatus);
                }
                if (time.getBegin() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), time.getBegin());
                    predicate.add(p);
                }
                if (time.getEnd() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("createtime").as(Date.class), time.getEnd());
                    predicate.add(p);
                }
                if(entity.getId()!=null){
                    Predicate p1 = cb.equal(root.get("id"), entity.getId());
                    predicate.add(p1);
                }
                if (!StringUtils.isEmpty(entity.getOrdersn())) {
                    Predicate p = cb.like(root.get("ordersn"), "%"+entity.getOrdersn()+"%");
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }



    @Override
    public Page<ViewOrderExport> findExportByCondition(Integer index, final ViewOrderExport entity, final TimeCondition time, Pageable pageable) {
        return viewOrderExportDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (time.getBegin() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), time.getBegin());
                    predicate.add(p);
                }
                if (time.getEnd() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("createtime").as(Date.class), time.getEnd());
                    predicate.add(p);
                }
                if(entity.getMerchant()!=null&&!StringUtils.isEmpty(entity.getMerchant().getKpname())){
                    Predicate p = cb.equal(root.get("merchant").get("kpname"), entity.getMerchant().getKpname());
                    predicate.add(p);
                }
                if (entity.getMerchant()!=null&&entity.getMerchant().getId()!=null) {
                    Predicate p = cb.equal(root.get("merchant").get("id"), entity.getId());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getSceneid())) {
                    Predicate p = cb.equal(root.get("sceneid"), entity.getSceneid());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getOrdersn())) {
                    Predicate p = cb.equal(root.get("ordersn"), entity.getOrdersn());
                    predicate.add(p);
                }
                if (entity.getOrdertype() != null) {
                    Predicate p = cb.equal(root.get("ordertype"), entity.getOrdertype());
                    cb.or(p,p);
                    predicate.add(p);
                }
                if (entity.getPaytype() != null) {
                    Predicate p = cb.equal(root.get("paytype"), entity.getPaytype());
                    predicate.add(p);
                }
                if (entity.getOrderstatus() != null) {
                    Predicate p = cb.equal(root.get("orderstatus"), entity.getOrderstatus());
                    predicate.add(p);
                }

                if (entity.getMerchant() != null && entity.getMerchant().getDistrict() != null && entity.getMerchant().getDistrict().getCountry() != null
                        && entity.getMerchant().getDistrict().getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("district").get("country").get("id"), entity.getMerchant().getDistrict().getCountry().getId());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }
    @Override
    public Integer getBuyNum(Order order) {
        if (order.getUser() == null) {
            return 0;
        }
        return this.orderDao.getBuyNum(order.getUser().getUserid().intValue());
    }


    @Transactional
    @Override
    public void refund(Order entity) {
        Order order = this.orderDao.findOne(entity.getId());
        if(order==null){
            throw new APIException(ResultCodes.OrderNotFound, " Detail：OrderID" + entity.getId());
        }
        if(!Order.getRefundQualified(order)){
            throw new APIException(ResultCodes.OrderOther,"只有 【退款失败、已消费、已评价、自动核销、商户核销】 的订单支持发起退款申请");
        }

        if (order.getRefundstatus() != null) {
            if (order.getRefundstatus() == RefundStatus.RefundInProgress) {
                throw new APIException(ResultCodes.Refunding, " Detail：OrderID" + order.getId());
            } else if (order.getRefundstatus() == RefundStatus.Failure) {
                throw new APIException(ResultCodes.RefundingFail, " Detail：OrderID" + order.getId());
            } else if (order.getRefundstatus() == RefundStatus.Success) {
                throw new APIException(ResultCodes.RefundingSuccess, " Detail：OrderID" + order.getId());
            }
        }
        order.setRefundremark(entity.getRefundremark());
        order.setId(entity.getId());
        order.setStatusname("已申请退款");
        order.setRefundreason("匿名操作");
        order.setRefundstatus(RefundStatus.RefundInProgress);//退款中
        order.setRefundtime(DateUtils.newDate());
        order.setRefundsource((byte) 2);//// 退款来源：1.客户端，2.cms，3.商户端
        this.orderDao.save(order);
    }

    @Override
    public Order saveNotNull(Order entity) {
        if (entity.getId() == null) {
            return orderDao.save(entity);
        }
        Order oldEntity = orderDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return orderDao.save(oldEntity);
    }



    @Override
    @Transactional
    public void validate(Long id, Integer type, String cmsusername) {
        Order order = orderDao.findOne(id);
        if (order == null) {
            return;
        }
        if (order.getRefundstatus() != RefundStatus.RefundInProgress) {
            throw new APIException(ResultCodes.Audited);
        } else {
            order.setRefundaudittime(DateUtils.newDate());
            order.setRefundstatus(RefundStatus.toEnum(type));
        }
        if (order.getRefundstatus() == RefundStatus.Failure) {
            order.setStatusname("退款失败");
        } else if (order.getRefundstatus() == RefundStatus.Success) {
            order.setStatusname("退款成功");
        }
        this.saveNotNull(order);
        OrderRefundLog log = new OrderRefundLog();
        log.setOrder(order);
        log.setCmsusername(cmsusername);
        if (order.getRefundstatus() == RefundStatus.Failure) {
            log.setStatus(2);
        } else if (order.getRefundstatus() == RefundStatus.Success) {
            log.setStatus(1);
        }
        this.orderRefundLogService.saveNotNull(log);

        backCardAndCoupon(order, DateUtils.newDate());


    }

    private void backCardAndCoupon(Order order, Date now) {
        List<OrderVouchCard> entityOrderVouchCardList = this.orderVouchCardDao.findByOrderId(order.getId());
        if (entityOrderVouchCardList != null && entityOrderVouchCardList.size() > 0) {
            for (OrderVouchCard entityOrderVouchCard : entityOrderVouchCardList) {
                UserCardItem entityUserCardItem = this.userCardItemDao.findOne(entityOrderVouchCard.getCardId());
                if (entityUserCardItem.getValidtime() != null && now.compareTo(entityUserCardItem.getValidtime()) > 0) {
                    entityUserCardItem.setStatus(CardStatusEnum.EXPIRE.getValue());
                } else {
                    entityUserCardItem.setStatus(CardStatusEnum.UNUSE.getValue());
                }

                this.userCardItemDao.save(entityUserCardItem);
            }
        }

        if (order.getDiscoutcardid() != null) {
            UserCardItem entityUserCardItem = this.userCardItemDao.findOne(order.getDiscoutcardid());
            if (entityUserCardItem.getValidtime() != null && now.compareTo(entityUserCardItem.getValidtime()) > 0) {
                entityUserCardItem.setStatus(CardStatusEnum.EXPIRE.getValue());
            } else {
                entityUserCardItem.setStatus(CardStatusEnum.UNUSE.getValue());
            }

            this.userCardItemDao.save(entityUserCardItem);
        }


        if (order.getCoupon() != null && order.getCoupon().getId() != null) {
            Coupon entityCoupon = this.couponDao.findOne(order.getCoupon().getId());
            if (entityCoupon.getValidTime() != null && now.compareTo(entityCoupon.getValidTime()) > 0) {
                entityCoupon.setStatus(CouponStatusEnum.EXPIRE.getValue());
            } else {
                entityCoupon.setStatus(CouponStatusEnum.UNUSE.getValue());
            }
            this.couponDao.save(entityCoupon);
        }

    }


}
