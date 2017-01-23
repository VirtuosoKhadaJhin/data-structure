package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.user.PasUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


/**
 * Created by Alan.ye on 2016/9/6.
 */
public interface PasUserProfileDao extends JpaRepository<PasUserProfile, Long> {

    @Query(value = "select new PasUserProfile(p.id,p.userid,p.nickname) from PasUserProfile p where p.userid=:userid")
    PasUserProfile findPartsByUserid(@Param("userid") Long userid);


    @Query(value = "select new PasUserProfile(p.id,p.userid,p.nickname) from PasUserProfile p where p.userid in (?1)")
    List<PasUserProfile> findByUserid(Collection<Long> userIds);
}
