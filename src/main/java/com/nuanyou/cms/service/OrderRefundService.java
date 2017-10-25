package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.order.OrderRefundLog;
import org.springframework.data.domain.Page;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Felix on 2016/9/10.
 */
public interface OrderRefundService {

    void validate(Long id, Integer type);

    void autoHandleRefund(String transactionId) throws Exception;

    OrderRefundLog saveNotNull(OrderRefundLog entity);

    Page<OrderRefundLog> findByCondition(int index, OrderRefundLog entity);

}
