package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserParamVo;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.service.BdUserManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        Page<BdUserVo> vos = userManagerService.findAllBdUserVos(requestVo);
        model.addAttribute("page", vos);
        model.addAttribute("requestVo", requestVo);//刷新界面
        return "bdUser/list";
    }
    
    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping("edit")
    public String edit(Long id, Model model) {
        BdUserVo vo = userManagerService.findUserById(id);
        List<BdRole> roles = userManagerService.findAllRoles();
        List<BdCountry> countries = userManagerService.findAllCountry();
    
        model.addAttribute("countries", countries);
        model.addAttribute("roles", roles);
        model.addAttribute("vo", vo);
        
        return "bdUser/edit";
    }
    
    @RequestMapping("add")
    public String add(Model model) {
        List<BdRole> roles = userManagerService.findAllRoles();
        List<BdCountry> countries = userManagerService.findAllCountry();
        model.addAttribute("roles", roles);
        model.addAttribute("countries", countries);
        return "bdUser/add";
    }
    
    @RequestMapping("del")
    public String del(Long id) {
        userManagerService.del(id);
        return "redirect:/bdUser/list";
    }
    
    /**
     * 保存添加内容
     *
     * @return
     */
    @RequestMapping("saveAdd")
    public String saveAdd(BdUserParamVo paramVo) {
    
        userManagerService.saveAddUserAndRole(paramVo);
        
        return "redirect:/bdUser/list";
    }
    
    
    @RequestMapping("saveEdit")
    public String saveEdit(BdUserParamVo paramVo) {
        
        userManagerService.saveEditUserAndRole(paramVo);
        
        return "redirect:/bdUser/list";
    }
    
}
