package com.nuanyou.cms.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nuanyou.cms.dao.SendMessageLogDao;
import com.nuanyou.cms.dao.UserRechargeLogDao;
import com.nuanyou.cms.entity.SendMessageLog;
import com.nuanyou.cms.entity.UserRechargeLog;
import com.nuanyou.cms.model.*;
import com.nuanyou.cms.service.RemoteService;
import com.nuanyou.cms.util.HttpUtils;
import com.nuanyou.cms.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Alan.ye on 2016/10/20.
 */
@Service
public class RemoteServiceImpl implements RemoteService {

    private static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKrN7VkgMo02hYK7VuJ7KgBGQB8VBxW2KJI7eUk+95BhmcEDAZT5SKxrihjEHDerX+31TkrIddgl3KaHmaHHwAzVQV6fG3G4XXxG3EiqkKQySp4SVN5b9++nzit+ddofi4mXEt8MlkYzWV9nNp7guUQCXy9frp4KyfHMtsBS930ZAgMBAAECgYEAmMZGhNCKxso8kxlz9nHJuKMdWW/afW4ITfwKWRyMHMVf3EcPFCwA98/cnphS0Oxlipc+px80YNhEy2NAZHchbCFN25d0pSmC0WY1a70W7MYbkblAeLoLBNPNEw9OmYmog6lz+FUa0Oz1Oq9DwZRva5qqonLUKC1c+3yEZw/ioAECQQDmF+QB50ZE26/kn67LtOfKAuYMQ0lHFL0A8pszwJWbPNoKpaGgpnLA4zJiI78aUn1eGoG57Qp9h3EbdbUbFmkZAkEAvgkeWx5XAAVyko06bqY9EZ7gH7z2wF25YnRzXcW3E2BIuknDCQqQ7DstP8aHFdunnMlHVMA/rGJOIZ4r58g0AQJAE+OiyOtV7qPSy39mG6OymYqwmgTC88r+H3PZKJsQE5AqBNuWYg2hQ70f4M3YOg1BWv4NkqXDz2ACze3ZztKcGQJBAIk9CpghTBEu3fQqW/WGxnmQNCmXjNeVmAkbMimZXMJ4eW1XUauY3tpLTj1NgUbuz5gx3/q7sAAtKmGq2ehUtAECQQDC8npeJa1+vKaAcrYdcIEjEKnU9OubmwYGWpfEv1ljHwz6djVYaLAgN6TvkUdHGQsmmGV0+CanSBAZBkh2PDVR";

    @Value("${withdrawurl}")
    private String withdrawurl;

    @Autowired
    private SendMessageLogDao sendMessageLogDao;

    @Autowired
    private UserRechargeLogDao userRechargeLogDao;


    public void pushMessage(Long userId, String context, Long cmsUserId) {
        Map<String, String> jsonText = new HashMap<>();
        jsonText.put("content", context);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("msgtype", "text");
        jsonMap.put("text", jsonText);

        String json = JSON.toJSONString(jsonMap);

        String url = withdrawurl + "/push/nuanyou/" + userId;

        String response = HttpUtils.post(url, "wxdata", json, "appdata", json);
        Map map = JSONArray.parseObject(response, Map.class);
        SendMessageLog sendMessageLog = new SendMessageLog();
        sendMessageLog.setCmsUserId(cmsUserId);
        sendMessageLog.setUserId(userId);
        sendMessageLog.setContext(context);
        sendMessageLog.setCode(map.get("code").toString());
        sendMessageLog.setMsg(map.get("msg").toString());
        sendMessageLogDao.save(sendMessageLog);
    }

    public void recharge(Long userid, BigDecimal amount, Integer type, Long cmsUserId, String cmsUserName) throws Exception {
        if (type.equals(2))
            amount = amount.multiply(new BigDecimal(-1));

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userid", String.valueOf(userid)));
        params.add(new BasicNameValuePair("amount", amount.toPlainString()));

        Utils.sign(params, PRIVATE_KEY);
        URI uri = new URI(withdrawurl + "/s/wallet/recharge/cms");
        HttpPost post = new HttpPost(uri);
        post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }
        userRechargeLogDao.save(new UserRechargeLog(userid, amount, type, cmsUserId, cmsUserName));
    }

    @Override
    public PageRemote<Withdraw> withdraw(WithdrawParam withdrawVO, Integer index) {
        String url = withdrawurl + "/s/wallet/withdraw/log";
        PageRemote<Withdraw> page=new PageRemote<Withdraw>();
        Map<String,String> params=new HashMap<String,String>();
        if (StringUtils.isNotBlank(withdrawVO.getStatus())){
            params.put("status",withdrawVO.getStatus());
        } if (StringUtils.isNotBlank(withdrawVO.getUserid())){
            params.put("userid",withdrawVO.getUserid());
        }if (withdrawVO.getBegin()!=null){
            params.put("from",withdrawVO.getBegin().getTime()/1000+"");
        }if (withdrawVO.getEnd()!=null){
            params.put("to",withdrawVO.getEnd().getTime()/1000+"");
        }
        params.put("length", PageUtil.pageSize.toString());
        params.put("offset",new Integer((index-1)*PageUtil.pageSize).toString());
        String response=HttpUtils.post(url,params);
        System.out.println("response"+response);
        WithdrawPage json= JSON.parseObject(response,WithdrawPage.class);
        if (json.getCode()==0){
            page.setTotalPages(json.getPages());
            page.setNumber(index-1);
            page.setContent(json.getData());
        }
        return page;
    }

    @Override
    public void operateWithdraw(String type, Integer operateid, String message) {
        String approve = withdrawurl + "/s/wallet/withdraw/approve";
        Map<String,String> params=new HashMap<String,String>();
        if ("success".equals(type)){
            params.put("id",operateid.toString());
            params.put("status",2+"");
        }else if("failure".equals(type)){
            params.put("id",operateid.toString());
            params.put("status",3+"");
            params.put("message",message);
        }
        String response=HttpUtils.post(approve,params);
    }



}