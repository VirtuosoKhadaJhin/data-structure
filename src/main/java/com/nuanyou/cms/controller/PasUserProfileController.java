package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.PasUserProfileDao;
import com.nuanyou.cms.entity.user.PasUserProfile;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.RemoteService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;


@Controller
@RequestMapping("pasUserProfile")
public class PasUserProfileController {

    @Autowired
    private PasUserProfileDao pasUserProfileDao;


    @Autowired
    private RemoteService remoteService;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        PasUserProfile entity = pasUserProfileDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "updateBalance", method = RequestMethod.POST)
    @ResponseBody
    public APIResult updateBalance(Long id, Integer type, BigDecimal updatebalance) throws Exception {
        PasUserProfile user = pasUserProfileDao.findOne(id);
        if (user == null)
            throw new APIException(ResultCodes.NotFoundUser);
        //TODO 获取CMS用户
        remoteService.recharge(user.getId(), updatebalance, type, null, null);
        return new APIResult();
    }

    @RequestMapping(path = "pushMessage", method = RequestMethod.POST)
    @ResponseBody
    public APIResult pushMessage(Long id, String message) {
        //TODO 获取CMS用户
        remoteService.pushMessage(id, message, null);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(PasUserProfile entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("nickname", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        Page<PasUserProfile> page = pasUserProfileDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        model.addAttribute("entity", entity);
        return "pasUserProfile/list";
    }

}