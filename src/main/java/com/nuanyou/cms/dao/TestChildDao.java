package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.TestChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/10/20.
 */
public interface TestChildDao  extends JpaRepository<TestChild, Long>, JpaSpecificationExecutor {

}
