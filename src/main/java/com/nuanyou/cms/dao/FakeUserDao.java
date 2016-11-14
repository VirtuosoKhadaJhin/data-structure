package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.FakeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface FakeUserDao extends JpaRepository<FakeUser, Long>, JpaSpecificationExecutor {

    @Query(value = "select new FakeUser(t.id,t.nickname) from FakeUser t")
    List<FakeUser> getIdNameList();

}
