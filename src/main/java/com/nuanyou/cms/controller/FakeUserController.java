package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.FakeUserDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.FakeUser;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.service.FakeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("fakeUser")
public class FakeUserController {

    @Autowired
    private FakeUserService fakeUserService;
    @Autowired
    private FakeUserDao fakeUserDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping("add")
    public String add(FakeUser entity) {
        fakeUserService.saveNotNull(entity);
        return "fakeUser/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        // all countries
        List<Country> countries = this.countryDao.findAll();
        FakeUser entity = null;
        if (id != null) {
            entity = fakeUserDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "fakeUser/edit";
    }

    @RequestMapping("update")
    public String update(FakeUser entity, HttpServletResponse response) throws IOException {
        fakeUserService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, FakeUser entity, Model model) {
        Page<FakeUser> page = fakeUserService.findByCondition(index, entity);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "fakeUser/list";
    }


}

