package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.BdUser;
import org.apache.tomcat.jni.Shm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface BdUserDao extends JpaRepository<BdUser, Long>, JpaSpecificationExecutor {
    BdUser findUserById(Long id);

    List<BdUser> findByIdIn(Collection<Long> userIds);

    @Query(value = "SELECT t from BdUser t where t.deleted=0")
    List<BdUser> findallBdUser();

    @Query(value = "SELECT t from BdUser t where t.country.id=:id and t.deleted = 0")
    List<BdUser> findBdUsersByCountryId(@Param("id") Long id);

    @Query(value = "select  t from BdUser t where t.id in (:bdUserIds) and t.country.id=:countryId and t.deleted = 0")
    List<BdUser> findBdUsersByIdsAndCountryId(@Param("bdUserIds") List<Long> bdUserIds, @Param("countryId") Long countryId);

    @Query(value = "SELECT t from BdUser t where t.name=:name and t.deleted = 0")
    List<BdUser> checkNameRepeat(@Param("name") String name);

    @Query(value = "select t from BdUser t where t.name=?2 and t.id != ?1 and t.deleted = 0")
    List<BdUser> findByNameNonBdUser(Long id, String name);

    @Query(value = "SELECT t from BdUser t where t.dmail=:dmail and t.deleted = 0")
    List<BdUser> checkDmailRepeat(@Param("dmail")String dmail);

    @Query(value = "select t from BdUser t where t.dmail=?2 and t.id != ?1 and t.deleted = 0")
    List<BdUser> findByDmailNonBdUser(Long id, String dmail);

    @Query(value = "SELECT new BdUser(t.id, t.chineseName) from BdUser t where t.deleted=0 and t.id in ?1 ")
    List<BdUser> findByIds(Collection<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE BdUser set deleted=1 where id=?1")
    void updateDeleteUser(Long id);
}
