package com.nuanyou.cms.model;

/**
 * Created by Alan.ye on 2016/9/30.
 */
public class OptionVO {
    private Long id;
    private String name;

    public OptionVO() {
    }

    public OptionVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
