package com.nuanyou.cms.remote.sevenmoor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/12/15.
 */
@Component
public class Sevenmoor {

    @Value("${7moor.domain}")
    private String domain;
    @Value("${7moor.id}")
    private String id;
    @Value("${7moor.secret}")
    private String secret;
    @Value("${7moor.version}")
    private String version;

    private String buildAuthorization(Date now){
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return null;
    }

    public JSONObject addCustomer(String mchName) throws URISyntaxException {
        String url = domain + "/merchant";
        System.out.println("RemoteMerchantService url:"+url);
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);

        AddCustomers addCustomers = new AddCustomers();
        addCustomers.setVersion("");
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setName(mchName);
        customers.add(customer);
        addCustomers.setCustomers(customers);

        StringEntity StringEntity = new StringEntity(JSON.toJSONString(addCustomers), Charset.forName("utf-8"));
        post.addHeader(HTTP.CONTENT_TYPE,"application/json");
        post.addHeader(HTTP.CONTENT_TYPE,"application/json");
        StringEntity.setContentEncoding("UTF-8");
        StringEntity.setContentType("application/json");
        post.setEntity(StringEntity);
//        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
//        String responseText = EntityUtils.toString(response.getEntity());
//        System.out.println("RemoteMerchantService responseText:"+responseText);
//        ApiResult apiResult = JSON.parseObject(responseText,ApiResult.class);
//        if (apiResult.getCode() == null || apiResult.getCode() != 0) {
//            throw new APIException(ResultCodes.MerchantServiceException, apiResult.getCode()+"-"+apiResult.getMsg());
//        }
//        if (apiResult.getData() == null)
//            throw new APIException(ResultCodes.MerchantServiceException);
//        Integer mchId = (Integer)apiResult.getData();
//        return mchId.longValue();
        return null;
    }

    public static JSONObject updateCustomer(){
        return null;
    }
}
