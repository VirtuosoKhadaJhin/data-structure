package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.output.ContractTemplates;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.AccountHandleService;
import com.nuanyou.cms.service.ContractModuleService;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.JsonUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contract")
public class ContractController {

    @Autowired
    @Qualifier("s3")
    private FileClient fileClient;
    @Autowired
    private RemoteContractService contractService;
    @Autowired
    private ContractModuleService contractModuleService;
    @Autowired
   private AccountHandleService accountHandleService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private MerchantDao merchantDao;


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

        Page<Contract> page = contractModuleService.getContracts(userId, merchantId, id, merchantName, "[2]", JsonUtils.toJson(templateid), JsonUtils.toJson(type), businessLicense, paperContract, startTime, endTime, index, limit);
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


    @RequestMapping("detail")
    public String getDetail(Model model,
                            @RequestParam(value = "id", required = true) Long id) {
        APIResult<Contract> result = contractService.getContract(id);
        Contract detail = result.getData();
        if (detail != null) {
            Long mchid = detail.getMchId();
            String localName = merchantDao.getLocalName(mchid);
            model.addAttribute("id", detail.getId());
            model.addAttribute("relatedMchName", localName); //商户本地名称
            model.addAttribute("mchName", detail.getMchName());//企业名称
            model.addAttribute("approveTime", detail.getApproveTime());
            model.addAttribute("templateTitle", detail.getTemplateTitle());
            model.addAttribute("startTime", detail.getStartTime());
            model.addAttribute("type", detail.getType());
            model.addAttribute("endTime", detail.getEndTime());
            model.addAttribute("submitTime", detail.getSubmitTime());
            model.addAttribute("rejectTime", detail.getRejectTime());
            model.addAttribute("status", detail.getStatus());
            model.addAttribute("pdfUrl", detail.getPdfUrl());
            model.addAttribute("htmlContent", detail.getHtmlContent());
            model.addAttribute("username", detail.getUsername());
            model.addAttribute("signImgUrl", detail.getSignImgUrl());
            model.addAttribute("businessLicense", detail.getBusinessLicense());
            model.addAttribute("paperContract", detail.getPaperContract());
            model.addAttribute("remark", detail.getRemark());
            model.addAttribute("contractNo", detail.getContractNo());
        }
        return "contract/detail";
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
        Page<Contract> page = contractModuleService.getContracts(userId, merchantId, id, merchantName, "[4]", JsonUtils.toJson(templateid), JsonUtils.toJson(type), null, null, startTime, endTime, index, limit);
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


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        Contract entity = null;
        if (id != null) {
            entity = this.contractModuleService.getContract(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "contract/edit";
    }

    @Autowired
    private UserService userService;

    @RequestMapping(path = "verify", method = RequestMethod.GET)
    @ResponseBody
    public APIResult verify(Long id, Boolean valid, Long contractId) throws ParseException {
        //Long userid = UserHolder.getUser().getUserid();
        //String email = UserHolder.getUser().getEmail();
       // CmsUser user = userService.getUserByEmail(email);

//        //审核
//        APIResult approve = this.contractService.approve(user.getId(), contractId, valid);
//        if (approve.getCode() != 0) {
//            throw new APIException(approve.getCode(), approve.getMsg());
//        }
        if (valid) {
            //2 得到合同信息
            APIResult<Contract> resDetail = this.contractService.getContract(contractId);
            //3插入对账系统
            Contract detail = resDetail.getData();
            this.accountHandleService.addSettlementForAccount(detail);
        }
        return new APIResult<>(ResultCodes.Success);
    }

    @RequestMapping(path = "upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file,
                       @ApiParam(value = "合同id", required = true) @RequestParam(value = "id", required = true) Long id,
                       @ApiParam(value = "附件类型: 1.营业执照 2.纸质合同 3.签名", required = true) @RequestParam(value = "type", required = true) int type,
                       HttpServletResponse response) throws IOException {
        String filename = file.getOriginalFilename();
        String format = filename.substring(filename.lastIndexOf("."), filename.length());
        String url = fileClient.uploadFile(file.getInputStream(), format);
        APIResult apiResult = contractService.addComponent(id, type, url);
        response.setContentType("text/html;charset=UTF-8");
        if (apiResult.isSuccess())
            response.getWriter().println("<script>parent.window.location.reload();</script>");
        else
            response.getWriter().println("<script>parent.alert('" + apiResult.getMsg() + "');</script>");
    }








    @RequestMapping("api/templates")
    @ResponseBody
    public List<ContractTemplate> templates(Long id, Integer type) {
        APIResult<ContractTemplates> contractTemplateList = this.contractService.findContractTemplateList(id, null, null, 1, 1000);
        List<ContractTemplate> contractConfig = contractTemplateList.getData().getList();
        return contractConfig;
    }



}