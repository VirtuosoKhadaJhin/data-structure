package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRelUserRole;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserManagerVo;
import com.nuanyou.cms.service.BdUserManagerService;
import com.nuanyou.cms.util.MD5Utils;

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
        Page<BdUserManagerVo> vos = userManagerService.findAllBdUserManagerVos(requestVo);
        model.addAttribute("vos", vos);
        return "bdUser/list";
    }
    
    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping("edit")
    public String edit(Long id, Model model) {
        BdUserManagerVo vo = userManagerService.findUserById(id);
        List<BdRole> roles = userManagerService.findAllRole();
        List<BdCountry> countries = userManagerService.findAllCountry();
    
        model.addAttribute("countries", countries);
        model.addAttribute("roles", roles);
        model.addAttribute("vo", vo);
        
        return "bdUser/edit";
    }
    
    @RequestMapping("add")
    public String add(Model model) {
        List<BdRole> roles = userManagerService.findAllRole();
        List<BdCountry> countries = userManagerService.findAllCountry();
        model.addAttribute("roles", roles);
        model.addAttribute("countries", countries);
        return "bdUser/add";
    }
    
    /**
     * 保存添加内容
     *
     * @return
     */
    @RequestMapping("saveAdd")
    public String saveAdd(Model model, String name, Long countryId, String email, Long roleId) {
    
        BdUser user = new BdUser();
        BdRelUserRole userRole = new BdRelUserRole();
    
        //保存用户信息
        user.setName(name);
        user.setCountryId(countryId);
        user.setEmail(email);
        String pwd = MD5Utils.encrypt("12345");
        user.setPwd(pwd);
        userManagerService.saveUser(user);
    
        //保存用户角色信息
        userRole.setUser(user);
        BdRole role = userManagerService.findRoleById(roleId);
        
        userRole.setRole(role);
        
        userManagerService.saveUserRole(userRole);
        
        return "forward:/bdUser/list";
    }
    
    @RequestMapping("saveEdit")
    public String saveEdit(String id, String name, String countryId, String email, String roleId) {
        
        BdUser user = userManagerService.findBdUserById(Long.parseLong(id));
        user.setId(Long.parseLong(id));
        user.setName(name);
        user.setCountryId(Long.parseLong(countryId));
        user.setEmail(email);
        userManagerService.updateUser(user);
        
        BdRelUserRole userRole = new BdRelUserRole();
        BdRole role = userManagerService.findRoleById(Long.parseLong(roleId));
        userRole.setUser(user);
        userRole.setRole(role);
        userManagerService.updateUserRole(userRole);
        
        return "forward:/bdUser/list";
    }
}
