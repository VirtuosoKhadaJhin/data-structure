package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Rank;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/30.
 */
public interface RankService {
    Page<Rank> findByCondition(Integer index, Rank entity);

    Rank saveNotNull(Rank entity);
}
