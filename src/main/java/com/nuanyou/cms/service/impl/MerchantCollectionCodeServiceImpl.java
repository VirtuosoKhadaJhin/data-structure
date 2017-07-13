package com.nuanyou.cms.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.nuanyou.cms.commons.MD5Utils;
import com.nuanyou.cms.dao.EntityBdMerchantCollectionCodeDao;
import com.nuanyou.cms.entity.EntityBdMerchantCollectionCode;
import com.nuanyou.cms.service.MerchantCollectionCodeService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by young on 2017/6/28.
 */
@Service
public class MerchantCollectionCodeServiceImpl implements MerchantCollectionCodeService {

    public static final String APPID = "nuanyou";
    public static final String SECRET = "91nuanyou";

    @Autowired
    private EntityBdMerchantCollectionCodeDao entityBdMerchantCollectionCodeDao;
    @Value("${numberbind.domain}")
    private String domain;

    @Override
    public Long findMchIdByCode (String code) {
        Long mchId = null;
        EntityBdMerchantCollectionCode entityBdMerchantCollectionCode = entityBdMerchantCollectionCodeDao.findByCollectionCode(code);
        if (entityBdMerchantCollectionCode !=null) {
            mchId = entityBdMerchantCollectionCode.getMchId();
        }
        return mchId;
    }

    @Override
    public Boolean hasCollectionCode (String code) {
        EntityBdMerchantCollectionCode entityBdMerchantCollectionCode = entityBdMerchantCollectionCodeDao.findByCollectionCode(code);
        if (entityBdMerchantCollectionCode == null) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public EntityBdMerchantCollectionCode findCollectionCode (String code) {
        EntityBdMerchantCollectionCode entityBdMerchantCollectionCode = entityBdMerchantCollectionCodeDao.findByCollectionCode(code);
        return entityBdMerchantCollectionCode;
    }

    @Override
    public void saveEntityBdMerchantCollectionCode (EntityBdMerchantCollectionCode entityBdMerchantCollectionCode) {
        entityBdMerchantCollectionCodeDao.save(entityBdMerchantCollectionCode);
    }

    @Override
    public List<EntityBdMerchantCollectionCode> findEntityBdMerchantCollectionCodesByMchId(Long mchId) {
        return entityBdMerchantCollectionCodeDao.findByMchId(mchId);
    }

    @Override
    @Async
    public boolean bindNumberLink(Long number, String target) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("number", String.valueOf(number)));
        nameValuePairs.add(new BasicNameValuePair("url", target));
        nameValuePairs.add(new BasicNameValuePair("appid", APPID));
        nameValuePairs.add(new BasicNameValuePair("sign", sign(nameValuePairs, SECRET)));

        HttpPost post = new HttpPost(domain + "number/bind");
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));

        CloseableHttpClient build = HttpClientBuilder.create().build();

        try (CloseableHttpResponse response = build.execute(post)) {
            JSONObject object = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

            if (object.containsKey("status")) {
                int status = object.getInteger("status");
                if (status == 0) {
                    return true;
                } else {
                    throw new Exception(object.getString("msg"));
                }
            }
        }
        return false;
    }

    @Override
    @Async
    public boolean unbindNumberLink(Long number) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("number", String.valueOf(number)));
        nameValuePairs.add(new BasicNameValuePair("appid", APPID));
        nameValuePairs.add(new BasicNameValuePair("sign", sign(nameValuePairs, SECRET)));

        HttpPost post = new HttpPost(domain + "number/unbind");
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));

        CloseableHttpClient build = HttpClientBuilder.create().build();

        try (CloseableHttpResponse response = build.execute(post)) {
            JSONObject object = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

            if (object.containsKey("status")) {
                int status = object.getInteger("status");
                if (status == 0) {
                    return true;
                } else {
                    throw new Exception(object.getString("msg"));
                }
            }
        }
        return false;
    }

    private String sign(final List<NameValuePair> nameValuePairs, String secret) {
        Collections.sort(nameValuePairs,new Comparator<NameValuePair>(){
            @Override
            public int compare(NameValuePair b1, NameValuePair b2) {
                return b1.getName().compareTo(b2.getName());
            }
        });
//        nameValuePairs.sort(Comparator.comparing(NameValuePair::getName));
        List<String> s = new ArrayList<>();
        for (NameValuePair nameValuePair : nameValuePairs) {
            s.add(nameValuePair.getName() + "=" + nameValuePair.getValue());
        }
        s.add("key=" + secret);
        String toSign = StringUtils.join(s, "&");
        String sign = MD5Utils.MD5(toSign);
        System.out.println(toSign + "->" + sign);
        return sign;
    }

    @Override
    public Page<EntityBdMerchantCollectionCode> query (final Long mchId, final String collectionCode, Pageable pageable) {
        Page<EntityBdMerchantCollectionCode> result = entityBdMerchantCollectionCodeDao.findAll(new Specification(){
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (mchId != null) {
                    predicate.add(cb.equal(root.get("mchId"), mchId));
                }
                if (collectionCode != null) {
                    predicate.add(cb.equal(root.get("collectionCode"),collectionCode));
                }

                return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
            }
        },pageable);
        return result;
    }

}
