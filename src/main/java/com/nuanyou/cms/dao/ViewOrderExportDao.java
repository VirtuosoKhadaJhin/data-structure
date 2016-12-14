package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.entity.order.ViewOrderExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Felix on 2016/9/8.
 */
public interface ViewOrderExportDao extends JpaRepository<ViewOrderExport, Long>, JpaSpecificationExecutor {

}
