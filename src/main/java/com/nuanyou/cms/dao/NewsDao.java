package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Felix on 2016/9/7.
 */
public interface NewsDao extends JpaRepository<News, Long> {
}
