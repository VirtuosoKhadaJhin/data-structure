package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;

/**
 * Created by Felix on 2017/6/14.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "bd_contract_param_distribute",catalog = "bd")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ContractParamDistribute {

    private Long id;
    private Long paramId;
    private String nameMapping;
    private Long systemId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public String getNameMapping() {
        return nameMapping;
    }

    public void setNameMapping(String nameMapping) {
        this.nameMapping = nameMapping;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
}
