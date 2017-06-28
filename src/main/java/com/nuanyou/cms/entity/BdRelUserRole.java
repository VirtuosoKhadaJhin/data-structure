package com.nuanyou.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by sharp on 2017/6/26 - 15:21
 */
@Entity
@Table(name = "bd_rel_user_roles", catalog = "nuanyou20" )
public class BdRelUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="uid")
    private BdUser user;
    
    @ManyToOne
    @JoinColumn(name = "roleid")
    private BdRole role;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BdUser getUser() {
        return user;
    }
    
    public void setUser(BdUser user) {
        this.user = user;
    }
    
    public BdRole getRole() {
        return role;
    }
    
    public void setRole(BdRole role) {
        this.role = role;
    }
}
