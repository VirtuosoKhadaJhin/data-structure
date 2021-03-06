package com.nuanyou.cms.config;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Byron on 2017/6/13.
 */
@Component
public class PaymentInterceptor extends HandlerInterceptorAdapter {

    public static final String SEARCH_PAYMENT_ORDER_RECORD_SUCCESS = "SEARCH_PAYMENT_ORDER_RECORD_SUCCESS";

    private static final Logger _LOGGER = LoggerFactory.getLogger(PaymentInterceptor.class);

    @Value("${paymentRecord.waiting-time}")
    private Integer paymentWaitingTime;

    /**
     * String:订单ID
     * Date：发起请求的时间点
     */
    private static final Map<String, Date> searchDateMap = Collections.synchronizedMap(new LinkedHashMap<String, Date>());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();
        if (requestUrl.contains("list")) {
            _LOGGER.info("");
            return true;
        }
        String orderId = request.getParameter("orderId");
        searchDateMap.put(orderId, new Date());
        request.getSession().setAttribute("searchDateMap", searchDateMap);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object object = request.getAttribute(SEARCH_PAYMENT_ORDER_RECORD_SUCCESS);
        if (object != null) {
            String orderId = (String) object.toString();
            searchDateMap.remove(orderId);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DateTime dateTime = new DateTime();
        long beforeFiveMisTime = dateTime.minusMinutes(paymentWaitingTime).toDate().getTime();
        Iterator<Map.Entry<String, Date>> iterator = searchDateMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Date> date = iterator.next();
            long requestTime = date.getValue().getTime();
            if (beforeFiveMisTime > requestTime) {
                iterator.remove();
            }
        }
    }
}
