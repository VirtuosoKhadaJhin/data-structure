package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.LangsCategoryVo;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.LangsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("langsCategory")
public class LangsCategoryController {

    @Autowired
    private LangsCategoryService categoryService;

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       String name,
                       Boolean isGlobal, Model model) {
        LangsCategory example=new LangsCategory();
        example.setName(name);example.setIsGlobal(isGlobal);example.setIndex(index);example.setSize(PageUtil.pageSize);
        Page<LangsCategory> categoryPage = this.categoryService.findAllCategories(example);
        model.addAttribute("page", categoryPage);
        model.addAttribute("entity", example);
        return "langsCategory/list";
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        LangsCategory entity = null;
        if (id != null) {
            entity = categoryService.findLangsCategory(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "langsCategory/edit";
    }

    @RequestMapping("update")
    public String update(LangsCategory entity) throws IOException {
        LangsCategory category;
        if (entity.getId() == null) {
            category = categoryService.save(entity);
        } else {


            category = categoryService.update(entity);
        }
        String url = "edit?type=3&id=" + category.getId();
        return "redirect:" + url;
    }

    /**
     * 判断是否有关联的多语言数据字典
     *
     * @param categoryVo
     * @return
     */
    @RequestMapping(value = "verifyRelatedLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult modifyLocalLangsDictionary(@RequestBody LangsCategoryVo categoryVo) {
        APIResult<Boolean> result = new APIResult<Boolean>(ResultCodes.Success);
        Boolean record = categoryService.verifyRelatedLangsDictionary(categoryVo);
        result.setData(record);
        return result;
    }


}