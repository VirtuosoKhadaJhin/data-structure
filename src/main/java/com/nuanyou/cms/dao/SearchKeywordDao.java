package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.SearchKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/26.
 */
public interface SearchKeywordDao extends JpaRepository<SearchKeyword, Long>, JpaSpecificationExecutor {

}
