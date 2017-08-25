package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.*;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("langsDictionary")
public class LangsDictionaryController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private LangsCategoryService categoryService;

    @Autowired
    private LangsDictionaryService dictionaryService;

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
        List<LangsCategory> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("page", allDictionary);
        model.addAttribute("requestVo", requestVo);
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
        List<String> roleCountryCodes = countryService.countryCodes();
        List<LangsCountryVo> langsCountryVos = LangsCountry.viewRoleCountrysResultList(roleCountryCodes);
        List<LangsCategory> categories = categoryService.findAllCategories();

        // 页面显示当地语言
        if (requestVo.getCountryKey() != 0) {
            String localLangs = LangsCountry.toEnum(requestVo.getCountryKey()).getValue();
            model.addAttribute("localLangs", localLangs);
        }

        model.addAttribute("categories", categories);
        model.addAttribute("page", allDictionary);
        model.addAttribute("entity", requestVo);
        model.addAttribute("langsCountryVos", langsCountryVos);
        model.addAttribute("langsCountries", LangsCountry.localValues(requestVo.getCountryKey()));
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

    /**
     * 本地语言增加页面
     *
     * @param model
     * @return
     */
    @RequestMapping("add")
    public String add(Model model) {
        LangsCountry[] values = LangsCountry.values();
        List<LangsCategory> langsCategories = categoryService.findAllCategories();

        model.addAttribute("langsCountries", values);
        model.addAttribute("langsCategories", langsCategories);
        return "langsDictionary/add";
    }

    /**
     * 多语言编辑
     *
     * @param model
     * @return
     */
    @RequestMapping("edit")
    public String edit(String keyCode, Model model) throws UnsupportedEncodingException {
        keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();

        // 多语言种类
        LangsCountry[] values = LangsCountry.values();

        // 多语言分类
        List<LangsCategory> langsCategories = categoryService.findAllCategories();

        // 多语言数据
        LangsDictionaryVo langsDictionary = dictionaryService.findLangsDictionary(keyCode, null);

        model.addAttribute("langsCountries", values);
        model.addAttribute("langsDictionary", langsDictionary);
        model.addAttribute("langsCategories", langsCategories);
        return "langsDictionary/edit";
    }

    /**
     * 本地语言编辑
     *
     * @param dictionaryVo
     * @param model
     * @return
     */
    @RequestMapping("localEdit")
    public String localAdd(LangsDictionaryVo dictionaryVo, Model model) {
        List<LangsCategory> selectableLangsCategory = categoryService.findAllCategories();
        // 根据keyCode查询中文、英文、当地文
        List<LangsDictionary> dictionarys = dictionaryService.viewLocalLangsDictionary(dictionaryVo);
        model.addAttribute("dictionarys", dictionarys);
        model.addAttribute("dictionaryVo", dictionaryVo);
        model.addAttribute("langsCountries", LangsCountry.localValues(dictionaryVo.getCountryKey()));
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);

        return "langsDictionary/local_edit";
    }

    /**
     * (待定)修改当地语言接口
     *
     * @param dictionaryVo
     * @return
     */
    @RequestMapping(value = "modifyLocalLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult modifyLocalLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) {
        APIResult result = new APIResult(ResultCodes.Success);
        dictionaryService.modifyLocalLangsDictionary(dictionaryVo);
        return result;
    }

    /**
     * 根据keyCode查询查询中文、英文、当地文
     *
     * @param dictionaryVo
     * @return List<LangsDictionary>
     */
    @RequestMapping(value = "viewLocalLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult viewLocalLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) {
        APIResult result = new APIResult(ResultCodes.Success);
        // 根据keyCode查询中文、英文、当地文
        List<LangsDictionary> dictionarys = dictionaryService.viewLocalLangsDictionary(dictionaryVo);
        result.setData(dictionarys);

        return result;
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
     * 得到当地语言
     *
     * @param request
     * @return LangsDictionary
     */
    @RequestMapping(value = "viewLocalLanguage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult viewLocalLanguage(HttpServletRequest request) {
        APIResult<LangsCountryVo> result = new APIResult<LangsCountryVo>(ResultCodes.Success);
        LangsCountry langCountry = LangsCountry.toEnum(request.getLocale().getLanguage() + "-" + request.getLocale().getCountry());
        LangsCountryVo vo = new LangsCountryVo(langCountry.getKey(), langCountry.getValue(), langCountry.getDesc());
        result.setData(vo);
        return result;
    }

    /**
     * 保存多语言
     *
     * @param dictionaryVo
     * @return LangsDictionary
     */
    @RequestMapping(value = "saveLangsDictionary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult saveLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) {
        APIResult<LangsDictionary> result = new APIResult<LangsDictionary>(ResultCodes.Success);
        Boolean recordResult = dictionaryService.saveLangsDictionary(dictionaryVo);
        if (BooleanUtils.isFalse(recordResult)) {
            result.setCode(ResultCodes.Fail.getCode());
            result.setMsg("保存失败，请重新尝试！");
        } else if (recordResult == null) {
            result.setCode(ResultCodes.Fail.getCode());
            result.setMsg("请求参数异常，请重试尝试！");
        }
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
    public APIResult viewLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) throws UnsupportedEncodingException {
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
    public APIResult modifyLangsDictionary(@RequestBody LangsDictionaryVo dictionaryVo) throws UnsupportedEncodingException {
        APIResult<EntityNyLangsDictionary> result = new APIResult<EntityNyLangsDictionary>(ResultCodes.Success);
        String keyCode = dictionaryVo.getKeyCode();
        dictionaryVo.setKeyCode((new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim());
        dictionaryService.modifyLangsDictionary(dictionaryVo);
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
    public APIResult findOneByKeyCode(@RequestParam String keyCode) throws UnsupportedEncodingException {
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
        List<LangsDictionary> list = dictionaryService.findAllLanguagesByCatId(id);
        return new APIResult(list);
    }


}