package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/7.
 */
public interface TestDao extends JpaRepository<Test, Long>, JpaSpecificationExecutor {

}
