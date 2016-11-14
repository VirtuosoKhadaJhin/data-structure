package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.NewsDao;
import com.nuanyou.cms.entity.News;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Controller
@RequestMapping("news")
public class NewsController {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private NewsService newsService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(News entity, Model model) {
        if (entity.getId() != null) {
            entity = newsDao.findOne(entity.getId());
        }
        model.addAttribute("entity", entity);
        return "news/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        News entity = newsDao.findOne(id);
        model.addAttribute("entity", entity);

        model.addAttribute("disabled", true);
        return "news/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(News entity, Model model) {
        entity = newsService.saveNotNull(entity);
        model.addAttribute("entity", entity);
        model.addAttribute("disabled", true);
        return "news/edit";
    }

    @RequestMapping("list")
    public String list(News entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("title", contains().ignoreCase());

        Page<News> page = newsDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        model.addAttribute("entity", entity);
        return "news/list";
    }

}