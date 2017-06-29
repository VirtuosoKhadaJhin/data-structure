package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import com.nuanyou.cms.model.DistrictVo;
import com.nuanyou.cms.model.MissionDistributeParamVo;
import com.nuanyou.cms.model.MissionRequestVo;
import com.nuanyou.cms.model.MissionTaskVo;
import com.nuanyou.cms.service.*;
import com.nuanyou.cms.sso.client.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Byron on 2017/6/27.
 */
@Controller
@RequestMapping("mission_task")
public class MissionTaskController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private MissionGroupService missionGroupService;

    @Autowired
    private MissionTaskService missionTaskService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 审核战队任务列表
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(MissionRequestVo requestVo, Model model) {
        List<Country> countries = countryService.getIdNameList();
        List<City> cities = cityService.findCityByCountryId(requestVo.getCountry());
        List<Merchant> merchants = merchantService.findMerchant(requestVo.getCountry(), requestVo.getCity());
        List<MissionGroup> groups = missionGroupService.findByCountryAndCityId(requestVo.getCountry(), requestVo.getCity());
        Page<MissionTaskVo> page = missionTaskService.findAllMissionTask(requestVo);
        model.addAttribute("page", page);
        model.addAttribute("requestVo", requestVo);
        model.addAttribute("countries", countries);
        model.addAttribute("cities",cities);
        model.addAttribute("groups", groups);
        model.addAttribute("merchants", merchants);
        model.addAttribute("taskStatus", MissionTaskStatus.values());
        return "mission/list";
    }

    /**
     * 审核
     *
     * @param vo
     * @return
     */
    @RequestMapping("approval")
    @ResponseBody
    public APIResult approvalTask(@RequestBody MissionRequestVo vo) {
        missionTaskService.approvalTask(vo);
        return new APIResult(ResultCodes.Success);
    }

    /**
     * 指派任务列表
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("distribute")
    public String distributeTask(MissionRequestVo requestVo, Model model) {
        List<City> cities = cityService.findCityByCountryId(requestVo.getCountry());
        List<Merchant> merchants = merchantService.findMerchant(requestVo.getCountry(), requestVo.getCity());
        Long userid = UserHolder.getUser().getUserid();
        //TODO 获取当前用户所在的组
        List<BdUser> bdUsers = missionGroupService.findBdUsersByGroupId(1L);
        List<DistrictVo> districts = districtService.findByCity(requestVo.getCity());
        requestVo.setStatus(MissionTaskStatus.UN_FINISH);
        Page<MissionTaskVo> page = missionTaskService.findAllMissionTask(requestVo);
        model.addAttribute("page", page);
        model.addAttribute("cities", cities);
        model.addAttribute("districts", districts);
        model.addAttribute("bdUsers", bdUsers);
        model.addAttribute("merchants", merchants);
        model.addAttribute("requestVo", requestVo);
        model.addAttribute("taskStatus", MissionTaskStatus.values());
        return "distribute/list";
    }

    /**
     * 指派任务到队员
     *
     * @param vo
     * @return
     */
    @RequestMapping("distributeTask")
    @ResponseBody
    public APIResult distributeTask(@RequestBody MissionDistributeParamVo vo) {
        missionTaskService.distributeTask(vo);
        return new APIResult(ResultCodes.Success);
    }
}
