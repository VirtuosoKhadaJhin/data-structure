package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdCountry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/26 - 19:29
 */
public interface BdCountryDao extends JpaRepository<BdCountry, Long>, JpaSpecificationExecutor {
}
