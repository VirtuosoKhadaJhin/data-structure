package com.nuanyou.cms.domain;

import com.amazonaws.services.sns.AmazonSNSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by ICQ on 28/10/2016.
 */
@Service
public class NotificationPublisher {
    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class.getSimpleName());

    @Value("${AWS.sns.topic.refund}")
    private String refundTopic;

    @Value("${AWS.sns.topic.refundSuccess}")
    private String refundSuccessTopic;

    @Value("${AWS.sns.topic.refundFail}")
    private String refundFailTopic;

    @Autowired
    private AmazonSNSClient snsClient;

    public void publishRefund(String orderId) {
        snsClient.publish(refundTopic, orderId);
    }

    public void publishRefundSuccess(String orderId) {
        snsClient.publish(refundSuccessTopic, orderId);
    }

    public void publishRefundFail(String orderId) {
        snsClient.publish(refundFailTopic, orderId);
    }
}
