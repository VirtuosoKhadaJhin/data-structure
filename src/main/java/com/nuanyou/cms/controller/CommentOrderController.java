package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CommentOrderDao;
import com.nuanyou.cms.dao.FakeUserDao;
import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.entity.CommentReply;
import com.nuanyou.cms.entity.FakeUser;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CommentOrderService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("commentOrder")
public class CommentOrderController {

    @Autowired
    private CommentOrderService commentOrderService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CommentOrderDao commentOrderDao;

    @Autowired
    private FakeUserDao fakeUserDao;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        List<FakeUser> fakeUsers = fakeUserDao.getIdNameList();
        model.addAttribute("fakeUsers", fakeUsers);

        if (id != null) {
            CommentOrder entity = commentOrderDao.findOne(id);
            model.addAttribute("entity", entity);
        }
        return "commentOrder/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public APIResult detail(Long id) {
        CommentOrder entity = commentOrderDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(CommentOrder entity, Model model) {
        entity.setAnonymous(false);
        if (entity.getDisplay() == null)
            entity.setDisplay(true);

        if (entity.getType() == null) {
            entity.setType(2);
        }
        entity = commentOrderService.saveNotNull(entity);
        model.addAttribute("entity", entity);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        List<FakeUser> fakeUsers = fakeUserDao.getIdNameList();
        model.addAttribute("fakeUsers", fakeUsers);

        model.addAttribute("disabled", true);
        return "commentOrder/edit";
    }

    @RequestMapping(path = "reply", method = RequestMethod.POST)
    @ResponseBody
    public APIResult reply(CommentReply entity) {
        commentOrderService.reply(entity);
        return new APIResult();
    }

    @RequestMapping(path = "replies", method = RequestMethod.GET)
    @ResponseBody
    public APIResult replyList(Long id) {
        List<CommentReply> replies = commentOrderService.findReply(id);
        return new APIResult(replies);
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        commentOrderService.delete(id);
        return new APIResult();
    }

    /**
     * 评价管理
     */
    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String scoreStr,
                       TimeCondition time, CommentOrder entity, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "replyTime", "createTime");

        Page<CommentOrder> page = commentOrderService.findByCondition(entity, time, scoreStr, pageable);
        model.addAttribute("page", page);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        model.addAttribute("entity", entity);
        model.addAttribute("scoreStr", scoreStr);
        model.addAttribute("time", time);
        return "commentOrder/list";
    }

    /**
     * 评价美化
     */
    @RequestMapping("listFake")
    public String listFake(CommentOrder entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        entity.setType(2);

        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "replyTime", "createTime");
        Page<CommentOrder> page = commentOrderDao.findAll(Example.of(entity), pageable);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);
        return "commentOrder/listFake";
    }

}