package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.LangsDictionary;
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
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       String baseName,
                       String keyCode,
                       String message,
                       Model model) {
        LangsDictionary example=new LangsDictionary();
        example.setBaseName(baseName);example.setKeyCode(keyCode);example.setIndex(index);example.setMessage(message);
        Page<LangsDictionary> allDictionary = dictionaryService.findAllDictionary(example);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", example);
        model.addAttribute ( "langsCountries", LangsCountry.values () );
        return "langsDictionary/list";
    }



    @RequestMapping("list1")
    public String list1(@RequestParam(required = false, defaultValue = "1") int index,
                       String baseName,
                       String keyCode,
                       String message,
                       Model model) {
        LangsDictionary example=new LangsDictionary();
        example.setBaseName(baseName);example.setKeyCode(keyCode);example.setIndex(index);example.setMessage(message);
        Page<LangsDictionary> allDictionary = dictionaryService.findAllDictionary(example);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", example);
        model.addAttribute ( "langsCountries", LangsCountry.values () );
        return "langsDictionary/list1";
    }



    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        LangsCountry[] values = LangsCountry.values();
        LangsCategory example=new LangsCategory();
        example.setIndex(1);example.setSize(100000);
        Page<LangsCategory> selectableLangsCategory = this.categoryService.findAllCategories(example);
        LangsDictionary entity = null;
        if (id != null) {
            entity = dictionaryService.findLangsDictionary(id);
        }
        model.addAttribute("entity", entity);
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



    @RequestMapping(value = "saveLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult saveTemplate(@RequestBody LangsDictionaryVo dictionaryVo
    ) throws IOException {


        return null;
    }


    @RequestMapping("api/list")
    @ResponseBody
    public APIResult list(Long id) {
        List<LangsDictionary> list = dictionaryService.findIdNameListByCat(id);
        return new APIResult(list);
    }


}