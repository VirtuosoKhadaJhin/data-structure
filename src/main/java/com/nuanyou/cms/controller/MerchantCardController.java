package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantCardDao;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantCard;
import com.nuanyou.cms.entity.enums.CardType;
import com.nuanyou.cms.entity.enums.TuanType;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantCardService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;


@Controller
@RequestMapping("merchantCard")
public class MerchantCardController {

    @Autowired
    private MerchantCardDao merchantCardDao;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantCardService merchantCardService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        CardType[] cardTypes = CardType.values();
        model.addAttribute("cardTypes", cardTypes);

        if (id != null) {
            MerchantCard entity = merchantCardDao.findOne(id);
            model.addAttribute("entity", entity);
        }

        model.addAttribute("tuanTypes", TuanType.values());
        return "merchantCard/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        this.edit(id, model);
        model.addAttribute("disabled", true);
        return "merchantCard/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(MerchantCard entity, Model model) {
        entity = merchantCardService.saveNotNull(entity);
        model.addAttribute("entity", entity);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        CardType[] cardTypes = CardType.values();
        model.addAttribute("cardTypes", cardTypes);

        model.addAttribute("disabled", true);
        return "merchantCard/edit";
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    public String remove(Long id) {
        merchantCardDao.delete(id);
        return "merchantCard/list";
    }

    @RequestMapping("list")
    public String list(MerchantCard entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", startsWith().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        Page<MerchantCard> page = merchantCardDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        CardType[] cardTypes = CardType.values();
        model.addAttribute("cardTypes", cardTypes);

        model.addAttribute("entity", entity);
        return "merchantCard/list";
    }

    @RequestMapping(path = "api/list")
    @ResponseBody
    public APIResult list(MerchantCard merchantCard) {
        List<MerchantCard> sourceList = merchantCardDao.findAll(Example.of(merchantCard));
        return new APIResult(sourceList);
    }

}