package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Channel;
import com.nuanyou.cms.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2016/9/9.
 */
public interface ChannelDao extends JpaRepository<Channel, Long> {
}
