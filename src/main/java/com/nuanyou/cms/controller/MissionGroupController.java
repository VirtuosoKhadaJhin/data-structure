package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.MissionGroupManagerVo;
import com.nuanyou.cms.service.MissionGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 战队， 组， 地推管理
 * Created by sharp on 2017/6/28 - 15:31
 */
@Controller
@RequestMapping("missionGroup")
public class MissionGroupController {
    
    @Autowired
    private MissionGroupService missionGroupService;
    
    /**
     * 获取列表
     *
     */
    @RequestMapping("list")
    public String list(MissionGroupManagerVo requestVo, Model model) {
        Page<MissionGroupManagerVo> vos = missionGroupService.findAllGroups(requestVo);
        model.addAttribute("vos", vos);
        return "missionGroup/list";
    }
    
    /**
     * 添加
     *
     */
    @RequestMapping("add")
    public String add(Model model) {
        List<BdCountry> countries = missionGroupService.findAllCountries();
        List<City> cities = missionGroupService.findAllCities();
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        
        return "missionGroup/add";
    }
    
    
    /**
     * 保存添加内容
     *
     * @return
     */
    @RequestMapping("saveAdd")
    public String saveAdd(Model model, String name, String countryId, String cityId, String isPublic, String desc) {
    
        MissionGroup group = new MissionGroup();
        
        //设置战队信息
        group.setName(name);
        // TODO: 2017/6/28 jap 将string 转化为long
        group.setCountryId(Long.valueOf(countryId));
        group.setCityId(Long.parseLong(cityId));
        group.setIsPublic(Byte.parseByte(isPublic));
        group.setDesc(desc);
        group.setDelFlag(Byte.decode("0")); //设置默认值
        
        //存储战队信息
        missionGroupService.saveGroup(group);
        
        //        BdUser user = new BdUser();
//        BdRelUserRole userRole = new BdRelUserRole();
//
//        //保存用户信息
//        user.setName(name);
//        user.setCountryId(countryId);
//        user.setEmail(email);
//        String pwd = MD5Utils.encrypt("12345");
//        user.setPwd(pwd);
//        userManagerService.saveUser(user);
//
//        //保存用户角色信息
//        userRole.setUser(user);
//        BdRole role = userManagerService.findRoleById(roleId);
//
//        userRole.setRole(role);
//
//        userManagerService.saveUserRole(userRole);
        
        return "forward:/missionGroup/list";
    }
    
    @RequestMapping("saveEdit")
    public String saveEdit(String id, String name, String countryId, String email, String roleId) {
    
//        BdUser user = userManagerService.findBdUserById(Long.parseLong(id));
//        user.setId(Long.parseLong(id));
//        user.setName(name);
//        user.setCountryId(Long.parseLong(countryId));
//        user.setEmail(email);
//        userManagerService.updateUser(user);
//
//        BdRelUserRole userRole = new BdRelUserRole();
//        BdRole role = userManagerService.findRoleById(Long.parseLong(roleId));
//        userRole.setUser(user);
//        userRole.setRole(role);
//        userManagerService.updateUserRole(userRole);
        
        return "forward:/missionGroup/list";
    }
    
    
    /**
     *
     */
    
    
    /**
     *
     */
    
}
