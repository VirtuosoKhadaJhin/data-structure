package com.nuanyou.cms.remote.sevenmoor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
    @Value("${7moor.version}")
    private String version;

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
        if (jsonObject.getInteger("code") != 200) {
            throw new APIException(ResultCodes.SevenmoorServiceException, jsonObject.get("message"));
        }
        return jsonObject.getJSONObject("data").getString("version");
    }

    public void addCustomer(String mchId,String mchName,List<String> tels) throws URISyntaxException, IOException {

        List<Customer> customerList = queryCustomer(tels);
        if (customerList == null) {

            Date now = new Date();
            String url = domain + "/v20170418/customer/insert/" + id + "?sig=" + sign(now);
            System.out.println("RemoteMerchantService addCustomer url:" + url);
            URI uri = new URI(url);
            HttpPost post = new HttpPost(uri);

            AddCustomers addCustomers = new AddCustomers();
            addCustomers.setVersion(version);
            List<Customer> customers = new ArrayList<>();
            Customer customer = new Customer();
            customer.set_id(mchId);
            customer.setName(mchName);
            customer.setStatus("status0");
            List<Tel> phone = new ArrayList<>();
            for (String telephone : tels) {
                Tel tel = new Tel();
                tel.setTel(telephone);
                phone.add(tel);
            }
            customer.setPhone(phone);
            customers.add(customer);
            addCustomers.setCustomers(customers);

            StringEntity StringEntity = new StringEntity(JSON.toJSONString(addCustomers), Charset.forName("utf-8"));
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");
            post.addHeader("Authorization", buildAuthorization(now));
            StringEntity.setContentEncoding("UTF-8");
            StringEntity.setContentType("application/json");
            post.setEntity(StringEntity);
            CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
            String responseText = EntityUtils.toString(response.getEntity());
            System.out.println("RemoteMerchantService addCustomer responseText:" + responseText);
            JSONObject jsonObject = JSON.parseObject(responseText);
            if (jsonObject.getInteger("code") != 200 && jsonObject.getInteger("code") != 210) {
                throw new APIException(ResultCodes.SevenmoorServiceException, jsonObject.get("message"));
            }
//        if (jsonObject.getInteger("code") == 210) {
////            updateCustomer(mchId, mchName);
//        }
        } else {
            if (customerList.size() > 0) {
                updateCustomer(customerList.get(0).get_id(), mchName);
            }
        }
    }

    public void updateCustomer(String customerId,String mchName) throws URISyntaxException, IOException {
        Date now = new Date();
        String url = domain + "/v20170418/customer/update/"+id+"?sig="+sign(now);
        System.out.println("RemoteMerchantService updateCustomer url:"+url);
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);

        UpdateCustomer updateCustomer = new UpdateCustomer();
        Customer customer = new Customer();
        customer.set_id(customerId);
        customer.setName(mchName);
        customer.setVersion(version);
        customer.setStatus("status0");
//        List<Tel> phone = new ArrayList<>();
//        for (String telephone : tels) {
//            Tel tel = new Tel();
//            tel.setTel(telephone);
//            phone.add(tel);
//        }
//        customer.setPhone(phone);
        updateCustomer.setCustomer(customer);

        JSONObject object = (JSONObject)JSONObject.toJSON(updateCustomer);
        object.getJSONObject("customer").remove("id");
        object.getJSONObject("customer").put("_id",updateCustomer.getCustomer().get_id());
        StringEntity StringEntity = new StringEntity(object.toJSONString(), Charset.forName("utf-8"));
        post.addHeader(HTTP.CONTENT_TYPE,"application/json");
        post.addHeader("Authorization",buildAuthorization(now));
        StringEntity.setContentEncoding("UTF-8");
        StringEntity.setContentType("application/json");
        post.setEntity(StringEntity);
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
        String responseText = EntityUtils.toString(response.getEntity());
        System.out.println("RemoteMerchantService updateCustomer responseText:"+responseText);

    }

    public List<Customer> queryCustomer(List<String> tels) throws URISyntaxException, IOException {
        Date now = new Date();
        String url = domain + "/v20170418/customer/select/"+id+"?sig="+sign(now);
        System.out.println("RemoteMerchantService queryCustomer url:"+url);
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);

        QueryCustomer query = new QueryCustomer();
        query.setVersion(version);
        if (tels != null && tels.size() > 0) {
            query.setPhone(tels.get(0));
        }

        StringEntity StringEntity = new StringEntity(JSON.toJSONString(query), Charset.forName("utf-8"));
        post.addHeader(HTTP.CONTENT_TYPE,"application/json");
        post.addHeader("Authorization",buildAuthorization(now));
        StringEntity.setContentEncoding("UTF-8");
        StringEntity.setContentType("application/json");
        post.setEntity(StringEntity);
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post);
        String responseText = EntityUtils.toString(response.getEntity());
        System.out.println("RemoteMerchantService queryCustomer responseText:"+responseText);
        JSONObject jsonObject = JSON.parseObject(responseText);
        if (jsonObject.getInteger("code") != 200 && jsonObject.getInteger("code") != 210) {
            throw new APIException(ResultCodes.SevenmoorServiceException, jsonObject.get("message"));
        }
        if (jsonObject.getJSONObject("data") != null) {
            if (jsonObject.getJSONObject("data").getInteger("count") > 0) {
                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("customers");
                List<Customer> customers = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Customer customer = JSON.parseObject(json.toJSONString(), Customer.class);
                    customer.set_id(json.getString("_id"));
                    customers.add(customer);
                }
                return customers;
            }else  return null;
        }
        return null;
    }
}
