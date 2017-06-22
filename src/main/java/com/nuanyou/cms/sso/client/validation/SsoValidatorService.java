package com.nuanyou.cms.sso.client.validation;

import com.nuanyou.cms.sso.client.validation.impl.TicketValidationException;

import java.net.URL;

/**
 * Created by Felix on 2017/6/21.
 */
public interface SsoValidatorService {


    /**
     * sso服务器验证ticket后得到response
     * @param validationUrl
     * @param ticket
     * @return
     */
    public abstract String retrieveResponseFromServer(URL validationUrl, String ticket);

    public abstract User parseResponseFromServer(final String response) throws TicketValidationException;

    public  String constructValidationUrl(final String ticket, final String validateCodeUrl);

    User validate(String ticket) throws TicketValidationException;
}
