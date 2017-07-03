package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.model.MissionGroupRequestVo;
import com.nuanyou.cms.model.MissionGroupVo;
import com.nuanyou.cms.service.BdUserService;
import com.nuanyou.cms.service.CityService;
import com.nuanyou.cms.service.CountryService;
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
    private BdUserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    /**
     * 获取列表
     */
    @RequestMapping("list")
    public String list(MissionGroupVo requestVo, Model model) {
        Page<MissionGroupVo> vos = missionGroupService.findAllGroups(requestVo);
        List<Country> countries = countryService.findAllCountries();
        List<City> cities = cityService.findAllCities();
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
        MissionGroup group = missionGroupService.findGroupById(groupId);
        List<BdUser> list = userService.findByGroupId(groupId);
        model.addAttribute("group", group);
        model.addAttribute("list", list);
        return "missionGroup/member";
    }

    /**
     * 查询已有的组员(指派队长)
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("queryGroupBdUsers")
    @ResponseBody
    public APIResult<List<BdUserVo>> queryGroupBdUsers(@RequestBody MissionGroupVo requestVo) {
        APIResult<List<BdUserVo>> result = new APIResult<List<BdUserVo>>();
        List<BdUserVo> res = missionGroupService.members(requestVo.getGroupId(), requestVo.getCountryId());
        result.setData(res);
        return result;
    }

    /**
     * 指派队长
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("distributeLeader")
    @ResponseBody
    public APIResult<Boolean> distributeLeader(@RequestBody MissionGroupVo requestVo) {
        APIResult<Boolean> result = new APIResult<Boolean>();
        Boolean res = missionGroupService.distributeLeader(requestVo.getGroupId(), requestVo.getLeaderId());
        result.setData(res);
        return result;
    }

    /**
     * 跳转添加页面
     *
     * @param model
     * @return
     */
    @RequestMapping("add")
    public String add(Model model) {
        List<Country> countries = countryService.findAllCountries();
        List<City> cities = cityService.findAllCities();
        List<BdUser> allBdUsers = missionGroupService.findAllBdUserNonGroup();
        model.addAttribute("allBdUsers", allBdUsers);
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        return "missionGroup/add";
    }

    /**
     * 跳转到编辑页面
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("edit")
    public String edit(Model model, Long id) {
        MissionGroup group = missionGroupService.findGroupById(id);
        List<Country> countries = countryService.findAllCountries();
        List<City> cities = cityService.findAllCities();
        List<BdUser> nonGroupUsers = missionGroupService.findNonGroupByCountryId(group.getCountry().getId(), id);
        model.addAttribute("nonGroupUsers", nonGroupUsers);
        model.addAttribute("countries", countries);
        model.addAttribute("group", group);
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
     * 新增组信息
     *
     * @param paramVo
     * @return
     */
    @RequestMapping("saveGroup")
    @ResponseBody
    public APIResult saveGroupInfo(@RequestBody  MissionGroupParamVo paramVo) {
        missionGroupService.saveGroup(paramVo);
        return new APIResult(ResultCodes.Success);
    }

    /**
     * 编辑组信息
     *
     * @param id
     * @param paramVo
     * @return
     */
    @RequestMapping("editGroup")
    @ResponseBody
    public APIResult editGroup(String id, MissionGroupParamVo paramVo) {
        missionGroupService.updateGroup(id, paramVo);
        return new APIResult(ResultCodes.Success);
    }

    /**
     * 查询没有组的BdUser
     *
     * @return
     */
    @RequestMapping("findNonGroupByCountryId")
    @ResponseBody
    public APIResult<List<BdUser>> findNonGroupByCountryId(@RequestBody MissionGroupVo requestVo) {
        APIResult<List<BdUser>> result = new APIResult<List<BdUser>>();
        List<BdUser> bdUsers = missionGroupService.findNonGroupByCountryId(requestVo.getCountryId(), requestVo.getGroupId());
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
}
