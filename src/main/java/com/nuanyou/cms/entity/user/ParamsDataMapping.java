package com.nuanyou.cms.entity.user;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;

/**
 * Created by Felix on 2016/9/7.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_param_data_mapping")
public class ParamsDataMapping {
    private Long id;
    private String name;
    private String regex;
    private Integer dataType;
    private String note;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
