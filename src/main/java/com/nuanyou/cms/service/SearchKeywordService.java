package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.SearchKeyword;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/26.
 */
public interface SearchKeywordService {
    Page<SearchKeyword> findByCondition(int index, SearchKeyword entity);

    SearchKeyword saveNotNull(SearchKeyword entity);
}
