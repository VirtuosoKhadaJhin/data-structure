package com.nuanyou.cms.controller;

import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserManagerVo;
import com.nuanyou.cms.service.BdUserManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * bd宝用户管理
 * Created by sharp on 2017/6/22 - 14:29
 */

@Controller
@RequestMapping("bdUser")
public class BdUserManagerController {
    
    @Autowired
    private BdUserManagerService userManagerService;
    
    /**
     * 获取列表
     *
     * @return
     */
    @RequestMapping("list")
    public String list(BdUserManagerRequestVo requestVo, Model model) {
        Page<BdUserManagerVo> bdUserManagerVos = userManagerService.findAllBdUsers(requestVo);
        model.addAttribute("page", bdUserManagerVos);
        model.addAttribute("requestVo", requestVo);
        return "bdUser/list";
    }
    
    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping("edit")
    public String edit() {
    
        return "";
    }
    
    /**
     * 更新
     *
     * @return
     */
    @RequestMapping("update")
    public String update() {
    
        return "";
    }
}
