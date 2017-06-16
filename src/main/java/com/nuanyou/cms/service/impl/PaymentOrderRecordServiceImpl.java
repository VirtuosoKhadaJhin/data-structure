package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.PaymentOrderRecordDao;
import com.nuanyou.cms.entity.PaymentOrderRecord;
import com.nuanyou.cms.model.PaymentOrderRecordVo;
import com.nuanyou.cms.model.PaymentRecordRequestVo;
import com.nuanyou.cms.service.PaymentOrderRecordService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * Created by Byron on 2017/6/9.
 */
@Service
public class PaymentOrderRecordServiceImpl implements PaymentOrderRecordService {

    private final static Logger _LOGGER = LoggerFactory.getLogger ( PaymentOrderRecordServiceImpl.class );

    @Autowired
    private PaymentOrderRecordDao paymentRecordDao;

    @Override
    public Page<PaymentOrderRecordVo> findAllPaymentOrderRecord(final PaymentRecordRequestVo paramVo) {


        List<PaymentOrderRecord> records = paymentRecordDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate> ();

                if (paramVo.getMchId () != null) {
                    predicate.add(cb.equal(root.get("mchId"), paramVo.getMchId()));
                }
                if (StringUtils.isNotEmpty ( paramVo.getMchName () )) {
                    predicate.add(cb.like(root.get("mchName"), "%" + paramVo.getMchName() + "%"));
                }
                if (StringUtils.isNotEmpty(paramVo.getMchKpName())) {
                    predicate.add(cb.like(root.get("mchKpName"), "%" + paramVo.getMchKpName() + "%"));
                }
                if (paramVo.getTradeNo () != null) {
                    predicate.add(cb.equal(root.get("tradeNo"), paramVo.getTradeNo()));
                }
                if (paramVo.getOutTradeNo () != null) {
                    predicate.add(cb.equal(root.get("outTradeNo"), paramVo.getOutTradeNo()));
                }
                if (paramVo.getStatus () != null) {
                    predicate.add(cb.equal(root.get("status"), paramVo.getStatus().getKey()));
                }
                if (paramVo.getPaymentOrderMethod() != null) {
                    predicate.add(cb.equal(root.get("method"), paramVo.getPaymentOrderMethod().getKey()));
                }
                if (paramVo.getBeginPrice () != null) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("price"), paramVo.getBeginPrice()));
                }
                if (paramVo.getEndPrice() != null) {
                    predicate.add(cb.lessThanOrEqualTo(root.get("price"), paramVo.getEndPrice()));
                }
                if (StringUtils.isNotEmpty ( paramVo.getPayChannel () )) {
                    predicate.add(cb.like(root.get("channelName"), "%" + paramVo.getPayChannel() + "%"));
                }
                if (paramVo.getBeginDt () != null) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("payTime").as(Date.class), paramVo.getBeginDt()));
                }
                if (paramVo.getEndDt () != null) {
                    predicate.add(cb.lessThanOrEqualTo(root.get("payTime").as(Date.class), paramVo.getEndDt()));
                }

                Predicate[] pre = new Predicate[predicate.size ()];
                return query.where(predicate.toArray(pre)).orderBy(cb.desc(root.get("payTime"))).getRestriction();
            }
        });


        List<PaymentOrderRecordVo> orderRecordVos = this.convertToPaymentRecordVo(records);
        Integer pageIndex = paramVo.getIndex();
        Integer pageNum = paramVo.getPageNum();
        Pageable pageable = new PageRequest(pageIndex - 1, pageNum);
        if (CollectionUtils.isEmpty(orderRecordVos)) {
            return new PageImpl<PaymentOrderRecordVo>(orderRecordVos, pageable, 0);
        }
        int toIndex = pageIndex * pageNum;
        List<PaymentOrderRecordVo> subList = orderRecordVos.subList((pageIndex - 1) * pageNum, toIndex > orderRecordVos.size() ? orderRecordVos.size() : toIndex);
        Page<PaymentOrderRecordVo> voPage = new PageImpl<PaymentOrderRecordVo>(subList, pageable, records.size());
        return voPage;
    }

    private List<PaymentOrderRecordVo> convertToPaymentRecordVo(List<PaymentOrderRecord> content) {
        if (CollectionUtils.isEmpty ( content )) {
            return Lists.newArrayList();
        }
        List<PaymentOrderRecordVo> vos = Lists.newArrayList ();
        for (PaymentOrderRecord record : content) {
            PaymentOrderRecordVo recordVo = BeanUtils.copyBean ( record, new PaymentOrderRecordVo () );
            vos.add ( recordVo );
        }
        return vos;
    }
}
