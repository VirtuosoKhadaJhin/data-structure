package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.service.LangsDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Byron on 2017/6/1.
 */
@Controller
@RequestMapping("/langsDictionary")
public class LangsDictionaryController {

    private final static Logger _LOGGER = LoggerFactory.getLogger ( LangsDictionaryController.class );

    @Autowired
    private LangsDictionaryService dictionaryService;

    /**
     * 多元化字典项首页
     *
     * @param langsDictionary
     * @return
     */
    @RequestMapping("/index")
    @ResponseBody
    public APIResult index(@RequestBody LangsDictionary langsDictionary) {
        APIResult result = new APIResult ( ResultCodes.Success );
        dictionaryService.findAllDictionary ( langsDictionary );
        return result;
    }

    /**
     * 多元化字典项详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findDictionaryDetail")
    @ResponseBody
    public APIResult findDictionaryDetail(Long id) {
        APIResult result = new APIResult ( ResultCodes.Success );
        LangsDictionary dictionary = dictionaryService.findLangsDictionary ( id );
        result.setData ( dictionary );
        return result;
    }

    /**
     * 新增多元化字典项
     *
     * @param dictionary
     * @return
     */
    @RequestMapping("/addDictionary")
    @ResponseBody
    public APIResult addDictionary(@RequestBody LangsDictionary dictionary) {
        APIResult result = new APIResult ( ResultCodes.Success );
        dictionary = dictionaryService.addLangsDictionary ( dictionary );
        result.setData ( dictionary );
        return result;
    }

    /**
     * 更新多元化字典项
     *
     * @param dictionary
     * @return
     */
    @RequestMapping("/updateDictionary")
    @ResponseBody
    public APIResult updateDictionary(@RequestBody LangsDictionary dictionary) {
        APIResult result = new APIResult ( ResultCodes.Success );
        dictionary = dictionaryService.updateLangsDictionary ( dictionary );
        result.setData ( dictionary );
        return result;
    }

    /**
     * 删除多元化字典项
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteDictionary")
    @ResponseBody
    public APIResult deleteDictionary(Long id) {
        APIResult result = new APIResult ( ResultCodes.Success );
        dictionaryService.deleteLangsDictionary ( id );
        return result;
    }
}
