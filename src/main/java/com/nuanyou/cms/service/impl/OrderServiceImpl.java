package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.domain.NotificationPublisher;
import com.nuanyou.cms.entity.UserCardItem;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.enums.*;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.entity.order.OrderRefundLog;
import com.nuanyou.cms.entity.order.OrderVouchCard;
import com.nuanyou.cms.entity.order.ViewOrderExport;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.OrderRefundLogService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.ConvertFileEncoding;
import com.nuanyou.cms.util.DateUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class OrderServiceImpl implements OrderService {

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
    @Autowired
    private NotificationPublisher notificationPublisher;
    @Autowired
    private ViewOrderExportDao viewOrderExportDao;
    @Autowired
    private MerchantDao merchantDao;

    private static String timePattern = "yyyy-MM-dd HH:mm:ss";
    private static String decimalPattern = "#0.00";

    @Override
    public Page<Order> findByCondition(Integer index, final Order entity, final TimeCondition time, Pageable pageable) {
        return orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();

                if (entity.getId() != null) {
                    Predicate p = cb.equal(root.get("id"), entity.getId());
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
                if (time.getBegin_2() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("refundtime").as(Date.class), time.getBegin_2());
                    predicate.add(p);
                }
                if (time.getEnd_2() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("refundtime").as(Date.class), time.getEnd_2());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && !StringUtils.isEmpty(entity.getMerchant().getName())) {
                    Predicate pStatus = cb.like(root.get("merchant").get("name").as(String.class), entity.getMerchant().getName());
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
    public List<Order> findRefundByCondition(final Order entity, final TimeCondition time) {
        return orderDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (entity.getRefundstatus() != null) {
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
                if (time.getBegin_2() != null) {
                    Predicate p = cb.greaterThanOrEqualTo(root.get("refundtime").as(Date.class), time.getBegin_2());
                    predicate.add(p);
                }
                if (time.getEnd_2() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("refundtime").as(Date.class), time.getEnd_2());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && !StringUtils.isEmpty(entity.getMerchant().getName())) {
                    Predicate pStatus = cb.like(root.get("merchant").get("name").as(String.class), entity.getMerchant().getName());
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
        }, new Sort(Sort.Direction.DESC, "refundtime"));
    }


    @Override
    public void putOrderToExcel(int index, HttpServletRequest request, HttpServletResponse response, ViewOrderExport entity,
                                TimeCondition time, String[] titles, String filename, HSSFWorkbook workbook, HSSFSheet firstSheet, HSSFRow firstRow) throws IOException {
        Long begin = System.currentTimeMillis();
        Page<ViewOrderExport> page = findExportByCondition(index, entity, time, null);
        Long end = System.currentTimeMillis();
        System.out.println("读的时间差:" + (end - begin));
        Long beginWrite = System.currentTimeMillis();
        for (int i = 0; i < titles.length; i++) {
            HSSFCell c = firstRow.createCell(i);
            c.setCellValue(titles[i]);
        }
        NumberTool numberFormatter = new NumberTool();
        DateTool dateFormatter = new DateTool();
        FillContent(firstSheet, page, numberFormatter, dateFormatter);
        setResponseOut(filename, workbook, request, response);
        Long endWrite = System.currentTimeMillis();
        System.out.println("写的时间差:" + (endWrite - beginWrite));
    }

    private void FillContent(HSSFSheet sheet, Page<ViewOrderExport> page, NumberTool numberFormatter, DateTool dateFormatter) {
        for (int i = 0; i < page.getContent().size(); i++) {
            HSSFRow r = sheet.createRow(i + 1);
            ViewOrderExport each = page.getContent().get(i);
            r.createCell(0).setCellValue(i + 1);
            r.createCell(1).setCellValue(each.getId());
            r.createCell(2).setCellValue(each.getOrdersn());
            r.createCell(3).setCellValue(each.getTransactionid());
            r.createCell(4).setCellValue(each.getSceneid());
            r.createCell(5).setCellValue(each.getOrdertype() == null ? "" : each.getOrdertype().getName());
            r.createCell(6).setCellValue(each.getPaytype() == null ? "" : each.getPaytype().getName());
            r.createCell(7).setCellValue(each.getPlatform() == null ? "" : each.getPlatform().getName());
            r.createCell(8).setCellValue(each.getOs() == null ? "" : each.getOs().getName());
            r.createCell(9).setCellValue(each.getOrdercode());
            r.createCell(10).setCellValue(each.getMerchant() == null || each.getMerchant().getId() == null ? "" : each.getMerchant().getId().toString());
            r.createCell(11).setCellValue(each.getMerchant() == null || StringUtils.isEmpty(each.getMerchant().getName()) ? "" : each.getMerchant().getName());
            r.createCell(12).setCellValue(each.getMerchant() == null || StringUtils.isEmpty(each.getMerchant().getKpname()) ? "" : each.getMerchant().getKpname());
            r.createCell(13).setCellValue(each.getUserId() == null ? "" : each.getUserId().toString());
            r.createCell(14).setCellValue(numberFormatter.format(decimalPattern, each.getKpprice()));
            r.createCell(15).setCellValue(numberFormatter.format(decimalPattern, each.getOkpprice()));
            r.createCell(16).setCellValue(each.getPayable() == null ? each.getPrice().doubleValue() : each.getPayable().doubleValue());
            r.createCell(17).setCellValue(each.getOprice() == null ? "" : each.getOprice().toPlainString());
            r.createCell(18).setCellValue(each.getStatusname());
            r.createCell(19).setCellValue(dateFormatter.format(timePattern, each.getCreatetime()));
            r.createCell(20).setCellValue(dateFormatter.format(timePattern, each.getUsetime()));
            r.createCell(21).setCellValue(each.getAddress() == null ? "" : each.getAddress());
            r.createCell(22).setCellValue(each.getPostalcode() == null ? "" : each.getPostalcode());
            r.createCell(23).setCellValue(each.getProvince() == null ? "" : each.getProvince());
            r.createCell(24).setCellValue(each.getDistrict() == null ? "" : each.getDistrict());
            r.createCell(25).setCellValue(each.getCity() == null ? "" : each.getCity());
            r.createCell(26).setCellValue(each.getTel() == null ? "" : each.getTel());
        }
    }


    private void setResponseOut(String filename, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws IOException {
        filename = ConvertFileEncoding.encodeFilename(filename, request);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        workbook.write(response.getOutputStream());
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
                if (entity.getMerchant() != null && !StringUtils.isEmpty(entity.getMerchant().getKpname())) {
                    Predicate p = cb.equal(root.get("merchant").get("kpname"), entity.getMerchant().getKpname());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("id"), entity.getMerchant().getId());
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

                if (entity.getMerchant() != null && entity.getMerchant().getDistrict() != null && entity.getMerchant().getDistrict().getCountry() != null
                        && entity.getMerchant().getDistrict().getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("district").get("country").get("id"),
                            entity.getMerchant().getDistrict().getCountry().getId());
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
        if (order == null) {
            throw new APIException(ResultCodes.OrderNotFound, " Detail：OrderID" + entity.getId());
        }
        if (!Order.getRefundQualified(order)) {
            throw new APIException(ResultCodes.OrderOther, "只有 【退款失败、已消费、已评价、自动核销、商户核销】 的订单支持发起退款申请");
        }

        if (order.getRefundstatus() != null) {
            if (order.getRefundstatus() == RefundStatus.RefundInProgress) {
                throw new APIException(ResultCodes.Refunding, " Detail：OrderID" + order.getId());
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
        order.setRefundsource(RefundSource.CMS);//// 退款来源：1.客户端，2.cms，3.商户端
        this.orderDao.save(order);

        this.notificationPublisher.publishRefund(order.getId().toString());
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
