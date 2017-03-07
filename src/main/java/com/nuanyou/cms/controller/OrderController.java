package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.NewOrderStatus;
import com.nuanyou.cms.entity.enums.OrderPayType;
import com.nuanyou.cms.entity.enums.OrderType;
import com.nuanyou.cms.entity.enums.RefundStatus;
import com.nuanyou.cms.entity.order.*;
import com.nuanyou.cms.entity.user.PasUserProfile;
import com.nuanyou.cms.model.OrderDetail;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.OrderService;
import com.nuanyou.cms.util.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    private PasUserProfileDao pasUserProfileDao;

    @RequestMapping("add")
    public String add(Order entity) {
        orderDao.save(entity);
        return "order/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        OrderDetail orderDetail = getOrderDetail(id);
        model.addAttribute("orderDetail", orderDetail);
        return "order/edit";
    }

    private OrderDetail getOrderDetail(Long id) {
        Order order = this.orderDao.findOne(id);
        OrderSms sms = this.orderSmsDao.findByOrderId(id);
        OrderLogistics logistics = this.orderLogisticsDao.findByOrderId(id);
        Integer buyNum = this.orderService.getBuyNum(order);
        return new OrderDetail(
                sms == null ? null : sms.getCode(),
                sms == null ? null : sms.getTimes(),
                logistics == null ? null : logistics.getAddress(),
                order == null ? null : order.getYoufurmbreduce(),
                order == null ? null : order.getYoufulocalereduce(),
                order == null ? null : order.getMchrmbreduce(),
                order == null ? null : order.getMchlocalereduce(),
                buyNum
        );
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
        List<RefundStatus> refundStatuses = Arrays.asList(RefundStatus.values());
        model.addAttribute("refundStatuses", refundStatuses);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("time", time);
        return "order/refundList";
    }

    @RequestMapping("export")
    public void export(@RequestParam(required = false, defaultValue = "1") int index, Model model,
                       HttpServletRequest request, HttpServletResponse response,
                       ViewOrderExport entity, TimeCondition time) throws IOException {
        String[] titles = new String[]{
                "序号", "ID", "订单编号", "订单流水号", "渠道", "订单类型", "支付类型", "来源平台", "来源系统", "使用码", "商户ID", "商户中文名称",
                "商户本地名称", "用户ID", "总价(本地)", "原价(本地)", "总价(人民币)", "原价(人民币)", "订单状态", "下单时间", "使用时间", "地址",
                "邮编", "省会", "区", "城市", "电话"};
        String filename = "order.xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("订单列表");
        HSSFRow firstRow = firstSheet.createRow(0);
        this.orderService.putOrderToExcel(index, request, response, entity, time, titles, filename, workbook, firstSheet, firstRow);
    }

    @RequestMapping("refundList/export")
    public void export(Order entity, TimeCondition time, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=" + "UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("退款订单列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));

        BeanUtils.cleanEmpty(entity);
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
        propertyHeaderMap.put("createtime", "下单时间");
        propertyHeaderMap.put("orderstatus.name", "订单状态");
        propertyHeaderMap.put("ordertype.name", "订单类型");
        propertyHeaderMap.put("merchant.name", "商户中文名称");
        propertyHeaderMap.put("userId", "退款人");
        propertyHeaderMap.put("user.nickname", "退款人名称");
        propertyHeaderMap.put("refundstatus.name", "退款状态");
        propertyHeaderMap.put("refundreason", "退款理由");
        propertyHeaderMap.put("refundtime", "申请退款时间");
        propertyHeaderMap.put("refundaudittime", "处理时间");
        propertyHeaderMap.put("refundsource.name", "来源");
        propertyHeaderMap.put("refundremark", "备注");
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

