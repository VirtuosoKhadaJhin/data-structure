package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.model.DistrictVo;
import com.nuanyou.cms.model.mission.MissionBdMerchantTrack;
import com.nuanyou.cms.model.mission.MissionDistributeParamVo;
import com.nuanyou.cms.model.mission.MissionRequestVo;
import com.nuanyou.cms.model.mission.MissionTaskVo;
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

    @Autowired
    private BdUserService bdUserService;

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

        if (requestVo.getStatus() == null) {
            requestVo.setStatus(MissionTaskStatus.FINISHED);
        }
        List<Country> countries = countryService.getIdNameList();
        List<Merchant> merchants = merchantService.findMerchantByCountry(requestVo.getCountry());
        List<MissionGroup> groups = missionGroupService.findByCountry(requestVo.getCountry());
        List<BdUser> bdUsers = bdUserService.findByCountryAndGroup(requestVo.getCountry(), requestVo.getGroupId());
        Page<MissionTaskVo> page = missionTaskService.findAllMissionTask(requestVo);
        model.addAttribute("page", page);
        model.addAttribute("requestVo", requestVo);
        model.addAttribute("countries", countries);
        model.addAttribute("groups", groups);
        model.addAttribute("bdUsers", bdUsers);
        model.addAttribute("merchants", merchants);
        model.addAttribute("taskStatus", MissionTaskStatus.values());
        return "mission/list";
    }

    /**
     * 查看签到图历史
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("tracks")
    public String list(MissionBdMerchantTrack requestVo, Model model) {
        Page<MissionBdMerchantTrack> page = missionTaskService.findAllTrackByMchId(requestVo);

        model.addAttribute("page", page);
        model.addAttribute("requestVo", requestVo);
        return "mission/tracks";
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
        requestVo.setAudit(false);
        requestVo.setPageSize(50);
        String email = UserHolder.getUser().getEmail();
        BdUser bdUser = bdUserService.findBdUserByDemail(email);
        if (bdUser == null) {
            throw new APIException(ResultCodes.NotFoundCurrentBdUser, ResultCodes.NotFoundCurrentBdUser.message);
        }
        MissionGroup missionGroup = missionGroupService.findGroupByUserId(bdUser.getId());
        if (missionGroup == null) {
            throw new APIException(ResultCodes.NotFoundGroup, ResultCodes.NotFoundGroup.message);
        }
        if (!missionGroup.getLeader().getId().equals(bdUser.getId()) && !missionGroup.getViceLeader().getId().equals(bdUser.getId())) {
            throw new APIException(ResultCodes.CurrentUserNotLeader, ResultCodes.CurrentUserNotLeader.message);
        }
        List<Merchant> merchants = merchantService.findMerchantByCountry(missionGroup.getCountry() == null ? null : missionGroup.getCountry().getId());
        List<BdUser> bdUsers = missionGroupService.findBdUsersByGroupId(missionGroup.getId());
        List<DistrictVo> districts = districtService.findByCity(missionGroup.getCity() == null ? null : missionGroup.getCity().getId());
        requestVo.setGroupId(missionGroup.getId());
        Page<MissionTaskVo> page = missionTaskService.findAllMissionTask(requestVo);
        model.addAttribute("page", page);
        model.addAttribute("districts", districts);
        model.addAttribute("bdUsers", bdUsers);
        model.addAttribute("group", missionGroup);
        model.addAttribute("merchants", merchants);
        model.addAttribute("requestVo", requestVo);
        model.addAttribute("taskStatus", MissionTaskStatus.values());
        return "distribute/list";
    }

    /**
     * 更新组是否可见
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("updateGroupPublic")
    @ResponseBody
    public APIResult updateGroupPublic(@RequestBody MissionTaskVo requestVo) {
        missionGroupService.updateGroupPublic(requestVo.getGroupId(), requestVo.getIsPublic());
        return new APIResult(ResultCodes.Success);
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

    /**
     * 获取国家下的城市
     *
     * @param country
     * @return
     */
    @RequestMapping("findCityByCountry")
    @ResponseBody
    public APIResult findCityByCountry(Long country) {
        List<City> cities = cityService.findCityByCountryId(country);
        return new APIResult(cities);
    }

    /**
     * 根据城市获取商家
     *
     * @param city
     * @return
     */
    @RequestMapping("findMerchantByCity")
    @ResponseBody
    public APIResult findMerchantByCity(Long city) {
        List<Merchant> merchants = merchantService.findMerchantByCountry(city);
        return new APIResult(merchants);
    }

    /**
     * 根据商圈获取商家
     *
     * @param district
     * @return
     */
    @RequestMapping("findMerchantByDistrict")
    @ResponseBody
    public APIResult findMerchantByDistrict(Long district) {
        List<Merchant> merchants = merchantService.findMerchantByDistrict(district);
        return new APIResult(merchants);
    }
}
