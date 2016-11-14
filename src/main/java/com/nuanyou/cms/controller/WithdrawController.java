package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.PageRemote;
import com.nuanyou.cms.model.Withdraw;
import com.nuanyou.cms.model.WithdrawParam;
import com.nuanyou.cms.service.RemoteService;
import com.nuanyou.cms.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private RemoteService remoteService;



    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") Integer index, Model model,
                       WithdrawParam withdrawVO) {
        PageRemote<Withdraw> page=this.remoteService.withdraw(withdrawVO,index);
        model.addAttribute("page",page);
        model.addAttribute("entity",withdrawVO);
        return "withdraw/list";
    }

    @RequestMapping("api/operate")
    @ResponseBody
    public APIResult operate(String type,Integer operateid,String message) {
        this.remoteService.operateWithdraw(type,operateid,message);
        return new APIResult<>(ResultCodes.Success);
    }






}

