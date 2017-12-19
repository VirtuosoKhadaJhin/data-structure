package com.nuanyou.cms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.ContactModel;
import com.nuanyou.cms.model.ContractModel;
import com.nuanyou.cms.model.CustomerServiceInfo;
import com.nuanyou.cms.service.RemoteCrmService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by young on 2017/12/15.
 */
@Service
public class RemoteCrmServiceImpl implements RemoteCrmService {

    @Value("${crm.kr.domain}")
    private String krDomain;
    @Value("${crm.sg.domain}")
    private String sgDomain;
    private String domain;
    @Value("${crm.authKey}")
    private String authKey;

    @Override
    public void setKrDomain(){
        domain = krDomain;
    }

    @Override
    public void setSgDomain(){
        domain = sgDomain;
    }

    @Override
    public CustomerServiceInfo getCustomerServiceInfo(String callNo) throws Exception {
        HttpGet get = new HttpGet(domain + "/api/cs/merchant/" + callNo+"?authKey="+authKey);
        CloseableHttpClient build = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        CustomerServiceInfo info = new CustomerServiceInfo();
        try {
            response = build.execute(get);
            JSONObject responseJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            System.out.println("RemoteCrmService responseText:"+responseJson.toJSONString());
            if (!responseJson.containsKey("code") || responseJson.getInteger("code") != 0) {
                throw new APIException(ResultCodes.CrmServiceException);
            }
            JSONObject data = responseJson.getJSONObject("data");

            info = JSONObject.parseObject(data.toJSONString(),CustomerServiceInfo.class);

            return info;
        } catch (ConnectException e) {
            throw new APIException(ResultCodes.CrmServiceException,e.getLocalizedMessage());
        } catch (Exception e) {
            throw new APIException(ResultCodes.CrmServiceException,e.getLocalizedMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
