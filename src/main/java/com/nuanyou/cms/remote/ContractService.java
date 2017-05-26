package com.nuanyou.cms.remote;


import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.contract.output.*;
import com.nuanyou.cms.model.contract.request.BatchTemplateParameterRequest;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequest;
import com.nuanyou.cms.model.contract.request.TemplateRequest;
import com.nuanyou.cms.util.MimeTypes;
import io.swagger.annotations.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Alan.ye on 2017/4/20.
 */
@FeignClient(url = "${contractService}", name = "RemoteContractService")
@RestController
@RequestMapping(value = "/contracts", produces = MimeTypes.MIME_TYPE_JSON)
@Api(value = "/contracts", description = "the contract API")
public interface ContractService {



    @ApiOperation(value = "合同模版列表.", notes = "合同模版列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "合同配置")})
    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    public APIResult<ContractTemplates> findContractTemplateList(
            @ApiParam(value = "国家id") @RequestParam(value = "countryid", required = false) Long countryId,
            @ApiParam(value = "状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam(value = "模版类型: 1.主合同 2.附加合同") @RequestParam(value = "type", required = false) Integer type,
            @ApiParam(value = "页序号，默认从1开始") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页条目数,默认20条") @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit);


    @ApiOperation(value = "新增模版参数", notes = "新增模版参数")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增模版参数")})
    @RequestMapping(value = "/template/paramter",method = RequestMethod.POST)
    public APIResult<ContractParameter> saveTemplateParamter( TemplateParameterRequest request);


    @ApiOperation(value = "批量新增模版参数", notes = "批量新增模版参数")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "批量新增模版参数")})
    @RequestMapping(value = "/template/batch/paramter",method = RequestMethod.POST)
    public APIResult<List<Long>> saveTemplateParamters(@RequestBody BatchTemplateParameterRequest request);


    @ApiOperation(value = "模版参数列表.", notes = "模版参数列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "模版参数列表")})
    @RequestMapping(value = "/template/parameters", method = RequestMethod.GET)
    public APIResult<ContractParameters> findAllTemplateParameters( @ApiParam(value = "页序号，默认从1开始") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                    @ApiParam(value = "每页条目数,默认20条") @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit);

    @ApiOperation(value = "新增合同模版", notes = "新增合同模版")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增合同模版")})
    @RequestMapping(value = "/template",method = RequestMethod.POST)
    public APIResult<ContractTemplate> saveTemplate(@RequestBody TemplateRequest request);

    @ApiOperation(value = "合同模版详情.", notes = "合同模版详情", response = ContractTemplate.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "合同模版详情", response = ContractTemplate.class)})
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    APIResult<ContractTemplate> getContractConfig(@ApiParam(value = "模版id", required = true) @RequestParam(value = "id", required = true) Long id);

    @ApiOperation(value = "获取合同列表.", notes = "获取合同列表", response = Contracts.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "合同列表", response = Contracts.class)})
    @RequestMapping(value = "", method = RequestMethod.GET)
    APIResult<Contracts> list(
            @ApiParam(value = "用户id") @RequestParam(value = "userid", required = false) Long userId,
            @ApiParam(value = "商户id") @RequestParam(value = "merchantid", required = false) Long merchantId,
            @ApiParam(value = "合同id") @RequestParam(value = "id", required = false) Long id,
            @ApiParam(value = "商户名称") @RequestParam(value = "merchantname", required = false) String merchantName,
            @ApiParam(value = "合同状态: 1.已驳回 2.审核中 3.未生效 4.已生效 (JsonArray<Integer>)") @RequestParam(value = "status", defaultValue = "[]", required = false) String status,
            @ApiParam(value = "合同模版: 为空则查询全部 (JsonArray<Long>)") @RequestParam(value = "templateid", defaultValue = "[]", required = false) String templateid,
            @ApiParam(value = "合同类型: 1.主合同 2.附加合同 (JsonArray<Integer>)") @RequestParam(value = "type", defaultValue = "[]", required = false) String type,
            @ApiParam(value = "是否有营业执照") @RequestParam(value = "hasbusinesslicense", required = false) Boolean hasBusinessLicense,
            @ApiParam(value = "是否有纸质合同") @RequestParam(value = "haspapercontract", required = false) Boolean hasPaperContract,
            @ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(value = "starttime", required = false) String startTime,
            @ApiParam(value = "结束时间(yyyy-MM-dd)") @RequestParam(value = "endtime", required = false) String endTime,
            @ApiParam(value = "页序号，默认从1开始") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页条目数,默认20条") @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit);

    @ApiOperation(value = "合同新增", notes = "合同新增", response = ContractSave.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "录入后的id与pdfurl", response = ContractSave.class)})
    @RequestMapping(value = "", consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult<ContractSave> add(
            @ApiParam(value = "用户id", required = true) @RequestParam(value = "userid", required = true) Long userId,
            @ApiParam(value = "模板id", required = true) @RequestParam(value = "templateid", required = true) Long templateId,
            @ApiParam(value = "主合同id", required = false) @RequestParam(value = "parentid", required = false) Long parentid,
            @ApiParam(value = "商户id", required = false) @RequestParam(value = "merchantid", required = false) Long merchantId,
            @ApiParam(value = "参数集合json字符串", required = true) @RequestParam(value = "parameters", required = true) String parameters);


    @ApiOperation(value = "合同更新", notes = "合同更新", response = ContractSave.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "录入后的id与pdfurl", response = ContractSave.class)})
    @RequestMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    APIResult<ContractSave> update(
            @ApiParam(value = "用户id", required = true) @RequestParam(value = "userid", required = true) Long userId,
            @ApiParam(value = "合同id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "参数集合json字符串", required = true) @RequestParam(value = "parameters", required = true) String parameters);


    @ApiOperation(value = "获取合同详情.", notes = "获取合同详情", response = Contract.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "合同详情", response = Contract.class)})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    APIResult<Contract> detail(@ApiParam(value = "合同id", required = true) @PathVariable(value = "id") long id);

    @ApiOperation(value = "合同预览.", notes = "合同预览", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Html", response = String.class)})
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    APIResult<String> preview(@ApiParam(value = "合同id", required = true) @RequestParam(value = "id", required = true) long id);


    @ApiOperation(value = "发送邮件.", notes = "新发送邮件 若存在错误的邮箱地址 则仅发送正确的地址，返回错误的", response = NullData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "是否成功", response = NullData.class)})
    @RequestMapping(value = "/{id}/_mailto", method = RequestMethod.POST)
    APIResult sendEmail(
            @ApiParam(value = "用户id", required = true) @RequestParam(value = "userid", required = true) Long userId,
            @ApiParam(value = "合同id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "商户邮箱(多个值以,分割 为空则仅发送BD)", required = false) @RequestParam(value = "email", required = false) String email);

    @ApiOperation(value = "添加营业执照/纸质合同/签名.", notes = "添加营业执照/纸质合同/签名", response = APIResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作结果(成功：200，其他：)", response = APIResult.class)})
    @RequestMapping(value = "/component/update", consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult addComponent(
            @ApiParam(value = "合同id", required = true) @RequestParam(value = "id", required = true) Long id,
            @ApiParam(value = "附件类型: 1.营业执照 2.纸质合同 3.签名", required = true) @RequestParam(value = "type", required = true) int type,
            @ApiParam(value = "附件URL", required = true) @RequestParam(value = "url", required = true) String url);


    @ApiOperation(value = "提交审核.", notes = "提交审核", response = NullData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作结果(成功：200，其他：)", response = NullData.class)})
    @RequestMapping(value = "/{id}/_audit", consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult audit(@ApiParam(value = "合同id", required = true) @RequestParam(value = "id", required = true) Long id);

    @ApiOperation(value = "删除合同.", notes = "删除合同", response = NullData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作结果(成功：200，其他：)", response = NullData.class)})
    @RequestMapping(value = "/_trashcan", consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult remove(@ApiParam(value = "合同id", required = true) @RequestParam(value = "id", required = true) Long id);


    @ApiOperation(value = "商户合同关联.", notes = "商户合同关联", response = NullData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作结果（成功：200，其他：）", response = NullData.class)})
    @RequestMapping(value = "/_attach", produces = MimeTypes.MIME_TYPE_JSON, consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult relateMerchantContract(
            @ApiParam(value = "用户id", required = true) @RequestParam(value = "userid", required = true) Long userId,
            @ApiParam(value = "商户id", required = true) @RequestParam(value = "merchantid", required = true) Long merchantId,
            @ApiParam(value = "合同id(多个值以,分隔)", required = true) @RequestParam(value = "id", required = true) String id);

    @ApiOperation(value = "商户合同取消关联.", notes = "商户合同取消关联", response = NullData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作结果（成功：200，其他：）", response = NullData.class)})
    @RequestMapping(value = "/{id}/_detach", produces = MimeTypes.MIME_TYPE_JSON, consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult cancelRelateMerchantContract(
            @ApiParam(value = "用户id", required = true) @RequestParam(value = "userid", required = true) Long userId,
            @ApiParam(value = "商户id", required = true) @RequestParam(value = "merchantid", required = true) Long merchantId,
            @ApiParam(value = "合同id", required = true) @PathVariable(value = "id") Long id) ;

    @ApiOperation(value = "合同审批.", notes = "合同审批", response = APIResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "操作结果（成功：200，其他：）", response = APIResult.class)})
    @RequestMapping(value = "/{id}/_approve", produces = MimeTypes.MIME_TYPE_JSON, consumes = MimeTypes.MIME_TYPE_FORM_DATA, method = RequestMethod.POST)
    APIResult approve(
            @ApiParam(value = "用户id", required = true) @RequestParam(value = "userid") Long userId,
            @ApiParam(value = "合同id", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(value = "是否生效", required = true) @RequestParam(value = "valid") boolean valid);


}