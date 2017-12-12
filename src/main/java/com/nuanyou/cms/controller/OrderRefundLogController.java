package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.OrderRefundLogDao;
import com.nuanyou.cms.entity.order.OrderRefundLog;
import com.nuanyou.cms.model.mission.MissionDistributeParamVo;
import com.nuanyou.cms.service.OrderRefundService;
import com.nuanyou.cms.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("orderRefundLog")
public class OrderRefundLogController {

    @Autowired
    private OrderRefundLogDao orderRefundLogDao;
    @Autowired
    private OrderRefundService orderRefundService;


    @RequestMapping(path = "add", method = RequestMethod.GET)
    public String add(OrderRefundLog entity, Model model) {
        model.addAttribute("entity", entity);
        return "orderRefundLog/add";
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
        public String edit(Long id, Model model) {
        OrderRefundLog entity = orderRefundLogDao.findOne(id);
        model.addAttribute("entity", entity);

        return "orderRefundLog/edit";
    }


 /* @RequestMapping(path = "validate", method = RequestMethod.GET)
    public String validate(Long id, Model model,Integer type,HttpServletResponse response) throws IOException {
        this.orderRefundService.validate(id,type);
        response.sendRedirect( "../orderRefundLog/edit?type=3&id="+id);
        return "orderRefundLog/edit";
    }*/


    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(OrderRefundLog entity, Model model) {
        entity = orderRefundService.saveNotNull(entity);
        model.addAttribute("entity", entity);
        System.out.println(JsonUtils.toJson(entity));
        return "orderRefundLog/edit";
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    public String remove(Long id, Model model) {
        orderRefundLogDao.delete(id);
        return "orderRefundLog/list";
    }

    @RequestMapping("list")
    public String list(OrderRefundLog entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Page<OrderRefundLog> page = orderRefundService.findByCondition(index, entity);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "orderRefundLog/list";
    }

 /* @RequestMapping("autoHandleRefund")
    @ResponseBody
    public APIResult autoHandleRefund(@RequestParam Long id, @RequestParam String transactionId) throws Exception {
        APIResult apiResult = orderRefundService.autoHandleRefund(id, transactionId);
        return new APIResult(apiResult);
    }
*/
}