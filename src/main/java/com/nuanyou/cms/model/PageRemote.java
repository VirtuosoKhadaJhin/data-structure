package com.nuanyou.cms.model;

import java.util.List;

/**
 * Created by Felix on 2016/10/20.
 */
public class PageRemote<T> {

    private Integer totalPages;
    private Integer number;
    private List<T> content;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
