package com.nuanyou.cms.service;

import com.nuanyou.cms.model.PushGroupListVo;
import org.springframework.data.domain.Page;

/**
 * Created by yangkai on 2017/2/15.
 */
public interface PushGroupService {

    Page<PushGroupListVo> list(int index);

    void changeStatus(Long id, Boolean status);
}
