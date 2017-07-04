package com.nuanyou.cms.sso.client.validation.service.impl;

import com.alibaba.fastjson.JSON;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.sso.client.validation.service.SsoValidatorService;
import com.nuanyou.cms.sso.client.validation.vo.User;
import com.nuanyou.cms.sso.client.validation.exception.TicketValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felix on 2017/6/21.
 */

public class SsoValidatorServiceImpl implements SsoValidatorService {

    protected final Log log = LogFactory.getLog(getClass());

    private final String validateCodeUrl;

    private  Boolean needAutoLogOut;


    private String encoding;

    protected SsoValidatorServiceImpl(final String validateCodeUrl, Boolean needAutoLogOut) {
        this.validateCodeUrl = validateCodeUrl;
        this.needAutoLogOut=needAutoLogOut;
        CommonUtils.assertNotNull(this.validateCodeUrl, "validateCodeUrl cannot be null.");
    }



    public String getValidateCodeUrl() {
        return validateCodeUrl;
    }


    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }



    public User validate(final String ticket, String serverName) throws TicketValidationException {
        //发送Http请求
        final String validationUrl =this.constructValidationUrl(ticket,validateCodeUrl,needAutoLogOut,serverName);
        if (log.isDebugEnabled()) {
            log.debug("Constructing validation url: " + validationUrl);
        }
        try {
            log.debug("Retrieving response from server.");
            final String serverResponse= CommonUtils.getResponseFromServer(new URL(validationUrl), null);
            if (serverResponse == null) {
                throw new TicketValidationException("The SSO server returned no response.");
            }
            log.debug("Server response: " + serverResponse);
            return this.parseResponseFromServer(serverResponse);
        } catch (final MalformedURLException e) {
            throw new TicketValidationException(e);
        }
    }

    @Override
    public String retrieveResponseFromServer(URL validationUrl, String ticket) {
        return CommonUtils.getResponseFromServer(validationUrl, null);
    }

    @Override
    public final User parseResponseFromServer(final String response) throws TicketValidationException {
        System.out.println("Successfully retrive user info: "+response);
        Integer code= JSON.parseObject(response).getInteger("code");
        User user=null;
        if(code!=null&&code==0){
            user= JSON.parseObject(JSON.parseObject(response).get("data").toString(),User.class);
            if(user==null){
                throw new TicketValidationException("No user was found in the response from the SSO server.");
            }
        }else{
            throw new TicketValidationException("No user was found in the response from the SSO server.");
        }
        return user;

    }

    private String logoutParameterName = "logoutRequest";

    /**
     *
     * 组装准备发送验证ticket请求的URL
     */
    @Override
    public final String constructValidationUrl(final String ticket, final String validateCodeUrl, boolean needAutoLogOut, String serverName) {
        final Map<String,String> urlParameters = new HashMap<String,String>();
        urlParameters.put("code", ticket);
        if(needAutoLogOut){
            urlParameters.put("callback",encodeUrl(serverName+"?logoutRequest="+ticket) );
        }
        final StringBuilder buffer = new StringBuilder();
        buffer.append(validateCodeUrl);
        int i = 0;
        for (Map.Entry<String,String> entry : urlParameters.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            if (value != null) {
                buffer.append(i++ == 0 ? "?" : "&");
                buffer.append(key);
                buffer.append("=");
                buffer.append(value);
            }
        }

        return buffer.toString();

    }


    protected final String encodeUrl(final String url) {
        if (url == null) {
            return null;
        }

        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return url;
        }
    }
}
