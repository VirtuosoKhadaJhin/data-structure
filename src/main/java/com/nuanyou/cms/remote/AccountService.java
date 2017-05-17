package com.nuanyou.cms.remote;


import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.contract.output.NullData;
import com.nuanyou.cms.util.MimeTypes;
import io.swagger.annotations.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by Alan.ye on 2017/4/20.
 */
@FeignClient(url = "${accountService}", name = "RemoteAccountService")
@RestController
@RequestMapping(value = "merchantSettlement", produces = MimeTypes.MIME_TYPE_JSON)
@Api(value = "/", description = "the account API")
public interface AccountService {



    @ApiOperation(value = "插入清算.", notes = "插入清算", response = NullData.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "插入清算", response = NullData.class)})
    @RequestMapping(value="add", method = RequestMethod.POST)
    public APIResult<NullData> add(
            @ApiParam(value = "") @RequestParam(value = "merchantId", required = true) Long merchantId,
            @ApiParam(value = "") @RequestParam(value = "enabled", required = true) Boolean enabled,
            @ApiParam(value = "") @RequestParam(value = "dayType", required = true) Integer dayType,
            @ApiParam(value = "") @RequestParam(value = "poundage", required = true) BigDecimal poundage,
            @ApiParam(value = "") @RequestParam(value = "paymentDays", required = true) Long paymentDays,
            @ApiParam(value = "") @RequestParam(value = "startPrice", required = true) BigDecimal startPrice,
            @ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(value = "startTime", required = false) String startTime);



}