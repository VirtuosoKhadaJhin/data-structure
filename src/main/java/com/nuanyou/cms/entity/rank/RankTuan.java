package com.nuanyou.cms.entity.rank;

import com.nuanyou.cms.entity.item.ItemTuanExtend;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Felix on 2016/10/10.
 */
@Entity
@DiscriminatorValue("2")
public class RankTuan extends Rank {

    private ItemTuanExtend itemTuan;


    @ManyToOne
    @JoinColumn(name = "objid")
    public ItemTuanExtend getItemTuan() {
        return itemTuan;
    }

    public void setItemTuan(ItemTuanExtend itemTuan) {
        this.itemTuan = itemTuan;
    }

    public RankTuan() {
    }
}
