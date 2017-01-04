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
import com.nuanyou.cms.util.ConvertFileEncoding;
import com.nuanyou.cms.util.DateUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
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
    private OrderSubsidyDao orderSubsidyDao;
    @Autowired
    private OrderLogisticsDao orderLogisticsDao;

    @Autowired
    private PasUserProfileDao pasUserProfileDao;

    private static String timePattern="yyyy-MM-dd HH:mm:ss";
    private static String decimalPattern="#0.00";

    @RequestMapping("add")
    public String add(Order entity) {
        orderDao.save(entity);
        return "order/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        OrderDetail orderDetail = getOrderDetail(id);
        model.addAttribute("orderDetail",orderDetail);
        return "order/edit";
    }

    private OrderDetail getOrderDetail(Long id) {
        Order order=this.orderDao.findOne(id);
        OrderSms sms=this.orderSmsDao.findByOrderId(id);
        OrderLogistics logistics=this.orderLogisticsDao.findByOrderId(id);
        Integer buyNum=this.orderService.getBuyNum(order);
        return new OrderDetail(
                sms==null?null:sms.getCode(),
                sms==null?null:sms.getTimes(),
                logistics==null?null:logistics.getAddress(),
                order==null?null:order.getYoufurmbreduce(),
                order==null?null:order.getYoufulocalereduce(),
                order==null?null:order.getMchrmbreduce(),
                    order==null?null:order.getMchlocalereduce(),
                buyNum
        );
    }


    @RequestMapping("update")
    public String update(Order entity, HttpServletResponse response) throws IOException {
        orderService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Order entity, Model model, TimeCondition time) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");
        List<Country> countries = this.countryDao.findAll();
        List<OrderType> orderTypes= Arrays.asList( OrderType.values());
        List<OrderPayType> orderPayTypes=Arrays.asList( OrderPayType.values());
        List<NewOrderStatus> newOrderStatuses=Arrays.asList( NewOrderStatus.values());
        List<RefundStatus> refundStatuses=Arrays.asList( RefundStatus.values());
        List<Merchant> merchants = this.merchantService.getIdNameList();
        Page<Order> page = orderService.findByCondition(index, entity, time,pageable);
        for (Order order : page.getContent()) {
            PasUserProfile user=this.pasUserProfileDao.findByUserid(order.getUserId());
            if(user!=null){
                order.setUser(user);
            }else{
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



    @RequestMapping("export")
    public void export(@RequestParam(required = false, defaultValue = "1") int index,  Model model,
                       HttpServletRequest request,HttpServletResponse response,
                       ViewOrderExport entity, TimeCondition time) throws IOException {
        //"短信通知状态","短信通知次数","购买次数","优付优惠（本地）","优付优惠（人民币）","商户优惠（本地）","商户优惠（人民币）","收货地址"
        String[] titles=new String[]{
                         "序号","ID","订单ID","渠道","订单类型","支付类型","来源平台","来源系统","使用码","商户中文名称",
                         "商户本地名称","用户ID", "总价(本地)","原价(本地)","总价(人民币)","原价(人民币)","订单状态","下单时间", "使用时间","地址",
                         "邮编","省会","区","城市","电话"};
        String filename = "order.xls";
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("订单列表");
        HSSFRow row = sheet.createRow(0);
        Page<ViewOrderExport> page = orderService.findExportByCondition(index, entity, time,null);
        Long begin=System.currentTimeMillis();
        for (int i = 0; i < titles.length; i++) {
            HSSFCell c = row.createCell(i);
            c.setCellValue(titles[i]);
        }
        Long end=System.currentTimeMillis();
        System.out.println("时间差:"+(end-begin)/1000);
        NumberTool numberFormatter=new NumberTool();
        DateTool dateFormatter=new DateTool();
        FillContent(sheet, page, numberFormatter, dateFormatter);
        setResponseOut(filename,workbook,request,response);
    }

    private void FillContent(HSSFSheet sheet, Page<ViewOrderExport> page, NumberTool numberFormatter, DateTool dateFormatter) {
        for (int i = 0; i < page.getContent().size(); i++) {
            HSSFRow r = sheet.createRow(i+1);
            ViewOrderExport each=page.getContent().get(i);
            r.createCell(0).setCellValue(i+1);
            r.createCell(1).setCellValue(each.getId());
            r.createCell(2).setCellValue(each.getOrdersn());
            r.createCell(3).setCellValue(each.getSceneid());
            r.createCell(4).setCellValue(each.getOrdertype()==null?"":each.getOrdertype().getName());
            r.createCell(5).setCellValue(each.getPaytype()==null?"":each.getPaytype().getName());
            r.createCell(6).setCellValue(each.getPlatform()==null?"":each.getPlatform().getName());
            r.createCell(7).setCellValue(each.getOs()==null?"":each.getOs().getName());
            r.createCell(8).setCellValue(each.getOrdercode());
            r.createCell(9).setCellValue(each.getMerchant()==null|| StringUtils.isEmpty(each.getMerchant().getName())?"":each.getMerchant().getName());
            r.createCell(10).setCellValue(each.getMerchant()==null|| StringUtils.isEmpty(each.getMerchant().getKpname())?"":each.getMerchant().getKpname());
            r.createCell(11).setCellValue(each.getUserId()==null?"":each.getUserId().toString());
            //r.createCell(12).setCellValue(each.getUsername()==null?"":each.getUsername());
            r.createCell(12).setCellValue(numberFormatter.format(decimalPattern,each.getKpprice()));
            r.createCell(13).setCellValue(numberFormatter.format(decimalPattern,each.getOkpprice()));
            r.createCell(14).setCellValue(each.getPayable()==null?each.getPrice().doubleValue():each.getPayable().doubleValue());
            r.createCell(15).setCellValue(each.getOprice()==null?"":each.getOprice().toPlainString());
            //r.createCell(17).setCellValue(each.getMerchantsubsidy()==null?"":each.getMerchantsubsidy().toPlainString());
            r.createCell(16).setCellValue(each.getStatusname());
            r.createCell(17).setCellValue(dateFormatter.format(timePattern,each.getCreatetime()));
            r.createCell(18).setCellValue(dateFormatter.format(timePattern,each.getUsetime()));
            r.createCell(19).setCellValue(each.getAddress()==null?"":each.getAddress());
            r.createCell(20).setCellValue(each.getPostalcode()==null?"":each.getPostalcode());
            r.createCell(21).setCellValue(each.getProvince()==null?"":each.getProvince());
            r.createCell(22).setCellValue(each.getDistrict()==null?"":each.getDistrict());
            r.createCell(23).setCellValue(each.getCity()==null?"":each.getCity());
            r.createCell(24).setCellValue(each.getTel()==null?"":each.getTel());
        }
    }



    private void setResponseOut(String filename, HSSFWorkbook workbook,HttpServletRequest request,HttpServletResponse response) throws IOException {
        filename = ConvertFileEncoding.encodeFilename(filename, request);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        workbook.write(response.getOutputStream());
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
        List<RefundStatus> refundStatuses=Arrays.asList( RefundStatus.values());
        model.addAttribute("refundStatuses", refundStatuses);
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

