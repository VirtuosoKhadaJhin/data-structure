package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by sharp on 2017/6/22 - 18:29
 */
public interface BdUserDao extends JpaRepository<BdUser, Long>, JpaSpecificationExecutor {
    BdUser findUserById(Long id);

    List<BdUser> findByIdIn(List<Long> userIds);

    /**
     * 查找所有可见的bduser
     *
     * @return
     */
    @Query(value = "SELECT t from BdUser t where deleted=0")
    List<BdUser> findallBdUser();

    /**
     * 通过countryId找到bdusers
     *
     * @return
     */
    @Query(value = "SELECT t from BdUser t where countryid=:id")
    List<BdUser> findBdUsersByCountryId(@Param("id") Long id);

    @Query(value = "select  t from BdUser t where id in (:bdUserIds)and countryid=:countryId")
    List<BdUser> findBdUsersByIdsAndCountryId(@Param("bdUserIds") List<Long> bdUserIds, @Param("countryId") Long countryId);
}
