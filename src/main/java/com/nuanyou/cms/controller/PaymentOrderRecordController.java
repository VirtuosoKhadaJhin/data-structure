package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.PaymentOrderMethod;
import com.nuanyou.cms.entity.enums.PaymentOrderStatus;
import com.nuanyou.cms.entity.enums.PaymentResultStatus;
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

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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

    @Value("${paymentRecord.service-address}")
    private String paymentServiceAddress;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 订单支付记录列表
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(PaymentRecordRequestVo requestVo, Model model, HttpServletRequest request) {
        Page<PaymentOrderRecordVo> records = paymentOrderRecordService.findAllPaymentOrderRecord(requestVo);
        List<Merchant> merchants = merchantService.getIdNameList();
        LinkedHashMap<String, Date> searchDateMap = (LinkedHashMap<String, Date>) request.getSession().getAttribute("searchDateMap");
        model.addAttribute("paymentMethods", PaymentOrderMethod.values());
        model.addAttribute("paymentStatuses", PaymentOrderStatus.values());
        model.addAttribute("page", records);
        model.addAttribute("requestVo", requestVo);
        model.addAttribute("searchDateMap", searchDateMap);
        model.addAttribute("merchants", merchants);
        return "paymentRecord/list";
    }

    /**
     * 查询支付订单的最新状态
     *
     * @param orderId
     * @return
     */
    @RequestMapping("findPaymentOrderRecord")
    @ResponseBody
    public APIResult findPaymentOrderRecord(@RequestParam("app") String app, @RequestParam("orderId") Long orderId) {
        PaymentOrderRecordVo vo = paymentOrderRecordService.findPaymentOrderRecord(orderId);
        return new APIResult<>(vo);
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
    public APIResult searchRecord(@RequestParam("app") String app, @RequestParam("orderId") Long orderId, Model model, HttpServletRequest request) {
        APIResult result = new APIResult<>(ResultCodes.Success);
        String uri = paymentServiceAddress + "/wechat/query/" + app + "/kr/" + orderId;
        try {
            CodePayResponse response = httpService.doGetJson(new URI(uri));
            CodePayResponse.Result data = response.getData();
            result.setData(data);
            result.setCode(response.getCode());
            result.setMsg(response.getMsg());
            if (response.isSuccess() && PaymentResultStatus.toEnum(data.getStatus()) == PaymentResultStatus.PAY_SUCCES) {
                request.setAttribute("SEARCH_PAYMENT_ORDER_RECORD_SUCCESS", orderId);
            }
        } catch (Exception e) {
            result.setCode(ResultCodes.Fail.getCode());
            _LOGGER.error("Request a single query order payment information failure!", e);
        }
        return result;
    }
}
