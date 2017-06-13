package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
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

    private static final Integer LOCAL_KEY = 5;

    /**
     * 多语言列表查询
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(LangsDictionaryRequestVo requestVo, Model model) {
        Page<LangsDictionaryVo> allDictionary = dictionaryService.findAllDictionary(requestVo);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", requestVo);
        model.addAttribute("langsCountries", LangsCountry.values());
        return "langsDictionary/list";
    }

    /**
     * 删除语言
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/remove")
    @ResponseBody
    public APIResult<LangsDictionary> remove(@RequestBody LangsDictionaryRequestVo requestVo) {
        APIResult result = new APIResult(ResultCodes.Success);
        dictionaryService.remove(requestVo);
        return result;
    }

    /**
     * 多语言列表查询
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("localList")
    public String local(LangsDictionaryRequestVo requestVo, Model model) {
        Page<LangsDictionaryVo> allDictionary = dictionaryService.findAllLocalDictionary(requestVo);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", requestVo);
        model.addAttribute ( "langsCountries", LangsCountry.localValues (LOCAL_KEY) );
        return "langsDictionary/local_list";
    }

    /**
     * 新增单个语言记录
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "saveMessage", method = RequestMethod.POST)
    @ResponseBody
    public APIResult saveMessage(@RequestBody LangsDictionaryRequestVo vo) {
        APIResult<LangsDictionary> result = new APIResult<LangsDictionary>(ResultCodes.Success);
        LangsDictionary langsDictionary = dictionaryService.saveMessage(vo);
        result.setData(langsDictionary);
        return result;
    }

    /**
     * suggest搜索
     *
     * @param key
     * @return
     */
    @RequestMapping("/suggest")
    @ResponseBody
    public APIResult<LangsDictionary> suggestSearch(@RequestParam("key") String key) {
        APIResult result = new APIResult(ResultCodes.Success);
        List<LangsDictionary> searchResult = dictionaryService.findSuggestSearch(key);
        result.setData(searchResult);
        return result;
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

    @RequestMapping("edit")
    public String edit(Model model) {
        LangsCountry[] values = LangsCountry.values();
        LangsCategory example = new LangsCategory();
        example.setIndex(1);
        example.setSize(100000);
        Page<LangsCategory> selectableLangsCategory = this.categoryService.findAllCategories(example);

        model.addAttribute("langsCountries", values);
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);
        return "langsDictionary/edit";
    }

    @RequestMapping("localAdd")
    public String localAdd(Model model) {
        LangsCategory example = new LangsCategory();
        example.setIndex(1);
        example.setSize(100000);
        Page<LangsCategory> selectableLangsCategory = this.categoryService.findAllCategories(example);
        model.addAttribute("langsCountries", LangsCountry.localValues (LOCAL_KEY));
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);
        return "langsDictionary/local_add";
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
        APIResult result = new APIResult(ResultCodes.Success);
        boolean verifykeyCodeResult = dictionaryService.verifykeyCode(dictionaryVo);
        result.setData(verifykeyCodeResult);

        return result;
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
        APIResult result = new APIResult(ResultCodes.Success);
        String keyCode = dictionaryService.saveLangsDictionary(dictionaryVo);
        result.setData(keyCode);
        return result;
    }

    /**
     * 查询单个的语言
     *
     * @param dictionaryVo
     * @return
     */
    @RequestMapping(value = "viewLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult viewLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) {
        APIResult<LangsDictionaryVo> result = new APIResult<LangsDictionaryVo>(ResultCodes.Success);
        LangsDictionaryVo langsDictionary = dictionaryService.findLangsDictionary(dictionaryVo.getKeyCode(), null);
        result.setData(langsDictionary);
        return result;
    }

    /**
     * 修改单个的语言
     *
     * @param dictionaryVo
     * @return
     */
    @RequestMapping(value = "modifyLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult modifyLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) {
        APIResult<EntityNyLangsDictionary> result = new APIResult<EntityNyLangsDictionary>(ResultCodes.Success);
        dictionaryService.modifyLangsDictionary ( dictionaryVo );
        return result;
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
        if (id == null) {
            return null;
        }
        List<LangsDictionary> list = dictionaryService.findIdNameListByCat(id);
        return new APIResult(list);
    }


}