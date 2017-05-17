package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.NewOrderStatus;
import com.nuanyou.cms.entity.enums.OrderPayType;
import com.nuanyou.cms.entity.enums.OrderType;
import com.nuanyou.cms.entity.enums.RefundStatus;
import com.nuanyou.cms.entity.order.*;
import com.nuanyou.cms.entity.user.PasUserProfile;
import com.nuanyou.cms.model.OrderSave;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.remote.RemoteOrderService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.ExcelUtil;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private RemoteOrderService remoteOrderService;
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
    private OrderLogisticsDao orderLogisticsDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private PasUserProfileDao pasUserProfileDao;
    @Autowired
    private UserTelDao userTelDao;
    @Autowired
    private OrderDirectMailDao directMailDao;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping("add")
    public String add(Order entity) {
        orderDao.save(entity);
        return "order/list";
    }

    @RequestMapping(path = "virtual", method = RequestMethod.GET)
    public String virtual() {
        return "order/virtual";
    }

    @RequestMapping(path = "virtual", method = RequestMethod.POST)
    @ResponseBody
    public APIResult virtual(Long id, Integer number) {
        if (id == null)
            return new APIResult(ResultCodes.MissingParameter);
        if (number == null || number < 1)
            return new APIResult(ResultCodes.MissingParameter);
        Item item = new Item();
        item.setId(id);
        item.setDisplay(true);
        item.setItemType(2);
        List<Item> items = itemDao.findAll(Example.of(item));
        if (items.size() < 1)
            return new APIResult(ResultCodes.NotFoundItem);

        APIResult<OrderSave> result = remoteOrderService.ordersSaveTuanPost(7, id, number);
        if (result.isSuccess()) {
            OrderSave orderSave = result.getData();
            result = remoteOrderService.ordersPayCallbackPost(orderSave.getId());
        }
        return result;
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        Order order = this.orderDao.findOne(id);
        OrderSms sms = this.orderSmsDao.findByOrderId(id);
        OrderLogistics logistics = this.orderLogisticsDao.findByOrderId(id);
        Integer buyNum = this.orderService.getBuyNum(order.getUserId() == null ? null : order.getUserId());
        OrderDirectMail directMail = this.directMailDao.findByOrderId(id);
        PasUserProfile user = null;
        UserTel userTel = null;
        if (order.getUserId() != null) {
            user = pasUserProfileDao.findPartsByUserid(order.getUserId());
            userTel = userTelDao.findByUserid(order.getUserId());
        }
        model.addAttribute("order", order);
        model.addAttribute("sms", sms);
        model.addAttribute("logistics", logistics);
        model.addAttribute("buyNum", buyNum);
        model.addAttribute("directMail", directMail);
        model.addAttribute("user", user);
        model.addAttribute("userTel", userTel);
        return "order/edit";
    }


    @RequestMapping("update")
    public String update(Order entity) throws IOException {
        orderService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
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


    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, TimeCondition time) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");
        List<Country> countries = this.countryDao.findAll();
        List<OrderType> orderTypes = Arrays.asList(OrderType.values());
        List<OrderPayType> orderPayTypes = Arrays.asList(OrderPayType.values());
        List<NewOrderStatus> newOrderStatuses = Arrays.asList(NewOrderStatus.values());
        List<Merchant> merchants = this.merchantService.getIdNameList();
        Page<Order> page = orderService.findByCondition(index, entity, time, pageable);
        for (Order order : page.getContent()) {
            PasUserProfile user = pasUserProfileDao.findPartsByUserid(order.getUserId());
            if (user != null) {
                order.setUser(user);
            } else {
                order.setUser(null);
            }
        }
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        model.addAttribute("orderTypes", orderTypes);
        model.addAttribute("newOrderStatuses", newOrderStatuses);
        model.addAttribute("orderPayTypes", orderPayTypes);
        model.addAttribute("merchants", merchants);
        model.addAttribute("time", time);
        return "order/list";
    }

    @RequestMapping("refundList")
    public String refundList(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, TimeCondition time) {
        Page<Order> page = orderService.findRefundByCondition(index, entity, time);
        for (Order order : page.getContent()) {
            //注意：此处为避免查询慢，只查询userid和nickname字段
            PasUserProfile userProfile = pasUserProfileDao.findPartsByUserid(order.getUserId());
            if (userProfile != null) {
                order.setUser(userProfile);
            }
        }
        List<Country> countries = this.countryDao.findAll();
        model.addAttribute("countries", countries);
        List<RefundStatus> refundStatuses = new ArrayList<RefundStatus>(
                Arrays.asList(RefundStatus.values())
        );
        refundStatuses.remove(0);
        model.addAttribute("refundStatuses", refundStatuses);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("time", time);
        return "order/refundList";
    }

    @RequestMapping(value = "count", method = RequestMethod.POST)
    @ResponseBody
    public APIResult count(ViewOrderExport entity, TimeCondition time) throws IOException {
        long size = this.orderService.countViewOrderExports(entity, time);
        if (size > 2000)
            return new APIResult(ResultCodes.Fail, ": 数据大于2000条，请缩小筛选范围后导出。");
        return new APIResult();
    }


    @RequestMapping("export")
    public void export(ViewOrderExport entity, TimeCondition time, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("订单列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));
        Long begin = System.currentTimeMillis();
        List<ViewOrderExport> page = this.orderService.findExportByCondition(entity, time, null);
        Long end = System.currentTimeMillis();
        log.info("read data from sql:" + (end - begin) / 1000 + "s");
        LinkedHashMap<String, String> propertyHeaderMap = new LinkedHashMap<>();
        propertyHeaderMap.put("id", "ID");
        propertyHeaderMap.put("ordersn", "订单编号");
        propertyHeaderMap.put("transactionid", "订单流水号");
        propertyHeaderMap.put("transactionid", "订单流水号");
        propertyHeaderMap.put("orderstatus.name", "订单状态");
        propertyHeaderMap.put("refundstatus.name", "退款状态");
        propertyHeaderMap.put("createtime", "下单时间");
        propertyHeaderMap.put("sceneid", "渠道");
        propertyHeaderMap.put("scenetime", "渠道时间");
        propertyHeaderMap.put("tempsceneid", "临时渠道");
        propertyHeaderMap.put("tempscenetime", "临时渠道时间");
        propertyHeaderMap.put("ordertype.name", "订单类型");
        propertyHeaderMap.put("paytype.name", "支付类型");
        propertyHeaderMap.put("platform.name", "来源平台");
        propertyHeaderMap.put("os.name", "来源系统");
        propertyHeaderMap.put("ordercode", "使用码");
        propertyHeaderMap.put("mchid", "商户ID");
        propertyHeaderMap.put("merchantname", "商户中文名称");
        propertyHeaderMap.put("merchantkpname", "商户本地名称");
        propertyHeaderMap.put("userId", "userId");
        propertyHeaderMap.put("nickname", "用户名");
        propertyHeaderMap.put("couponInfo", "优惠券/面值/本地面值");
        propertyHeaderMap.put("merchantpriceF", "商户价(本地)");
        propertyHeaderMap.put("kppriceF", "总价(本地)");
        propertyHeaderMap.put("okppriceF", "原价(本地)");
        propertyHeaderMap.put("payableF", "总价(人民币)");
        propertyHeaderMap.put("opriceF", "原价(人民币)");
        propertyHeaderMap.put("youfurmbreduceF", "优付优惠（人民币）");
        propertyHeaderMap.put("youfulocalereduceF", "优付优惠（本地）");
        propertyHeaderMap.put("mchrmbreduceF", "商户优惠（人民币）");
        propertyHeaderMap.put("mchlocalereduceF", "商户优惠（本地）");
        propertyHeaderMap.put("usetime", "使用时间");
        propertyHeaderMap.put("address", "地址");
        propertyHeaderMap.put("postalcode", "邮编");
        propertyHeaderMap.put("province", "省会");
        propertyHeaderMap.put("district", "区");
        propertyHeaderMap.put("city", "城市");
        propertyHeaderMap.put("tel", "订单手机号");
        propertyHeaderMap.put("username", "收货人");
        propertyHeaderMap.put("postagermb", "邮费(人民币)");
        propertyHeaderMap.put("postage", "邮费(本地)");
        propertyHeaderMap.put("fullOrderItems", "商品");
        Long begin_w = System.currentTimeMillis();
        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, page);
        Long end_w = System.currentTimeMillis();
        log.info("write into excel:" + (end_w - begin_w) / 1000 + "s");
        OutputStream os = response.getOutputStream();
        ex.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping("refundList/export")
    public void export(Order entity, TimeCondition time, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=" + "UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("退款订单列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));

        //BeanUtils.cleanEmpty(entity);
        List<Order> list = orderService.findRefundByCondition(entity, time);

        Map<Long, PasUserProfile> userMap = new HashMap<>();
        for (Order order : list) {
            Long userId = order.getUserId();
            if (userId == null)
                continue;
            PasUserProfile user = userMap.get(userId);
            if (user == null) {
                user = new PasUserProfile();
                userMap.put(userId, user);
            }
            order.setUser(user);
        }
        if (!userMap.isEmpty()) {
            List<PasUserProfile> users = pasUserProfileDao.findByUserid(userMap.keySet());
            for (PasUserProfile user : users) {
                PasUserProfile pasUserProfile = userMap.get(user.getUserid());
                pasUserProfile.setNickname(user.getNickname());
            }
        }
        LinkedHashMap<String, String> propertyHeaderMap = new LinkedHashMap<>();
        propertyHeaderMap.put("id", "ID");
        propertyHeaderMap.put("ordersn", "订单编号");
        propertyHeaderMap.put("transactionid", "订单流水号");
        propertyHeaderMap.put("createtime", "下单时间");
        propertyHeaderMap.put("orderstatus.name", "订单状态");
        propertyHeaderMap.put("ordertype.name", "订单类型");
        propertyHeaderMap.put("merchant.name", "商户中文名称");
        propertyHeaderMap.put("userId", "退款人");
        propertyHeaderMap.put("merchant.id", "商户ID");
        propertyHeaderMap.put("user.nickname", "退款人名称");
        propertyHeaderMap.put("refundstatus.name", "退款状态");
        propertyHeaderMap.put("refundreason", "退款理由");
        propertyHeaderMap.put("refundtime", "申请退款时间");
        propertyHeaderMap.put("refundaudittime", "处理时间");
        propertyHeaderMap.put("usetime", "使用时间");
        propertyHeaderMap.put("refundsource.name", "来源");
        propertyHeaderMap.put("refundremark", "备注");
        propertyHeaderMap.put("ordercode", "使用码");
        propertyHeaderMap.put("paytype.name", "支付类型");
        propertyHeaderMap.put("kppriceF", "总价(本地)");
        propertyHeaderMap.put("okppriceF", "原价(本地)");
        propertyHeaderMap.put("payableF", "总价(人民币)");
        propertyHeaderMap.put("opriceF", "原价(人民币)");
        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, list);
        OutputStream os = response.getOutputStream();
        ex.write(os);
        os.flush();
        os.close();
    }


    @RequestMapping("refund")
    @ResponseBody
    public APIResult refund(Order entity) {
        this.orderService.refund(entity);
        return new APIResult<>(ResultCodes.Success);
    }


    @RequestMapping(path = "refundEdit", method = RequestMethod.GET)
    public String refundEdit(Long id, Model model, Integer type) {
        Order entity = null;
        if (id != null) {
            entity = orderDao.findOne(id);
        }
        OrderRefundLog log = this.logDao.findByOrderId(id);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("log", log);
        return "order/refundEdit";
    }

    @RequestMapping(path = "validate", method = RequestMethod.POST)
    public String validate(Long id, Integer type, HttpServletResponse response, String cmsusername) throws IOException {
        this.orderService.validate(id, type, cmsusername);
        response.sendRedirect("../order/refundEdit?refundEdit=3&id=" + id);
        return null;
    }


}

