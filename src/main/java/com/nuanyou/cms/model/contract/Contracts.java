package com.nuanyou.cms.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Alan.ye on 2017/4/20.
 */
public class Contracts {

    private Long total;

    private List<Contract> list;

    @ApiModelProperty(value = "总数量")
    @JsonProperty("total")
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @ApiModelProperty(value = "合同集合")
    @JsonProperty("list")
    public List<Contract> getList() {
        return list;
    }

    public void setList(List<Contract> list) {
        this.list = list;
    }

}