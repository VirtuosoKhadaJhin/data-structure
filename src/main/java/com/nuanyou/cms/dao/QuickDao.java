package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Quick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface QuickDao extends JpaRepository<Quick, Long>, JpaSpecificationExecutor {

}