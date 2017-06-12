package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.PaymentOrderRecordVo;
import com.nuanyou.cms.model.PaymentRecordRequestVo;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.PaymentOrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Byron on 2017/6/12.
 */

@Controller
@RequestMapping("paymentOrderRecord")
public class PaymentOrderRecordController {

    @Autowired
    private PaymentOrderRecordService paymentOrderRecordService;

    @Autowired
    private MerchantService merchantService;

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
        model.addAttribute ( "records", records );
        model.addAttribute ( "requestVo", requestVo );
        model.addAttribute ( "merchants", merchants );
        return "paymentRecord/list";
    }

}
