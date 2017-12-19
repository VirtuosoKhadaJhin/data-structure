package com.nuanyou.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.domain.NotificationPublisher;
import com.nuanyou.cms.entity.UserCardItem;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.enums.*;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.entity.order.OrderItem;
import com.nuanyou.cms.entity.order.OrderVouchCard;
import com.nuanyou.cms.entity.order.ViewOrderExport;
import com.nuanyou.cms.model.OrderModel;
import com.nuanyou.cms.model.PageModel;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.OrderRefundLogService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.DateUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    /*  @Autowired
        private OrderRefundLogService orderRefundLogService;
        @Autowired
        private ItemTuanDao itemTuanDao;*/
    @Autowired
    private OrderVouchCardDao orderVouchCardDao;
    @Autowired
    private UserCardItemDao userCardItemDao;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private NotificationPublisher notificationPublisher;
    @Autowired
    private ViewOrderExportDao viewOrderExportDao;
    /*  @Autowired
        private MerchantDao merchantDao;*/
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private UserService userService;

    @Value("${orderService}")
    private String orderService;

    private static String timePattern = "yyyy-MM-dd HH:mm:ss";
    private static String decimalPattern = "#0.00";
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Page<Order> findByCondition(Integer index, final Order entity, final TimeCondition time, Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        return orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("countryid").in(countryIds));
                }
                if (entity.getId() != null) {
                    Predicate p = cb.equal(root.get("id"), entity.getId());
                    predicate.add(p);
                }
                if (time.getBegin_minute() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), time.getBegin_minute());
                    predicate.add(p);
                }
                if (time.getEnd_minute() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("createtime").as(Date.class), time.getEnd_minute());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && StringUtils.isNotBlank(entity.getMerchant().getKpname())) {
                    Predicate p = cb.equal(root.get("merchant").get("kpname"), entity.getMerchant().getKpname());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getSceneid())) {
                    Predicate p = cb.equal(root.get("sceneid"), entity.getSceneid());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getOrdersn())) {
                    Predicate p = cb.like(root.get("ordersn"), "%" + entity.getOrdersn() + "%");
                    predicate.add(p);
                }
                if (entity.getOrdertype() != null) {
                    Predicate p = cb.equal(root.get("ordertype"), entity.getOrdertype());
                    cb.or(p, p);
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
                if (entity.getCountryid() != null) {
                    Predicate p = cb.equal(root.get("countryid"), entity.getCountryid());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public Page<Order> findRefundByCondition(Integer index, final Order entity, final TimeCondition time, final List<Long> countryids) {
        Pageable pageable = null;
        if (index != null) pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "refundtime");
        final List<Long> countryIds = userService.findUserCountryId();
        return orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryids != null && countryids.size() > 0) {
                    predicate.add(root.get("countryid").in(countryids));
                } else {
                    if (countryIds != null && countryIds.size() > 0) {
                        predicate.add(root.get("countryid").in(countryIds));
                    }
                }
                if (entity.getRefundstatus() != null) {
                    Predicate pStatus = cb.equal(root.get("refundstatus"), entity.getRefundstatus());
                    predicate.add(pStatus);
                } else {
                    cb.isNotNull(root.get("refundstatus"));
                }
                if (time.getBegin() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), time.getBegin());
                    predicate.add(p);
                }
                if (time.getEnd() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("createtime").as(Date.class), time.getEnd());
                    predicate.add(p);
                }
                if (time.getBegin_2() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("refundtime").as(Date.class), time.getBegin_2());
                    predicate.add(p);
                }
                if (time.getEnd_2() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("refundtime").as(Date.class), time.getEnd_2());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && !StringUtils.isEmpty(entity.getMerchant().getName())) {
                    Predicate pStatus = cb.like(root.get("merchant").get("name").as(String.class), "%" + entity.getMerchant().getName() + "%");
                    predicate.add(pStatus);
                }
                if (entity.getCountryid() != null) {
                    Predicate p1 = cb.equal(root.get("countryid"), entity.getCountryid());
                    predicate.add(p1);
                }
                if (entity.getId() != null) {
                    Predicate p1 = cb.equal(root.get("id"), entity.getId());
                    predicate.add(p1);
                }
                if (!StringUtils.isEmpty(entity.getOrdersn())) {
                    Predicate p = cb.like(root.get("ordersn"), "%" + entity.getOrdersn() + "%");
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public List<Order> findRefundByCondition(final Order entity, final TimeCondition time, final List<Long> countryids) {
        return findRefundByCondition(null, entity, time, countryids).getContent();
    }

    @Override
    public List<ViewOrderExport> findExportByCondition(final Order entity, final TimeCondition time, List<Long> countryids, Pageable pageable) {
        Long begin_exportOrder = System.currentTimeMillis();
        List<ViewOrderExport> orderList = getViewOrderExports(entity, time, countryids);
        Long end_exportOrder = System.currentTimeMillis();
        log.info("count(order):" + orderList.size());
        log.info("read data from ViewOrderExport:" + (end_exportOrder - begin_exportOrder) / 1000 + "s");

        Long begin_items = System.currentTimeMillis();
        List<OrderItem> itemList = getOrderItems(entity, time);
        Long end_items = System.currentTimeMillis();
        log.info("count(item):" + itemList.size());
        log.info("read data from OrderItem:" + (end_items - begin_items) / 1000 + "s");

        Long begin_relation = System.currentTimeMillis();
        relationItem(itemList, orderList);
        Long end_relation = System.currentTimeMillis();
        log.info("read data from relationItem:" + (end_relation - begin_relation) / 1000 + "s");
        return orderList;

    }

    private void relationItem(List<OrderItem> itemList, List<ViewOrderExport> orderList) {
        int i = 0;
        for (ViewOrderExport order : orderList) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (; i < itemList.size(); i++) {
                if (order.getId().equals(itemList.get(i).getOrder().getId())) {
                    orderItems.add(itemList.get(i));
                    continue;
                } else {
                    break;
                }
            }
            order.setOrderItems(orderItems);
        }
    }

    private void relationBuyTimes(List<ViewOrderExport> orderList) {

        Map<Long, Integer> map = new HashMap<>();
        for (ViewOrderExport viewOrderExport : orderList) {
            Long userId = viewOrderExport.getUserId();
            if (userId == null) {
                continue;
            }
            Integer buyTimes = 0;
            if (!map.containsKey(userId)) {
                buyTimes = this.getBuyNum(viewOrderExport.getUserId());
            } else {
                buyTimes = map.get(userId);
            }
            viewOrderExport.setBuyTimes(buyTimes);
        }
    }

    @Override
    public long countViewOrderExports(final Order entity, final TimeCondition time, final List<Long> countryids) {
        return viewOrderExportDao.count(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = getOrderExportPredicates(root, cb, entity, time, countryids);
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        });
    }

    private List<ViewOrderExport> getViewOrderExports(final Order entity, final TimeCondition time, final List<Long> countryids) {
        return viewOrderExportDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = getOrderExportPredicates(root, cb, entity, time, countryids);
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, new Sort(Sort.Direction.ASC, "id"));
    }

    private List<Predicate> getOrderExportPredicates(Root root, CriteriaBuilder cb, Order entity, TimeCondition time, List<Long> countryids) {
        List<Predicate> predicate = new ArrayList<Predicate>();
        if (countryids != null && countryids.size() > 0) {
            predicate.add(root.get("countryid").in(countryids));
        }
        if (entity.getId() != null) {
            Predicate p = cb.equal(root.get("id"), entity.getId());
            predicate.add(p);
        }
        if (time.getBegin_minute() != null) {
            Predicate p = cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), time.getBegin_minute());
            predicate.add(p);
        }
        if (time.getEnd_minute() != null) {
            Predicate p = cb.lessThanOrEqualTo(root.get("createtime").as(Date.class), time.getEnd_minute());
            predicate.add(p);
        }
        if (entity.getMerchant() != null && !StringUtils.isEmpty(entity.getMerchant().getKpname())) {
            Predicate p = cb.equal(root.get("merchantkpname"), entity.getMerchant().getKpname());
            predicate.add(p);
        }
        if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
            Predicate p = cb.equal(root.get("mchid"), entity.getMerchant().getId());
            predicate.add(p);
        }
        if (!StringUtils.isEmpty(entity.getSceneid())) {
            Predicate p = cb.equal(root.get("sceneid"), entity.getSceneid());
            predicate.add(p);
        }
        if (!StringUtils.isEmpty(entity.getOrdersn())) {
            Predicate p = cb.like(root.get("ordersn"), "%" + entity.getOrdersn() + "%");
            predicate.add(p);
        }
        if (entity.getOrdertype() != null) {
            Predicate p = cb.equal(root.get("ordertype"), entity.getOrdertype());
            cb.or(p, p);
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
        if (entity.getCountryid() != null) {
            Predicate p1 = cb.equal(root.get("countryid"), entity.getCountryid());
            predicate.add(p1);
        }
        return predicate;
    }

    private List<OrderItem> getOrderItems(final Order entity, final TimeCondition time) {
        final List<Long> countryIds = userService.findUserCountryId();
        return orderItemDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("order").get("merchant").get("district").get("country").get("id").in(countryIds));
                }
                if (time.getBegin() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("order").get("createtime").as(Date.class), time.getBegin());
                    predicate.add(p);
                }
                if (time.getEnd() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("order").get("createtime").as(Date.class), time.getEnd());
                    predicate.add(p);
                }

                if (entity.getMerchant() != null && !StringUtils.isEmpty(entity.getMerchant().getKpname())) {
                    Predicate p = cb.equal(root.get("order").get("merchant").get("kpname"), entity.getMerchant().getKpname());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
                    Predicate p = cb.equal(root.get("order").get("merchant").get("id"), entity.getMerchant().getId());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getSceneid())) {
                    Predicate p = cb.equal(root.get("order").get("sceneid"), entity.getSceneid());
                    predicate.add(p);
                }
                if (!StringUtils.isEmpty(entity.getOrdersn())) {
                    Predicate p = cb.equal(root.get("order").get("ordersn"), entity.getOrdersn());
                    predicate.add(p);
                }
                if (entity.getOrdertype() != null) {
                    Predicate p = cb.equal(root.get("order").get("ordertype"), entity.getOrdertype());
                    cb.or(p, p);
                    predicate.add(p);
                }
                if (entity.getPaytype() != null) {
                    Predicate p = cb.equal(root.get("order").get("paytype"), entity.getPaytype());
                    predicate.add(p);
                }
                if (entity.getOrderstatus() != null) {
                    Predicate p = cb.equal(root.get("order").get("orderstatus"), entity.getOrderstatus());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && entity.getMerchant().getDistrict() != null && entity.getMerchant().getDistrict().getCountry() != null && entity.getMerchant().getDistrict().getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("order").get("merchant").get("district").get("country").get("id"), entity.getMerchant().getDistrict().getCountry().getId());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, new Sort(Sort.Direction.ASC, "order.id"));
    }

    @Override
    public Integer getBuyNum(Long userId) {
        if (userId == null) {
            return 0;
        }
        return this.orderDao.getBuyNum(userId.intValue());
    }

    /**
     * 退款申请
     *
     * @param entity
     * @throws URISyntaxException
     * @throws IOException
     */
    @Transactional
    @Override
    public void refund(Order entity) throws URISyntaxException, IOException {
        if (entity != null) {
            Order order = this.orderDao.findOne(entity.getId());
            if (order == null) {
                throw new APIException(ResultCodes.OrderNotFound, " Detail：OrderID" + entity.getId());
            }
            if (!order.getRefundQualified(order)) {
                throw new APIException(ResultCodes.OrderOther, "只有 【退款失败、已消费、已评价、自动核销、商户核销】 的订单支持发起退款申请");
            }

            if (order.getRefundstatus() != null) {
                if (order.getRefundstatus() == RefundStatus.RefundInProgress) {
                    throw new APIException(ResultCodes.Refunding, " Detail：OrderID" + order.getId());
                } else if (order.getRefundstatus() == RefundStatus.Success) {
                    throw new APIException(ResultCodes.RefundingSuccess, " Detail：OrderID" + order.getId());
                }
            }
            //虚拟订单
            if (order.getPaytype().getValue() == OrderPayType.VIRTUAL.getValue()) {
                order.setRefundremark(entity.getRefundremark());
                order.setId(entity.getId());
                order.setStatusname("已申请退款");
                order.setRefundreason("匿名操作");
                order.setRefundstatus(RefundStatus.RefundInProgress);//退款中
                order.setOldrefundstatus(0);
                order.setRefundtime(DateUtils.newDate());
                order.setRefundsource(RefundSource.CMS);//// 退款来源：1.客户端，2.cms，3.商户端
                this.orderDao.save(order);
                this.notificationPublisher.publishRefund(order.getId().toString());
            } else {//非虚拟订单
                HttpPost method = new HttpPost(orderService + "/orders/" + order.getId() + "/refunds");
                // 接收参数json列表
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("refundRemark", entity.getRefundremark());
                jsonParam.put("refundReason", "匿名操作");
                jsonParam.put("applicantId", UserHolder.getUser().getUserid().toString());
                jsonParam.put("refundSource", RefundSource.CMS.getValue());//
                StringEntity entitys = new StringEntity(jsonParam.toString(), "utf-8");
                entitys.setContentEncoding("UTF-8");
                entitys.setContentType("application/json");
                method.setEntity(entitys);
                CloseableHttpResponse response = HttpClientBuilder.create().build().execute(method);
                String responseText = EntityUtils.toString(response.getEntity());
                System.out.println(responseText);
                APIResult apiResult = JSON.parseObject(responseText, APIResult.class);
                if (apiResult.getCode() == null || apiResult.getCode() != 0) {
                    throw new APIException(ResultCodes.UnkownError, apiResult.getMsg());
                }
            }
        }
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

//2017/12/06 退款改造 重构
/*  @Override
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
            this.notificationPublisher.publishRefundFail(order.getId().toString());
        } else if (order.getRefundstatus() == RefundStatus.Success) {
            order.setOrderstatus(NewOrderStatus.RefundSuccess);
            order.setStatusname("退款成功");
            this.notificationPublisher.publishRefundSuccess(order.getId().toString());
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


    }*/

    @Override
    public Boolean checkUnRedeem(Long itemId) {
        List<OrderItem> orderItems = orderItemDao.findByItemid(itemId.intValue());
        List<Long> orderIds = Lists.newArrayList();
        for (OrderItem orderItem : orderItems) {
            orderIds.add(orderItem.getOrder().getId());
        }
        Integer unRedeemedNum = orderDao.findUnRedeemedAndIdIn(orderIds);

        return unRedeemedNum > 0;
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

    /**
     * 退款审核操作通过1拒绝2
     *
     * @param operationType
     * @param orderId
     * @return
     */
    @Override
    public APIResult refundOperate(Integer operationType, Long orderId) throws IOException {
        Order orderInfo = orderDao.findOne(orderId);
        APIResult apiResult = null;
        JSONObject jsonParam;
        if (null != orderInfo) {
            if (RefundStatus.RefundInProgress == orderInfo.getRefundstatus()) {
                //如果支付类型为7  虚拟下单
                if (orderInfo.getPaytype().getValue() == OrderPayType.VIRTUAL.getValue()) {
                    RefundStatus StatusNameObj = RefundStatus.Failure;
                    String StatusnameStr = "退款失败";
                    if (operationType == 1) {//通过
                        StatusNameObj = RefundStatus.Success;
                        orderInfo.setOrderstatus(NewOrderStatus.RefundSuccess);
                        StatusnameStr = "退款成功";
                        notificationPublisher.publishRefundSuccess(orderId.toString());
                    }else{
                        notificationPublisher.publishRefundFail(orderId.toString());
                    }
                    orderInfo.setStatusname(StatusnameStr);
                    orderInfo.setRefundaudittime(DateUtils.newDate());//退款时间
                    orderInfo.setRefundstatus(StatusNameObj);//退款状态 202 失败 203 成功
                    orderDao.save(orderInfo);

                    return new APIResult();
                }
                String url = "";
                if (operationType == 1) {//通过
                    url = orderService + "/orders/" + orderId + "/refunds/_approval";
                } else {//拒绝
                    url = orderService + "/orders/" + orderId + "/refunds/_cancel";
                }
                jsonParam = new JSONObject();
                jsonParam.put("operateId", UserHolder.getUser().getUserid());
                jsonParam.put("operateType", RefundSource.CMS.getValue());
                apiResult = httpPost(url, jsonParam);
            }
        }
        return apiResult;
    }

    /**
     * 退款审核操作通过手共登记 2 联机 1
     *
     * @param operationType
     * @param orderId
     * @return
     */
    @Override
    public APIResult checkPass(Integer operationType, Long orderId) throws IOException {
        Order orderInfo = orderDao.findOne(orderId);
        APIResult apiResult = null;
        JSONObject jsonParam;
        if (null != orderInfo) {
            //状态为审核通过的
            if (RefundStatus.REFUNDAPPROVAL == orderInfo.getRefundstatus()) {
                String url = "";
                if (operationType == 1) {//联机
                    url = orderService + "/orders/" + orderId + "/refunds/_perform";
                } else {//手工
                    url = orderService + "/orders/" + orderId + "/refunds/_book";
                }
                jsonParam = new JSONObject();
                jsonParam.put("operateId", UserHolder.getUser().getUserid());
                jsonParam.put("operateType", RefundSource.CMS.getValue());
                apiResult = httpPost(url, jsonParam);
            }
        }
        return apiResult;
    }

    @Override
    public PageModel<OrderModel> getMerchantOrders(final Long mchId, Integer page, Integer size) {
        Pageable pageable = new PageRequest(page-1,size);
        Page<Order> orders = orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.equal(root.get("merchant").get("id"),mchId));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).orderBy(cb.desc(root.get("createtime"))).getRestriction();
            }
        }, pageable);
        List<OrderModel> orderModels = new ArrayList<>();
        for (Order order : orders.getContent() ) {
            OrderModel orderModel = new OrderModel();
            orderModel.setId(order.getId());
            orderModel.setCreatetime(order.getCreatetime());
            if (order.getKpprice() != null) {
                orderModel.setKpprice(order.getKpprice().setScale(2));
            }
            BigDecimal price = order.getPayable()!=null?order.getPayable():order.getPrice();
            if (price != null) {
                orderModel.setPrice(price.setScale(2));
            }
            orderModel.setType(order.getOrdertype().getName());
            orderModel.setStstus(order.getOrderstatus().getName());
            orderModel.setOrdersn(order.getOrdersn());
            orderModel.setTransactionid(order.getTransactionid());
            orderModel.setUsetime(order.getUsetime());

            orderModels.add(orderModel);
        }
        PageModel<OrderModel> pageModel = new PageModel<OrderModel>();
        pageModel.setPage(page);
        pageModel.setSize(size);
        pageModel.setPages(orders.getTotalPages());
        pageModel.setTotal(orders.getTotalElements());
        pageModel.setList(orderModels);
        return pageModel;
    }

    /**
     * 提交orderService 订单退款服务
     *
     * @param url
     * @param jsonParam
     * @return
     * @throws IOException
     */
    private static APIResult httpPost(String url, JSONObject jsonParam) throws IOException {
        APIResult apiResult = null;
        if (null != url && !"".equals(url)) {
            HttpPost post = new HttpPost(url);
            if (jsonParam != null) {
                // 接收参数json列表
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
                CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
                String responseText = EntityUtils.toString(response.getEntity());
                System.out.println(responseText);
                apiResult = JSON.parseObject(responseText, APIResult.class);
                if (apiResult.getCode() == null || apiResult.getCode() != 0) {
                    throw new APIException(ResultCodes.UnkownError, apiResult.getMsg());
                }
            }
        }
        return apiResult;
    }
}
