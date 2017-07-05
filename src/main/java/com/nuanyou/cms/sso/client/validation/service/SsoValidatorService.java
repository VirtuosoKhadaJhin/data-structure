package com.nuanyou.cms.sso.client.validation.service;

import com.nuanyou.cms.sso.client.validation.exception.TicketValidationException;
import com.nuanyou.cms.sso.client.validation.vo.User;

import java.net.URL;

/**
 * Created by Felix on 2017/6/21.
 */
public interface SsoValidatorService {

    String retrieveResponseFromServer(URL validationUrl);

    User parseResponseFromServer(final String response) throws TicketValidationException;

    String constructValidationUrl(final String ticket, final String validateCodeUrl, boolean needAutoLogOut, String serverName);

    User validate(String ticket, String serverName, String validateCodeUrl, Boolean needAutoLogOut) throws TicketValidationException;

}
