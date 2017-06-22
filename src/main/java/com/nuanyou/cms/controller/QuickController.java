package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.QuickDao;
import com.nuanyou.cms.entity.Quick;
import com.nuanyou.cms.entity.enums.PeriodType;
import com.nuanyou.cms.model.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("quick")
public class QuickController {

    @Autowired
    private QuickDao quickDao;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        Quick entity = quickDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(Quick entity) {
        if (entity.getId() == null)
            entity.setSort((int) quickDao.count() + 1);
        quickDao.save(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        quickDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(Quick entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, new Sort(Sort.Direction.ASC, "sort", "id"));
        if (entity.getPosition() == null)
            entity.setPosition("index");

        Page<Quick> page = quickDao.findAll(Example.of(entity), pageable);
        model.addAttribute("page", page);

        PeriodType[] periodTypes = PeriodType.values();
        model.addAttribute("periodTypes", periodTypes);

        model.addAttribute("entity", entity);
        return "quick/list";
    }

    @RequestMapping(path = "sort")
    @ResponseBody
    public APIResult setSort(@RequestParam(value = "id") Long id,
                             @RequestParam(value = "top", required = false) Boolean top,
                             @RequestParam(value = "move", required = false) Integer move,
                             @RequestParam(value = "value", required = false) Integer value) {
        Integer sort = quickDao.getSortById(id);
        if (sort == null)
            sort = 1;
        int max = (int) quickDao.count();

        if (value != null) {
            sort = value;
            if (sort < 1)
                sort = 1;
            if (sort > max)
                sort = max;

        } else if (move != null) {
            sort += move;
            if (sort < 1)
                sort = 1;
            if (sort > max)
                sort = max;

        } else if (top != null) {
            sort = top ? 1 : max;

        }
        quickDao.updateSort(id, sort);
        return new APIResult<>();
    }

}