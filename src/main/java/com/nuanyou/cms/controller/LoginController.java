package com.nuanyou.cms.controller;

import com.nuanyou.cms.sso.client.session.SessionMappingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("")
public class LoginController {
    @Autowired
    private SessionMappingStorage sessionMappingStorage;

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) throws IOException{
        if (request.getSession()!=null) {
            request.getSession().invalidate();
        }
        return "redirect:/index?relogin=true";
    }
}