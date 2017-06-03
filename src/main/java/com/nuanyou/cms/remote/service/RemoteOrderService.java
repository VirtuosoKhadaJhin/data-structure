package com.nuanyou.cms.remote.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.OrderSave;
import com.nuanyou.cms.util.HttpUtils;
import com.nuanyou.cms.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 17/3/16.
 */
@Service
public class RemoteOrderService {

    @Value("${nuanyou-api}")
    private String nuanyouapi;

    public APIResult<OrderSave> ordersSaveTuanPost(Integer payType, Long itemId, Integer number) {
        Map<String, String> param = new HashMap<>();
        param.put("paytype", "1");
        param.put("productdetail", "{\"id\":" + String.valueOf(itemId) + ",\"num\":" + String.valueOf(number) + "}");
        String post = HttpUtils.post(nuanyouapi + "/order/save/tuan/virtual", param);
        APIResult<OrderSave> orderSave = JsonUtils.toObj(post, new TypeReference<APIResult<OrderSave>>() {
        });
        return orderSave;
    }

    public APIResult<OrderSave> ordersPayCallbackPost(Long orderId) {
        Map<String, String> param = new HashMap<>();
        param.put("orderid", String.valueOf(orderId));
        param.put("paystatus", "2");
        param.put("paytype", "7");
        param.put("transactionid", "0");
        param.put("appid", "dianping20170316184405");
        String post = HttpUtils.get(nuanyouapi + "/order/paidCallback", param);
        APIResult result = JsonUtils.toObj(post, APIResult.class);
        return result;
    }
}