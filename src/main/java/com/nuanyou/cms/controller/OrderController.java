package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.OrderPayType;
import com.nuanyou.cms.entity.enums.OrderType;
import com.nuanyou.cms.entity.order.*;
import com.nuanyou.cms.model.OrderDetail;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundLogDao logDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderSmsDao orderSmsDao;


    @Autowired
    private OrderSubsidyDao orderSubsidyDao;

    @Autowired
    private OrderLogisticsDao orderLogisticsDao;


    @RequestMapping("add")
    public String add(Order entity) {
        orderDao.save(entity);
        return "order/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        Order order=this.orderDao.findOne(id);
        OrderSms sms=this.orderSmsDao.findByOrderId(id);
        OrderLogistics logistics=this.orderLogisticsDao.findByOrderId(id);
        Integer buyNum=this.orderService.getBuyNum(order);
        OrderDetail orderDetail=new OrderDetail(
                sms==null?null:sms.getCode(),
                sms==null?null:sms.getTimes(),
                logistics==null?null:logistics.getAddress(),
                order==null?null:order.getYoufurmbreduce(),
                order==null?null:order.getYoufulocalereduce(),
                order==null?null:order.getMchrmbreduce(),
                order==null?null:order.getMchlocalereduce(),
                buyNum
        );
        model.addAttribute("orderDetail",orderDetail);
        return "order/edit";
    }




    @RequestMapping("update")
    public String update(Order entity, HttpServletResponse response) throws IOException {
        orderService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, TimeCondition time) {
        List<Country> countries = this.countryDao.findAll();
        List<OrderType> orderTypes= Arrays.asList( OrderType.values());
        List<OrderPayType> orderPayTypes=Arrays.asList( OrderPayType.values());
        List<Merchant> merchants = this.merchantService.getIdNameList();
        Page<Order> page = orderService.findByCondition(index, entity, time);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        model.addAttribute("orderTypes", orderTypes);
        model.addAttribute("orderPayTypes", orderPayTypes);
        model.addAttribute("merchants", merchants);
        model.addAttribute("time", time);
        return "order/list";
    }


    @RequestMapping("api/detail")
    @ResponseBody
    public APIResult detail(Long id) {
        Map<String,Object> map=new HashMap<String,Object>();
        Order order=this.orderDao.findOne(id);
        OrderSms sms=this.orderSmsDao.findByOrderId(id);
        OrderSubsidy subsidy=this.orderSubsidyDao.findByOrderId(id);
        OrderLogistics logistics=this.orderLogisticsDao.findByOrderId(id);
        Integer buyNum=this.orderService.getBuyNum(order);
        OrderDetail orderDetail=new OrderDetail(
                sms==null?null:sms.getCode(),
                sms==null?null:sms.getTimes(),
                logistics==null?null:logistics.getAddress(),
                subsidy==null?null:subsidy.getYoufusubsidyprice(),
                subsidy==null?null:subsidy.getYoufusubsidykpprice(),
                subsidy==null?null:subsidy.getMchsubsidyprice(),
                subsidy==null?null:subsidy.getMchsubsidykpprice(),
                buyNum
        );
        map.put("orderDetail",orderDetail);
        return new APIResult(map);
    }





    @RequestMapping("itemList")
    public String itemList(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(entity);
        Page<OrderItem> page = orderItemDao.findAll(Example.of(orderItem), pageable);
        model.addAttribute("page", page);
        return "order/orderItemlist";
    }


    @RequestMapping("refund")
    @ResponseBody
    public APIResult refund(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, HttpServletResponse response) {
        this.orderService.refund(entity);
        return new APIResult<>(ResultCodes.Success);
    }




    @RequestMapping("refundList")
    public String refundList(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, TimeCondition time) {
        Page<Order> page = orderService.findRefundByCondition(index, entity, time);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("time", time);
        return "order/refundList";
    }



    @RequestMapping(path = "refundEdit", method = RequestMethod.GET)
    public String refundEdit(Long id, Model model, Integer type) {
        Order entity = null;
        if (id != null) {
            entity = orderDao.findOne(id);
        }
        OrderRefundLog log=this.logDao.findByOrderId(id);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("log", log);
        return "order/refundEdit";
    }

    @RequestMapping(path = "validate", method = RequestMethod.POST)
    public String validate(Long id, Model model,Integer type,HttpServletResponse response,String cmsusername) throws IOException {
        this.orderService.validate(id,type,cmsusername);
       response.sendRedirect( "../order/refundEdit?refundEdit=3&id="+id);
        return null;
    }



}

