package com.nuanyou.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.contract.request.NotificationPreviewRequest;
import com.nuanyou.cms.service.HttpService;
import com.nuanyou.cms.util.Utils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("notificationPreview")
public class NotificationPreviewController {

    @Autowired
    private HttpService httpService;

    //地区
    @Value("${itemtuan.pushconfig}")
    private String pushConfigUrl;

    @Value("${nuanyou.domain}")
    private String userUrl;

    public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKrN7VkgMo02hYK7VuJ7KgBGQB8VBxW2KJI7eUk+95BhmcEDAZT5SKxrihjEHDerX+31TkrIddgl3KaHmaHHwAzVQV6fG3G4XXxG3EiqkKQySp4SVN5b9++nzit+ddofi4mXEt8MlkYzWV9nNp7guUQCXy9frp4KyfHMtsBS930ZAgMBAAECgYEAmMZGhNCKxso8kxlz9nHJuKMdWW/afW4ITfwKWRyMHMVf3EcPFCwA98/cnphS0Oxlipc+px80YNhEy2NAZHchbCFN25d0pSmC0WY1a70W7MYbkblAeLoLBNPNEw9OmYmog6lz+FUa0Oz1Oq9DwZRva5qqonLUKC1c+3yEZw/ioAECQQDmF+QB50ZE26/kn67LtOfKAuYMQ0lHFL0A8pszwJWbPNoKpaGgpnLA4zJiI78aUn1eGoG57Qp9h3EbdbUbFmkZAkEAvgkeWx5XAAVyko06bqY9EZ7gH7z2wF25YnRzXcW3E2BIuknDCQqQ7DstP8aHFdunnMlHVMA/rGJOIZ4r58g0AQJAE+OiyOtV7qPSy39mG6OymYqwmgTC88r+H3PZKJsQE5AqBNuWYg2hQ70f4M3YOg1BWv4NkqXDz2ACze3ZztKcGQJBAIk9CpghTBEu3fQqW/WGxnmQNCmXjNeVmAkbMimZXMJ4eW1XUauY3tpLTj1NgUbuz5gx3/q7sAAtKmGq2ehUtAECQQDC8npeJa1+vKaAcrYdcIEjEKnU9OubmwYGWpfEv1ljHwz6djVYaLAgN6TvkUdHGQsmmGV0+CanSBAZBkh2PDVR";

    /**
     * 消推页面
     *
     * @param 
     * @return
     */
    @RequestMapping("list")
    public String list() {
        return "notificationPreview/list";
    }

    /**
     * 获取消息推送后数据
     *
     * @param merchantId
     * @return
     */
    @ResponseBody
    @RequestMapping("notificationPreviewData")
    public APIResult notificationPreviewData(@RequestParam("merchantId") String merchantId) throws IOException {
        HttpGet get = new HttpGet(pushConfigUrl + "/push_configs?mchid=" + merchantId);
        String pushDetailResponse = httpService.doRequest(get);
        JSONObject pushDetail = JSONObject.parseObject(pushDetailResponse);
        JSONObject pushDetailList = JSONObject.parseObject(pushDetail.get("data").toString());
        return new APIResult<>(pushDetailList);
    }

    /**
     * 获取消息推送后数据
     * @param successRefundRequest
     * @return
     */
    @ResponseBody
    @RequestMapping("content")
    public APIResult generatedContent(@RequestBody(required = true) NotificationPreviewRequest[] successRefundRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String title = "";
        String imgUrl = "";
        String linkUrl = "";
        String contentPartTwo = "";
        int itemLength = successRefundRequest.length;
        for (NotificationPreviewRequest item : successRefundRequest) {
            title = item.getTitle();
            imgUrl = item.getImgUrl();
            linkUrl = item.getLinkUrl();
            contentPartTwo += "<item><Title><![CDATA[" + title + "]]></Title><PicUrl><![CDATA[" + imgUrl + "]]></PicUrl><Url><![CDATA[" + linkUrl + "]]></Url></item>";
        }
        String contentPartOne = " <MsgType><![CDATA[news]]></MsgType><ArticleCount>"+itemLength+"</ArticleCount>" + "<Articles>%s</Articles>";
        String content = String.format(contentPartOne, contentPartTwo);
        System.out.println(content);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sceneid", "PushMsgAfterConsumePreview"));
        params.add(new BasicNameValuePair("subscribereply", content));
        params.add(new BasicNameValuePair("scanreply", content));
        Utils.sign(params, PRIVATE_KEY);
        HttpPost post = new HttpPost(userUrl + "/s/nuanyou/saveScanReply/cms");
        post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
            return new APIResult<>();
        }
    }
}

