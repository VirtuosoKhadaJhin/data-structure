package com.nuanyou.cms.entity.rank;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by Felix on 2016/9/30.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_rank")
@DiscriminatorColumn(name = "objtype", discriminatorType = DiscriminatorType.INTEGER)
public class Rank {
    private Long id;
    private City city;
    private Integer sort;
    private Boolean display;
    private Date createtime;
    private Date updatetime;
    private Country country;


    //private Long objid;
    //private Byte objtype;
   /* private Merchant merchant;
    private Item item;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityid")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }




    
/*    @Column(name = "objid", nullable = true)
    public Long getObjid() {
        return objid;
    }

    public void setObjid(Long objid) {
        this.objid = objid;
    }*/

    
  /*  @Column(name = "objtype", nullable = true)
    public Byte getObjtype() {
        return objtype;
    }

    public void setObjtype(Byte objtype) {
        this.objtype = objtype;
    }*/


    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }


    @LastModified
    @Column(name = "updatetime", nullable = true)
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }


/*    @Transient
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Transient
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }*/

    @Transient
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


}



