package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlement;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlementCommission;
import com.nuanyou.cms.remote.model.request.DayType;
import com.nuanyou.cms.remote.service.RemoteSettlementService;
import com.nuanyou.cms.service.AccountHandleService;
import com.nuanyou.cms.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by Felix on 2017/6/3.
 */
@Service
public class AccountHandleServiceImpl implements AccountHandleService {

    @Value("${contractConfig.dayTypeNames}")
    private String dayTypeNames;
    @Value("${contractConfig.poundageNames}")
    private String poundageNames;
    @Value("${contractConfig.paymentDaysNames}")
    private String paymentDaysNames;
    @Value("${contractConfig.youfuStartTimeNames}")
    private String youfuStartTimeNames;
    @Value("${contractConfig.startPriceNames}")
    private String startPriceNames;
    @Value("${contractConfig.commissionNames}")
    private String commissionNames;
    @Value("${contractConfig.groupBuyingStartTimeNames}")
    private String groupBuyingStartTime;
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
    @Autowired
    private RemoteSettlementService remoteAccountSettlementService;

    public void addSettlementForAccount(Contract detail) {
        if (detail.getMchId() == null) {
            throw new APIException(ResultCodes.ContractNotAssignedForMerchant);
        }
        Map<String, String> result = detail.getParameters();
        AcMerchantSettlement settlementRequest =new  AcMerchantSettlement();
        AcMerchantSettlementCommission settlementCommissionRequest =new  AcMerchantSettlementCommission();
        setMerchantSettlementRequest(result, settlementRequest, detail.getMchId());
        setMerchantSettlementCommissionRequest(result, settlementCommissionRequest);
        if (detail.getParentId() == null) {
            validate(settlementRequest.getPoundage(), settlementRequest.getDay(), settlementRequest.getDayType(), settlementRequest.getStartTime(), settlementRequest.getStartPrice());
        }
        APIResult<AcMerchantSettlement> res = remoteAccountSettlementService.getSettlement(settlementRequest.getMerchantId());
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        AcMerchantSettlement settlement = res.getData();
        Long settlementId=null;
        if(settlement==null){
            if(settlementRequest.getStartTime()==null){
                settlementRequest.setStartTime(DateUtils.getTodayDate());
            }
            APIResult<AcMerchantSettlement> addSettlementRes = remoteAccountSettlementService.addSettlement(settlementRequest);
            if (addSettlementRes.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            settlementId=addSettlementRes.getData().getId();
        }else{
            settlementRequest.setId(settlement.getId());
            if(settlementRequest.getStartTime()==null){
                settlementRequest.setStartTime(DateUtils.getTomorrowDate());
            }
            APIResult<AcMerchantSettlement> updateSettlementRes = remoteAccountSettlementService.updateSettlement(settlement.getId(), settlementRequest);
            if (updateSettlementRes.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            settlementId=updateSettlementRes.getData().getId();
        }
        settlementCommissionRequest.setSettlementId(settlementId);
        if(settlementCommissionRequest.getGroupon()!=null){
            APIResult<AcMerchantSettlement> settlementCommissionRes = remoteAccountSettlementService.addOrUpdateCommission(settlementCommissionRequest);
            if (settlementCommissionRes.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
        }
    }

    private void validate(BigDecimal poundage, Long paymentDays, DayType dateType, Date startTimeStr, BigDecimal startPrice) {
        if (poundage == null || paymentDays == null || dateType == null || startPrice == null) {
            throw new APIException(ResultCodes.PoundageOrPayDaysIsNull);
        }
    }

    private void setMerchantSettlementCommissionRequest(Map<String, String> result, AcMerchantSettlementCommission settlementCommissionRequest) {
        String startTimeStr = getValue(result, groupBuyingStartTime);//开始时间
        String commissionStr= getValue(result, commissionNames);//佣金
        BigDecimal commission=commissionStr==null?null:new BigDecimal(commissionStr);
        Date dateTime= startTimeStr==null?null: DateTime.parse(startTimeStr,format).toDate();
        settlementCommissionRequest.setGroupon(commission);
        settlementCommissionRequest.setStartTime(dateTime);
    }

    private String getValue(Map<String, String> result, String names) {
        String[] nameList = names.split(",");
        String value = null;
        for (String p : nameList) {
            String temp = result.get(p) == null ? null : result.get(p);
            if (temp != null) {
                value = temp;
                break;
            }
        }
        return value;
    }

    private void setMerchantSettlementRequest(Map<String, String> result, AcMerchantSettlement request, Long mchId) {
        String poundageStr = getValue(result, poundageNames);//手续费
        String paymentDaysStr =getValue(result, paymentDaysNames);//账期
        String dateTypeStr = getValue(result, dayTypeNames);//类型
        String startTimeStr = getValue(result, youfuStartTimeNames);//开始时间
        String startPriceStr = getValue(result, startPriceNames);//起结金额
        BigDecimal poundage=poundageStr==null?null:new BigDecimal(poundageStr);
        Integer dateType=dateTypeStr==null?null:new Integer(dateTypeStr);
        Long paymentDays=paymentDaysStr==null?null:Long.valueOf(paymentDaysStr);
        Date dateTime= startTimeStr==null?null:DateTime.parse(startTimeStr,format).toDate();
        BigDecimal startPrice=startPriceStr==null?null:new BigDecimal(startPriceStr);
        request.setEnabled(true);
        request.setDayType( DayType.toEnum(dateType));
        request.setPoundage(poundage);
        request.setDay(paymentDays);
        request.setStartPrice(startPrice);
        request.setMerchantId(mchId);
        request.setStartTime(dateTime);
    }
}
