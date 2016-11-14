package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.CommentReplyDao;
import com.nuanyou.cms.dao.FakeOrderDao;
import com.nuanyou.cms.dao.FakeUserDao;
import com.nuanyou.cms.entity.FakeOrder;
import com.nuanyou.cms.entity.FakeUser;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.FakeOrderService;
import com.nuanyou.cms.service.FakeUserService;
import com.nuanyou.cms.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("fakeOrder")
public class FakeOrderController {

    @Autowired
    private FakeOrderService fakeOrderService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private FakeOrderDao fakeOrderDao;

    @Autowired
    private CommentReplyDao commentReplyDao;

    @Autowired
    private FakeUserDao fakeUserDao;
    @Autowired
    private FakeUserService fakeUserService;

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<Merchant> merchants = this.merchantService.getIdNameList();
        List<FakeUser> fakeUsers=fakeUserService.getIdNameList();
        FakeOrder entity = null;
        if (id != null) {
            entity = fakeOrderDao.findOne(id);
        }
        model.addAttribute("merchants", merchants);
        model.addAttribute("fakeUsers", fakeUsers);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "fakeOrder/edit";
    }


    @RequestMapping("update")
    public String update(FakeOrder entity, HttpServletResponse response) throws IOException {
        fakeOrderService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }


    /**
     * 评价美化
     */
    @RequestMapping("list")
    public String listFake(FakeOrder entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        Page<FakeOrder> page = fakeOrderService.findByCondition(entity, pageable);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "fakeOrder/list";
    }

}