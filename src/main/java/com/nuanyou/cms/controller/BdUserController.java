package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.BdUserRequestVo;
import com.nuanyou.cms.model.BdUserParamVo;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.service.BdUserService;

import com.nuanyou.cms.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("bdUser")
public class BdUserController {

    @Autowired
    private BdUserService bdUserService;

    @Autowired
    private CountryService countryService;

    /**
     * 获取BD用户列表
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(BdUserRequestVo requestVo, Model model) {
        Page<BdUserVo> vos = bdUserService.findAllBdUserVos(requestVo);
        List<Country> allCountries = countryService.findAllCountries();
        model.addAttribute("allCountries", allCountries);
        model.addAttribute("requestVo", requestVo);//刷新界面
        model.addAttribute("page", vos);
        return "bdUser/list";
    }

    /**
     * 添加 BD用户
     *
     * @param model
     * @return
     */
    @RequestMapping("add")
    public String add(Model model) {
        List<BdRole> roles = bdUserService.findAllRoles();
        List<BdCountry> countries = bdUserService.findAllCountry();
        model.addAttribute("roles", roles);
        model.addAttribute("countries", countries);
        model.addAttribute("vo", new BdUser());
        return "bdUser/edit";
    }

    /**
     * 添加 BD用户
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("edit")
    public String edit(Long id, Model model) {
        BdUserVo vo = bdUserService.findUserById(id);
        List<BdRole> roles = bdUserService.findAllRoles();
        List<BdCountry> countries = bdUserService.findAllCountry();
        model.addAttribute("roles", roles);
        model.addAttribute("countries", countries);
        model.addAttribute("vo", vo);
        return "bdUser/edit";
    }

    /**
     * 用户名查重
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("checkUserUnique")
    @ResponseBody
    public APIResult checkUserUnique(@RequestBody BdUserRequestVo requestVo) {
        Boolean isRepat = bdUserService.checkBdUserUnique(requestVo.getId(), requestVo.getName());
        APIResult<Boolean> result = new APIResult<Boolean>();
        result.setData(isRepat);
        return result;
    }

    /**
     * 逻辑删除BD用户
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public APIResult del(@RequestBody BdUserRequestVo requestVo) {
        bdUserService.del(requestVo.getId());
        return new APIResult(ResultCodes.Success);
    }

    @RequestMapping("saveBdUser")
    @ResponseBody
    public APIResult saveBdUser(@RequestBody BdUserParamVo paramVo) {
        if (paramVo.getId() == null) {
            bdUserService.saveAddUserAndRole(paramVo);
        } else {
            bdUserService.saveEditUserAndRole(paramVo);
        }
        return new APIResult(ResultCodes.Success);
    }
}
