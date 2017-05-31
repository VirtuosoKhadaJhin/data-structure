package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CmsUser;

/**
 * Created by Felix on 2017/5/31.
 */
public interface UserService {
    CmsUser getUserByEmail(String email);

    String findNameById(Long id);
}
