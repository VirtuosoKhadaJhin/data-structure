package com.nuanyou.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Felix on 2016/9/14.
 */

@Controller
@RequestMapping("index")
public class IndexController {

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String index() {

        return "index";
    }
}
