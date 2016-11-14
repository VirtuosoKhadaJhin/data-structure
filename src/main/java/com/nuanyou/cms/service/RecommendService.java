package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Recommend;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.data.domain.Page;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface RecommendService {

    Recommend saveNotNull(Recommend entity);

}