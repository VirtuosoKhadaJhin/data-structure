package com.nuanyou.cms.controller;

import com.google.common.collect.Maps;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.component.ZxingCode;
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
import com.nuanyou.cms.model.enums.OrderSaleChannel;
import com.nuanyou.cms.remote.service.RemoteOrderService;
import com.nuanyou.cms.service.*;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.util.ExcelUtil;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RemoteOrderService remoteOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundLogDao logDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CountryService countryService;
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
    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("s3")
    private FileClient fileClient;

    @Value("${s3.krCodeMainImgPath}")
    private String krCodeMainImgPath;

    @Value("${s3.jpCodeMainImgPath}")
    private String jpCodeMainImgPath;

    @Value("${s3.thCodeMainImgPath}")
    private String thCodeMainImgPath;

    @Value("${s3.geCodeMainImgPath}")
    private String geCodeMainImgPath;

    @Value("${s3.enCodeMainImgPath}")
    private String enCodeMainImgPath;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("add")
    public String add(Order entity) {
        orderDao.save(entity);
        return "order/list";
    }

    @RequestMapping(path = "virtual", method = RequestMethod.GET)
    public String virtual(Model model) {
        model.addAttribute("channels", OrderSaleChannel.values());
        return "order/virtual";
    }

    @RequestMapping(path = "virtual", method = RequestMethod.POST)
    @ResponseBody
    public APIResult virtual(Long id, Integer number, String channel, String channelOrderNo) {
        if (channel == null || channel == "")
            return new APIResult(ResultCodes.MissingParameter);
        if (channelOrderNo == null || channelOrderNo == "")
            return new APIResult(ResultCodes.MissingParameter);
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

        APIResult<OrderSave> result = remoteOrderService.ordersSaveTuanPost(7, id, number, channel, channelOrderNo);
        if (result.isSuccess()) {
            Long orderId = result.getData().getId();
            OrderSave orderSave = result.getData();
            result = remoteOrderService.ordersPayCallbackPost(orderSave.getId());
            if (result.isSuccess()) {
                return new APIResult(orderId);
            }
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
        List<Country> countries = countryService.getIdNameList();
        List<OrderType> orderTypes = Arrays.asList(OrderType.values());
        List<OrderPayType> orderPayTypes = Arrays.asList(OrderPayType.values());
        List<NewOrderStatus> newOrderStatuses = Arrays.asList(NewOrderStatus.values());
        List<Merchant> merchants = merchantService.getAllIdNameList();
        Page<Order> page = orderService.findByCondition(index, entity, time, pageable);
        Map<Long, Order> maps = Maps.newHashMap();
        for (Order order : page.getContent()) {
            if (order.getUserId() != null) {
                maps.put(order.getUserId(), order);
            }
        }
        Set<Long> userIds = maps.keySet();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<PasUserProfile> userProfiles = pasUserProfileDao.findByUserid(userIds);
            for (PasUserProfile userProfile : userProfiles) {
                Long userId = userProfile.getId();
                if (maps.containsKey(userId)) {
                    Order order = maps.get(userId);
                    order.setUser(userProfile);
                }
            }
        }
        List<Long> countryids = userService.findUserCountryId();
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        model.addAttribute("orderTypes", orderTypes);
        model.addAttribute("newOrderStatuses", newOrderStatuses);
        model.addAttribute("orderPayTypes", orderPayTypes);
        model.addAttribute("merchants", merchants);
        model.addAttribute("time", time);
        model.addAttribute("countryids", countryids);
        return "order/list";
    }

    @RequestMapping("refundList")
    public String refundList(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, TimeCondition time) {
        Page<Order> page = orderService.findRefundByCondition(index, entity, time, null);
        for (Order order : page.getContent()) {
            //注意：此处为避免查询慢，只查询userid和nickname字段
            PasUserProfile userProfile = pasUserProfileDao.findPartsByUserid(order.getUserId());
            if (userProfile != null) {
                order.setUser(userProfile);
            }
        }
        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);
        List<RefundStatus> refundStatuses = new ArrayList<RefundStatus>(
                Arrays.asList(RefundStatus.values())
        );
        List<Long> countryids = userService.findUserCountryId();
        refundStatuses.remove(0);
        model.addAttribute("refundStatuses", refundStatuses);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("time", time);
        model.addAttribute("countryids", countryids);
        return "order/refundList";
    }

    @RequestMapping(value = "count", method = RequestMethod.POST)
    @ResponseBody
    public APIResult count(Order entity, TimeCondition time, String countryids) throws IOException {
        long size = this.orderService.countViewOrderExports(entity, time, (!countryids.equals("[]")) ? CommonUtils.StringToList(countryids) : null);
        if (size > 10000)
            return new APIResult(ResultCodes.Fail, ": 数据大于10000条，请缩小筛选范围后导出。");
        return new APIResult();
    }

    @RequestMapping("export")
    public void export(Order entity, TimeCondition time, String countryids, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("订单列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));
        Long begin = System.currentTimeMillis();
        List<ViewOrderExport> page = this.orderService.findExportByCondition(entity, time, (!countryids.equals("[]")) ? CommonUtils.StringToList(countryids) : null, null);
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
        propertyHeaderMap.put("settlement", "清算机构");
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
    public void exportRefund(Order entity, TimeCondition time, String countryids, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("退款订单列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));

        //BeanUtils.cleanEmpty(entity);
        List<Order> list = orderService.findRefundByCondition(entity, time, (!countryids.equals("[]")) ? CommonUtils.StringToList(countryids) : null);

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
        propertyHeaderMap.put("settlement", "清分机构");
        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, list);
        OutputStream os = response.getOutputStream();
        ex.write(os);
        os.flush();
        os.close();
    }

    /**
     * 退款申请
     * @param entity
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @RequestMapping("refund")
    @ResponseBody
    public APIResult refund(Order entity) throws IOException, URISyntaxException {
        this.orderService.refund(entity);
        return new APIResult<>(ResultCodes.Success);
    }

/*  @RequestMapping(path = "refundEdit", method = RequestMethod.GET)
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
    }*/

 /*  /**
     * 手动通过
     * @param id
     * @param type
     * @param response
     * @param cmsusername
     * @return
     * @throws IOException
     *//*
    @RequestMapping(path = "validate", method = RequestMethod.POST)
    public String validate(Long id, Integer type, HttpServletResponse response, String cmsusername) throws IOException {
        this.orderService.validate(id, type, cmsusername);
        response.sendRedirect("../order/refundEdit?refundEdit=3&id=" + id);
        return null;
    }*/

    /**
     * 下载条形码合成图片
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(path = "downloadBarcode")
    public  void  downloadBarcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/x-msdownload");
        String  fileName = "核销条形码.jpg";
        String fname ="";
        // 解决中文文件名乱码问题
        if (request.getHeader("User-Agent").toLowerCase()
                .indexOf("firefox") > 0) {
            fname = new String(fileName.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
        } else if (request.getHeader("User-Agent").toUpperCase()
                .indexOf("MSIE") > 0) {
            fname = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        }else if (request.getHeader("User-Agent").toUpperCase()
                .indexOf("CHROME") > 0) {
            fname = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// 谷歌
        }
        response.setHeader("Content-Disposition", "attachment;filename="+fname);
        String keycode = request.getParameter("VerificationCode");
        String countryid = request.getParameter("countryid");
        String titleInfo = enCodeMainImgPath;
        switch(countryid){
            case "1": titleInfo = krCodeMainImgPath;break;
            case "2": titleInfo = jpCodeMainImgPath;break;
            case "3": titleInfo = thCodeMainImgPath;break;
            case "4": titleInfo = geCodeMainImgPath;break;
            default:break;
        }
        if (keycode != null && !"".equals(keycode)) {
            OutputStream out = response.getOutputStream();
            ZxingCode.encode(out,keycode, titleInfo, fileClient);
        }
    }

    /**
     * 退款操作通过还是拒绝
     * @param orderId
     * @param operationType
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "refundOperate")
    public APIResult refundOperate(Integer operationType, Long orderId) throws IOException {
        return orderService.refundOperate(operationType,orderId);
    }

    /**
     * 退款通过后操作
     * @param operationType
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "checkPass")
    public APIResult checkPass(Integer operationType, Long orderId) throws IOException {
        orderService.checkPass(operationType,orderId);
        return new APIResult();
    }
}