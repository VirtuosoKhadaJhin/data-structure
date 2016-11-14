package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.SearchKeywordDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.SearchKeyword;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.service.SearchKeywordService;
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
@RequestMapping("searchKeyword")
public class SearchKeywordController {

    @Autowired
    private SearchKeywordService searchKeywordService;
    @Autowired
    private SearchKeywordDao searchKeywordDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping("add")
    public String add(SearchKeyword entity) {
        searchKeywordDao.save(entity);
        return "searchKeyword/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<Country> countries = this.countryDao.findAll();
        SearchKeyword entity = null;
        if (id != null) {
            entity = searchKeywordDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "searchKeyword/edit";
    }

    @RequestMapping("update")
    public String update(SearchKeyword entity, HttpServletResponse response) throws IOException {
        searchKeywordService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, SearchKeyword entity, Model model) {
        List<Country> countries = countryDao.findAll();
        Page<SearchKeyword> page = searchKeywordService.findByCondition(index, entity);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        return "searchKeyword/list";
    }


}

