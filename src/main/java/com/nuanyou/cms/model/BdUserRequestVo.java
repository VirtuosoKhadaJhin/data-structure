package com.nuanyou.cms.model;

public class BdUserRequestVo {

    private Long id;
    private String name;
    private String chineseName;
    private String dmail;
    private Long conturyid;
    private String role; //角色

    Integer index = 1;
    Integer pageNum = 20;

    public BdUserRequestVo() {
    }

    public BdUserRequestVo(Integer index, Integer pageNum) {
        this.index = index;
        this.pageNum = pageNum;
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

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getDmail() {
        return dmail;
    }

    public void setDmail(String dmail) {
        this.dmail = dmail;
    }

    public Long getConturyid() {
        return conturyid;
    }

    public void setConturyid(Long conturyid) {
        this.conturyid = conturyid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
