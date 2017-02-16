package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.entity.order.ViewOrderExport;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
public interface OrderService {

    Page<Order> findByCondition(Integer index, Order entity, TimeCondition time, Pageable pageable);

    Page<ViewOrderExport> findExportByCondition(Integer index, ViewOrderExport entity, TimeCondition time, Pageable o);

    Page<Order> findRefundByCondition(Integer index, Order entity, TimeCondition time);

    List<Order> findRefundByCondition(Order entity, TimeCondition time);

    Integer getBuyNum(Order order);

    void refund(Order entity);

    Order saveNotNull(Order entity);

    void validate(Long id, Integer type, String cmsusername);

    void putOrderToExcel(int index, HttpServletRequest request, HttpServletResponse response, ViewOrderExport entity, TimeCondition time, String[] titles, String filename, HSSFWorkbook workbook, HSSFSheet firstSheet, HSSFRow firstRow) throws IOException;
}


