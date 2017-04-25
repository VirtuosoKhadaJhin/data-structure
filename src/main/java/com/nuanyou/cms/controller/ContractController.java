package com.nuanyou.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.MerchantSettlementParamConfig;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.Contracts;
import com.nuanyou.cms.remote.AccountService;
import com.nuanyou.cms.remote.ContractService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contract")
public class ContractController {


    @Autowired
    private ContractService contractService;

    @RequestMapping("list")
    public String list(
            Model model,
            @ApiParam(value = "用户id") @RequestParam(value = "userid", required = false) Long userId,
            @ApiParam(value = "商户名称") @RequestParam(value = "merchantname", required = false) String merchantName,
            @ApiParam(value = "商户id") @RequestParam(value = "merchantid", required = false) Long merchantId,
            @ApiParam(value = "合同状态: 1.已驳回 2.审核中 3.未生效 4.已生效(多个值以,分割)") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "合同类型: 为空则查询全部") @RequestParam(value = "templateid", required = false) Long templateid,
            @ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(value = "starttime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @ApiParam(value = "结束时间(yyyy-MM-dd)") @RequestParam(value = "endtime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @ApiParam(value = "页序号，默认从1开始") @RequestParam(value = "page", required = false, defaultValue = "1") Integer index,
            @ApiParam(value = "每页条目数,默认20条") @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {

        APIResult<Contracts> contracts = contractService.list(userId,merchantName,merchantId,status,templateid,startTime,endTime,index,limit);
        Contracts contractsData=(Contracts)contracts.getData();
        Pageable pageable=new PageRequest(index,limit);
        Page<Contract> page=new PageImpl<Contract>(
                contractsData.getList(),
                pageable,
                contractsData.getTotal()
        );
        model.addAttribute("page", page);
        model.addAttribute("merchantname", merchantName);
        model.addAttribute("merchantname", merchantName);
        model.addAttribute("merchantname", merchantName);
        return "contract/list";
    }


    @RequestMapping("filedList")
    public String filedList(
                            Model model,
                            @ApiParam(value = "用户id") @RequestParam(value = "userid", required = false) Long userId,
                            @ApiParam(value = "商户名称") @RequestParam(value = "merchantname", required = false) String merchantName,
                            @ApiParam(value = "商户id") @RequestParam(value = "merchantid", required = false) Long merchantId,
                            @ApiParam(value = "合同状态: 1.已驳回 2.审核中 3.未生效 4.已生效(多个值以,分割)") @RequestParam(value = "status", required = false) String status,
                            @ApiParam(value = "合同类型: 为空则查询全部") @RequestParam(value = "templateid", required = false) Long templateid,
                            @ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(value = "starttime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                            @ApiParam(value = "结束时间(yyyy-MM-dd)") @RequestParam(value = "endtime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                            @ApiParam(value = "页序号，默认从1开始") @RequestParam(value = "page", required = false, defaultValue = "1") Integer index,
                            @ApiParam(value = "每页条目数,默认20条") @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        APIResult<Contracts> contracts = contractService.list(userId,merchantName,merchantId,status,templateid,startTime,endTime,index,limit);
        Contracts contractsData=(Contracts)contracts.getData();
        Pageable pageable=new PageRequest(index,limit);
        Page<Contract> page=new PageImpl<Contract>(
                contractsData.getList(),
                pageable,
                contractsData.getTotal()
        );
        model.addAttribute("page", page);
        model.addAttribute("merchantname", merchantName);
        model.addAttribute("merchantname", merchantName);
        model.addAttribute("merchantname", merchantName);
        return "contract/filedList";
    }

    static Map<Integer,MerchantSettlementParamConfig> config =new HashMap<>();
    static {
        config.put(1,new MerchantSettlementParamConfig(1L,"poundage_huigou","account_period_huigou"));
        config.put(5,new MerchantSettlementParamConfig(5L,"poundage_radio","settle_day"));
        config.put(6,new MerchantSettlementParamConfig(6L,"poundage_radio","settle_day"));

    }


    @Value("${merchantSettlement.default.daytype}")
    private Integer daytype;
    @Value("${merchantSettlement.default.startprice}")
    private BigDecimal startprice;

    @Autowired
    private AccountService accountService;

    @RequestMapping(path = "verify", method = RequestMethod.GET)
    @ResponseBody
    public APIResult verify(Long id, Integer type,Long contractId) throws ParseException {
//        if(true){
//            APIResult res= accountService.add(333L,true,daytype,new BigDecimal(3.4),4L,startprice,new DateTime().toString("yyyy-MM-dd"));
//            System.out.println(res.getCode()+"felix");
//            return  null;
//        }

        //1 通过
        boolean valid=false;
        if(type==1){
            valid=true;
        }else if(type==2){
            valid=false;
        }else{
            throw new APIException(ResultCodes.TypeMismatch);
        }
        APIResult approve = this.contractService.approve(UserHolder.getUser().getUserid(), contractId, valid);
        if(approve.getCode()!=0){
            throw new APIException(ResultCodes.Fail);
        }
        if(type==1){
            //2 得到合同信息
            APIResult<Contract> resDetail = this.contractService.detail(contractId);
            //3插入对账系统
            Contract detail=(Contract)resDetail.getData();
            this.addForAccount(detail);

        }
        return new APIResult<>(ResultCodes.Success);
    }

    private void addForAccount(Contract detail) {
        JSONObject result=JSONObject.parseObject(detail.getParameters());
        BigDecimal poundage=result.getBigDecimal("poundage_huigou")!=null?result.getBigDecimal("poundage_huigou"):
                result.getBigDecimal("account_period_huigou")!=null?result.getBigDecimal("account_period_huigou"):null;
        Long paymentDays=result.getBigDecimal("poundage_radio")!=null?result.getLong("poundage_radio"):
                result.getLong("settle_day")!=null?result.getLong("settle_day"):null;
        if(poundage!=null&&paymentDays!=null){
            Long merchantId=detail.getMchid();
            APIResult res= accountService.add(merchantId,true,daytype,poundage,paymentDays,startprice,new DateTime().toString("yyyy-MM-dd"));
            if(res.getCode()!=0){
                throw new APIException(ResultCodes.AddMerchantSettlementError);
            }
        }else{
            throw new APIException(ResultCodes.PoundageOrPayDaysIsNull);
        }


    }



}
