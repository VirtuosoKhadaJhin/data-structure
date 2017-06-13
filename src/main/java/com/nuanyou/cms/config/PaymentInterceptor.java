package com.nuanyou.cms.config;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Byron on 2017/6/13.
 */
@Component
public class PaymentInterceptor extends HandlerInterceptorAdapter {

    private static final String PAYMENT_SEARCH_URL = "/paymentOrderRecord/searchRecord";

    /**
     * String:订单ID
     * Date：发起请求的时间点
     */
    private static final LinkedHashMap<String, Date> searchDateMap = new LinkedHashMap<String, Date>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.equalsIgnoreCase(PAYMENT_SEARCH_URL)) {
            String orderId = request.getParameter("orderId");
            searchDateMap.put(orderId, new Date());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        DateTime dateTime = new DateTime();
        long beforeFiveMisTime = dateTime.minusMinutes(5).toDate().getTime();
        if (requestURI.equalsIgnoreCase(PAYMENT_SEARCH_URL) && searchDateMap.size() > 0) {
            Iterator<Map.Entry<String, Date>> iterator = searchDateMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Date> date = iterator.next();
                long requestTime = date.getValue().getTime();
                if (beforeFiveMisTime > requestTime) {
                    iterator.remove();
                }
            }
        }
        modelAndView.getModelMap().addAttribute("searchDateMap", searchDateMap);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("_________________________________________________");
    }
}
