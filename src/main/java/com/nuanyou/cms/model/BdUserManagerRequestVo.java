package com.nuanyou.cms.model;

/**
 * // TODO: 2017/6/22 注释
 * Created by sharp on 2017/6/22 - 15:37
 */
public class BdUserManagerRequestVo {
    
    private String username;
    private Long id;
    private String email;
    private Long conturyid;
    private String role; //角色
    
    Integer index = 1;
    Integer pageNum = 20;
    
    public BdUserManagerRequestVo() {
    }
    
    public BdUserManagerRequestVo(Integer index, Integer pageNum) {
        this.index = index;
        this.pageNum = pageNum;
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getConturyid() {
        return conturyid;
    }
    
    public void setConturyid(Long conturyid) {
        this.conturyid = conturyid;
    }
    
}
