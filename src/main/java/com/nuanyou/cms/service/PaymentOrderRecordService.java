package com.nuanyou.cms.service;

import com.nuanyou.cms.model.PaymentOrderRecordVo;
import com.nuanyou.cms.model.PaymentRecordRequestVo;
import org.springframework.data.domain.Page;

/**
 * Created by Byron on 2017/6/9.
 */
public interface PaymentOrderRecordService {

    Page<PaymentOrderRecordVo> findAllPaymentOrderRecord(PaymentRecordRequestVo paramVo);

}
