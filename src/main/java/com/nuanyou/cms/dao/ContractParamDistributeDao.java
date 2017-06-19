package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.ContractParamDistribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface ContractParamDistributeDao extends JpaRepository<ContractParamDistribute, Long>, JpaSpecificationExecutor {


    List<ContractParamDistribute> findBySystemId(Long systemId);
}
