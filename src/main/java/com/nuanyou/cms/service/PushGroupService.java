package com.nuanyou.cms.service;

import com.nuanyou.cms.model.PushGroupVo;
import org.springframework.data.domain.Page;

/**
 * Created by yangkai on 2017/2/15.
 */
public interface PushGroupService {

    Page<PushGroupVo> list(int index);

    void changeStatus(Long id, Boolean status);
}
