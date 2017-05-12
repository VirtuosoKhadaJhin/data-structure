package com.nuanyou.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.output.Contracts;
import com.nuanyou.cms.remote.AccountService;
import com.nuanyou.cms.remote.ContractService;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.util.JsonUtils;
import io.swagger.annotations.ApiParam;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contract")
public class ContractController {


    @Autowired
    private ContractService contractService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CountryService countryService;
    @Value("${merchantSettlement.default.daytype}")
    private Integer daytype;
    @Value("${merchantSettlement.default.startprice}")
    private BigDecimal startprice;


    @Value("${contractConfig.poundageNames}")
    private String poundageNames;

    @Value("${contractConfig.paymentDaysNames}")
    private String paymentDaysNames;


    @RequestMapping("list")
    public String list(Model model,
                       @RequestParam(value = "userid", required = false) Long userId,
                       @RequestParam(value = "merchantid", required = false) Long merchantId,
                       @RequestParam(value = "id", required = false) Long id,
                       @RequestParam(value = "merchantname", required = false) String merchantName,
                       @RequestParam(value = "countryId", required = false) Long countryId,
                       @RequestParam(value = "status", required = false) Integer[] status,
                       @RequestParam(value = "templateid", required = false) Long[] templateid,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "starttime", required = false) String startTime,
                       @RequestParam(value = "endtime", required = false) String endTime,
                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                       @RequestParam(value = "businessLicense", required = false) Boolean businessLicense,
                       @RequestParam(value = "contractNum", required = false) Boolean contractNum,
                       @RequestParam(value = "paperContract", required = false) Boolean paperContract) {

        APIResult<Contracts> contracts = contractService.list(userId, merchantId, id, merchantName, "[2]", JsonUtils.toJson(templateid), JsonUtils.toJson(type), businessLicense, paperContract, startTime, endTime, index, limit);
        Contracts contractsData = contracts.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<Contract> list = contractsData.getList();
        if (list == null)
            list = new ArrayList(0);
        Page<Contract> page = new PageImpl(list, pageable, contractsData.getTotal());

        List<Country> countries = countryService.getIdNameList();

        model.addAttribute("page", page);
        model.addAttribute("countries", countries);
        model.addAttribute("countryId", countryId);

        model.addAttribute("userid", userId);
        model.addAttribute("merchantname", merchantName);
        model.addAttribute("merchantid", merchantId);
        model.addAttribute("status", status);
        model.addAttribute("templateid", templateid);
        model.addAttribute("businessLicense", businessLicense);
        model.addAttribute("paperContract", paperContract);
        model.addAttribute("type", type);
        model.addAttribute("starttime", startTime);
        model.addAttribute("contractNum", contractNum);
        model.addAttribute("endtime", endTime);
        model.addAttribute("index", index);
        model.addAttribute("limit", limit);
        return "contract/list";
    }

    @RequestMapping("filedList")
    public String filedList(
            Model model,
            @ApiParam(value = "用户id") @RequestParam(value = "userid", required = false) Long userId,
            @RequestParam(value = "id", required = false) Long id,
            @ApiParam(value = "商户名称") @RequestParam(value = "merchantname", required = false) String merchantName,
            @ApiParam(value = "商户id") @RequestParam(value = "merchantid", required = false) Long merchantId,
            @ApiParam(value = "合同状态: 1.已驳回 2.审核中 3.未生效 4.已生效(多个值以,分割)") @RequestParam(value = "status", required = false) Integer[] status,
            @ApiParam(value = "合同类型: 为空则查询全部") @RequestParam(value = "templateid", required = false) Long[] templateid,
            @RequestParam(value = "type", required = false) Integer[] type,
            @ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(value = "starttime", required = false) String startTime,
            @ApiParam(value = "结束时间(yyyy-MM-dd)") @RequestParam(value = "endtime", required = false) String endTime,
            @ApiParam(value = "页序号，默认从1开始") @RequestParam(value = "page", required = false, defaultValue = "1") Integer index,
            @ApiParam(value = "每页条目数,默认20条") @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {

        APIResult<Contracts> contracts = contractService.list(userId, merchantId, id, merchantName, "[4]", JsonUtils.toJson(templateid), JsonUtils.toJson(type), null, null, startTime, endTime, index, limit);
        Contracts contractsData = contracts.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<Contract> list = contractsData.getList();
        if (list == null)
            list = new ArrayList(0);
        Page<Contract> page = new PageImpl(list, pageable, contractsData.getTotal());
        model.addAttribute("page", page);

        model.addAttribute("userid", userId);
        model.addAttribute("merchantname", merchantName);
        model.addAttribute("merchantid", merchantId);
        model.addAttribute("status", status);
        model.addAttribute("templateid", templateid);
        model.addAttribute("type", type);
        model.addAttribute("starttime", startTime);
        model.addAttribute("endtime", endTime);
        model.addAttribute("index", index);
        model.addAttribute("limit", limit);
        return "contract/filedList";
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    @ResponseBody
    public void preview(@RequestParam(value = "id", required = true) long id,
                        HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        APIResult<String> preview = contractService.preview(id);
        PrintWriter writer = response.getWriter();
        writer.write(preview.getData());
        writer.close();
    }


    @RequestMapping(path = "verify", method = RequestMethod.GET)
    @ResponseBody
    public APIResult verify(Long id,Boolean valid, Long contractId) throws ParseException {
        Long userid = UserHolder.getUser().getUserid();
        //审核
        APIResult approve = this.contractService.approve(userid, contractId, valid);
        if (approve.getCode() != 0) {
            throw new APIException(approve.getCode(),approve.getMsg());
        }
        if (valid) {
            //2 得到合同信息
            APIResult<Contract> resDetail = this.contractService.detail(contractId);
            //3插入对账系统
            Contract detail =  resDetail.getData();
            this.addForAccount(detail);
        }
        return new APIResult<>(ResultCodes.Success);
    }

    private void addForAccount(Contract detail) {
        if (detail.getMchid() == null) {
            throw new APIException(ResultCodes.ContractNotAssignedForMerchant);
        }
        Map<String, String> result = detail.getParameters();
        //JSONObject result=JSONObject.parseObject(detail.getParameters(), Map.class);
        String[] poundageNamesList = poundageNames.split(",");
        BigDecimal poundage = null;
        for (String p : poundageNamesList) {
            BigDecimal temp = result.get(p) == null ? null : new BigDecimal(result.get(p));
            if (temp != null) {
                poundage = temp;
                break;
            }
        }
        String[] paymentDaysNamesList = paymentDaysNames.split(",");
        Long paymentDays = null;
        for (String p : paymentDaysNamesList) {
            Long temp = result.get(p) == null ? null : new Long(result.get(p));
            if (temp != null) {
                paymentDays = temp;
                break;
            }
        }


        if (poundage != null && paymentDays != null) {
            Long merchantId = detail.getMchid();
            APIResult res = accountService.add(merchantId, true, daytype, poundage, paymentDays, startprice, new DateTime().toString("yyyy-MM-dd"));
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
        } else {
            throw new APIException(ResultCodes.PoundageOrPayDaysIsNull);
        }

    }

    public static void main1(String[] args) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("merchantId", "7456"));
        params.add(new BasicNameValuePair("enabled", "true"));
        params.add(new BasicNameValuePair("dayType", "1"));
        params.add(new BasicNameValuePair("poundage", "1"));
        params.add(new BasicNameValuePair("startPrice", "1"));
        params.add(new BasicNameValuePair("startTime", "2017-05-12"));
        params.add(new BasicNameValuePair("paymentDays", "1"));
        String url = "http://testaccount.99mice.com/merchantSettlement/add" ;
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);
        post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(post)) {
            String responseText = EntityUtils.toString(response.getEntity());
            System.out.println(responseText);
            Integer status = JSONObject.parseObject(responseText).getInteger("code");
            System.out.println(status+"felix");
            if (status == null || status != 0) {

            }

        }
    }

    @RequestMapping("api/templates")
    @ResponseBody
    public APIResult templates(Long id, Integer type) {
        APIResult<List<ContractTemplate>> contractConfig = this.contractService.getContractConfig(id, type);
        return contractConfig;
    }

}