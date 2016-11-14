package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Banner;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/28.
 */
public interface BannerService {

    Page<Banner> findByCondition(Integer index, Banner entity);

    Banner saveNotNull(Banner entity);

}
