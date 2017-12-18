package com.nuanyou.cms.remote.sevenmoor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.util.MD5Utils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/12/15.
 */
@Component
public class SevenmoorService {

    @Value("${7moor.domain}")
    private String domain;
    @Value("${7moor.id}")
    private String id;
    @Value("${7moor.secret}")
    private String secret;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private String buildAuthorization(Date now) throws UnsupportedEncodingException {
        byte[] bytes = (id + ":" +sdf.format(now)).getBytes("UTF-8");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(bytes);
    }

    private String sign(Date now){
        String text = id + secret + sdf.format(now);
        return MD5Utils.encrypt(text).toUpperCase();
    }

    public String getVersion() throws URISyntaxException, IOException {
        Date now = new Date();
        String url = domain + "/v20170418/customer/getTemplate/"+id+"?sig="+sign(now);
        System.out.println("RemoteMerchantService getVersion url:"+url);
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);


        post.addHeader(HTTP.CONTENT_TYPE,"application/json");
        post.addHeader("Authorization",buildAuthorization(now));
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
        String responseText = EntityUtils.toString(response.getEntity());
        System.out.println("RemoteMerchantService getVersion responseText:"+responseText);
        JSONObject jsonObject = JSON.parseObject(responseText);
        if (jsonObject.get("code") != 200) {
            throw new APIException(ResultCodes.SevenmoorServiceException, jsonObject.get("message"));
        }
        return jsonObject.getJSONObject("data").getString("version");
    }

    public void addCustomer(String id,String mchName) throws URISyntaxException, IOException {
        Date now = new Date();
        String url = domain + "/v20170418/customer/insert/"+id+"?sig="+sign(now);
        System.out.println("RemoteMerchantService addCustomer url:"+url);
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);

        String v = getVersion();
        System.out.println(v);

        AddCustomers addCustomers = new AddCustomers();
        addCustomers.setVersion("1");
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(mchName);
        customer.setStatus("status0");
        customers.add(customer);
        addCustomers.setCustomers(customers);

        StringEntity StringEntity = new StringEntity(JSON.toJSONString(addCustomers), Charset.forName("utf-8"));
        post.addHeader(HTTP.CONTENT_TYPE,"application/json");
        post.addHeader("Authorization",buildAuthorization(now));
        StringEntity.setContentEncoding("UTF-8");
        StringEntity.setContentType("application/json");
        post.setEntity(StringEntity);
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
        String responseText = EntityUtils.toString(response.getEntity());
        System.out.println("RemoteMerchantService addCustomer responseText:"+responseText);
        JSONObject jsonObject = JSON.parseObject(responseText);
        if (jsonObject.get("code") != 200) {
            throw new APIException(ResultCodes.SevenmoorServiceException, jsonObject.get("message"));
        }
    }

    public static JSONObject updateCustomer(){
        return null;
    }
}
