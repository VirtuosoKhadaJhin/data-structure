package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.OrderDao;
import com.nuanyou.cms.dao.OrderRefundLogDao;
import com.nuanyou.cms.entity.enums.RefundStatus;
import com.nuanyou.cms.entity.order.OrderRefundLog;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.HttpService;
import com.nuanyou.cms.service.OrderRefundService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.RSAUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2016/9/10.
 */
@Service
public class OrderRefundServiceImpl implements OrderRefundService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private HttpService httpService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundLogDao orderRefundLogDao;

    @Value("${autoHandleRefund.url}")
    private String refundUrl;

    @Override
    public Page<OrderRefundLog> findByCondition(int index, final OrderRefundLog entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "createTime");
        return orderRefundLogDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (entity.getId() != null) {
                    Predicate p = cb.equal(root.get("id"), entity.getId());
                    predicate.add(p);
                }
                if (entity.getOrder() != null && StringUtils.isNotBlank(entity.getOrder().getOrdercode())) {
                    Predicate p = cb.equal(root.get("order").get("ordercode"), entity.getOrder().getOrdercode());
                    predicate.add(p);
                }
          /*      if (entity.getOrder() != null && entity.getOrder().getUser() != null && StringUtils.isNotBlank(entity.getOrder().getUser().getNickname())) {
                    Predicate p = cb.equal(root.get("order").get("user").get("nickname"), entity.getOrder().getUser().getNickname());
                    predicate.add(p);
                }*/
                if (entity.getStatus() != null) {
                    Predicate p = cb.equal(root.get("status"), entity.getStatus());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    @Transactional
    public void validate(Long id, Integer type) {
        OrderRefundLog entity = orderRefundLogDao.findOne(id);
        if (entity != null) {
            if (entity.getStatus() == 1 && entity.getStatus() == 2) {
                throw new APIException(ResultCodes.Audited);
            }
        }

        entity.setStatus(type);
        if (entity.getStatus() == 2) {
            entity.getOrder().setRefundstatus(RefundStatus.Failure);
        } else if (entity.getStatus() == 1) {
            entity.getOrder().setRefundstatus(RefundStatus.Success);
        }
        this.orderService.saveNotNull(entity.getOrder());
        this.saveNotNull(entity);
    }

    @Override
    public APIResult autoHandleRefund(Long id, String transactionId) throws Exception {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("tradeno", RSAUtils.encryptTradeNo(transactionId)));

        APIResult responseData = this.httpService.doPost(new URI(refundUrl), parameters, APIResult.class);
        System.out.println("responseText：" + responseData.toString());

        if(responseData.getCode() == ResultCodes.Success.getCode()){
            // 修改订单状态和退款订单状态 1：通过 2：不通过
            try{
                orderService.validate(id, RefundStatus.Success.getValue(), "admin");
            }catch (Exception e){

            }
        }

        return responseData;

    }

    @Override
    public OrderRefundLog saveNotNull(OrderRefundLog entity) {
        if (entity.getId() == null) {
            return orderRefundLogDao.save(entity);
        }
        OrderRefundLog oldEntity = orderRefundLogDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return orderRefundLogDao.save(oldEntity);
    }
}

