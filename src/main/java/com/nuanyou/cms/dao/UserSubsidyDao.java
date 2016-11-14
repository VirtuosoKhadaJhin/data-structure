package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.UserSubsidy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/6.
 */
public interface UserSubsidyDao extends JpaRepository<UserSubsidy, Long> {

    List<UserSubsidy> findByMchIdAndStartTimeLessThanAndEndTimeGreaterThan(Long mchId, Date startTime, Date endTime);

}
