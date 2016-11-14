package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.FakeUser;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface FakeUserService {

    Page<FakeUser> findByCondition(Integer index, FakeUser entity);

    FakeUser saveNotNull(FakeUser entity);

    List<FakeUser> getIdNameList();
}
