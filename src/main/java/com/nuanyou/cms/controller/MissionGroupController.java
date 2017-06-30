package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.model.MissionGroupRequestVo;
import com.nuanyou.cms.model.MissionGroupVo;
import com.nuanyou.cms.service.BdUserManagerService;
import com.nuanyou.cms.service.MissionGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private BdUserManagerService userManagerService;

    /**
     * 获取列表
     */
    @RequestMapping("list")
    public String list(MissionGroupVo requestVo, Model model) {
        Page<MissionGroupVo> vos = missionGroupService.findAllGroups(requestVo);
        List<Country> countries = missionGroupService.findAllCountries();
        List<City> cities = missionGroupService.findAllCities();
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("page", vos);//page统一命名，分页
        model.addAttribute("requestVo", requestVo);//刷新界面

        return "missionGroup/list";
    }

    /**
     * 查询组员列表
     *
     * @param groupId
     * @param model
     * @return
     */
    @RequestMapping("member")
    public String member(Long groupId, Model model) {
        List<BdUser> list = userManagerService.findByGroupId(groupId);
        model.addAttribute("list", list);
        return "missionGroup/member";
    }

    /**
     * 添加
     */
    @RequestMapping("add")
    public String add(Model model) {
        List<Country> countries = missionGroupService.findAllCountries();
        List<City> cities = missionGroupService.findAllCities();
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);

        return "missionGroup/add";
    }

    @RequestMapping("edit")
    public String edit(Model model, Long id) {
        MissionGroup group = missionGroupService.findGroupById(id);
        List<Country> countries = missionGroupService.findAllCountries();
        List<City> cities = missionGroupService.findAllCities();

        model.addAttribute("group", group);
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);

        return "missionGroup/edit";
    }

    /**
     * 删除战队
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public APIResult<Boolean> del(@RequestBody MissionGroupVo requestVo) {
        APIResult<Boolean> result = new APIResult<Boolean>();
        Boolean res = missionGroupService.delGroupById(requestVo.getId());
        result.setData(res);
        return result;
    }


    /**
     * 添加bdUser
     */
    @RequestMapping("addBdUser")
    public String addBdUser(Model model, String groupId) {
        //所有的bduser
        List<BdUser> bdUsers = userManagerService.findAllBdUsers();

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

        return "redirect:/missionGroup/list";
    }


    /**
     * 保存编辑
     *
     * @return
     */
    @RequestMapping("saveEdit")
    public String saveEdit(Model model, String id, MissionGroupParamVo paramVo) {

        missionGroupService.updateGroup(id, paramVo);
        return "redirect:/missionGroup/list";
    }

    /**
     * 查询Bd
     *
     * @return
     */
    @RequestMapping("findBdUserByCountryId")
    @ResponseBody
    public APIResult<List<BdUser>> findBdUserByCountryId(@RequestBody MissionGroupVo requestVo) {
        APIResult<List<BdUser>> result = new APIResult<List<BdUser>>();
        List<BdUser> bdUsers = missionGroupService.findBdUsersByCountryId(requestVo.getCountryId());
        result.setData(bdUsers);
        return result;
    }

    /**
     * 查询组已有的BD
     *
     * @return
     */
    @RequestMapping("findBdUserByGroupId")
    @ResponseBody
    public APIResult<List<Long>> findBdUserByGroupId(@RequestBody MissionGroupVo requestVo) {
        APIResult<List<Long>> result = new APIResult<List<Long>>();
        List<Long> list = missionGroupService.findBdUserByGroupId(requestVo.getGroupId());
        result.setData(list);
        return result;
    }

    /**
     * 保存Bd
     *
     * @return
     */
    @RequestMapping("saveGroupBds")
    @ResponseBody
    public APIResult<Boolean> saveGroupBds(@RequestBody MissionGroupRequestVo vo) {
        APIResult<Boolean> result = new APIResult<>(ResultCodes.Success);
        missionGroupService.saveGroupBds(vo.getGroupId(), vo.getUserIds());
        return result;
    }

    //    /**
    //     * 保存Bd
    //     *
    //     * @return
    //     */
    //    @RequestMapping("saveGroupBds")
    //    @ResponseBody
    //    public APIResult<Boolean> saveGroupBds(@RequestBody MissionGroupVo vo) {
    //        //[{"groupId":1,"bdId":5},{"groupId":1,"bdId":5}]
    //        APIResult<Boolean> result = new APIResult<>();
    //        Boolean saveResult = missionGroupService.saveGroupBds(vos);
    //        result.setData(saveResult);
    //        return result;
    //    }


    //    @RequestMapping("saveGroupBds")
    //    @ResponseBody
    //    public APIResult saveGroupBds(@RequestBody List<GroupBdParamVo> vos) {
    //        missionGroupService.saveGroupBds(vos);
    //        return new APIResult();
    //    }
}
