package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MerchantStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Alan.ye on 2016/10/17.
 */
public interface MerchantStaffDao extends JpaRepository<MerchantStaff, Long> {


    @Query(value = "select count(1) from MerchantStaff t where t.username=?1")
    int findByUsername(String username);
}
