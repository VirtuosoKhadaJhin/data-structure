package com.nuanyou.cms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.dao.ItemCatDao;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.ItemTuanDao;
import com.nuanyou.cms.dao.TuanActivityTemplateDao;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.TuanType;
import com.nuanyou.cms.model.ItemVO;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.service.ItemTuanImgService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;


@Controller
@RequestMapping("itemTuan")
public class ItemTuanController {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemTuanDao itemTuanDao;

    @Autowired
    private ItemTuanImgService itemTuanImgService;

    @Autowired
    private TuanActivityTemplateDao tuanActivityTemplateDao;

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ItemService itemService;

    @Autowired
    @Qualifier("s3")
    private FileClient fileClient;

    @RequestMapping(value = "upload/editor", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public void editorImgUpload(@RequestParam("imgFile") MultipartFile file, HttpServletResponse response) throws Exception {
        String fileType = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains(".")) {
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        try {
            String callBackImgUrl = fileClient.uploadFile(file.getInputStream(), fileType);
            response.getWriter().println("{\"error\" : 0,\"url\" : \"" + callBackImgUrl + "\"}");
        } catch (Exception e) {
            response.getWriter().println("{\"error\" : 1,\"url\" : \"上传失败，请等待网络稳定时重试！\"}");
        }
    }

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        Long mchId = merchants.get(0).getId();

        if (id != null) {
            Item entity = itemDao.findOne(id);
            BigDecimal price = itemService.calcItemTuanPrice(id);
            if (price.compareTo(BigDecimal.ZERO) != 0) {//有单品时
                entity.setOkpPrice(price);
                model.addAttribute("hasItems", true);
            }

            List<ItemTuanImg> itemTuanImgUrls = itemTuanImgService.getItemTuanImgUrls(id);
            model.addAttribute("imgUrls", itemTuanImgUrls);

            model.addAttribute("entity", entity);
            mchId = entity.getMerchant().getId();
        }

        List<ItemCat> cats = itemCatDao.findIdNameList(mchId);
        model.addAttribute("cats", cats);

        List<TuanActivityTemplate> templates = tuanActivityTemplateDao.findAll();
        model.addAttribute("templates", templates);

        model.addAttribute("tuanTypes", TuanType.values());
        return "itemTuan/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        this.edit(id, model);
        model.addAttribute("disabled", true);
        return "itemTuan/edit";
    }

    @RequestMapping(path = "checkUnRedeem")
    @ResponseBody
    public APIResult checkUnRedeem(Long itemId) {
        Boolean hasUnRedeemedOrder = orderService.checkUnRedeem(itemId);
        return new APIResult(hasUnRedeemedOrder);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(ItemVO vo, String itemTuans, Model model) {
        List<ItemTuan> itemTuanList = null;
        if (StringUtils.isNotBlank(itemTuans)) {
            itemTuanList = JsonUtils.toObj(itemTuans, new TypeReference<List<ItemTuan>>() {
            });
        }

        vo.setItemType(2);
        Item entity = itemService.saveNotNull(vo, itemTuanList);
        model.addAttribute("entity", entity);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        List<ItemCat> cats = itemCatDao.findIdNameList(entity.getMerchant().getId());
        model.addAttribute("cats", cats);

        List<TuanActivityTemplate> templates = tuanActivityTemplateDao.findAll();
        model.addAttribute("templates", templates);

        model.addAttribute("tuanTypes", TuanType.values());

        model.addAttribute("disabled", true);
        model.addAttribute("update", true);
        return "itemTuan/edit";
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    public String remove(Long id, Model model) {
        itemDao.delete(id);
        return "itemTuan/list";
    }

    @RequestMapping("list")
    public String list(Item entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("cat.name", contains().ignoreCase())
//                .withMatcher("merchant.name", contains().ignoreCase())
//                .withMatcher("name", contains().ignoreCase());

        entity.setItemType(2);
        Page<Item> page = itemService.findByCondition(entity, pageable);
        model.addAttribute("page", page);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        model.addAttribute("entity", entity);
        return "itemTuan/list";
    }

    @RequestMapping("saveImgUrl")
    @ResponseBody
    public APIResult saveImgUrl(@RequestParam(required = true) Long itemId, @RequestParam(required = true) String imgUrl) {
        ItemTuanImg itemTuanImg = itemTuanImgService.saveImgUrl(itemId, imgUrl);
        return new APIResult(itemTuanImg);
    }

    @RequestMapping("deleteImgUrl")
    @ResponseBody
    public APIResult deleteImgUrl(@RequestParam(required = true) Long itemId) {
        itemTuanImgService.deleteImgUrl(itemId);
        return new APIResult();
    }


    @RequestMapping(path = "api/list")
    @ResponseBody
    public APIResult list(Long itemId) {
        ItemTuan t = new ItemTuan();
        t.setItemId(itemId);

        List<ItemTuan> sourceList = itemTuanDao.findAll(Example.of(t));
        return new APIResult(sourceList);
    }

}