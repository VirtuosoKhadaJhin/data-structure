package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.LangsDictionaryRequestVo;
import com.nuanyou.cms.model.LangsDictionaryVo;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.service.LangsCategoryService;
import com.nuanyou.cms.service.LangsDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("langsDictionary")
public class LangsDictionaryController {

    @Autowired
    private LangsDictionaryService dictionaryService;
    @Autowired
    private LangsCategoryService categoryService;

    @RequestMapping("list")
    public String list(LangsDictionaryRequestVo requestVo, Model model) {
        Page<LangsDictionaryVo> allDictionary = dictionaryService.findAllDictionary(requestVo);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", requestVo);
        model.addAttribute("langsCountries", LangsCountry.values());
        return "langsDictionary/list";
    }

    @RequestMapping("list1")
    public String list1(LangsDictionaryRequestVo requestVo, Model model) {
        Page<LangsDictionaryVo> allDictionary = dictionaryService.findAllDictionary(requestVo);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", requestVo);
        model.addAttribute("langsCountries", LangsCountry.values());
        return "langsDictionary/list1";
    }

    @RequestMapping("add")
    public String add(Model model) {
        LangsCountry[] values = LangsCountry.values();
        LangsCategory example = new LangsCategory();
        example.setIndex(1);
        example.setSize(100000);
        Page<LangsCategory> selectableLangsCategory = this.categoryService.findAllCategories(example);

        model.addAttribute("langsCountries", values);
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);
        return "langsDictionary/add";
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        LangsCountry[] values = LangsCountry.values();
        LangsCategory example = new LangsCategory();
        example.setIndex(1);
        example.setSize(100000);
        Page<LangsCategory> selectableLangsCategory = this.categoryService.findAllCategories(example);
        LangsDictionaryVo vo = null;
        if (id != null) {
            vo = dictionaryService.findLangsDictionary(id);
        }
        model.addAttribute("entity", vo);
        model.addAttribute("langsCountries", values);
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);
        model.addAttribute("type", type);
        return "langsDictionary/edit";
    }

    @RequestMapping("update")
    public String update(LangsDictionary entity) throws IOException {
        LangsDictionary category;
        if (entity.getId() == null) {
            category = dictionaryService.addLangsDictionary(entity);
        } else {
            category = dictionaryService.updateLangsDictionary(entity);
        }
        String url = "edit?type=3&id=" + category.getId();
        return "redirect:" + url;
    }

    /**
     * 判断keyCode是否有效
     *
     * @param dictionaryVo
     * @return boolean
     */
    @RequestMapping(value = "verifykeyCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult verifykeyCode(@RequestBody LangsDictionaryVo dictionaryVo) {
        return new APIResult(dictionaryService.verifykeyCode(dictionaryVo));
    }

    /**
     * 保存多语言
     *
     * @param dictionaryVo
     * @return boolean
     */
    @RequestMapping(value = "saveLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult saveLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) {
        return new APIResult(dictionaryService.saveLangsDictionary(dictionaryVo));
    }

    /**
     * 查询单个的语言
     *
     * @param keyCode
     * @return LangsDictionaryVo
     */
    @RequestMapping(value = "findOneByKeyCode", method = RequestMethod.POST)
    @ResponseBody
    public APIResult findOneByKeyCode(@RequestParam String keyCode) {
        APIResult<LangsDictionaryVo> result = new APIResult<LangsDictionaryVo>(ResultCodes.Success);
        LangsDictionaryVo langsDictionary = dictionaryService.findLangsDictionary(keyCode, null);
        result.setData(langsDictionary);
        return result;
    }

    /**
     * 根据分类id查询所有的语言
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "api/list", method = RequestMethod.POST)
    @ResponseBody
    public APIResult list(@RequestParam Long id) {
        System.out.println(id);
        if (id == null) {
            return null;
        }
        List<LangsDictionary> list = dictionaryService.findIdNameListByCat(id);
        return new APIResult(list);
    }


}