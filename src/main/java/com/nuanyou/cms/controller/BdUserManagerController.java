package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRelUserRole;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserParamVo;
import com.nuanyou.cms.model.BdUserVo;
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
        Page<BdUserVo> vos = userManagerService.findAllBdUserVos(requestVo);
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
    
    /**
     * 保存添加内容
     *
     * @return
     */
    @RequestMapping("saveAdd")
    public String saveAdd(Model model, BdUserParamVo paramVo) {
    
        BdUser user = new BdUser();
        BdRelUserRole userRole = new BdRelUserRole();
    
        //保存用户信息
        user.setName(paramVo.getName());
        user.setChineseName(paramVo.getChineseName());
        user.setCountryId(paramVo.getCountryId());
        user.setEmail(paramVo.getEmail());
        user.setDmail(paramVo.getDmail());
    
        //设置默认密码
        String pwd = MD5Utils.encrypt("123456");
        user.setPwd(pwd);
    
        //设置默认显示
        user.setDeleted(Byte.valueOf("0"));
    
        //保存用户角色信息
        userRole.setUser(user);
        BdRole role = userManagerService.findRoleById(paramVo.getRoleId());
    
        userRole.setRole(role);
    
        userManagerService.saveUser(user);
        userManagerService.saveUserRole(userRole);
        
        return "forward:/bdUser/list";
    }
    
    @RequestMapping("saveEdit")
    public String saveEdit(BdUserParamVo paramVo) {
        //获取数据
        BdUser user = userManagerService.findBdUserById(paramVo.getId());
        BdRelUserRole userRole = new BdRelUserRole();
        BdRole role = userManagerService.findRoleById(paramVo.getRoleId());
    
        //设置数据
        user.setName(paramVo.getName());
        user.setChineseName(paramVo.getChineseName());
        user.setCountryId(paramVo.getCountryId());
        user.setEmail(paramVo.getEmail());
        user.setDmail(paramVo.getDmail());
    
        //保存用户角色信息
        userRole.setUser(user);
        userRole.setRole(role);
        
        //保存数据
        userManagerService.updateUser(user);
        userManagerService.updateUserRole(userRole);
        
        return "forward:/bdUser/list";
    }
}
