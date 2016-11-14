package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantCat;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by nuanyou on 2016/9/6.
 */
public interface MerchantCatService {
    Page<Merchant> findByCondition(Integer mchid, String name, Integer index, Integer pageSize);

    void add(MerchantCat merchantCat);

    Page<MerchantCat> findByCondition(MerchantCat merchantCat, Integer index);

    List<MerchantCat> getIdNameList();

    MerchantCat saveNotNull(MerchantCat entity);
}
