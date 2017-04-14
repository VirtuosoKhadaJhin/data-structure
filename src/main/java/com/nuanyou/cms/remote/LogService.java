package com.nuanyou.cms.remote;

import com.google.common.collect.ImmutableMap;
import com.nuanyou.cms.model.LogVO;
import com.nuanyou.cms.model.es.ESResult;
import com.nuanyou.cms.util.JsonUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Created by alan on 17/3/16.
 */
@Service
public class LogService {

    @Autowired
    private RestClient restClient;

    @Value("${elasticsearch.index}")
    private String index;

    public ESResult find(LogVO vo, TimeCondition time, int pageNo, int pageSize) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        if (vo.getUserId() != null)
            query.must(QueryBuilders.matchQuery("user_id", vo.getUserId()));

        if (StringUtils.isNotBlank(vo.getUserName()))
            query.must(QueryBuilders.matchQuery("user_name", vo.getUserName()));

        if (StringUtils.isNotBlank(vo.getUri()))
            query.must(QueryBuilders.matchQuery("operate_path", vo.getUri()));

        if (vo.getAction() != null)
            query.must(QueryBuilders.matchQuery("action", vo.getAction()));

        if (StringUtils.isNotBlank(vo.getTargetClass()))
            query.must(QueryBuilders.matchQuery("class", vo.getTargetClass()));

        if (time.getBegin() != null)
            query.must(QueryBuilders.rangeQuery("@timestamp").gt(time.getBegin()));

        if (time.getEnd() != null)
            query.must(QueryBuilders.rangeQuery("@timestamp").lt(time.getEnd()));

        StringBuilder q = new StringBuilder();
        q.append("{\"query\":").append(query);
        q.append(",\"sort\":{\"@timestamp\":\"desc\"}");
        q.append("}");

        HttpEntity entity = new NStringEntity(q.toString(), ContentType.APPLICATION_JSON);
        ImmutableMap<String, String> params = ImmutableMap.of("from", String.valueOf(pageNo * pageSize), "size", String.valueOf(pageSize), "pretty", "true");

        ESResult esResult = null;
        try {
            Response response = restClient.performRequest(RequestMethod.POST.toString(), "/" + index + "/_search", params, entity);
            String result = EntityUtils.toString(response.getEntity());
            esResult = JsonUtils.toObj(result, ESResult.class);
            esResult.setNumber(pageNo);
            esResult.setPageSize(pageSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return esResult;
    }

}