package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CmsUser;

import java.util.List;

/**
 * Created by Felix on 2017/5/31.
 */
public interface UserService {
    CmsUser getUserByEmail(String email);

    String findNameById(Long id);

    List<Long> findUserCountryId();

}
