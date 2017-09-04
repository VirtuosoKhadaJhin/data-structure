package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.model.VisitQueryRequest;
import com.nuanyou.cms.model.mission.MissionBdMerchantTrack;
import com.nuanyou.cms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by young on 2017/9/1.
 */
@Controller
@RequestMapping("visit")
public class VisitController {

    @Autowired
    private MerchantVisitService visitService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private BdUserService bdUserService;

    @Autowired
    private CityService cityService;

    /**
     * 查看拜访记录
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(VisitQueryRequest request, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "createTime");

        List<Country> countries = countryService.getIdNameList();
        List<BdUser> bdUsers = bdUserService.findAllBdUsers();
        Page<MerchantVisit> visits = visitService.queryMerchantVisit(request,pageable);

        List<City> cities;
        if (countries != null && countries.size() == 1) {
            cities = cityService.findCityByCountryId(countries.get(0).getId());
        }else {
            cities = cityService.findAllCities();
        }
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("page", visits);
        model.addAttribute("requestVo", request);
        model.addAttribute("bdUsers", bdUsers);
        return "visit/list";
    }


}
