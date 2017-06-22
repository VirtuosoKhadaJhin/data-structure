package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.PaymentOrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Byron on 2017/6/9.
 */
public interface PaymentOrderRecordDao extends JpaRepository<PaymentOrderRecord, Long> , JpaSpecificationExecutor {

}
