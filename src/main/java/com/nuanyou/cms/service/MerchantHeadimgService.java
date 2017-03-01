package com.nuanyou.cms.service;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.config.ImageSpec;
import com.nuanyou.cms.entity.MerchantHeadimg;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Felix on 2016/9/28.
 */
public interface MerchantHeadimgService {

    MerchantHeadimg saveNotNull(MerchantHeadimg entity);

    List<MerchantHeadimg> find(MerchantHeadimg entity);

    void setTop(Long id);

    APIResult setListImgUrl(Long id,String detailImgUrl,ImageSpec imageSpec) throws Exception;

}