package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRole;
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
     * 获取列表
     *
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
     * 编辑
     *
     * @return
     */
    @RequestMapping("edit")
    public String edit(Long id, Model model) {
        BdUserVo vo = bdUserService.findUserById(id);
        List<BdRole> roles = bdUserService.findAllRoles();
        List<BdCountry> countries = bdUserService.findAllCountry();
        model.addAttribute("countries", countries);
        model.addAttribute("roles", roles);
        model.addAttribute("vo", vo);
        return "bdUser/edit";
    }

    @RequestMapping("add")
    public String add(Model model) {
        List<BdRole> roles = bdUserService.findAllRoles();
        List<BdCountry> countries = bdUserService.findAllCountry();
        model.addAttribute("roles", roles);
        model.addAttribute("countries", countries);
        return "bdUser/add";
    }

    @RequestMapping("del")
    @ResponseBody
    public APIResult del(@RequestBody  Long id) {
        bdUserService.del(id);
        return new APIResult(ResultCodes.Success);
    }

    /**
     * 保存添加内容
     *
     * @return
     */
    @RequestMapping("saveAdd")
    @ResponseBody
    public APIResult saveAdd(BdUserParamVo paramVo) {
        bdUserService.saveAddUserAndRole(paramVo);
        return new APIResult(ResultCodes.Success);
    }

    @RequestMapping("saveEdit")
    @ResponseBody
    public APIResult saveEdit(BdUserParamVo paramVo) {
        bdUserService.saveEditUserAndRole(paramVo);
        return new APIResult(ResultCodes.Success);
    }
}
