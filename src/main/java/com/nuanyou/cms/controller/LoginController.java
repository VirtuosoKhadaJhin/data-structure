package com.nuanyou.cms.controller;

import com.nuanyou.cms.sso.client.validation.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("")
public class LoginController {
    public static final String SSO_USER = "sso_user";


    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        if (request.getSession()!=null) {
            request.getSession().removeAttribute(SSO_USER);
            request.getSession().invalidate();
            User user = request.getSession() != null ? (User) request.getSession().getAttribute(SSO_USER) : null;
            System.out.println(user);
        }
        return "forward:/index?relogin=true";
    }

}