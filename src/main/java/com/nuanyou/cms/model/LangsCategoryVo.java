package com.nuanyou.cms.model;

import java.io.Serializable;

/**
 * Created by mylon on 2017/6/15.
 */
public class LangsCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    public LangsCategoryVo() {
    }

    public LangsCategoryVo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
