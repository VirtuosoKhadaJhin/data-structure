package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.service.LangsCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Byron on 2017/5/31.
 */
@Controller
@RequestMapping("/category")
public class LangsCategoryController {

    private final static Logger _LOGGER = LoggerFactory.getLogger ( LangsCategoryController.class );

    @Autowired
    private LangsCategoryService categoryService;

    /**
     * 分类管理首页（查询）
     *
     * @param langsCategory
     * @return
     */
    @RequestMapping("/index")
    @ResponseBody
    public APIResult index(@RequestBody LangsCategory langsCategory) {
        APIResult result = new APIResult ( ResultCodes.Success );
        Page<LangsCategory> categoryPage = categoryService.findAllCategories ( langsCategory );
        result.setData ( categoryPage );
        return result;
    }

    /**
     * 查詢分类详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findCategoryDetail")
    @ResponseBody
    public APIResult findCategoryDetail(Long id) {
        APIResult result = new APIResult ( ResultCodes.Success );
        LangsCategory category = categoryService.findLangsCategory ( id );
        result.setData ( category );
        return result;
    }

    /**
     * 新增分类信息
     *
     * @param langsCategory
     * @return
     */
    @RequestMapping("/addCategory")
    @ResponseBody
    public APIResult addCategory(@RequestBody LangsCategory langsCategory) {
        APIResult result = new APIResult ( ResultCodes.Success );
        LangsCategory category = categoryService.save ( langsCategory );
        result.setData ( category );
        return result;
    }

    /**
     * 更新分类信息
     *
     * @param langsCategory
     * @return
     */
    @RequestMapping("/updateCategory")
    @ResponseBody
    public APIResult updateCategory(@RequestBody LangsCategory langsCategory) {
        APIResult result = new APIResult ( ResultCodes.Success );
        LangsCategory category = categoryService.update ( langsCategory );
        result.setData ( category );
        return result;
    }

    /**
     * 更新分类信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteCategory")
    @ResponseBody
    public APIResult deleteCategory(Long id) {
        APIResult result = new APIResult ( ResultCodes.Success );
        categoryService.delete ( id );
        return result;
    }

}
