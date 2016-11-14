package com.nuanyou.cms.model;

import org.apache.velocity.tools.generic.NumberTool;

import java.math.BigDecimal;

/**
 * Created by Felix on 2016/10/25.
 */
public class OrderDetail {
    private String sms_code;
    private Integer sms_times;
    private String logistics_address;

    private BigDecimal subsidy_youfusubsidyprice;
    private BigDecimal subsidy_youfusubsidykpprice;
    private BigDecimal subsidy_mchsubsidyprice;
    private BigDecimal subsidy_mchsubsidykpprice;


    private String subsidy_youfusubsidyprice_Format;
    private String subsidy_youfusubsidykpprice_Format;
    private String subsidy_mchsubsidyprice_Format;
    private String subsidy_mchsubsidykpprice_Format;
/*
    private BigDecimal subsidy_youfusubsidyprice;
    private String subsidy_youfusubsidykpprice_Format;
    private String subsidy_mchsubsidykpprice__Format;*/

    private Integer buyNum;


    public OrderDetail(String sms_code, Integer sms_times, String logistics_address,
                       BigDecimal subsidy_youfusubsidyprice, BigDecimal subsidy_youfusubsidykpprice,
                       BigDecimal subsidy_mchsubsidyprice, BigDecimal subsidy_mchsubsidykpprice,
                       Integer buyNum) {
        this.sms_code = sms_code;
        this.sms_times = sms_times;
        this.logistics_address = logistics_address;
        this.subsidy_youfusubsidyprice = subsidy_youfusubsidyprice;
        this.subsidy_youfusubsidykpprice = subsidy_youfusubsidykpprice;
        this.subsidy_mchsubsidyprice = subsidy_mchsubsidyprice;
        this.subsidy_mchsubsidykpprice = subsidy_mchsubsidykpprice;
        this.buyNum = buyNum;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public Integer getSms_times() {
        return sms_times;
    }

    public void setSms_times(Integer sms_times) {
        this.sms_times = sms_times;
    }

    public String getLogistics_address() {
        return logistics_address;
    }

    public void setLogistics_address(String logistics_address) {
        this.logistics_address = logistics_address;
    }

    public BigDecimal getSubsidy_youfusubsidyprice() {
        return subsidy_youfusubsidyprice;
    }

    public void setSubsidy_youfusubsidyprice(BigDecimal subsidy_youfusubsidyprice) {
        this.subsidy_youfusubsidyprice = subsidy_youfusubsidyprice;
    }

    public BigDecimal getSubsidy_youfusubsidykpprice() {
        return subsidy_youfusubsidykpprice;
    }

    public void setSubsidy_youfusubsidykpprice(BigDecimal subsidy_youfusubsidykpprice) {
        this.subsidy_youfusubsidykpprice = subsidy_youfusubsidykpprice;
    }

    public BigDecimal getSubsidy_mchsubsidyprice() {
        return subsidy_mchsubsidyprice;
    }

    public void setSubsidy_mchsubsidyprice(BigDecimal subsidy_mchsubsidyprice) {
        this.subsidy_mchsubsidyprice = subsidy_mchsubsidyprice;
    }

    public BigDecimal getSubsidy_mchsubsidykpprice() {
        return subsidy_mchsubsidykpprice;
    }

    public void setSubsidy_mchsubsidykpprice(BigDecimal subsidy_mchsubsidykpprice) {
        this.subsidy_mchsubsidykpprice = subsidy_mchsubsidykpprice;
    }





    NumberTool numberTool=new NumberTool();



    public String getSubsidy_youfusubsidyprice_Format() {

        if(subsidy_youfusubsidyprice==null){
            return null;
        }
        return "¥"+subsidy_youfusubsidyprice;
    }

    public String getSubsidy_youfusubsidykpprice_Format() {
        if(subsidy_youfusubsidykpprice==null){
            return null;
        }
        return numberTool.format("#0.00",subsidy_youfusubsidykpprice);
    }

    public String getSubsidy_mchsubsidyprice_Format() {

        if(subsidy_mchsubsidyprice==null){
            return null;
        }
        return "¥"+subsidy_mchsubsidyprice;
    }

    public String getSubsidy_mchsubsidykpprice_Format() {


        if(subsidy_mchsubsidykpprice==null){
            return null;
        }
        return numberTool.format("#0.00",subsidy_mchsubsidykpprice);
    }
}
