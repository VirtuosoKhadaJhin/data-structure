package com.nuanyou.cms.service;

import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserManagerVo;

import org.springframework.data.domain.Page;

/**
 * bd宝用户管理
 *
 * Created by sharp on 2017/6/22 - 15:03
 */
public interface BdUserManagerService {
    /**
     * 查询所有bd宝用户列表
     *
     * @return
     */
    Page<BdUserManagerVo> findAllBdUsers(final BdUserManagerRequestVo requestVo);
}
