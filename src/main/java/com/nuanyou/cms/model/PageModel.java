package com.nuanyou.cms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GENIUS on 2017/10/6.
 */
@ApiModel("分页器")
public class PageModel<T extends BasicModel> {
    @ApiModelProperty("列表数据")
    private List<T> list = new ArrayList<>();
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("大小")
    private Integer size;
    @ApiModelProperty("总数")
    private Long total;
    @ApiModelProperty("总页数")
    private Integer pages;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
