package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;

/**
 * Created by Felix on 2016/10/20.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_test")
public class Test {
    private Long id;
    private String name;
    private TestChild testChild;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @OneToOne(fetch = FetchType.LAZY,mappedBy = "test")
    public TestChild getTestChild() {
        return testChild;
    }

    public void setTestChild(TestChild testChild) {
        this.testChild = testChild;
    }
}
