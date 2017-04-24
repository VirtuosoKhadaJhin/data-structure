package com.nuanyou.cms.controller;

import com.nuanyou.cms.entity.Banner;
import com.nuanyou.cms.model.contract.Contract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contract")
public class ContractController {

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Banner entity, Model model) {

        List<Contract> contracts=new ArrayList<>();
        Contract contract=new Contract();
        contract.setId(3L);
        contracts.add(contract);

        model.addAttribute(contracts);
        return "contract/list";
    }


    @RequestMapping("filedList")
    public String filedList(@RequestParam(required = false, defaultValue = "1") int index, Banner entity, Model model) {

        List<Contract> contracts=new ArrayList<>();
        Contract contract=new Contract();
        contract.setId(3L);
        contracts.add(contract);
        model.addAttribute(contracts);
        return "contract/filedList";
    }


}
