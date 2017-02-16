package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.enums.PushDetailTypeEnum;
import com.nuanyou.cms.model.PushDetailCondition;
import com.nuanyou.cms.model.PushDetailListVo;
import com.nuanyou.cms.model.PushDetailVo;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.PushDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by yangkai on 2017/2/15.
 */
@Controller
@RequestMapping("pushDetail")
public class PushDetailController {
    @Autowired
    private PushDetailService pushDetailService;
    @Autowired
    private CountryService countryService;

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, PushDetailCondition pushDetailCondition, Model model) {

        model.addAttribute("condition", pushDetailCondition);

        Page<PushDetailListVo> pushDetailVoPage = this.pushDetailService.list(pushDetailCondition, index);
        model.addAttribute("page", pushDetailVoPage);

        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);
        return "pushDetail/list";
    }

    @RequestMapping("deletePushDetail")
    @ResponseBody
    public APIResult deletePushDetail(Long id, Model model) {
        this.pushDetailService.deletePushDetail(id);
        return new APIResult<>(ResultCodes.Success);
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<Country> countries = countryService.getIdNameList();

        PushDetailVo pushDetailVo = this.pushDetailService.findById(id);
        model.addAttribute("entity", pushDetailVo);

        model.addAttribute("pushTypes", PushDetailTypeEnum.values());
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "pushDetail/edit";
    }

    @RequestMapping("update")
    public String update(PushDetailVo pushDetailVo, HttpServletResponse response) throws IOException {
        this.pushDetailService.update(pushDetailVo);
        String url = "edit?type=3&id=" + pushDetailVo.getId();
        return "redirect:" + url;
    }
}
