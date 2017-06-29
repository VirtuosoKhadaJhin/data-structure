package com.nuanyou.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("")
public class LoginController {
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) throws IOException{
        if (request.getSession()!=null) {
            request.getSession().invalidate();
        }
        return "forward:/index?relogin=true";
    }
}