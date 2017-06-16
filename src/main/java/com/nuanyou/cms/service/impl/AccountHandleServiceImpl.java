package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.ContractParamDistributeDao;
import com.nuanyou.cms.entity.ContractParamDistribute;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Felix on 2017/6/3.
 */
@Service
public class AccountHandleServiceImpl implements AccountHandleService {

    private static String dayTypeNames = "youfu_billing_cycle";
    private static String poundageNames = "youfu_poundage";
    private static String paymentDaysNames = "youfu_payment_day";
    private static String youfuStartTimeNames = "youfu_start_time";
    private static String startPriceNames = "youfu_start_price";
    private static String commissionNames = "group_buying_commission";
    private static String groupBuyingStartTime = "group_buying_start_time";
    private static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private RemoteSettlementService remoteAccountSettlementService;

    public void addSettlementForAccount(Contract detail) {
        if (detail.getMchId() == null) {
            throw new APIException(ResultCodes.ContractNotAssignedForMerchant);
        }
        Map<String, String> result = detail.getParameters();
        AcMerchantSettlement settlementRequest = new AcMerchantSettlement();
        AcMerchantSettlementCommission settlementCommissionRequest = new AcMerchantSettlementCommission();
        setMerchantSettlementRequest(result, settlementRequest, detail.getMchId());
        setMerchantSettlementCommissionRequest(result, settlementCommissionRequest);
        if (detail.getParentId() == null) {//主合同验证优付的参数
            validateYoufuSettlement(settlementRequest.getPoundage(), settlementRequest.getDay(), settlementRequest.getDayType(), settlementRequest.getStartPrice());
            validateCommission(settlementCommissionRequest.getGroupon(),settlementCommissionRequest.getStartTime());
        }
        APIResult<AcMerchantSettlement> res = remoteAccountSettlementService.getSettlement(settlementRequest.getMerchantId());
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        AcMerchantSettlement settlement = res.getData();
        Long settlementId = null;
        if (settlement == null) {//若商家已经存在清算记录
            if (settlementRequest.getStartTime() == null) {
                settlementRequest.setStartTime(DateUtils.getTodayDate());
            }
            APIResult<AcMerchantSettlement> addSettlementRes = remoteAccountSettlementService.addSettlement(settlementRequest);
            if (addSettlementRes.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            settlementId = addSettlementRes.getData().getId();
        } else {
            settlementRequest.setId(settlement.getId());
            if (settlementRequest.getStartTime() == null) {
                settlementRequest.setStartTime(DateUtils.getTomorrowDate());
            }
            APIResult<AcMerchantSettlement> updateSettlementRes = remoteAccountSettlementService.updateSettlement(settlement.getId(), settlementRequest);
            if (updateSettlementRes.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            settlementId = updateSettlementRes.getData().getId();
        }
        settlementCommissionRequest.setSettlementId(settlementId);
        if (settlementCommissionRequest.getGroupon() != null) {//团购佣金
            APIResult<AcMerchantSettlement> settlementCommissionRes = remoteAccountSettlementService.addOrUpdateCommission(settlementCommissionRequest);
            if (settlementCommissionRes.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
        }
    }

    private void validateCommission(BigDecimal groupon, Date startTime) {

        if(groupon!=null||startTime!=null){
            throw new APIException(ResultCodes.Fail, "主合同时不能存在团购佣金等参数");
        }
    }

    private void validateYoufuSettlement(BigDecimal poundage, Long paymentDays, DayType dateType, BigDecimal startPrice) {
        if(dateType==null&&paymentDays==null){
            throw new APIException(ResultCodes.Fail, "主合同时结算周期和日结天数不能同时为空");
        }
        if (poundage == null) {
            throw new APIException(ResultCodes.Fail, "主合同时手续费不能为空");
        }else if (startPrice == null) {
            throw new APIException(ResultCodes.Fail, "主合同时起始金额不能为空");
        }
    }

    private void setMerchantSettlementCommissionRequest(Map<String, String> result, AcMerchantSettlementCommission settlementCommissionRequest) {
        String startTimeStr = getValue(result, groupBuyingStartTime);//开始时间
        String commissionStr = getValue(result, commissionNames);//佣金
        BigDecimal commission = commissionStr == null ? null : new BigDecimal(commissionStr);
        Date dateTime = startTimeStr == null ? null : DateTime.parse(startTimeStr, dateFormat).toDate();
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

    @Autowired
    private ContractParamDistributeDao contractParamDistributeDao;
    public  static  final Long ACCOUNT_SYSTEM_ID=1l;

    private void setMerchantSettlementRequest(Map<String, String> result, AcMerchantSettlement request, Long mchId) {
        String poundageStr = getValue(result, poundageNames);//手续费
        String paymentDaysStr = getValue(result, paymentDaysNames);//账期
        String dateTypeStr = getValue(result, dayTypeNames);//类型
        String startTimeStr = getValue(result, youfuStartTimeNames);//开始时间
        String startPriceStr = getValue(result, startPriceNames);//起结金额
        BigDecimal poundage = poundageStr == null ? null : new BigDecimal(poundageStr);
        Integer dateType = dateTypeStr == null ? null : new Integer(dateTypeStr);
        Long paymentDays = paymentDaysStr == null ? null : Long.valueOf(paymentDaysStr);
        Date dateTime = startTimeStr == null ? null : DateTime.parse(startTimeStr, dateFormat).toDate();
        BigDecimal startPrice = startPriceStr == null ? null : new BigDecimal(startPriceStr);
        request.setEnabled(true);
        if(paymentDays==null){//非日结
            if(dateType==null){
                request.setDayType(null);
                request.setDay(null);
            }else {
                request.setDayType(DayType.toEnum(dateType));//月结或者半月结的结算周期
                if(DayType.toEnum(dateType)==DayType.Month) {
                    request.setDay(1L);//月结
                }else{
                    request.setDay(null);//半月结
                }
            }
        }else{//日结
            if(dateType!=null){
                throw new APIException(ResultCodes.Fail,"日结天数和结算周期不能同时存在!");
            }
            request.setDayType(DayType.Day);
            request.setDay(paymentDays);
        }
        request.setPoundage(poundage);
        request.setStartPrice(startPrice);
        request.setMerchantId(mchId);
        request.setStartTime(dateTime);
    }
}
