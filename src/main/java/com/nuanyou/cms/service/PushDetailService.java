package com.nuanyou.cms.service;

import com.nuanyou.cms.model.PushDetailCondition;
import com.nuanyou.cms.model.PushDetailListVo;
import com.nuanyou.cms.model.PushDetailVo;
import org.springframework.data.domain.Page;

/**
 * Created by yangkai on 2017/2/15.
 */
public interface PushDetailService {
    void deletePushDetail(Long id);

    Page<PushDetailListVo> list(PushDetailCondition pushDetailCondition, int index);

    PushDetailVo findById(Long id);

    PushDetailVo update(PushDetailVo pushDetailVo);

    void checkSource(Long id, String source);
}
