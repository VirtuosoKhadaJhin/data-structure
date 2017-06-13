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

        List<PaymentOrderRecord> records = paymentRecordDao.findAll ( new Specification () {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate> ();

                if (paramVo.getMchId () != null) {
                    Predicate p = cb.equal ( root.get ( "mchId" ), paramVo.getMchId () );
                    predicate.add ( p );
                }
                if (StringUtils.isNotEmpty ( paramVo.getMchName () )) {
                    Predicate p = cb.like ( root.get ( "mchName" ), "%" + paramVo.getMchName () + "%" );
                    predicate.add ( p );
                }
                if (StringUtils.isNotEmpty(paramVo.getMchKpName())) {
                    Predicate p = cb.like(root.get("mchKpName"), "%" + paramVo.getMchKpName() + "%");
                    predicate.add(p);
                }
                if (paramVo.getTradeNo () != null) {
                    Predicate p = cb.equal ( root.get ( "tradeNo" ), paramVo.getTradeNo () );
                    predicate.add ( p );
                }
                if (paramVo.getOutTradeNo () != null) {
                    Predicate p = cb.equal ( root.get ( "outTradeNo" ), paramVo.getOutTradeNo () );
                    predicate.add ( p );
                }
                if (paramVo.getStatus () != null) {
                    Predicate p = cb.equal ( root.get ( "status" ), paramVo.getStatus ().getKey () );
                    predicate.add ( p );
                }
                if (paramVo.getBeginPrice () != null) {
                    Predicate p = cb.greaterThanOrEqualTo ( root.get ( "price" ), paramVo.getBeginPrice () );
                    predicate.add ( p );
                }
                if (paramVo.getEndPrice() != null) {
                    Predicate p = cb.lessThanOrEqualTo(root.get("price"), paramVo.getEndPrice());
                    predicate.add ( p );
                }
                if (StringUtils.isNotEmpty ( paramVo.getPayChannel () )) {
                    Predicate p = cb.like ( root.get ( "channelName" ), "%" + paramVo.getPayChannel () + "%" );
                    predicate.add ( p );
                }
                if (paramVo.getBeginDt () != null) {
                    Predicate p = cb.greaterThanOrEqualTo ( root.get ( "payTime" ).as ( Date.class ), paramVo.getBeginDt () );
                    predicate.add ( p );
                }
                if (paramVo.getEndDt () != null) {
                    Predicate p = cb.lessThanOrEqualTo ( root.get ( "payTime" ).as ( Date.class ), paramVo.getEndDt () );
                    predicate.add ( p );
                }

                Predicate[] pre = new Predicate[predicate.size ()];
                return query.where(predicate.toArray(pre)).orderBy(cb.desc(root.get("payTime"))).getRestriction();
            }
        } );

        Pageable pageable = new PageRequest ( paramVo.getIndex () - 1, paramVo.getPageNum () );
        List<PaymentOrderRecordVo> orderRecordVos = this.convertToPaymentRecordVo ( records );
        if (CollectionUtils.isEmpty(orderRecordVos)) {
            return new PageImpl<PaymentOrderRecordVo>(orderRecordVos, pageable, 0);
        }
        Page<PaymentOrderRecordVo> voPage = new PageImpl<PaymentOrderRecordVo> ( orderRecordVos, pageable, records.size () );
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
