package com.nuanyou.cms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantCatDao;
import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.service.MerchantCatService;
import com.nuanyou.cms.util.NodeData;
import com.nuanyou.cms.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("merchantCat")
public class MerchantCatController {

    @Autowired
    private MerchantCatService merchantCatService;

    @Autowired
    private MerchantCatDao merchantCatDao;

    @RequestMapping("add")
    public String add(MerchantCat merchantCat) {
        merchantCatService.add(merchantCat);
        return "merchantCat/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        MerchantCat entity = null;
        if (id != null) {
            entity = merchantCatDao.findOne(id);
        }
        List<MerchantCat> merchantCats = merchantCatService.getIdNameList();
        model.addAttribute("merchantCats", merchantCats);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "merchantCat/edit";
    }

    @RequestMapping("update")
    public String update(MerchantCat entity, HttpServletResponse response, Model model) throws IOException {
        if (entity.getPcat().getId() == null) {
            entity.setPcat(null);
        }
        merchantCatService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String nameOrId,
                       MerchantCat entity, Model model) {
        if (StringUtils.isNotBlank(nameOrId)) {
            if (StringUtils.isNumeric(nameOrId)) {
                entity.setId(NumberUtils.toLong(nameOrId));
            } else {
                entity.setName(nameOrId);
            }
        }

        Page<MerchantCat> page = merchantCatService.findByCondition(entity, index);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("nameOrId", nameOrId);
        return "merchantCat/list";
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
        NodeData n1 = new NodeData(0,-1,"root",false);
        rs.add(n1);
        List<MerchantCat> list = merchantCatDao.findByPcat(null);
        for (MerchantCat merchantCat : list) {
            NodeData n = new NodeData(merchantCat.getId(),0,merchantCat.getName(),false);
            rs.add(n);
            List<MerchantCat> children=merchantCatDao.findByPcat(merchantCat);
            for (MerchantCat child : children) {
                NodeData ch = new NodeData(child.getId(),child.getPcat().getId(),child.getName(),false);
                rs.add(ch);
            }
        }
        String nodeData=toJsonString(rs);
        model.addAttribute("nodeData",nodeData);
        return "merchantCat/list1";
    }

    public static String toJsonString(List<NodeData> rs) {
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        return JSON.toJSONString(rs);
        //return JSON.toJSONStringZ(rs, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
    }
}