package com.nuanyou.cms.model;

import org.apache.commons.lang3.BooleanUtils;

/**
 * Created by mylon on 2017/6/23.
 */
public class MerchantCatResultVo {

    private String keyCode;
    private Boolean enFlag = false;
    private Boolean zhFlag = false;

    public MerchantCatResultVo() {
    }

    public Boolean isAllHandled() {
        if (BooleanUtils.isTrue(enFlag) && BooleanUtils.isTrue(zhFlag)) {
            return true;
        } else {
            return false;
        }
    }

    public MerchantCatResultVo(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public Boolean getEnFlag() {
        return enFlag;
    }

    public void setEnFlag(Boolean enFlag) {
        this.enFlag = enFlag;
    }

    public Boolean getZhFlag() {
        return zhFlag;
    }

    public void setZhFlag(Boolean zhFlag) {
        this.zhFlag = zhFlag;
    }
}
