package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.CodePayResponse;
import com.nuanyou.cms.model.PaymentOrderRecordVo;
import com.nuanyou.cms.model.PaymentRecordRequestVo;
import com.nuanyou.cms.service.HttpService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.PaymentOrderRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Byron on 2017/6/12.
 */

@Controller
@RequestMapping("paymentOrderRecord")
public class PaymentOrderRecordController {

    private static final Logger _LOGGER = LoggerFactory.getLogger(PaymentOrderRecordController.class);

    @Autowired
    private PaymentOrderRecordService paymentOrderRecordService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private HttpService httpService;

    @Value("${payment_service_address}")
    private String paymentServiceAddress;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 单个查询支付订单记录
     *
     * @param app
     * @param orderId
     * @return
     */
    @RequestMapping("searchRecord")
    @ResponseBody
    public APIResult searchRecord(@RequestParam("app") String app, @RequestParam("orderId") Long orderId) {
        APIResult result = new APIResult<>(ResultCodes.Success);
        String uri = paymentServiceAddress + "/wechat/query/" + app + "/kr/" + orderId;
        try {
            CodePayResponse response = httpService.doGetJson(new URI(uri));
            result.setData(response.getData());
            result.setCode(response.getCode());
            result.setMsg(response.getMsg());
        } catch (Exception e) {
            result.setCode(ResultCodes.Fail.getCode());
            _LOGGER.error("Request a single query order payment information failure!", e);
        }
        return result;
    }

    /**
     * 订单支付记录列表
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(PaymentRecordRequestVo requestVo, Model model) {
        Page<PaymentOrderRecordVo> records = paymentOrderRecordService.findAllPaymentOrderRecord ( requestVo );
        List<Merchant> merchants = merchantService.getIdNameList ();
        model.addAttribute("page", records);
        model.addAttribute ( "requestVo", requestVo );
        model.addAttribute ( "merchants", merchants );
        return "paymentRecord/list";
    }
}
