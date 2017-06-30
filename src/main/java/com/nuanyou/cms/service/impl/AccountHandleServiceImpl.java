package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.ContractParamDistributeDao;
import com.nuanyou.cms.entity.ContractParamDistribute;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlement;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlementBank;
import com.nuanyou.cms.remote.model.request.AcMerchantSettlementCommission;
import com.nuanyou.cms.remote.model.request.DayType;
import com.nuanyou.cms.remote.service.RemoteContractService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Felix on 2017/6/3.
 */
@Service
public class AccountHandleServiceImpl implements AccountHandleService {

    private static String dayTypeNames = "youfu_billing_cycle";//结算周期（月结或者半月结）
    private static String poundageNames = "youfu_poundage";//手续费
    private static String paymentDaysNames = "youfu_payment_day";//日结天数
    private static String youfuStartTimeNames = "youfu_start_time";//生效日期
    private static String startPriceNames = "youfu_start_price";//起结金额

    private static String accountName="account_name";//开户名
    private static String bank="bank";//开户行
    private static String bankBranch="bank_branch";//分行
    private static String bankAccount="bank_account";//账号

    private static String commissionNames = "group_buying_commission";//团购佣金
    private static String groupBuyingStartTime = "group_buying_start_time";//团购的生效时间

    private static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");



    public  static  final Long ACCOUNT_SYSTEM_ID=1l;

    @Autowired
    private RemoteSettlementService remoteAccountSettlementService;
    @Autowired
    private ContractParamDistributeDao contractParamDistributeDao;
    @Autowired
    private RemoteContractService remoteContractService;



    public void addSettlementForAccount(Contract detail) {
        if (detail.getMchId() == null) {
            throw new APIException(ResultCodes.ContractNotAssignedForMerchant);
        }
        Map<String, String> result = detail.getParameters();
        AcMerchantSettlement settlementRequest = new AcMerchantSettlement();//1 结算
        AcMerchantSettlementCommission settlementCommissionRequest = new AcMerchantSettlementCommission();//2 团购佣金
        AcMerchantSettlementBank settlementBankRequest = new AcMerchantSettlementBank();//3 结算银行
        List<ContractParamDistribute> distributes = contractParamDistributeDao.findBySystemId(ACCOUNT_SYSTEM_ID);
        Map<String,String> distributes_map=getDistributeMap(distributes);

        setMerchantSettlementRequest(result, settlementRequest, detail.getMchId(),distributes_map);
        setMerchantSettlementCommissionRequest(result, settlementCommissionRequest,distributes_map);
        setMerchantSettlementBank(result, settlementBankRequest,distributes_map);

        if (detail.getParentId() == null) {//主合同验证
            validateYoufuSettlement(settlementRequest.getPoundage(), settlementRequest.getDay(), settlementRequest.getDayType(), settlementRequest.getStartPrice());
            validateCommission(settlementCommissionRequest.getGroupon(),settlementCommissionRequest.getStartTime());
            validateMerchantSettlentBank(settlementBankRequest.getBankCode(), settlementBankRequest.getName(), settlementBankRequest.getAccount(), settlementBankRequest.getBranch());
        }
        APIResult<AcMerchantSettlement> res = remoteAccountSettlementService.getSettlement(settlementRequest.getMerchantId());
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        AcMerchantSettlement settlement = res.getData();
        Long settlementId = null;
        if (settlement == null) {//1 商家结算
            if (settlementRequest.getStartTime() == null) {
                settlementRequest.setStartTime(DateUtils.getTodayDate());
            }
            APIResult<AcMerchantSettlement> addSettlementRes = remoteAccountSettlementService.addSettlement(settlementRequest);
            if (addSettlementRes.getCode() != 0) {
                throw new APIException(addSettlementRes.getCode(), addSettlementRes.getMsg());
            }
            settlementId = addSettlementRes.getData().getId();
        } else {
            settlementRequest.setId(settlement.getId());
            if (settlementRequest.getStartTime() == null) {
                settlementRequest.setStartTime(DateUtils.getTomorrowDate());
            }
            APIResult<AcMerchantSettlement> updateSettlementRes = remoteAccountSettlementService.updateSettlement(settlement.getId(), settlementRequest);
            if (updateSettlementRes.getCode() != 0) {
                throw new APIException(updateSettlementRes.getCode(), updateSettlementRes.getMsg());
            }
            settlementId = updateSettlementRes.getData().getId();
        }
        settlementCommissionRequest.setSettlementId(settlementId);
        if (settlementCommissionRequest.getGroupon() != null) {//2 团购
            APIResult<AcMerchantSettlement> settlementCommissionRes = remoteAccountSettlementService.addOrUpdateCommission(settlementCommissionRequest);
            if (settlementCommissionRes.getCode() != 0) {
                throw new APIException(settlementCommissionRes.getCode(), settlementCommissionRes.getMsg());
            }
        }
        settlementBankRequest.setSettlementId(settlementId);
        if(settlementBankRequest.getBankCode()!=null){//3 商家银行
            APIResult<AcMerchantSettlementBank> banRes = remoteAccountSettlementService.addBank(settlementBankRequest);
            if (banRes.getCode() != 0) {
                throw new APIException(banRes.getCode(), banRes.getMsg());
            }
        }
    }

    private void validateMerchantSettlentBank(String bankCode, String name, String account, String branch) {
        if(bankCode==null){
            throw new APIException(ResultCodes.Fail, "开户行不能为空");
        }
        if(name==null){
            throw new APIException(ResultCodes.Fail, "开户名称不能为空");
        }
        if(account==null){
            throw new APIException(ResultCodes.Fail, "账号不能为空");
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





    private void setMerchantSettlementCommissionRequest(Map<String, String> result, AcMerchantSettlementCommission settlementCommissionRequest, Map<String,String> distributes) {
        String startTimeStr = getValue(result, distributes.get(groupBuyingStartTime) );//开始时间
        String commissionStr = getValue(result,distributes.get(commissionNames) );//佣金
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

    private void setMerchantSettlementBank(Map<String, String> result, AcMerchantSettlementBank merchantSettlementBank, Map<String, String> distributes) {
        String accountNameStr = getValue(result,distributes.get(accountName));//开户名
        String bankStr = getValue(result,distributes.get(bank) );//开户行
        String bankBranchStr = getValue(result,distributes.get(bankBranch) );//分行
        String bankAccountStr = getValue(result,distributes.get(bankAccount) );//账号
        merchantSettlementBank.setBankCode(bankStr);
        merchantSettlementBank.setBranch(bankBranchStr);
        merchantSettlementBank.setAccount(bankAccountStr);
        merchantSettlementBank.setName(accountNameStr);
    }

    private void setMerchantSettlementRequest(Map<String, String> result, AcMerchantSettlement request, Long mchId,Map<String,String> distributes) {
        String poundageStr = getValue(result,distributes.get(poundageNames));//手续费
        String paymentDaysStr = getValue(result,distributes.get(paymentDaysNames) );//账期
        String dateTypeStr = getValue(result,distributes.get(dayTypeNames) );//类型
        String startTimeStr = getValue(result,distributes.get(youfuStartTimeNames) );//开始时间
        String startPriceStr = getValue(result, distributes.get(startPriceNames) );//起结金额
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

    private Map<String,String> getDistributeMap(List<ContractParamDistribute> distributes) {
        Map<String,String> map=new HashMap<>();
        for (ContractParamDistribute distribute : distributes) {
            APIResult<ContractParameter> contractParameterAPIResult = this.remoteContractService.saveTemplateParamter(distribute.getParamId());
            if (contractParameterAPIResult.getCode() != 0) {
                throw new APIException(contractParameterAPIResult.getCode(), contractParameterAPIResult.getMsg());
            }
            ContractParameter data = contractParameterAPIResult.getData();
            String paramName=data.getKey();
            map.put(distribute.getNameMapping(),paramName);
        }
        return map;
    }
}
