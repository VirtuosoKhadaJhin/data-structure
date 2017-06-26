package com.nuanyou.cms.remote.service;


import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlement;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlementBank;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlementCommission;
import com.nuanyou.cms.util.MimeTypes;
import io.swagger.annotations.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Alan.ye on 2017/4/20.
 */
@FeignClient(url = "${accountService}", name = "remoteAccountSettlementService")
@RestController
@RequestMapping(value = "/account", produces = MimeTypes.MIME_TYPE_JSON)
@Api(value = "/account", description = "the accountService API")
public interface RemoteSettlementService {



    @RequestMapping("/merchant/settlement")
    @ResponseBody
    public APIResult<AcMerchantSettlement> getSettlement(
            @ApiParam(value = "商户ID") @RequestParam(value = "merchantId", required = true) Long merchantId);

    @ApiOperation(value = "增加一个结算", notes = "...")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "...")})
    @RequestMapping(value = "/merchant/settlement", method = {RequestMethod.POST})
    @ResponseBody
    public APIResult<AcMerchantSettlement> addSettlement( AcMerchantSettlement acMerchantSettlement) ;

    @ApiOperation(value = "更新一个结算", notes = "...")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "...")})
    @RequestMapping(value = "/merchant/settlement/{id}", method = {RequestMethod.POST})
    @ResponseBody
    public APIResult<AcMerchantSettlement> updateSettlement(@PathVariable(value = "id") Long id, AcMerchantSettlement acMerchantSettlement) ;



    @ApiOperation(value = "增加或者更新一个团购佣金", notes = "...")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "...")})
    @RequestMapping(value = "/merchant/settlement/commission", method = {RequestMethod.POST})
    @ResponseBody
    public APIResult<AcMerchantSettlement> addOrUpdateCommission(@RequestBody AcMerchantSettlementCommission settlementCommission);

    @ApiOperation(value = "增加或者更新一个团购佣金", notes = "...")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "...")})
    @RequestMapping(value = "/merchant/settlement/bank", method = {RequestMethod.POST})
    @ResponseBody
    APIResult<AcMerchantSettlementBank> addBank(AcMerchantSettlementBank settlementBankRequest);
}