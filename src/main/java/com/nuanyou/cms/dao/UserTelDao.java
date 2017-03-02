package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.UserTel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Felix on 2017/3/2.
 */
public interface UserTelDao extends JpaRepository<UserTel, Long> {
    UserTel findByUserid(Long userid);
}
