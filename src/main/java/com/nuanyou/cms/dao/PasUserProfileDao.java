package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.user.PasUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by Alan.ye on 2016/9/6.
 */
public interface PasUserProfileDao extends JpaRepository<PasUserProfile, Long> {

    PasUserProfile findByUserid(Long userId);
}
