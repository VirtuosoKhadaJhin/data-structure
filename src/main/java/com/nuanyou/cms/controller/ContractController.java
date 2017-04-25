package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.Contracts;
import com.nuanyou.cms.service.ContractService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

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


}
