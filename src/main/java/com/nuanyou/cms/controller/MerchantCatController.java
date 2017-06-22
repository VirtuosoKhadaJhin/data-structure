package com.nuanyou.cms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantCatDao;
import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.MerchantCatVo;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.service.LangsCategoryService;
import com.nuanyou.cms.service.MerchantCatService;
import com.nuanyou.cms.util.NodeData;
import com.nuanyou.cms.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("merchantCat")
public class MerchantCatController {

    @Autowired
    private MerchantCatDao merchantCatDao;

    @Autowired
    private MerchantCatService merchantCatService;

    @Autowired
    private LangsCategoryService categoryService;

    @RequestMapping("add")
    public String add(MerchantCat merchantCat) {
        merchantCatService.add(merchantCat);
        return "merchantCat/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type, HttpServletRequest request) {
        MerchantCat entity = null;
        if (id != null) {
            entity = merchantCatDao.findOne(id);
        }
        List<MerchantCat> merchantCats = merchantCatService.getIdNameList();

        //本地语言
        List<LangsCountry> langsCountries = getNativeLangs(request);

        // 语言分类, 用于增加多语言弹窗里面的select下拉列表
        List<LangsCategory> categories = categoryService.findAllCategories();

        model.addAttribute("langsCountries", langsCountries);
        model.addAttribute("categories", categories);
        model.addAttribute("merchantCats", merchantCats);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "merchantCat/edit";
    }

    @RequestMapping("update")
    public String update(MerchantCatVo merchantCatVo) throws IOException {
        merchantCatService.updateMerchantCat(merchantCatVo);
        return "redirect:list";
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String nameOrId,
                       MerchantCat entity, Model model,
                       HttpServletRequest request) {
        if (StringUtils.isNotBlank(nameOrId)) {
            if (StringUtils.isNumeric(nameOrId)) {
                entity.setId(NumberUtils.toLong(nameOrId));
            } else {
                entity.setName(nameOrId);
            }
        }

        Locale locale = request.getLocale();
        List<MerchantCatVo> page = merchantCatService.findParentCat(entity, index, locale);

        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("nameOrId", nameOrId);
        return "merchantCat/list";
    }

    /**
     * 根据一级分类的ID分页查询二级分类
     *
     * @return
     */
    @RequestMapping("viewCat")
    @ResponseBody
    public APIResult<Page<MerchantCatVo>> viewCat(@RequestBody MerchantCatVo merchantCatVo, HttpServletRequest request) {
        Locale locale = request.getLocale();
        Page<MerchantCatVo> page = merchantCatService.findChildCat(merchantCatVo, merchantCatVo.getIndex(), locale, merchantCatVo.getPcat().getId());
        return new APIResult(page);
    }

    /**
     * 根据ID删除分类
     *
     * @return
     */
    @RequestMapping("delCat")
    @ResponseBody
    public APIResult<Boolean> viewCat(@RequestBody MerchantCatVo merchantCatVo) {
        APIResult<Boolean> result = new APIResult<Boolean>();
        Boolean delResult = merchantCatService.delCat(merchantCatVo);
        result.setData(delResult);
        return result;
    }

    @RequestMapping("api/list")
    @ResponseBody
    public APIResult list(Long id) {
        MerchantCat entity = new MerchantCat();
        entity.setPcat(new MerchantCat(id));
        List<MerchantCat> list = merchantCatDao.findAll(Example.of(entity));
        return new APIResult(list);
    }


    @RequestMapping("treeList")
    public String treeList(@RequestParam(required = false, defaultValue = "1") int index,
                           @RequestParam(required = false) String nameOrId,
                           MerchantCat entity, Model model) {
        List<NodeData> rs = new ArrayList<>();
        NodeData n1 = new NodeData(0, -1, "root", false);
        rs.add(n1);
        List<MerchantCat> list = merchantCatDao.findByPcat(null);
        for (MerchantCat merchantCat : list) {
            NodeData n = new NodeData(merchantCat.getId(), 0, merchantCat.getName(), false);
            rs.add(n);
            List<MerchantCat> children = merchantCatDao.findByPcat(merchantCat);
            for (MerchantCat child : children) {
                NodeData ch = new NodeData(child.getId(), child.getPcat().getId(), child.getName(), false);
                rs.add(ch);
            }
        }
        String nodeData = toJsonString(rs);
        model.addAttribute("nodeData", nodeData);
        return "merchantCat/list1";
    }

    public static String toJsonString(List<NodeData> rs) {
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        return JSON.toJSONString(rs);
        //return JSON.toJSONStringZ(rs, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
    }

    private List<LangsCountry> getNativeLangs(HttpServletRequest request) {
        List<LangsCountry> langsCountries = new ArrayList<>();
        Locale locale = request.getLocale();
        String lang = locale.toLanguageTag();
        LangsCountry langsCountry = LangsCountry.toEnum(lang);
        langsCountries.add(langsCountry);
        if (!langsCountry.equals((LangsCountry.ZH_CN))) {
            langsCountries.add(LangsCountry.ZH_CN);
        }
        if (!langsCountry.equals((LangsCountry.EN_UK))) {
            langsCountries.add(LangsCountry.EN_UK);
        }
        return langsCountries;
    }

}