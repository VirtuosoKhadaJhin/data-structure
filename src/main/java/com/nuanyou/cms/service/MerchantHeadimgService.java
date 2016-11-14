package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.MerchantHeadimg;

import java.util.List;

/**
 * Created by Felix on 2016/9/28.
 */
public interface MerchantHeadimgService {

    MerchantHeadimg saveNotNull(MerchantHeadimg entity);

    List<MerchantHeadimg> find(MerchantHeadimg entity);

    void setTop(Long id);

}