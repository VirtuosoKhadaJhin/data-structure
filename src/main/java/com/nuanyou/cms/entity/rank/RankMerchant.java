package com.nuanyou.cms.entity.rank;

import com.nuanyou.cms.entity.Merchant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Felix on 2016/10/10.
 */
@Entity
@DiscriminatorValue("1")
public class RankMerchant extends Rank {

    private Merchant merchant;


    @ManyToOne
    @JoinColumn(name = "objid")
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public RankMerchant() {
    }
}
