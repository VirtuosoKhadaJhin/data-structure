package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.model.MissionGroupManagerVo;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.service.BdUserManagerService;
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
    
    @Autowired
    private BdUserManagerService bdUserManagerService;
    
    /**
     * 获取列表
     */
    @RequestMapping("list")
    public String list(MissionGroupManagerVo requestVo, Model model) {
        Page<MissionGroupManagerVo> vos = missionGroupService.findAllGroups(requestVo);
        model.addAttribute("vos", vos);
        return "missionGroup/list";
    }
    
    /**
     * 添加
     */
    @RequestMapping("add")
    public String add(Model model) {
        List<BdCountry> countries = missionGroupService.findAllCountries();
        List<City> cities = missionGroupService.findAllCities();
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        
        return "missionGroup/add";
    }
    
    @RequestMapping("edit")
    public String edit(Model model, String id) {
    
        return "missionGroup/edit";
    }
    
    /**
     * 添加bdUser
     */
    @RequestMapping("addBdUser")
    public String addBdUser(Model model, String groupId) {
        //所有的bduser
        List<BdUser> bdUsers = bdUserManagerService.findAllBdUsers();
        
        //该组的bduser
        List<BdUser> bdUsersByGroupId = missionGroupService.findBdUsersByGroupId(Long.valueOf(groupId));
        
        model.addAttribute("bdUsers", bdUsers);
        
        return "missionGroup/addBdUser";
    }
    
    /**
     * 保存添加内容
     *
     * @return
     */
    @RequestMapping("saveAdd")
    public String saveAdd(Model model, MissionGroupParamVo paramVo) {
        missionGroupService.saveGroup(paramVo);
        return "missionGroup/list";
    }
    
    
    /**
     * 保存编辑
     * @return
     */
    @RequestMapping("saveEdit")
    public String saveEdit(String id, String name, String countryId, String email, String roleId) {
        
        
        return "forward:/missionGroup/list";
    }
    
    
    /**
     * 保存添加组员
     */
    @RequestMapping("saveAddBdUser")
    public String saveAddBdUser() {
    
        return "forward:/missionGroup/list";
    }
    
}
