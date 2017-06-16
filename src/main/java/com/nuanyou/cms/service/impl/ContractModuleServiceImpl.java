package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CmsUserDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.model.LangsDictionaryVo;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.output.Contracts;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.ContractModuleService;
import com.nuanyou.cms.service.LangsDictionaryService;
import com.nuanyou.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Felix on 2017/5/31.
 */
@Service
public class ContractModuleServiceImpl implements ContractModuleService {
    @Autowired
    private RemoteContractService contractService;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private CmsUserDao cmsUserDao;
    @Autowired
    private UserService userService;
    @Autowired
    private LangsDictionaryService langsDictionaryService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Page<Contract> getContracts(Long userId, Long merchantId, Long id, String merchantName, String status, String templateid, String type, Boolean businessLicense, Boolean paperContract, String startTime, String endTime, Integer index, Integer limit) {
        APIResult<Contracts> contracts = contractService.list(userId, merchantId, id, merchantName, status, templateid, type, businessLicense, paperContract, startTime, endTime, index, limit);
        Contracts contractsData = contracts.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<Contract> list = contractsData.getList();
        if (list == null)
            list = new ArrayList(0);

        // 本地名称显示商户本地名称
        for (Contract contract : list) {
            setExtraProperties(contract);
        }
        return (Page<Contract>) new PageImpl(list, pageable, contractsData.getTotal());
    }

    @Override
    public Contract getContract(Long id) {
        APIResult<Contract> res = this.contractService.getContract(id);
        Contract data = res.getData();
        setExtraProperties(data);
        return data;
    }

    public void setExtraProperties(Contract contract) {

        Long mchid = contract.getMchId();
        String localName =null;
        if (mchid != null) {
            localName=merchantDao.getLocalName(mchid);
        }
        merchantDao.getLocalName(mchid);
        contract.setRelatedMchName(localName);
        contract.setApproverName(this.userService.findNameById(contract.getApproverId()));
    }

    @Override
    public Map<String, String> setParamsTitle(Long templateId, Map<String, String> parameters) throws UnsupportedEncodingException {
        APIResult<ContractTemplate> contractConfig = this.contractService.getContractConfig(templateId);
        if (contractConfig.getCode() != 0) {
            throw new APIException(contractConfig.getCode(), contractConfig.getMsg());
        }
        ContractTemplate data = contractConfig.getData();
        List<ContractParameter> templateParams = data.getParameters();
        Map<String, String> paramsLang=new HashMap<String,String>();
        for (String key : parameters.keySet()) {
            String title=null;
            for (ContractParameter templateParam : templateParams) {
                if(templateParam.getKey().equals(key)){
                    String titleLangsKey=templateParam.getName().getKey();
                    title= langsDictionaryService.findLocalMessageByKeyCode(titleLangsKey, request.getLocale());
                    break;
                }
            }
            if(title==null){
                paramsLang.put(key,parameters.get(key));
            }else{
                paramsLang.put(title,parameters.get(key));
            }

        }


        return paramsLang;
    }
}
