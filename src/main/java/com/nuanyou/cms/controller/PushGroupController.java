package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.PushGroupListVo;
import com.nuanyou.cms.service.PushGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yangkai on 2017/2/15.
 */
@Controller
@RequestMapping("pushGroup")
public class PushGroupController {
    @Autowired
    private PushGroupService pushGroupService;

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Page<PushGroupListVo> pushGroupVoPage = this.pushGroupService.list(index);
        model.addAttribute("page", pushGroupVoPage);
        return "pushGroup/list";
    }


    @RequestMapping("changeStatus")
    @ResponseBody
    public APIResult changeStatus(Long id, Boolean status, Model model) {
        this.pushGroupService.changeStatus(id, status);
        return new APIResult<>(ResultCodes.Success);
    }
}
