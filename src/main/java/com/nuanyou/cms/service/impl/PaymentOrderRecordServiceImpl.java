package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.PaymentOrderRecordDao;
import com.nuanyou.cms.model.PaymentOrderRecordVo;
import com.nuanyou.cms.model.PaymentRecordRequestVo;
import com.nuanyou.cms.service.PaymentOrderRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
public class PaymentOrderRecordServiceImpl implements PaymentOrderRecordService {

    private final static Logger _LOGGER = LoggerFactory.getLogger ( PaymentOrderRecordServiceImpl.class );

    @Autowired
    private PaymentOrderRecordDao paymentRecordDao;

    @Override
    public Page<PaymentOrderRecordVo> findAllPaymentOrderRecord(final PaymentRecordRequestVo paramVo) {

        Pageable pageable = new PageRequest ( paramVo.getIndex () - 1, paramVo.getPageNum () );

        Page<PaymentOrderRecordVo> records = paymentRecordDao.findAll ( new Specification () {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate> ();

                if (paramVo.getId () != null) {
                    Predicate p = cb.equal ( root.get ( "id" ), paramVo.getId () );
                    predicate.add ( p );
                }
                if (paramVo.getBeginDt () != null) {
                    Predicate p = cb.greaterThanOrEqualTo ( root.get ( "paytime" ).as ( Date.class ), paramVo.getBeginDt () );
                    predicate.add ( p );
                }
                if (paramVo.getEndDt () != null) {
                    Predicate p = cb.lessThanOrEqualTo ( root.get ( "paytime" ).as ( Date.class ), paramVo.getEndDt () );
                    predicate.add ( p );
                }
                if (!StringUtils.isEmpty ( paramVo.getTitle () )) {
                    Predicate p = cb.equal ( root.get ( "title" ), paramVo.getTitle () );
                    predicate.add ( p );
                }
                if (!StringUtils.isEmpty ( paramVo.getOrderNo () )) {
                    Predicate p = cb.like ( root.get ( "orderno" ), "%" + paramVo.getOrderNo () + "%" );
                    predicate.add ( p );
                }
                if (!StringUtils.isEmpty ( paramVo.getTradeNo () )) {
                    Predicate p = cb.like ( root.get ( "tradeno" ), "%" + paramVo.getTradeNo () + "%" );
                    predicate.add ( p );
                }
                if (paramVo.getPrice () != null) {
                    Predicate p = cb.equal ( root.get ( "price" ), paramVo.getPrice () );
                    predicate.add ( p );
                }
                if (paramVo.getMethod () != null) {
                    Predicate p = cb.equal ( root.get ( "method" ), paramVo.getMethod ().getKey () );
                    predicate.add ( p );
                }

                Predicate[] pre = new Predicate[predicate.size ()];
                return query.where ( predicate.toArray ( pre ) ).getRestriction ();
            }
        }, pageable );
        return records;
    }

    @Override
    public PaymentOrderRecordVo findPaymentOrderRecord(Long id) {
        return null;
    }

    @Override
    public List<PaymentOrderRecordVo> findPaymentOrderRecords() {
        return null;
    }

    @Override
    public PaymentOrderRecordVo updatePaymentOrderRecordInfo(Long id) {
        return null;
    }
}
